package com.tal.wangxiao.conan.utils.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author: dengkunnan
 * @date: 2021-01-10
 * @description: JSON Check Utils
 * isJSONValidByalibaba()    16510 ms	16161 ms	2018 ms
 * isJSONValidByJackson()	 17946 ms	17486 ms	2698 ms
 * isJSONValidByGoogle()     20648 ms	20208 ms	2508 ms
 * <p>
 * <p>
 * 性能相差不太多，范围大推介推荐方式isJSONValidByJackson
 * 平常用isJSONValidByalibaba 就行
 **/
@Slf4j
public class JsonCheckUtils {

    /**
     * 暴力解析:Alibaba fastjson
     *
     * @param test
     * @return
     */
    public static final boolean isJSONValidByalibaba(String test) {
        try {
            JSONObject.parseObject(test);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Jackson library
     *
     * @param jsonInString
     * @return
     */
    public static final boolean isJSONValidByJackson(String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /*   *//**
     * Google Gson
     *
     * @param jsonInString
     * @return
     *//*
    public final static boolean isJSONValidByGoogle(String jsonInString) {
        try {
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }*/

    /**
     * 是否是合法的Gson字符串
     *
     * @param targetStr
     * @return
     */
    private static boolean isGoodGson(String targetStr, Class clazz) {
        if (StringHandlerUtils.isNull(targetStr)) {
            return false;
        }
        try {
            new Gson().fromJson(targetStr, clazz);
            return true;
        } catch (JsonSyntaxException ex) {
            log.error("targetStr={} is not a valid {}" , targetStr, clazz.getName(), ex);
            return false;
        }
    }

    /**
     * 是否是合法的JsonArray (alibaba 认为前1种不是JSON串)
     * 例如：[{a:b}]  [{'a':'b'}]  [{"a":"b"}]
     *
     * @param targetStr
     * @return
     */
    public static boolean isJsonArray(String targetStr) {
        return isGoodGson(targetStr, JsonArray.class);
    }

    /**
     * 是否是合法的JsonObject(alibaba 认为前1种不是JSON串)
     * 例如：{a:b} {'a':'b'} {"a":"b"}
     *
     * @param targetStr
     * @return
     */
    public static boolean isJsonObject(String targetStr) {
        return isGoodGson(targetStr, JsonObject.class);
    }
}
