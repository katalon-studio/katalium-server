package com.katalon.kata.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.katalon.kata.utils.HttpUtil.getHttpClient;

public class KatalonUtil {

    private static final Logger LOG = Log.getLogger(KatalonUtil.class);

    private static final ObjectMapper objectMapper = JsonUtil.getObjectMapper();

    public static String requestToken(String serverApiUrl, String email, String password) throws Exception {

        String clientId = "kit";
        String clientSecret = "kit";

        final String TOKEN_URI = "/oauth/token";
        String url = serverApiUrl + TOKEN_URI;
        URIBuilder uriBuilder = new URIBuilder(url);

        List<NameValuePair> pairs = Arrays.asList(
                new BasicNameValuePair("username", email),
                new BasicNameValuePair("password", password),
                new BasicNameValuePair("grant_type", "password")
        );

        HttpPost httpPost = new HttpPost(uriBuilder.build());

        String clientCredentials = clientId + ":" + clientSecret;
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " +
                Base64.getEncoder().encodeToString(clientCredentials.getBytes()));
        HttpEntity entity = new UrlEncodedFormEntity(pairs);
        httpPost.setEntity(entity);

        HttpClient httpClient = getHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpPost);

        InputStream content = httpResponse.getEntity().getContent();
        Map map = objectMapper.readValue(content, Map.class);
        String token = (String) map.get("access_token");
        if (token == null) {
            LOG.debug("Cannot login with provided user credentials.");
        }
        return token;
    }
}
