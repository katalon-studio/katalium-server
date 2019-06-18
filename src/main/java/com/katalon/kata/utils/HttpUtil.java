package com.katalon.kata.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class HttpUtil {

  private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
  private static final int DEFAULT_SOCKET_TIMEOUT = 100000;

  public static HttpClient getHttpClient() {
    return getHttpClient(DEFAULT_CONNECT_TIMEOUT);
  }

  public static <T> T get(String url, Map<String, String> headers, Class<T> returnType) throws URISyntaxException, IOException {
    URI uri = new URIBuilder(url).build();
    URIBuilder uriBuilder = new URIBuilder(uri);

    HttpGet httpGet = new HttpGet(uriBuilder.build());
    httpGet.addHeader("Katalon", "Katalon");
    headers.forEach(httpGet::setHeader);

    T result = executeRequest(httpGet, returnType);
    return result;
  }

  private static <T> T executeRequest(HttpUriRequest httpRequest, Class<T> returnType) throws IOException {
    HttpClient httpClient = getHttpClient();
    HttpResponse httpResponse = httpClient.execute(httpRequest);
    String responseString = EntityUtils.toString(httpResponse.getEntity());
    Gson gson = new GsonBuilder().create();
    return gson.fromJson(responseString, returnType);
  }

  private static HttpClientBuilder getHttpClientBuilder() {
    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    SSLConnectionSocketFactory sslSocketFactory = getSslSocketFactory();
    httpClientBuilder.setSSLSocketFactory(sslSocketFactory)
        .setConnectionReuseStrategy(new NoConnectionReuseStrategy());
    return httpClientBuilder;
  }

  private static HttpClient getHttpClient(int connectTimeout) {
    RequestConfig config = RequestConfig.custom()
        .setConnectTimeout(connectTimeout)
        .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
        .build();
    HttpClientBuilder httpClientBuilder = getHttpClientBuilder();
    httpClientBuilder.setDefaultRequestConfig(config);
    return httpClientBuilder.build();
  }

  private static SSLConnectionSocketFactory getSslSocketFactory() {
    SSLContext sslContext = getSslContext();
    HostnameVerifier skipHostnameVerifier = new SkipHostnameVerifier();
    SSLConnectionSocketFactory sslSocketFactory =
        new SSLConnectionSocketFactory(sslContext, skipHostnameVerifier);
    return sslSocketFactory;
  }

  private static SSLContext getSslContext() {
    try {
      SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      TrustStrategy trustStrategy = new TrustAllStrategy();
      sslContextBuilder.loadTrustMaterial(keyStore, trustStrategy);
      sslContextBuilder.setProtocol("TLSv1.2");
      SSLContext sslContext = sslContextBuilder.build();
      return sslContext;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Trust all certificates.
   */
  private static class TrustAllStrategy implements TrustStrategy {

    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      return true;
    }
  }

  private static class SkipHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String s, SSLSession sslSession) {
      return true;
    }

  }
}
