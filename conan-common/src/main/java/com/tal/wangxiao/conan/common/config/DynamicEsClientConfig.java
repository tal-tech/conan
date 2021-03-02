package com.tal.wangxiao.conan.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

/**
 * @author: dengkunnan
 * @date: 2021-01-04 14:51
 * @description: 用于动态装配ES 数据源对象
 **/
@Slf4j
@Component
public class DynamicEsClientConfig {
    public static int CONNECT_TIMEOUT_MILLIS = 1500;
    public static int SOCKET_TIMEOUT_MILLIS = 90000;
    public static int CONNECTION_REQUEST_TIMEOUT_MILLIS = 1500;
    public static int MAX_CONN_PER_ROUTE = 100;
    public static int MAX_CONN_TOTAL = 100;


    private RestClientBuilder builder;

    private RestClient restClient;

    private RestHighLevelClient restHighLevelClient;


    /**
     * @param host es ip 可以是域名
     * @param port es端口
     * @return RestHighLevelClient
     * @Description 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public RestHighLevelClient getRestHighLevelClient(String host, int port) {
        builder = RestClient.builder(new HttpHost(host, port, "http"))
        .setMaxRetryTimeoutMillis(5 * 60 * 1000);
        setConnectTimeOutConfig();
        setMutiConnectConfig();
        restClient = builder.build();
        restHighLevelClient = new RestHighLevelClient(builder);
        log.info("init restHighLevelClient success");
        return restHighLevelClient;
    }

    public RestHighLevelClient getRestHighLevelClient() {
        return restHighLevelClient;
    }


    /**
     * 配置连接时间延时
     */
    private void setConnectTimeOutConfig() {
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            requestConfigBuilder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
            requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
            return requestConfigBuilder;
        });
    }

    /**
     * 使用异步httpclient时设置并发连接数
     */
    private void setMutiConnectConfig() {
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(MAX_CONN_TOTAL);
            httpClientBuilder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
            System.out.println(MAX_CONN_TOTAL + "MAX_CONN_TOTAL");
            return httpClientBuilder;
        });
    }

}
