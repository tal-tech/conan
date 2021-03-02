/*
package com.tal.wangxiao.conan.common.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class RestHightESConfig {

    public static int CONNECT_TIMEOUT_MILLIS = 1500;
    public static int SOCKET_TIMEOUT_MILLIS = 90000;
    public static int CONNECTION_REQUEST_TIMEOUT_MILLIS = 1500;
    public static int MAX_CONN_PER_ROUTE = 100;
    public static int MAX_CONN_TOTAL = 100;
    @Value("${elasticSearch.host}")
    private String host;
    @Value("${elasticSearch.port}")
    private int port;

    private RestClientBuilder builder;
    private RestClient restClient;
    private RestHighLevelClient restHighLevelClient;



    @Primary
    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient init() {
        builder = RestClient.builder(new HttpHost(host, port, "http"))
                .setMaxRetryTimeoutMillis(5 * 60 * 1000);
        setConnectTimeOutConfig();
        setMutiConnectConfig();
        restClient = builder.build();
        restHighLevelClient = new RestHighLevelClient(builder);
        System.out.println("init restHighLevelClient");
        return restHighLevelClient;
    }




    // 配置连接时间延时
    public void setConnectTimeOutConfig() {
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
            return requestConfigBuilder;
        });
    }

    // 使用异步httpclient时设置并发连接数
    public void setMutiConnectConfig() {
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
            System.out.println(MAX_CONN_TOTAL + "MAX_CONN_TOTAL");
            return httpClientBuilder;
        });
    }


}
*/
