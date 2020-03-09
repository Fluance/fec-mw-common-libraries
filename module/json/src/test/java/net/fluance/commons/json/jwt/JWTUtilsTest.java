/**
 * 
 */
package net.fluance.commons.json.jwt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import net.fluance.commons.json.jwt.JWTUtils.JwtPart;


public class JWTUtilsTest {

	private static Logger LOGGER = LogManager.getLogger(JWTUtilsTest.class);

	private final String keystoreFilePath = "/keystore.jks";
	private final String keyStorePassword = "fluance";
	private final String keyStoreType = "JKS";
	private final String keyAlias = "fluance1024";
	private final String keyPassword = "fluance";


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testVerifySignature() throws Exception {
		LOGGER.debug("Executing testVerifySignature...");
		Key key = MacProvider.generateKey();
		// Load key store
		File keystoreFile = new File(getClass().getResource(keystoreFilePath).getFile());
		KeyStore keyStore = loadKeyStore(keystoreFile, keyStorePassword, keyStoreType);
		assertNotNull(keyStore);
		// Load keys
		KeyPair keyPair = readKeyPair(keyStore, keyAlias, keyPassword);
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();

		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
		JwtBuilder builder =  Jwts.builder()
				.setSubject("Joe")
				.setIssuer("Swisscom")
				.setExpiration(tomorrow)
				//				.compressWith(CompressionCodecs.DEFLATE)
				.signWith(SignatureAlgorithm.RS256, privateKey);

		String compactJws = builder.compact();

		System.out.println(JWTUtils.extractParam(compactJws, "sub", publicKey));
		System.out.println(JWTUtils.verifySignature(compactJws, "RS512" ,publicKey));

		System.out.println(JWTUtils.getPart(compactJws, JwtPart.PAYLOAD));
		System.out.println(JWTUtils.getPart(compactJws, JwtPart.HEADER));


		Header header = Jwts.jwsHeader();

		header.setType("JWT");
		System.out.println(header.toString());
		String payload = "{\"iss\":\"fluance1024\", \"exp\":" + tomorrow.getTime() + ", \"pid\": 345757, \"firstName\": \"Margrit\", \"lastName\": \"PARASOLE\", \"birthDate\": \"1958-02-26 00:00:00\", \"sex\": \"Féminin\", \"role\": \"Patient\", \"username\": \"fluanceMe\"}";

		JwtBuilder builder2 =  Jwts.builder().setPayload(payload).setHeaderParam(JWTUtils.TYPE_KEY, "JWS")

				.signWith(SignatureAlgorithm.RS512, privateKey);
		compactJws = builder2.compact();


		System.out.println("HEADER_TO_USE : " + compactJws);

		String encodedHeader = compactJws.split("\\.")[0];
		System.out.println("Base64Utils.base64UrlDecode(encodedHeader) : " + base64UrlDecode(encodedHeader));
		String encodedPayload = compactJws.split("\\.")[1];
		System.out.println("Base64Utils.base64UrlDecode(encodedPayload) : " + base64UrlDecode(encodedPayload));

		System.out.println("VerifySignature : " + JWTUtils.verifySignature(compactJws, "RS512" ,publicKey));
	}
	
	@Test
	public void testGetJwtToken() throws Exception{
		LOGGER.debug("Executing testGetJwtToken...");
		// Load key store
		File keystoreFile = new File(getClass().getResource(keystoreFilePath).getFile());
		KeyStore keyStore = loadKeyStore(keystoreFile, keyStorePassword, keyStoreType);
		assertNotNull(keyStore);
		// Load keys
		KeyPair keyPair = readKeyPair(keyStore, keyAlias, keyPassword);
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
		Long exp = tomorrow.getTime();
		
		Map<String, Object> payload = new HashMap<>();
		
		payload.put("iss", "fluance");
		payload.put("exp", exp.toString());
		payload.put("pid", "345757");
		payload.put("firstName", "Margrit");

		Map<String, Object> header = new HashMap<>();

		header.put(JWTUtils.TYPE_KEY, "JWS");
		header.put("alg", "RS256");

		String token = JWTUtils.buildToken(privateKey, header, payload);
		
		System.out.println("TOKEN : "+token);
		String encodedHeader = token.split("\\.")[0];
		System.out.println("Base64Utils.base64UrlDecode(encodedHeader) : " + base64UrlDecode(encodedHeader));
		String encodedPayload = token.split("\\.")[1];
		System.out.println("Base64Utils.base64UrlDecode(encodedPayload) : " + base64UrlDecode(encodedPayload));

		System.out.println("VerifySignature : " + JWTUtils.verifySignature(token, "RS256" ,publicKey));
	
		assertTrue(JWTUtils.isJwt(token));
		assertTrue(JWTUtils.verifySignature(token, "rs256", publicKey));

	}

