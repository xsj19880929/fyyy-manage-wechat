package com.suryani.manage.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.quidsi.core.http.HTTPConstants;

public class ApiClient {

    @Autowired
    @Value("${api.url}")
    private String apiUrl;

    @Autowired
    @Value("${api.token}")
    private String token;

    @Inject
    private RestTemplate restTemplate;

    @Inject
    private HttpClient httpClient;

    public JSONObject get(String key, Map<String, Object> urlVariables) {
        UriComponentsBuilder ucb = UriComponentsBuilder.fromUriString(getUrl(key)).queryParam("token", this.token);
        if (urlVariables != null && !urlVariables.isEmpty()) {
            Set<String> keys = urlVariables.keySet();
            for (String pkey : keys) {
                ucb.queryParam(pkey, urlVariables.get(pkey));
            }
        }
        return check(key, restTemplate.getForObject(ucb.build().toUri(), JSONObject.class));
    }

    public JSONObject post(String key, MultiValueMap<String, Object> form) {
        URI uri = UriComponentsBuilder.fromUriString(getUrl(key)).queryParam("token", this.token).build().toUri();
        return check(key, restTemplate.postForObject(uri, form, JSONObject.class));
    }

    public JSONObject multipartPost(String key, MultipartEntity form) throws Exception, IOException {
        URI uri = UriComponentsBuilder.fromUriString(getUrl(key)).queryParam("token", this.token).build().toUri();
        HttpPost post = new HttpPost(uri);
        post.setHeader("Accept", HTTPConstants.CONTENT_TYPE_JSON);
        post.setEntity(form);
        HttpResponse response = httpClient.execute(post);
        int stCode = response.getStatusLine().getStatusCode();
        if (stCode != 200) {
            throw new RuntimeException("upload faild with key " + key + " and status is " + stCode);
        }
        return check(key, JSONObject.fromObject(EntityUtils.toString(response.getEntity())));
    }

    public JSONObject postJsonObj(String key, JSON obj) throws ClientProtocolException, IOException {
        URI uri = UriComponentsBuilder.fromUriString(getUrl(key)).queryParam("token", this.token).build().toUri();
        HttpPost post = new HttpPost(uri);
        post.setHeader("Accept", HTTPConstants.CONTENT_TYPE_JSON);
        StringEntity entity = new StringEntity(obj.toString(), "UTF-8");
        entity.setContentType("application/json;charset=UTF-8");
        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
        post.setEntity(entity);

        HttpResponse response = httpClient.execute(post);
        int stCode = response.getStatusLine().getStatusCode();
        if (stCode != 200) {
            throw new RuntimeException("post faild with key " + key + " and status is " + stCode);
        }
        return check(key, JSONObject.fromObject(EntityUtils.toString(response.getEntity())));
    }

    public String getUrl(String key) {
        return this.apiUrl + key;
    }

    private JSONObject check(String key, JSONObject rt) {
        if (!rt.containsKey("status")) {
            throw new RuntimeException("invalid call of " + key);
        }
        if (rt.getInt("status") != 200) {
            throw new RuntimeException("call faild of " + key + " and status is " + rt.getInt("status") + " , message is " + rt.getString("message"));
        }
        return rt;
    }

}
