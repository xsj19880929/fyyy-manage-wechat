package com.suryani.manage.common;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by guoshuai on 16/8/4.
 */
@Service
public class CHttpClient {
    private static HttpClient httpClient;

    static {
        try {
            HttpClientBuilder builder = HttpClients.custom();
            builder.setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .setSslcontext(new SSLContextBuilder()
                            .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                            .build());
//            int cpuNum = Runtime.getRuntime().availableProcessors();
            builder.setMaxConnPerRoute(30).setMaxConnTotal(300);
            builder.setDefaultSocketConfig(SocketConfig.custom()
                    .setSoKeepAlive(true).build());
            builder.setDefaultRequestConfig(RequestConfig.custom()
                    .setSocketTimeout(10000)
                    .setConnectTimeout(10000)
                    .setConnectionRequestTimeout(1000)
                    .build());
            httpClient = builder.build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new IllegalStateException(e);
        }
    }

    @PreDestroy
    public static void close() throws IOException {
        ((CloseableHttpClient) httpClient).close();
    }

    public HttpResponse execute(HttpUriRequest request) throws IOException {
        return execute(request, (HttpClientContext) null);
    }

    public HttpResponse execute(HttpHost target, HttpUriRequest request) throws IOException {
        return httpClient.execute(target, request);
    }

    public HttpResponse execute(HttpUriRequest request, HttpClientContext clientContext) throws IOException {
        return httpClient.execute(request, clientContext);
    }

    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpClientContext clientContext) throws IOException {
        return httpClient.execute(request, responseHandler, clientContext);
    }

    public <T> T execute(HttpHost target, HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpClientContext clientContext) throws IOException {
        return httpClient.execute(target, request, responseHandler, clientContext);
    }

    public <T> T execute(HttpHost target, HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
        return httpClient.execute(target, request, responseHandler);
    }

    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
        return execute(request, responseHandler, null);
    }


    public String executeString(HttpUriRequest request) throws IOException {
        return execute(request, new BasicResponseHandler());
    }

    public String executeString(HttpUriRequest request, HttpClientContext httpClientContext) throws IOException {
        return execute(request, new BasicResponseHandler(), httpClientContext);
    }

}
