package net.fluance.commons.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UriUtils {

	private UriUtils() {}

	private static Logger LOGGER = LogManager.getLogger(UriUtils.class);

	/**
	 * 
	 * @param uriString
	 * @return
	 * @throws URISyntaxException
	 */
	@Deprecated
	public static URI buildUri(String uriString) throws URISyntaxException {
		return URI.create(uriString);
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static URI buildUri(String scheme, String host, int port, String path, List<NameValuePair> params) throws URISyntaxException {
		String usedScheme = (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) ? "http" : scheme;
		if (params != null) {
			return new URIBuilder().setScheme(usedScheme).setHost(host).setPort(port).setPath(path).setParameters(params).build();
		} else {
			return new URIBuilder().setScheme(usedScheme).setHost(host).setPort(port).setPath(path).build();
		}
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static URI buildUri(String scheme, String host, int port, String path, Map<String, String> params) throws URISyntaxException {
		List<NameValuePair> usedParams = null;
		if (params != null && !params.isEmpty()) {
			usedParams = new ArrayList<NameValuePair>();
			Set<String> paramNames = params.keySet();
			for (String paramName : paramNames) {
				usedParams.add(new BasicNameValuePair(paramName, params.get(paramName)));
			}
		}
		return buildUri(scheme, host, port, path, usedParams);
	}

	public static URI buildUri(String scheme, String host, int port, String path) throws URISyntaxException {
		String usedScheme = (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) ? "http" : scheme;
		return new URIBuilder().setScheme(usedScheme).setHost(host).setPort(port).setPath(path).build();
	}

	/**
	 * Extracts the parameters from a URI. Handles the multi-valued (array) parameters
	 * 
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, List<String>> urlParameters(URL url, String charset) throws UnsupportedEncodingException {
		final Map<String, List<String>> queryPairs = new LinkedHashMap<String, List<String>>();
		final String[] pairs = url.getQuery().split("&");
		for (String pair : pairs) {
			final int idx = pair.indexOf("=");
			final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), charset) : pair;
			if (!queryPairs.containsKey(key)) {
				queryPairs.put(key, new LinkedList<String>());
			}
			final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), charset) : null;
			queryPairs.get(key).add(value);
		}
		return queryPairs;
	}

	public static Long getValueLongFromURI(String uri, String service) {
		try {
			if (!uri.contains(service))
				return null;
			int start = uri.indexOf(service + "/") + service.length() + 1;
			int end = uri.indexOf("/", start);
			if (end == -1)
				end = uri.length();
			String valueString = uri.substring(start, end);
			return Long.valueOf(valueString);
		} catch (NumberFormatException e) {
			LOGGER.warn("No ID found in the URL : " + uri);
			return null;
		}
	}

	/**
	 * 
	 * @param uri
	 * @param service
	 * @return PathParameter in String
	 */
	public static String getValueStringFromURI(String uri, String service) {
		try {
			if (!uri.contains(service))
				return null;
			int start = uri.indexOf(service + "/") + service.length() + 1;
			int end = uri.indexOf("/", start);
			if (end == -1)
				end = uri.length();
			String valueString = uri.substring(start, end);
			return valueString;
		} catch (NumberFormatException e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}
}
