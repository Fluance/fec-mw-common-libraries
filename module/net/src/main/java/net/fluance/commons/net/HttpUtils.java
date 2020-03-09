/**
 * 
 */
package net.fluance.commons.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpUtils {

	private HttpUtils() {}

	private static Logger LOGGER = LogManager.getLogger(HttpUtils.class);

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
	 * 
	 * @param uri
	 * @param headers
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static HttpGet buildGet(URI uri, List<Header> headers) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		HttpGet getRequest = new HttpGet(uri);
		if ((headers != null) && !(headers.isEmpty())) {
			for (Header header : headers) {
				getRequest.addHeader(header);
			}
		}
		return getRequest;
	}

	/**
	 * 
	 * @param uri
	 * @param headers
	 * @param accessToken
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static HttpGet buildOauth2AccessTokenGet(URI uri, List<Header> headers, String accessToken) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		Header authorizationHeader = new BasicHeader("Authorization", "Bearer " + accessToken);
		List<Header> usedHeaders = headers;
		if (usedHeaders == null) {
			usedHeaders = new ArrayList<>();
		}
		usedHeaders.add(authorizationHeader);
		return buildGet(uri, usedHeaders);
	}

	public static HttpGet buildGet(String scheme, String host, int port, String path, List<Header> headers, List<NameValuePair> params)
			throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		String usedScheme = (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) ? "http" : scheme;
		URI uri = buildUri(usedScheme, host, port, path, params);
		return buildGet(uri, headers);
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param headers
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static HttpGet buildGet(String scheme, String host, int port, String path, Map<String, String> headers, Map<String, String> params)
			throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		List<Header> usedHeaders = new ArrayList<Header>();
		Set<String> headerNames = headers.keySet();
		for (String headerName : headerNames) {
			usedHeaders.add(new BasicHeader(headerName, headers.get(headerName)));
		}
		URI uri = buildUri(scheme, host, port, path, params);
		return buildGet(uri, usedHeaders);
	}

	/**
	 * 
	 * @param getRequest
	 * @param trustAllCerts
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static CloseableHttpResponse sendGet(HttpGet getRequest, boolean trustAllCerts) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		switch (getRequest.getURI().getScheme()) {
			case "https":
				if (trustAllCerts) {
					SSLContextBuilder builder = new SSLContextBuilder();
					builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
					SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
					httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
				}
				break;
			default:
				break;
		}
		return httpclient.execute(getRequest);
	}

	/**
	 * 
	 * @param uri
	 * @param headers
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static HttpDelete buildDelete(URI uri, List<Header> headers) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		HttpDelete deleteRequest = new HttpDelete(uri);
		if ((headers != null) && !(headers.isEmpty())) {
			for (Header header : headers) {
				deleteRequest.addHeader(header);
			}
		}
		return deleteRequest;
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param headers
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static HttpDelete buildDelete(String scheme, String host, int port, String path, List<Header> headers, List<NameValuePair> params)
			throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		String usedScheme = (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) ? "http" : scheme;
		URI uri = buildUri(usedScheme, host, port, path, params);
		return buildDelete(uri, headers);
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param headers
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static HttpDelete buildDelete(String scheme, String host, int port, String path, Map<String, String> headers, Map<String, String> params)
			throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		List<Header> usedHeaders = new ArrayList<Header>();
		Set<String> headerNames = headers.keySet();
		for (String headerName : headerNames) {
			usedHeaders.add(new BasicHeader(headerName, headers.get(headerName)));
		}
		URI uri = buildUri(scheme, host, port, path, params);
		return buildDelete(uri, usedHeaders);
	}

	/**
	 * 
	 * @param getRequest
	 * @param trustAllCerts
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static CloseableHttpResponse sendDelete(HttpDelete delRequest, boolean trustAllCerts) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		switch (delRequest.getURI().getScheme()) {
			case "https":
				if (trustAllCerts) {
					SSLContextBuilder builder = new SSLContextBuilder();
					builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
					SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
					httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
				}
				break;
			default:
				break;
		}
		return httpclient.execute(delRequest);
	}

	/**
	 * 
	 * @param uri
	 * @param headers
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static HttpPost buildPost(URI uri, List<Header> headers) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		HttpPost postRequest = new HttpPost(uri);
		if ((headers != null) && !(headers.isEmpty())) {
			for (Header header : headers) {
				postRequest.addHeader(header);
			}
		}
		return postRequest;
	}

	public static HttpPost buildPost(URI uri, List<Header> headers, List<NameValuePair> params) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		HttpPost postRequest = buildPost(uri, headers);
		if ((params != null) && !(params.isEmpty())) {
			postRequest.setEntity(new UrlEncodedFormEntity(params));
		}
		return postRequest;
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param headers
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static HttpPost buildPost(String scheme, String host, int port, String path, List<Header> headers, List<NameValuePair> params)
			throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		String usedScheme = (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) ? "http" : scheme;
		URI uri = buildUri(usedScheme, host, port, path);
		return buildPost(uri, headers, params);
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param headers
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static HttpPost buildPost(String scheme, String host, int port, String path, Map<String, String> headersMap, Map<String, String> paramsMap)
			throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		List<Header> headers = headersFromMap(headersMap);
		List<NameValuePair> params = paramsFromMap(paramsMap);
		URI uri = buildUri(scheme, host, port, path, params);
		return buildPost(uri, headers);
	}

	public static HttpPost buildPost(URI uri, List<Header> headers, HttpEntity entity) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		HttpPost postRequest = buildPost(uri, headers);
		postRequest.setEntity(entity);
		return postRequest;
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param headersMap
	 * @param entity
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static HttpPost buildPost(String scheme, String host, int port, String path, Map<String, String> headersMap, HttpEntity entity)
			throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		URI uri = buildUri(scheme, host, port, path);
		List<Header> headers = headersFromMap(headersMap);
		return buildPost(uri, headers, entity);
	}

	/**
	 * 
	 * @param uri
	 * @param headers
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static HttpPut buildPut(URI uri, List<Header> headers) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		HttpPut putRequest = new HttpPut(uri);
		if ((headers != null) && !(headers.isEmpty())) {
			for (Header header : headers) {
				putRequest.addHeader(header);
			}
		}
		return putRequest;
	}

	public static HttpPut buildPut(URI uri, List<Header> headers, List<NameValuePair> params) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		HttpPut putRequest = buildPut(uri, headers);
		if ((params != null) && !(params.isEmpty())) {
			putRequest.setEntity(new UrlEncodedFormEntity(params));
		}
		return putRequest;
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param headers
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static HttpPut buildPut(String scheme, String host, int port, String path, List<Header> headers, List<NameValuePair> params)
			throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		String usedScheme = (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) ? "http" : scheme;
		URI uri = buildUri(usedScheme, host, port, path);
		return buildPut(uri, headers, params);
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param headers
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static HttpPut buildPut(String scheme, String host, int port, String path, Map<String, String> headersMap, Map<String, String> paramsMap)
			throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		List<Header> headers = headersFromMap(headersMap);
		List<NameValuePair> params = paramsFromMap(paramsMap);
		URI uri = buildUri(scheme, host, port, path, params);
		return buildPut(uri, headers);
	}

	public static HttpPut buildPut(URI uri, List<Header> headers, HttpEntity entity) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		HttpPut putRequest = buildPut(uri, headers);
		putRequest.setEntity(entity);
		return putRequest;
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param headersMap
	 * @param entity
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static HttpPut buildPut(String scheme, String host, int port, String path, Map<String, String> headersMap, HttpEntity entity)
			throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		URI uri = buildUri(scheme, host, port, path);
		List<Header> headers = headersFromMap(headersMap);
		return buildPut(uri, headers, entity);
	}

	public static HttpRequestBase buildRequest(String httpMethod, URI uri, List<Header> headers, HttpEntity entity) throws KeyManagementException, HttpException, NoSuchAlgorithmException, KeyStoreException, IOException, URISyntaxException {
		switch (httpMethod) {
			case HttpGet.METHOD_NAME:
				return buildGet(uri, headers);
			case HttpPost.METHOD_NAME:
				return buildPost(uri, headers, entity);
			case HttpPut.METHOD_NAME:
				return buildPut(uri, headers, entity);
			case HttpDelete.METHOD_NAME:
				return buildDelete(uri, headers);
			default: {
				String msg = "Method name must be a standard HTTP method name. Submitted: " + httpMethod;
				LOGGER.error(msg);
				throw new IllegalStateException(msg);
			}
		}
	}

	/**
	 * 
	 * @param getRequest
	 * @param trustAllCerts
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static CloseableHttpResponse sendPost(HttpPost postRequest, boolean trustAllCerts) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		switch (postRequest.getURI().getScheme()) {
			case "https":
				if (trustAllCerts) {
					SSLContextBuilder builder = new SSLContextBuilder();
					builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
					SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
					httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
				}
				break;
			default:
				break;
		}
		CloseableHttpResponse httpResponse = httpclient.execute(postRequest);
		return httpResponse;
	}

	public static CloseableHttpResponse sendPut(HttpPut putRequest, boolean trustAllCerts) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		switch (putRequest.getURI().getScheme()) {
			case "https":
				if (trustAllCerts) {
					SSLContextBuilder builder = new SSLContextBuilder();
					builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
					SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
					httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
				}
			default:
				break;
		}
		return httpclient.execute(putRequest);
	}

	public static CloseableHttpResponse send(HttpRequestBase request, boolean trustAll) throws KeyManagementException, HttpException, NoSuchAlgorithmException, KeyStoreException, IOException {
		CloseableHttpResponse response = null;
		switch (request.getMethod()) {
			case HttpGet.METHOD_NAME:
				response = sendGet((HttpGet) request, trustAll);
				break;
			case HttpPost.METHOD_NAME:
				response = sendPost((HttpPost) request, trustAll);
				break;
			case HttpPut.METHOD_NAME:
				response = sendPut((HttpPut) request, trustAll);
				break;
			case HttpDelete.METHOD_NAME:
				response = sendDelete((HttpDelete) request, trustAll);
				break;
			default: {
				String msg = "Method name must be a standard HTTP method name. Submitted: " + request.getMethod();
				LOGGER.error(msg);
				throw new IllegalStateException(msg);
			}
		}
		return response;
	}

	/**
	 * 
	 * @param httpMethod
	 * @param url
	 * @param headers
	 * @param payload
	 * @param contentType
	 * @param charset
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws HttpException
	 * @throws URISyntaxException
	 */
	public static CloseableHttpResponse send(String httpMethod, String url, List<Header> headers, String payload, ContentType contentType, String charset)
			throws UnsupportedOperationException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, HttpException, URISyntaxException {
		URI uri = UriUtils.buildUri(url);
		HttpEntity entity = null;
		if ("POST".equalsIgnoreCase(httpMethod) || "PUT".equalsIgnoreCase(httpMethod)) {
			entity = requestEntity(payload, contentType, charset);
		}
		HttpRequestBase request = HttpUtils.buildRequest(httpMethod, uri, headers, entity);
		return HttpUtils.send(request, true);
	}

	/**
	 * 
	 * @param uri
	 * @param headers
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static HttpPost buildSoap(URI uri, List<Header> headers, String action, String payload) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		HttpPost postRequest = buildSoap(uri, headers, action);
		StringEntity entity = new StringEntity(payload, Charset.forName("utf-8"));
		entity.setContentType("text/xml");
		postRequest.setEntity(entity);
		return postRequest;
	}

	/**
	 * 
	 * @param uri
	 * @param headers
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static HttpPost buildSoap(URI uri, List<Header> headers, String action, HttpEntity entity) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		HttpPost postRequest = buildSoap(uri, headers, action);
		postRequest.setEntity(entity);
		return postRequest;
	}

	/**
	 * 
	 * @param uri
	 * @param headers
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static HttpPost buildSoap(URI uri, List<Header> headers, String action) throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		HttpPost postRequest = new HttpPost(uri);
		postRequest.addHeader(new BasicHeader("SOAPAction", action));
		if ((headers != null) && !(headers.isEmpty())) {
			for (Header header : headers) {
				postRequest.addHeader(header);
			}
		}
		return postRequest;
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param headers
	 * @param params
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static HttpPost buildSoap(String scheme, String host, int port, String path, List<Header> headers, String action, String payload)
			throws URISyntaxException, KeyManagementException, HttpException, NoSuchAlgorithmException, KeyStoreException, IOException {
		String usedScheme = (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) ? "http" : scheme;
		URI uri = buildUri(usedScheme, host, port, path);
		return buildSoap(uri, headers, action, payload);
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param path
	 * @param headers
	 * @param action
	 * @param payload
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 * @throws URISyntaxException
	 */
	public static HttpPost buildSoap(String scheme, String host, int port, String path, Map<String, String> headers, String action, String payload)
			throws HttpException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, URISyntaxException {
		List<Header> usedHeaders = headersFromMap(headers);
		URI uri = buildUri(scheme, host, port, path);
		return buildSoap(uri, usedHeaders, action, payload);
	}

	/**
	 * 
	 * @param response
	 * @return
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public static String stringEntity(CloseableHttpResponse response) throws UnsupportedOperationException, IOException {
		HttpEntity responseEntity = response.getEntity();
		String entity = inputStreamToString(responseEntity.getContent());
		EntityUtils.consume(responseEntity);
		return entity;
	}

	/**
	 * 
	 * @param jsonPayload
	 * @param contentType
	 * @param charset
	 * @return
	 */
	public static HttpEntity requestEntity(String jsonPayload, ContentType contentType, String charset) {
		return new StringEntity(jsonPayload, (charset != null) ? contentType.withCharset(Charset.forName(charset)) : contentType);
	}

	/**
	 *
	 * @param headersMap
	 * @return
	 */
	private static List<Header> headersFromMap(Map<String, String> headersMap) {
		List<Header> usedHeaders = new ArrayList<Header>();
		if (headersMap != null && !headersMap.isEmpty()) {
			Set<String> headerNames = headersMap.keySet();
			for (String headerName : headerNames) {
				usedHeaders.add(new BasicHeader(headerName, headersMap.get(headerName)));
			}
		}
		return usedHeaders;
	}

	/**
	 * 
	 * @param paramsMap
	 * @return
	 */
	private static List<NameValuePair> paramsFromMap(Map<String, String> paramsMap) {
		List<NameValuePair> params = new ArrayList<>();
		if (paramsMap != null && !paramsMap.isEmpty()) {
			Set<String> paramNames = paramsMap.keySet();
			for (String paramName : paramNames) {
				params.add(new BasicNameValuePair(paramName, paramsMap.get(paramName)));
			}
		}
		return params;
	}

	private static String inputStreamToString(InputStream inputStream) {
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
					e.printStackTrace();
				}
			}
		}
		return stringBuilder.toString();
	}
}
