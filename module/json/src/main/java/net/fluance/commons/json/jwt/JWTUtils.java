/**
 * 
 */
package net.fluance.commons.json.jwt;

import java.io.IOException;
import java.security.Key;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtils {

	private static Logger LOGGER = LogManager.getLogger(JWTUtils.class);

	public static enum JwtPart {
		HEADER, PAYLOAD, SIGNATURE
	}

	public static final String SIGNING_ALGORITHM_KEY = "alg";
	public static final String TYPE_KEY = "typ";
	public static final String EXPIRATION_TIME_KEY = "exp";
	public static final String CONTENT_TYPE_KEY = "cty";
	public static final String ISSUER_KEY = "iss";
	public static final String SUBJECT_KEY = "sub";
	public static final String NOT_BEFORE_KEY = "nbf";
	public static final String ISSUED_AT_KEY = "iat";
	public static final String ID_KEY = "jti";
	public static final String AUDIANCE_KEY = "aud";
	public static final String NO_SIGNATURE_ALGORITHM_VALUE = "NONE";
	public static final String JWS_TYPE_VALUE = "JWS";
	public static final String JWE_TYPE_VALUE = "JWE";

	/**
	 * Verify the signature of the token using the specific algorithm and the specific key.
	 * 
	 * @param jwtToken
	 *            : the token to verify
	 * @param algorithm
	 *            : the algorithm used to verify the token.
	 * @param key
	 *            : the key used in the algorithm to verify the token
	 * @return true : the token is valid and signed by the expecting entity. false : the signature is corrupt.
	 */
	public static boolean verifySignature(String jwtToken, String algorithm, Key key) throws IllegalArgumentException {
		algorithm = algorithm.toLowerCase();
		if (algorithm.equals("rs512") || algorithm.equals("rs256") || algorithm.equals("rs512")) {
			try {
				Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody();
				return true;
			} catch (Exception e) {
				LOGGER.error("Exception when Verifying Signature ", e);
				return false;
			}
		} else {
			String message = "Algorithm not managed by JWT librairy : " + algorithm;
			LOGGER.warn(message);
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Build a token using giving header and payload in a Map and a key to sign it.
	 * 
	 * @param key
	 *            : the key used to sign the token
	 * @param header
	 *            : the header to set for the token
	 * @param payload
	 *            : the payload to set for the token
	 * @return the token encoded with three part : the header, the payload and the signature.
	 */
	public static String buildToken(Key key, Map<String, Object> header, Map<String, Object> payload) {
		LOGGER.debug("Executing buildToken...");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode payloadJson = objectMapper.valueToTree(payload);
		JsonNode headerJson = objectMapper.valueToTree(header);
		return buildToken(key, headerJson, payloadJson);
	}

	/**
	 * Build a token using giving header and payload in a JsonNode and a key to sign it.
	 * 
	 * @param key
	 *            : the key used to sign the token
	 * @param header
	 *            : the header to set for the token
	 * @param payload
	 *            : the payload to set for the token
	 * @return the token encoded with three part : the header, the payload and the signature.
	 */
	public static String buildToken(Key key, JsonNode header, JsonNode payload) {
		JwtBuilder builder = Jwts.builder().setPayload(payload.toString());
		Iterator<String> headersFields = header.fieldNames();
		if (!NO_SIGNATURE_ALGORITHM_VALUE.equalsIgnoreCase(header.get(SIGNING_ALGORITHM_KEY).asText()) && key == null) {
			throw new IllegalArgumentException("The key must be provided if signature algorithm is not none");
		}
		if (NO_SIGNATURE_ALGORITHM_VALUE.equalsIgnoreCase(header.get(SIGNING_ALGORITHM_KEY).asText()) && key != null) {
			throw new IllegalArgumentException("None algorithm is not compatible with signing");
		}
		while (headersFields.hasNext()) {
			String field = headersFields.next();
			builder.setHeaderParam(field, header.get(field));
		}
		if (!NO_SIGNATURE_ALGORITHM_VALUE.equalsIgnoreCase(header.get(SIGNING_ALGORITHM_KEY).asText()) && key != null) {
			builder.signWith(SignatureAlgorithm.valueOf(header.get(SIGNING_ALGORITHM_KEY).asText()), key);
			String compactJws = builder.compact();
			return compactJws;
		} else {
			return builder.compact();
		}
	}

	/**
	 * Verify if the token is a JWT and contains the field type : JWT.
	 * 
	 * @param token
	 *            : the token we want to verify
	 * @return true : the token contains the right field. false : the token is not a JWT.
	 */
	public static boolean isJwt(String token) {
		LOGGER.debug("Executing isJwt...");
		StringTokenizer strToken = new StringTokenizer(token, "\\.");
		int tokenCount = strToken.countTokens();
		if (tokenCount != 2 && tokenCount != 3) {
			return false;
		}
		String header = strToken.nextToken();
		String payload = strToken.nextToken();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			ObjectNode headerNode = (ObjectNode) objectMapper.readTree(base64UrlDecode(header));
			if (!headerNode.has(SIGNING_ALGORITHM_KEY) || headerNode.get(SIGNING_ALGORITHM_KEY) == null || headerNode.get(SIGNING_ALGORITHM_KEY).textValue().isEmpty()) {
				return false;
			}
			objectMapper.readTree(base64UrlDecode(payload));
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	/**
	 * Get the parameter in the given token coresponding to the param.
	 * 
	 * @param token
	 *            : the token.
	 * @param param
	 *            : the parameter we want to retrieve.
	 * @param key
	 *            : the key used to verify the signature.
	 * @return the parameter.
	 */
	public static String extractParam(String token, String param, Key key) {
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		return (String) claims.get(param);
	}

	/**
	 * 
	 * @param token
	 * @param part
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static ObjectNode getPart(String token, JwtPart part) throws JsonProcessingException, IOException {
		switch (part) {
			case HEADER:
				return (ObjectNode) new ObjectMapper().readTree(base64UrlDecode(getEncodedPart(token, part)));
			case PAYLOAD:
				return (ObjectNode) new ObjectMapper().readTree(base64UrlDecode(getEncodedPart(token, part)));
			case SIGNATURE:
				throw new IllegalArgumentException("JWT signature is not a json object");
			default:
				throw new IllegalArgumentException("Not a Json part");
		}
	}

	/**
	 * If the JWT is unsigned, the returned signature is an empty String.
	 * 
	 * @param token
	 * @param part
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static String getEncodedPart(String token, JwtPart part) throws JsonProcessingException, IOException {
		StringTokenizer strToken = new StringTokenizer(token, "\\.");
		int tokenCount = strToken.countTokens();
		if (tokenCount != 2 && tokenCount != 3) {
			String msg = "Malformed Jwt: " + token;
			LOGGER.error("An IllegalArgumentException has occured: " + msg);
			throw new IllegalArgumentException(msg);
		}
		String header = strToken.nextToken();
		String payload = strToken.nextToken();
		String signature = (tokenCount == 3) ? strToken.nextToken() : "";
		switch (part) {
			case HEADER:
				return header;
			case PAYLOAD:
				return payload;
			case SIGNATURE:
				return signature;
		}
		return null;
	}

	private static String base64UrlDecode(String input) {
		Base64 decoder = new Base64(true);
		byte[] decodedBytes = decoder.decode(input);
		return new String(decodedBytes);
	}
}
