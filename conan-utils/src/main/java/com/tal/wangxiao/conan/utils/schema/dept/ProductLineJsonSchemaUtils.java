package com.tal.wangxiao.conan.utils.schema.dept;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.github.fge.jackson.JsonLoader;
import com.tal.wangxiao.conan.utils.core.constant.DataType;
import com.tal.wangxiao.conan.utils.schema.JSONSchemaResult;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class ProductLineJsonSchemaUtils {

    public static void main(String arge[]) {
        String sc = "{\n" +
                "\t\"stat\": {\n" +
                "\t\t\"type\": \"number\",\n" +
                "\t\t\"minimum\": 0,\n" +
                "\t\t\"maximum\": 1\n" +
                "\t},\n" +
                "\n" +
                "\t\"status\": {\n" +
                "\t\t\"type\": \"string\",\n" +
                "\t\t\"equals\": true\n" +
                "\t}\n" +
                "}";

        //String jsonstr = "{\"stat\":2,\"data\":{\"id\":\"21510168\",\"uid\":\"21514649\",\"name\":\"21510168\",\"tal_id\":\"TalzJcmWz-EFWJ9Ve17iTR-Onw\",\"role\":\"1\",\"sex\":\"3\",\"realname\":\"\",\"nickname\":\"学员KuNY5K\",\"en_name\":\"\",\"avatar_path\":\"https:\\/\\/hwt.xesimg.com\\/user\\/h\\/wx-def20191008.png\",\"avatar_version\":\"0\",\"auth\":\"99\",\"status\":\"1\",\"create_time\":\"2019-09-08 17:03:09\",\"user_id\":\"21510168\",\"grade_id\":2,\"grade_name\":\"一年级\",\"grade_alias\":\"xiao1\"}}";
        String jsonstr = "{\n" +
                "  \"status\": true,\n" +
                "  \"msg\": \"Execute Success\",\n" +
                "  \"data\": {\n" +
                "    \"correct\": 0,\n" +
                "    \"correct_soon\": 0,\n" +
                "    \"correct_timeout\": 0,\n" +
                "    \"contact\": 0,\n" +
                "    \"contact_soon\": 0,\n" +
                "    \"contact_timeout\": 0,\n" +
                "    \"question\": 0,\n" +
                "    \"question_soon\": 0,\n" +
                "    \"question_timout\": 0,\n" +
                "    \"question_duty_task\": 0,\n" +
                "    \"rate\": \"0%\",\n" +
                "    \"rank\": 5954,\n" +
                "    \"to_be_done\": 0,\n" +
                "    \"done\": 0,\n" +
                "    \"timeout\": 0,\n" +
                "    \"live_remind\": [\n" +
                "      {\n" +
                "        \"plan_start_time\": 1598871600,\n" +
                "        \"plan_id\": 733002,\n" +
                "        \"stime_format\": \"2020-08-31 19:00:00\",\n" +
                "        \"plan_name\": \"【阅】关于你的故事——妙解人物形象（2课时）\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"plan_start_time\": 1598871600,\n" +
                "        \"plan_id\": 733002,\n" +
                "        \"stime_format\": \"2020-08-31 19:00:00\",\n" +
                "        \"plan_name\": \"【阅】关于你的故事——妙解人物形象（2课时）\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"time\": \"423.7ms\",\n" +
                "    \"refund_contact\": 0,\n" +
                "    \"refund_soon\": 0,\n" +
                "    \"refund_timeout\": 0,\n" +
                "    \"taday_video_task\": 0,\n" +
                "    \"tomorrow_video_task\": 0,\n" +
                "    \"all_video_task\": 0\n" +
                "  }\n" +
                "}";

        String rule3 = "{\n" +
                "\"ret\":{\n" +
                " \"equals\":\"true\"\n" +
                "},\n" +
                "\"msg\":{\n" +
                " \"equals\":\"操作成功\"\n" +
                "}\n" +
                "}";
        String str3 = "{\n" +
                "  \"ret\" : true,\n" +
                "  \"msg\" : \"操作成功1\",\n" +
                "  \"data\" : {\n" +
                "    \"records\" : [ ],\n" +
                "    \"total\" : 0,\n" +
                "    \"size\" : 10,\n" +
                "    \"current\" : 1,\n" +
                "    \"searchCount\" : true,\n" +
                "    \"pages\" : 0\n" +
                "  },\n" +
                "  \"errcode\" : 0\n" +
                "}";


        String rule4 = "{\n" +
                "\t\"ret\": {\n" +
                "\t\t\"equals\": \"true\"\n" +
                "\t},\n" +
                "\t\"msg\": {\n" +
                "\t\t\"equals\": \"\\\"操作成功\\\"\"\n" +
                "\t}\n" +
                "}";
        System.out.println(checkProductLineJson(rule4, str3).toString());
        ;
        //System.out.println( checkProductLineJson(sc, jsonstr).toString());;

    }

    public static JSONSchemaResult checkProductLineJson(String jsonSchema, String jsonStr) {
        Map<String, Object> jsonSchemaMap = JSONObject.parseObject(jsonSchema, Map.class);

        JSONSchemaResult jsonSchemaResult = new JSONSchemaResult();
        jsonSchemaResult.setIsSuccess(true);

        try {
            //通过jsonSchema获得对应JsonNode对象
            JsonNode jsonStrNdoe = JsonLoader.fromString(jsonStr);
            getTreeAndList(jsonSchemaMap, jsonStrNdoe, jsonSchemaResult);
            log.error("" + jsonSchemaResult.toString());
        } catch (Exception e) {
            jsonSchemaResult.setIsSuccess(false);
            log.error("" + e);
        }

        return jsonSchemaResult;

    }

    /**
     * 只针对json 中 没有出现数组的部分
     *
     * @param jsonSchemaMap demo: {
     *                      "status": {
     *                      "type": "integer",
     *                      "multipleOf": 1.5,
     *                      "minimum": 5,
     *                      "maximum": 10
     *                      },
     *                      "status1": {
     *                      "name": "xx"
     *                      }
     *                      }
     */
    private static void getTreeAndList(Map<String, Object> jsonSchemaMap, JsonNode strJson, JSONSchemaResult jsonSchemaResult) {
        Iterator<Map.Entry<String, JsonNode>> strMap = strJson.fields();
        StringBuilder result = new StringBuilder();
        while (strMap.hasNext()) {

            Map.Entry<String, JsonNode> strNewMap = strMap.next();


            if (strNewMap.getValue() instanceof ObjectNode) {
                getTreeAndList(jsonSchemaMap, strNewMap.getValue(), jsonSchemaResult);
            } else if (strNewMap.getValue() instanceof ArrayNode) {
                continue;
            }

            if (strNewMap.getValue() instanceof ObjectNode || strNewMap.getValue() instanceof ArrayNode) {
                continue;
            }
            Object replaceMap = jsonSchemaMap.get(strNewMap.getKey());
            if (StringHandlerUtils.isNull(replaceMap)) {
                continue;
            }
            Map<String, Object> jsonSchemaMapByThisKey = JSONObject.parseObject(replaceMap.toString(), Map.class);
            result.append(checkType(jsonSchemaMapByThisKey.get("type"), strNewMap.getValue(), strNewMap.getKey(), jsonSchemaResult));
            if (strNewMap.getValue() instanceof NumericNode) {
                result.append(checkMultipleOf(jsonSchemaMapByThisKey.get("multipleOf"), strNewMap.getValue(), strNewMap.getKey(), jsonSchemaResult));
                result.append(checkMinimum(jsonSchemaMapByThisKey.get("minimum"), strNewMap.getValue(), strNewMap.getKey(), jsonSchemaResult));
                result.append(checkMaximum(jsonSchemaMapByThisKey.get("maximum"), strNewMap.getValue(), strNewMap.getKey(), jsonSchemaResult));
                continue;
            } else if (!StringHandlerUtils.isNull(jsonSchemaMapByThisKey.get("equals"))) {
                result.append(checkStr(jsonSchemaMapByThisKey.get("equals"), strNewMap.getValue(), strNewMap.getKey(), jsonSchemaResult));
            }

        }

        if (StringHandlerUtils.isNull(jsonSchemaResult.getDesc())) {
            jsonSchemaResult.setDesc(result);
        } else {
            jsonSchemaResult.setDesc(jsonSchemaResult.getDesc() + " | " + result);
        }


    }

    /**
     * string	string 1
     * number	int/float 2
     * object	dict
     * array	list
     * boolean	bool
     * null	None
     */
    private static String checkType(Object type, Object strData, String key, JSONSchemaResult jsonSchemaResult) {
        try {
            return checkeTypeExcute(type, strData, key, jsonSchemaResult);
        } catch (Exception e) {
            return "产品线规则类型配置错误（未知类型）请检查 | ";
        }
    }

    private static String checkeTypeExcute(Object type, Object strData, String key, JSONSchemaResult jsonSchemaResult) {
        String result = "";
        if (StringHandlerUtils.isNull(type)) {
            return "";
        }

        String typeStr = StringHandlerUtils.lowerFirst(type.toString());
        char[] chars = typeStr.toCharArray();
        //解决枚举不能出现boolean 关键字问题
        if ("b".equals(chars[0] + "")) {
            chars[0] = 'B';
        }
        switch (DataType.getDataType(new String(chars))) {
            case string:
                if (!(strData instanceof TextNode)) {
                    result = "类型错误,定义类型string，" + key + "的实际类型" + getFiledsInfo(strData);
                    jsonSchemaResult.setIsSuccess(false);
                }
                break;
            case integer:
                if (!(strData instanceof IntNode)) {
                    result = "类型错误，定义类型Integer，" + key + "的实际类型" + getFiledsInfo(strData);
                    jsonSchemaResult.setIsSuccess(false);
                }
                break;
            case number:
                if (!(strData instanceof NumericNode)) {
                    result = "类型错误，定义类型Number，" + key + "的实际类型" + getFiledsInfo(strData);
                    jsonSchemaResult.setIsSuccess(false);
                }
                ;
            case object:
                if (!(strData instanceof Object)) {
                    result = "类型错误，定义类型Object，" + key + "的实际类型" + getFiledsInfo(strData);
                    jsonSchemaResult.setIsSuccess(false);
                }
                break;
            case array:
                if (!(strData instanceof ArrayNode)) {
                    result = "类型错误,定义类型Array，" + key + "的实际类型" + getFiledsInfo(strData);
                    jsonSchemaResult.setIsSuccess(false);
                }
                break;
            case Boolean:
                if (!(strData instanceof BooleanNode)) {
                    result = "类型错误,定义类型Boolean，" + key + "的实际类型" + getFiledsInfo(strData);
                    jsonSchemaResult.setIsSuccess(false);
                }
                break;
        }
        return result;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     */
    private static List getFiledsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        List list = new ArrayList();
        Map infoMap = null;
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap();
            if (!StringHandlerUtils.isNull(fields[i].getType())) {
                String str = fields[i].getType().toString();
                String[] strNumber = str.split("\\.");
                if (!StringHandlerUtils.isNull(strNumber)) {
                    String type = strNumber[strNumber.length - 1];
                    infoMap.put("type" , type);
                }

            }
            //只要一个就够了
            //infoMap.put("name", fields[i].getName());
            list.add(infoMap);
            return list;
        }
        return list;
    }

    private static String checkMultipleOf(Object multipleOf, Object strData, String key, JSONSchemaResult jsonSchemaResult) {
        String result = "";
        if (StringHandlerUtils.isNull(multipleOf)) {
            return result;
        }
        if (!(multipleOf instanceof Number)) {
            log.error("multipleOf 类型配置错误");
            return result;
        }
        if (strData instanceof Number) {
            return result;
        }
        try {
            Double multipleOfNumber = Double.parseDouble(multipleOf.toString());
            Double strDataOfNumber = Double.parseDouble(strData.toString());
            if (strDataOfNumber % multipleOfNumber != 0) {
                jsonSchemaResult.setIsSuccess(false);
                result = "数据不是" + multipleOfNumber + "的倍数，" + key + " = " + strDataOfNumber;

            }
        } catch (Exception e) {
            log.error("类型转换错误" + e.getMessage());
        }
        return result;

    }


    private static String checkMaximum(Object maximum, Object strData, String key, JSONSchemaResult jsonSchemaResult) {
        String result = "";
        if (StringHandlerUtils.isNull(maximum)) {
            return result;
        }
        if (!(maximum instanceof Number)) {
            log.error("multipleOf 类型配置错误");
            return result;
        }
        if (strData instanceof Number) {
            return result;
        }
        try {
            Double maximumNumber = Double.parseDouble(maximum.toString());
            Double strDataOfNumber = Double.parseDouble(strData.toString());
            if (strDataOfNumber > maximumNumber) {
                jsonSchemaResult.setIsSuccess(false);
                result = "数据大于最大值，maximum =" + maximumNumber + "，" + key + " = " + strDataOfNumber;

            }
        } catch (Exception e) {
            log.error("类型转换错误" + e.getMessage());
        }
        return result;

    }

    private static String checkMinimum(Object miniMum, Object strData, String key, JSONSchemaResult jsonSchemaResult) {
        String result = "";
        if (StringHandlerUtils.isNull(miniMum)) {
            return result;
        }
        if (!(miniMum instanceof Number)) {
            log.error("multipleOf 类型配置错误");
            return result;
        }
        if (strData instanceof Number) {
            return result;
        }
        try {
            Double miniMumNumber = Double.parseDouble(miniMum.toString());
            Double strDataOfNumber = Double.parseDouble(strData.toString());
            if (strDataOfNumber < miniMumNumber) {
                jsonSchemaResult.setIsSuccess(false);
                result = "数据小于最小值，miniMum =" + miniMumNumber + "，" + key + " = " + strDataOfNumber;

            }
        } catch (Exception e) {
            log.error("类型转换错误" + e.getMessage());
        }
        return result;
    }


    private static String checkStr(Object equals, Object strData, String key, JSONSchemaResult jsonSchemaResult) {
        String result = "";
        if (StringHandlerUtils.isNull(equals)) {
            return result;
        }
        if (StringHandlerUtils.isNull(strData)) {
            return result;
        }

        try {

            if (!equals.toString().trim().equals(strData.toString().trim())) {
                jsonSchemaResult.setIsSuccess(false);
                result = "数据不等于指定值，预期值 = " + equals + "，实际值" + key + " = " + strData;
            }
        } catch (Exception e) {
            log.error("类型转换错误" + e.getMessage());
        }
        return result;

    }

}