	@Test
	public void testIsJwt() throws Exception {
		LOGGER.debug("Executing testIsJwt...");
		String oauth2Token = "3f70d487d8ae247e39c68bfceac5a43e";
		assertFalse(JWTUtils.isJwt(oauth2Token));
		String JwtToken = "aaaaaaaaaa.bbbbbbbbbbb.cccccccccccc";
		assertFalse(JWTUtils.isJwt(JwtToken));

		String validJwtHeader1 = "{\"" + JWTUtils.SIGNING_ALGORITHM_KEY + "\":\"RS256\",\"" + JWTUtils.TYPE_KEY + "\":\"jwt\"}";
		String validJwtHeader2 = "{\"" + JWTUtils.SIGNING_ALGORITHM_KEY + "\":\"NONE\",\"" + JWTUtils.TYPE_KEY + "\":\"JWT\"}";
		String invalidJwtHeader3 = "{\"algorithm\":\"NONE\",\"type\":\"JWT\"}";
		String invalidJwtHeader4 = "{\"algorithm\":\"RS256\",\"" + JWTUtils.TYPE_KEY + "\":\"JWT\"}";
		String invalidJwtHeader6 = "{\"otherField\":\"value\",\"" + JWTUtils.TYPE_KEY + "\":\"JWT\"}";
		String invalidJwtHeader8 = "{}";
		String invalidJwtHeader10 = "{\"" + JWTUtils.SIGNING_ALGORITHM_KEY + "\":\"\",\"" + JWTUtils.TYPE_KEY + "\":\"JWT\"}";
		
		String validEncodedJwtHeader1 = base64Encode(validJwtHeader1);
		String validEncodedJwtHeader2 = base64Encode(validJwtHeader2);

		String invalidEncodedJwtHeader3 = base64Encode(invalidJwtHeader3);
		String invalidEncodedJwtHeader4 = base64Encode(invalidJwtHeader4);
		String invalidEncodedJwtHeader6 = base64Encode(invalidJwtHeader6);
		String invalidEncodedJwtHeader8 = base64Encode(invalidJwtHeader8);
		String invalidEncodedJwtHeader10 = base64Encode(invalidJwtHeader10);
		
		String validPayload="{\"" + JWTUtils.ISSUER_KEY + "\":\"${app.jwt.issuer}\", \"key2\":\"value2\", \"key3\":\"value3\"}";
		String validEncodedPayload=base64Encode(validPayload);
		String invalidPayload1="[]";
		String invalidEncodedPayload1=base64Encode(invalidPayload1);
		String invalidPayload2="Payload";
		String invalidEncodedPayload2=base64Encode(invalidPayload2);

		String signature = "WIUERWlkajdaSDAOJDAskASDJASKDdFSdf";
		
		String validToken1 = validEncodedJwtHeader1 + "." + validEncodedPayload + "." + signature;
		assertTrue(JWTUtils.isJwt(validToken1));
		String validToken2 = validEncodedJwtHeader2 + "." + validEncodedPayload + "." + signature;
		assertTrue(JWTUtils.isJwt(validToken2));

//		String invalidToken1 = invalidEncodedJwtHeader1 + "." + validEncodedPayload + "." + signature;
//		assertFalse(JWTUtils.isJwt(invalidToken1));
//		String invalidToken2 = invalidEncodedJwtHeader2 + "." + validEncodedPayload + "." + signature;
//		assertFalse(JWTUtils.isJwt(invalidToken2));
		String invalidToken3 = invalidEncodedJwtHeader3 + "." + validEncodedPayload + "." + signature;
		assertFalse(JWTUtils.isJwt(invalidToken3));
		String invalidToken4 = invalidEncodedJwtHeader4 + "." + validEncodedPayload + "." + signature;
		assertFalse(JWTUtils.isJwt(invalidToken4));
//		String invalidToken5 = invalidEncodedJwtHeader5 + "." + validEncodedPayload + "." + signature;
//		assertFalse(JWTUtils.isJwt(invalidToken5));
		String invalidToken6 = invalidEncodedJwtHeader6 + "." + validEncodedPayload + "." + signature;
		assertFalse(JWTUtils.isJwt(invalidToken6));
//		String invalidToken7 = invalidEncodedJwtHeader7 + "." + validEncodedPayload + "." + signature;
//		assertFalse(JWTUtils.isJwt(invalidToken7));
		String invalidToken8 = invalidEncodedJwtHeader8 + "." + validEncodedPayload + "." + signature;
		assertFalse(JWTUtils.isJwt(invalidToken8));
//		String invalidToken9 = invalidEncodedJwtHeader9 + "." + validEncodedPayload + "." + signature;
//		assertFalse(JWTUtils.isJwt(invalidToken9));
		String invalidToken10 = invalidEncodedJwtHeader10 + "." + validEncodedPayload + "." + signature;
		assertFalse(JWTUtils.isJwt(invalidToken10));
		String invalidToken11 = validEncodedJwtHeader1 + "." + invalidEncodedPayload1 + "." + signature;
		assertFalse(JWTUtils.isJwt(invalidToken11));
		String invalidToken12 = validEncodedJwtHeader1 + "." + invalidEncodedPayload2 + "." + signature;
		assertFalse(JWTUtils.isJwt(invalidToken12));

	}
	
