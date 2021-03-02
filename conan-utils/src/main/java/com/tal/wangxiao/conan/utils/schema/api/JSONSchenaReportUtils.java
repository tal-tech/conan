package com.tal.wangxiao.conan.utils.schema.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ：dengkunnan
 * @date ：2020.06.24
 * @description：JSONSchemaUtils Json schema 报表工具集合
 * @modified By：
 * @version: 1.0.0.1
 */
@Slf4j
public class JSONSchenaReportUtils {

    public static String getMultipleOf(JsonNode jsonNode) {
        JsonNode missing = jsonNode.get("divisor");
        if (StringHandlerUtils.isNull(missing)) {
            return "";
        }
        String result = "响应内容不符合倍数，当前值:";
        JsonNode found = jsonNode.get("value");
        result = result + found + "; 倍数值:";
        if (missing instanceof IntNode) {
            result = result + missing + "; ";
        }
        return result + getErrorKeyAndSchemaKey(jsonNode);
    }

    public static String getMaximum(JsonNode jsonNode) {
        JsonNode missing = jsonNode.get("maximum");
        if (StringHandlerUtils.isNull(missing)) {
            return "";
        }
        String result = "响应内容大于最大值，当前值:";
        JsonNode found = jsonNode.get("found");
        result = result + found + "; 最大值:";
        if (missing instanceof IntNode) {
            result = result + missing + "; ";
        }
        return result + getErrorKeyAndSchemaKey(jsonNode);
    }

    public static String getMinimum(JsonNode jsonNode) {
        JsonNode missing = jsonNode.get("minimum");
        if (StringHandlerUtils.isNull(missing)) {
            return "";
        }
        String result = "响应内容小于于最小值，当前值:";
        JsonNode found = jsonNode.get("found");
        result = result + found + "; 最小值:";
        if (missing instanceof IntNode) {
            result = result + missing + "; ";
        }
        return result + getErrorKeyAndSchemaKey(jsonNode);
    }


    public static String getType(JsonNode jsonNode) {

        JsonNode missing = jsonNode.get("expected");
        if (StringHandlerUtils.isNull(missing)) {
            return "";
        }
        String result = "响应内容类型错误，实际类型:";
        JsonNode found = jsonNode.get("found");
        result = result + found + "; 预期类型:";

        int count = 1;
        if (missing instanceof ArrayNode) {
            for (JsonNode node : missing) {
                result = result + node;
                if (count < missing.size()) {
                    result = result + ",";
                } else {
                    result = result + "; ";
                }
                count++;
            }
        }



        return result + getErrorKeyAndSchemaKey(jsonNode);
    }

    public static String getErrorKeyAndSchemaKey(JsonNode jsonNode) {
        String result = "";
        if(StringHandlerUtils.isNull(jsonNode.get("schema"))) {
            return result;
        }
        JsonNode keyUrl = jsonNode.get("schema").get("pointer");
        if (!StringHandlerUtils.isNull(keyUrl)) {
            try {
                String apiNUmber[] = keyUrl.toString().split("/");
                String apiKey = apiNUmber[apiNUmber.length - 1].substring(0, apiNUmber[apiNUmber.length - 1].length() - 1);
                result = result + "错误JsonKey:" + apiKey + "; ";
            } catch (Exception e) {
                log.error("没有发现错误key e= " + e);
                return "";
            }
        }
        result = result + "Schemakey和路径：" + jsonNode.get("keyword") + "," + keyUrl + "; ";
        return result;
    }


    public static String getMissing(JsonNode jsonNode) {
        JsonNode missing = jsonNode.get("missing");
        String result = "响应内容缺少必要参数，丢失的必要参数Key:";
        if (StringHandlerUtils.isNull(missing)) {
            return "";
        }
        int count = 1;
        if (missing instanceof ArrayNode) {
            for (JsonNode node : missing) {
                result = result + node;
                if (count < missing.size()) {
                    result = result + ",";
                } else {
                    result = result + "; ";
                }
                count++;
            }
            result = result + " 配置的必要参数key";
            int countRe = 1;
            for (JsonNode node : jsonNode.get("required")) {
                result = result + node;
                if (countRe < missing.size()) {
                    result = result + ",";
                } else {
                    result = result + "; ";
                }
                countRe++;
            }
            return result;
        }
        return "";
    }
}
