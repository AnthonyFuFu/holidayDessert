package com.holidaydessert.config;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.opensearch.client.RestClientBuilder.RequestConfigCallback;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfiguration {
    private static String schema = "https";
    private static ArrayList<HttpHost> hostList = null;

    private static int connectTimeOut = 1000;
    private static int socketTimeOut = 30000;
    private static int connectionRequestTimeOut = 500;

    private static int maxConnectNum = 100;
    private static int maxConnectPerRoute = 100;

    //opensearch properties
    @Value("${opensearch.ip}")
    private String opensearchHosts; // 集群地址,多個用逗號隔開
    @Value("${opensearch.port}")
    private int opensearchPort; // 集群地址端口
    @Value("${opensearch.username}")
    private String openSearchUserName;
    @Value("${opensearch.password}")
    private String openSearchPassWord;
    
    @Bean
    RestHighLevelClient client() throws Exception {
        hostList = new ArrayList<>();
        String[] hostStrs = opensearchHosts.split(",");
        for (String host : hostStrs) {
            hostList.add(new HttpHost(host, opensearchPort, schema));
        }
        // 帳號密碼認證
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(openSearchUserName, openSearchPassWord));
        //添加SSL認證
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // 信任所有
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        SSLIOSessionStrategy sessionStrategy = new SSLIOSessionStrategy(sslContext, NoopHostnameVerifier.INSTANCE);

        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        // 異步httpclient連接延時配置
        builder.setRequestConfigCallback(new RequestConfigCallback() {
            @Override
            public Builder customizeRequestConfig(Builder requestConfigBuilder) {
                requestConfigBuilder.setConnectTimeout(connectTimeOut);
                requestConfigBuilder.setSocketTimeout(socketTimeOut);
                requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
                return requestConfigBuilder;
            }
        });
        // 異步httpclient連接數量配置
        builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {

            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                httpClientBuilder.setMaxConnTotal(maxConnectNum);
                httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
                httpClientBuilder.setSSLStrategy(sessionStrategy);
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                return httpClientBuilder;
            }
        });
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

}
