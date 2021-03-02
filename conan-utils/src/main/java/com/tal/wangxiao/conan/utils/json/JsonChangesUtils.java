package com.tal.wangxiao.conan.utils.json;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;

/**
 * json工具类
 *
 * @author huyaoguo
 * @date 2021/1/7
 **/
public class JsonChangesUtils {

    /**
     * json字符串处理
     * @param jsonString Json字符串
     * @return 是否JSON字符串
     */
    public static boolean isJson(String jsonString) {
        jsonString = strReplceHandler(jsonString);
        try {
            JSONObject.parseObject(jsonString);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(jsonString);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    private static String strReplceHandler(String str) {
        if(StringHandlerUtils.isNull(str)) {
            return "";
        }
        if(str.trim().substring(0,1).equals("\"")){
            str = str.substring(1,str.length());
        }
        if(str.trim().substring(str.length()-1,str.length()).equals("\"")) {
            str= str.substring(0,str.length()-1);
        }
        str = str.replace("\\","");
        return str;
    }

    //根据jsonPath获取value
    public static String getValueByJsonPath(String json,String jsonPath){
        return JsonPath.read(json,jsonPath).toString();
    }
}
