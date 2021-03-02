package com.tal.wangxiao.conan.admin.service.impl;

import com.tal.wangxiao.conan.admin.service.DebugService;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.bo.ApiInfo;
import com.tal.wangxiao.conan.common.utils.HttpUtil;
import com.tal.wangxiao.conan.utils.str.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 接口调试实体类
 *
 * @author huyaoguo
 * @date 2019/11/26
 * @create: 2019-11-26 16:09
 **/
@Service
@Slf4j
public class DebugServiceImpl implements DebugService {

    @Override
    public Result debugOneApi(ApiInfo apiInfo) {
        String response = "";
        //先将header的string转化为map格式
        Map<String, Object> headerMap = StringUtil.headersConvertMap(apiInfo.getHeader());
        //参数处理
        String parms = "";
        if (apiInfo.getParms() != null) {
            parms = apiInfo.getParms().replace(";", "&");
        }
        String url = apiInfo.getUrl();
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        String body = "";
        try {
            url = truncateURL(url);
            if (("GET").equals(apiInfo.getMethod())) {
                //针对get请求防止出现url与参数同时存在的问题 还是处理一下url
                if (!"".equals(parms)) {
                    url = url + "?" + parms;
                }
                response = HttpUtil.request(HttpMethod.valueOf(apiInfo.getMethod()), url, parms, headerMap, String.class, HttpUtil.Type.PARAM);
            } else if (apiInfo.getHeader() != null && apiInfo.getHeader().contains("application/x-www-form-urlencoded")) {
                //    public static <T> T request(HttpMethod method, String url, String body, Map<String,Object> headers, Class<T> response, Type requestType) throws  RestClientException {
                response = HttpUtil.request(HttpMethod.valueOf(apiInfo.getMethod()), url, parms, headerMap, String.class, HttpUtil.Type.FORM);
            } else {
                response = HttpUtil.request(HttpMethod.valueOf(apiInfo.getMethod()), url, parms, headerMap, String.class, HttpUtil.Type.JSON);

            }
            log.info("接口请求 HTTP {} url={}", apiInfo.getMethod(), url);
        } catch (Exception e) {
            response = e.getMessage();
            log.info("接口请求失败 HTTP {} url={},error_message = {}", apiInfo.getMethod(), url, e.getMessage());
        }
        response = StringUtil.unicodeToString(response);
        return new Result<>(ResponseCode.SUCCESS, response);
    }

    private static String truncateURL(String url) {
        url = url.replace("https", "http");
        int index = url.indexOf("?");
        if (index >= 0) {
            url = url.substring(0, index);
        }
        return url;
    }
}
