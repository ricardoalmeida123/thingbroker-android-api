package br.ufscar.dc.thingbroker.services.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.util.Base64;
import br.ufscar.dc.thingbroker.model.EventData;

/**
 * 
 * @author ricardo
 */
class HTTPUtil {

	public static String doGet(String url, UsernamePasswordCredentials credentials) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
 
		if (credentials != null) {
			httpClient.getCredentialsProvider().setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
					credentials);
		}

		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(url);
			HttpResponse response;
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					throw new IOException("Http error " + statusCode);
				}
				return EntityUtils.toString(entity);
			}
		} catch (Exception ex) {
			if (httpGet != null) {
				httpGet.abort();
			}
			throw ex;
		}
		return null;
	}

	public static String doPost(String url, String bodyContent, UsernamePasswordCredentials credentials)
			throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		if (credentials != null) {
			httpClient.getCredentialsProvider().setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
					credentials);

			HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
				public void process(final HttpRequest request,
						final HttpContext context) throws HttpException,
						IOException {
					AuthState authState = (AuthState) context
							.getAttribute(ClientContext.TARGET_AUTH_STATE);
					CredentialsProvider credsProvider = (CredentialsProvider) context
							.getAttribute(ClientContext.CREDS_PROVIDER);
					HttpHost targetHost = (HttpHost) context
							.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

					if (authState.getAuthScheme() == null) {
						AuthScope authScope = new AuthScope(
								targetHost.getHostName(), targetHost.getPort());
						Credentials creds = credsProvider
								.getCredentials(authScope);
						if (creds != null) {
							authState.setAuthScheme(new BasicScheme());
							authState.setCredentials(creds);
						}
					}
				}
			};
			httpClient.addRequestInterceptor(preemptiveAuth, 0);
		}
		
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json");
			if (bodyContent != null) {
				BasicHttpEntity urlEncodedFormEntity = new BasicHttpEntity();
				urlEncodedFormEntity.setContent(new ByteArrayInputStream(bodyContent.getBytes()));
				httpPost.setEntity(urlEncodedFormEntity);
			}

			HttpResponse response;
			response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
						throw new IOException("Http error " + statusCode);
					}
				}
				return EntityUtils.toString(entity);
			}
		} catch (Exception ex) {
			if (httpPost != null) {
				httpPost.abort();
			}
			throw ex;
		}
		return null;
	}
	
	public static String doPost(String url, EventData[] bodyContent, UsernamePasswordCredentials credentials) throws Exception {
		HttpClient httpClient = new HttpClient();
		String encoding = null;	
		if(credentials != null) {
		  httpClient.getParams().setAuthenticationPreemptive(true);
		  encoding = Base64.encodeToString((credentials.getUserName() + ":" + credentials.getPassword()).getBytes(), Base64.NO_WRAP);
		}
		PostMethod httpPost = null;
		try {
			httpPost = new PostMethod(url);
			if(encoding != null) {
				httpPost.addRequestHeader("Authorization", "Basic " + encoding);
			}
			if (bodyContent != null) {
				List<Part> array = new ArrayList<Part>();
				for(EventData data : bodyContent) {
					FilePart part = new FilePart(data.getName(), new ByteArrayPartSource(data.getName(), data.getData()));
					part.setContentType(data.getMimeType()); 
					array.add(part);
				}
				MultipartRequestEntity entity = new MultipartRequestEntity(array.toArray(new Part[array.size()]), httpPost.getParams());
				httpPost.setRequestEntity(entity);
			}
			int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
						throw new IOException("Http error " + statusCode);
					}
				}
				return httpPost.getResponseBodyAsString();

		} catch (Exception ex) {
			System.out.println("Erro: " + ex.getMessage());
			if (httpPost != null) {
				httpPost.abort();
			}
			throw ex;
		}
		//return null;
	}
	
	public static String doPut(String url, String bodyContent, UsernamePasswordCredentials credentials)
			throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		if (credentials != null) {
			httpClient.getCredentialsProvider().setCredentials(
					new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
					credentials);

			HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
				public void process(final HttpRequest request,
						final HttpContext context) throws HttpException,
						IOException {
					AuthState authState = (AuthState) context
							.getAttribute(ClientContext.TARGET_AUTH_STATE);
					CredentialsProvider credsProvider = (CredentialsProvider) context
							.getAttribute(ClientContext.CREDS_PROVIDER);
					HttpHost targetHost = (HttpHost) context
							.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

					if (authState.getAuthScheme() == null) {
						AuthScope authScope = new AuthScope(
								targetHost.getHostName(), targetHost.getPort());
						Credentials creds = credsProvider
								.getCredentials(authScope);
						if (creds != null) {
							authState.setAuthScheme(new BasicScheme());
							authState.setCredentials(creds);
						}
					}
				}
			};
			httpClient.addRequestInterceptor(preemptiveAuth, 0);
		}
		
		HttpPut httpPut = null;
		try {
			httpPut = new HttpPut(url);
			httpPut.setHeader("Content-Type", "application/json");
			if (bodyContent != null) {
				BasicHttpEntity urlEncodedFormEntity = new BasicHttpEntity();
				urlEncodedFormEntity.setContent(new ByteArrayInputStream(bodyContent.getBytes()));
				httpPut.setEntity(urlEncodedFormEntity);
			}

			HttpResponse response;
			response = httpClient.execute(httpPut);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
						throw new IOException("Http error " + statusCode);
					}
				}
				return EntityUtils.toString(entity);
			}
		} catch (Exception ex) {
			if (httpPut != null) {
				httpPut.abort();
			}
			throw ex;
		}
		return null;
	}
	
	public static String doDelete(String url, UsernamePasswordCredentials credentials) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		if (credentials != null) {
			httpClient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),credentials);
		}

		HttpDelete httpDelete = null;
		try {
			httpDelete = new HttpDelete(url);
			HttpResponse response;

			response = httpClient.execute(httpDelete);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					throw new IOException("Http error " + statusCode);
				}
				return EntityUtils.toString(entity);
			}
		} catch (Exception ex) {
			if (httpDelete != null) {
				httpDelete.abort();
			}
			throw ex;
		}
		return null;
	}
}