	@Test
	public void testGetPart() throws JsonProcessingException, IOException {
		LOGGER.debug("Executing testGetPart...");
		String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJTSEEyNTZ3aXRoUlNBIiwieDV0IjoiTjJVNE16Vm1OamRqWmpnd01qTmxORFF3T1RSbE9UbGtOek0xT0RZNU4yUXhZMll3WWpBeU5BIn0.eyJpc3MiOiJodHRwOi8vd3NvMi5vcmcvZ2F0ZXdheSIsImV4cCI6MTQ3MDI5NTgxNDg0OSwiaHR0cDovL3dzbzIub3JnL2dhdGV3YXkvc3Vic2NyaWJlciI6ImFkbWluIiwiaHR0cDovL3dzbzIub3JnL2dhdGV3YXkvYXBwbGljYXRpb25uYW1lIjoiT0F1dGgyIFNlcnZpY2UgcHJvdmlkZXIiLCJodHRwOi8vd3NvMi5vcmcvZ2F0ZXdheS9lbmR1c2VyIjoiYWRtaW4iLCAiaHR0cDovL3dzbzIub3JnL2NsYWltcy9yb2xlIjoiYWRtaW4sSW50ZXJuYWwvc3lzYWRtaW4sSW50ZXJuYWwvU0FNTCBTZXJ2aWNlIFByb3ZpZGVyLEludGVybmFsL09BdXRoMiBTZXJ2aWNlIFByb3ZpZGVyLEludGVybmFsL1NBTUwgU2VydmljZSBwcm92aWRlcixJbnRlcm5hbC9PQXV0aDIgU2VydmljZSBwcm92aWRlcixJbnRlcm5hbC9ldmVyeW9uZSJ9.X3i1TTtIRilzPoYqcPQJZ_rhDskuUBQWLMX3VgVN78YjXScfPszJkKgJQMkVTpx3UvyQ97J9-5wJVZBiOzRfidbxrLqip3DdkVa81e0OZwUO7YGkKVQfUm-ptPAfpz-tUqq6qUlCfUm7g4-CuanPItdi3Z49w_QUwRCVOY6nnSo";
		JsonNode header = JWTUtils.getPart(jwt, JwtPart.HEADER);
		assertNotNull(header);
		LOGGER.debug("Header: " + header.toString());
		JsonNode payload = JWTUtils.getPart(jwt, JwtPart.PAYLOAD);
		assertNotNull(payload);
		LOGGER.debug("Payload: " + payload.toString());
	}
	
	private static KeyStore loadKeyStore(final File keystoreFile, final String password, final String keyStoreType)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException,
			java.security.cert.CertificateException {
		LOGGER.debug("Executing loadKeyStore...");
		if (null == keystoreFile) {
			throw new IllegalArgumentException("Keystore url may not be null");
		}
		LOGGER.debug("Initializing key store: " + keystoreFile.getAbsolutePath());
		final URI keystoreUri = keystoreFile.toURI();
		final URL keystoreUrl = keystoreUri.toURL();
		final KeyStore keystore = KeyStore.getInstance(keyStoreType);
		InputStream is = null;
		try {
			is = keystoreUrl.openStream();
			keystore.load(is, null == password ? null : password.toCharArray());
			LOGGER.debug("Loaded key store");
		} finally {
			if (null != is) {
				is.close();
			}
		}
		return keystore;
	}

	/**
	 * Read keypair from keystore
	 * 
	 * @param keystore
	 * @param alias
	 * @param password
	 * @return
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 */
	private KeyPair readKeyPair(final KeyStore keystore, final String alias, final String password)
			throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
		LOGGER.debug("Executing readKeyPair...");
		final Key key = (PrivateKey) keystore.getKey(alias, password.toCharArray());

		final Certificate cert = keystore.getCertificate(alias);
		final PublicKey publicKey = cert.getPublicKey();

		return new KeyPair(publicKey, (PrivateKey) key);
	}
	
	private String base64UrlDecode(String input) {
		LOGGER.debug("Executing base64UrlDecode...");
		String result = null;
	    Base64 decoder = new Base64(true);
	    byte[] decodedBytes = decoder.decode(input);
	    result = new String(decodedBytes);
	    return result;
	}
	
	public static String base64Encode(String str) {
		LOGGER.debug("Executing base64Encode...");
		byte[] encodedBytes = Base64.encodeBase64(str.getBytes());
		String encodedString = new String(encodedBytes);
		return encodedString;
	}
}
