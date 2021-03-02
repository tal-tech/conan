package com.tal.wangxiao.conan.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * HTTP工具类
 * @author conan
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static RestTemplate restTemplate = new RestTemplate();

    //http请求格式
    public enum Type{
        JSON, FORM, PARAM
    }

    /**
     * http请求
     * @param method 请求方法
     * @param url 请求路径
     * @param body 请求体
     * @param headers 请求Header头
     * @param response 相应结果的数据类型
     * @param requestType 请求方式
     * @param <T> 返回的数据类型
     * @return 相应接口的body
     * @throws Exception
     */
    public static <T> T request(HttpMethod method, String url, String body, Map<String,Object> headers, Class<T> response, Type requestType) throws  RestClientException {
        ResponseEntity<T> responseEntity = doRequest(method, url, body, headers, response,requestType);
        return responseEntity.getBody();
    }

    public static <T> T request(HttpMethod method, String url, String body, Map<String,Object> headers,String cookie , Class<T> response, Type requestType) throws  RestClientException {
        headers.put("Cookie",cookie);
        ResponseEntity<T> responseEntity = doRequest(method, url, body, headers, response,requestType);
        return responseEntity.getBody();
    }

    //钉钉URI请求
    public static <T> T request(HttpMethod method, URI url, String body, Map<String,Object> headers, Class<T> response, Type requestType) throws  RestClientException {
        ResponseEntity<T> responseEntity = doRequest(method, url, body, headers, response,requestType);
        return responseEntity.getBody();
    }

    /**
     * 获取response相应实体
     * @param method 请求方法
     * @param url 请求url
     * @param body 请求body
     * @param headers 请求header头
     * @param response 请求相应结果的数据类型
     * @param requestType 请求方式
     * @param <T> 返回的数据类型
     * @return response的相应实体
     * @throws RestClientException 请求异常接口非2XX
     */
    public static <T> ResponseEntity<T> doRequest(HttpMethod method, String url, String body, Map<String,Object> headers, Class<T> response, Type requestType) throws RestClientException {
        //解决中文乱码问题
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<T> responseEntity;
        responseEntity = restTemplate.exchange(url, method, getHttpEntity(body, headers, requestType), response);
        return responseEntity;
    }

    public static <T> ResponseEntity<T> doRequest(HttpMethod method, URI url, String body, Map<String,Object> headers, Class<T> response, Type requestType) throws RestClientException {
        //解决中文乱码问题
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<T> responseEntity;
        responseEntity = restTemplate.exchange(url, method, getHttpEntity(body, headers, requestType), response);
        return responseEntity;
    }

    /**
     * 获取HttpEntity
     * @param bodyString 请求body
     * @param headerMaps 请求Map
     * @param requestType 请求方式
     * @return
     */
    private static HttpEntity getHttpEntity(String bodyString, Map<String,Object> headerMaps,Type requestType) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity requestEntity = new HttpEntity(bodyString,headers);
        //添加header
        if (headerMaps.size() > 0) {
            for (Entry<String, Object> entry : headerMaps.entrySet()) {
                headers.add(entry.getKey(), entry.getValue().toString());
            }
        }
        if(Type.FORM.equals(requestType)){
            //form表单请求
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> parameters = paramsToMap(bodyString);
            if (parameters.size() > 0) {
                requestEntity = new HttpEntity<>(parameters, headers);
            } else {
                requestEntity = new HttpEntity<>(null, headers);
            }
        }else if(Type.JSON.equals(requestType)||Type.PARAM.equals(requestType)){
            if(Type.JSON.equals(requestType)){
                //JSON请求添加特殊header即可
                headers.setContentType(MediaType.APPLICATION_JSON);
            }
            if (!StringUtils.isEmpty(bodyString)) {
                requestEntity = new HttpEntity<>(bodyString, headers);
            } else {
                requestEntity = new HttpEntity<>(headers);
            }
        }
        return requestEntity;
    }


    //请求参数格式转化
    public static MultiValueMap<String,Object> paramsToMap(String body){
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        if(!Objects.isNull(body)){
            //itemId=112003271415222864 &
            String[] paramsArr = body.split("&");
            for (String paramStr: paramsArr) {
                String[] paramArr = paramStr.split("=");
                if(paramArr.length>1){
                    parameters.add(paramArr[0],paramArr[1]);
                }
            }
        }
        return parameters;
    }



}
