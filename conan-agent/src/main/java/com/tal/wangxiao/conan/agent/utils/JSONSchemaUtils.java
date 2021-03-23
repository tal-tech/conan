package com.tal.wangxiao.conan.agent.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.JsonNodeReader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.tal.wangxiao.conan.utils.schema.JSONSchemaResult;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ：dengkunnan
 * @date ：2020.05.25
 * @description：JSONSchemaUtils Json schema 验证工具
 * 1.对string json 或json 文件和基准string json 验证
 * 2.精准结构下，精准数据替换
 * @modified By：
 * @version: 1.0.0.1
 */
@Slf4j
public class JSONSchemaUtils {
    
    /**
     * @param jsonSchema schemaNode 基准模版
     * @param jsonStr    待替换json
     * @descrption 针对json中所有结构中配置replace替换数据进行精准数据替换
     */
    public static String preciseReplacement(String jsonSchema, String jsonStr) {
        try {
            JsonNode schemaNode = JsonLoader.fromString(jsonSchema);
            //通过jsonStr字符串获得对应JsonNode对象
            JsonNode jsonStrNode = JsonLoader.fromString(jsonStr);
            getObjectTree(schemaNode, jsonStrNode);
            String text = jsonStrNode.asText();
            return text;
        } catch (Exception e) {
            log.error("json转换JsonNode失败， 请检查JSON" + e);
            return "";
        }
    }

    /**
     * @param schemaNode schemaNode 基准模版
     * @param strJson    待替换json
     * @descrption 针对json中所有结构中配置replace替换数据进行精准数据替换
     */
    private static void getObjectTree(JsonNode schemaNode, JsonNode strJson) {
        Iterator<Map.Entry<String, JsonNode>> schemMap = schemaNode.fields();
        Iterator<Map.Entry<String, JsonNode>> strMap = strJson.fields();
        while (strMap.hasNext()) {
            schemMap.hasNext();
            Map.Entry<String, JsonNode> strNewMap = strMap.next();
            Map.Entry<String, JsonNode> schemNewMap = schemMap.next();

            if (strNewMap.getValue() instanceof ObjectNode) {
                getObjectTree(schemNewMap.getValue().get("properties"), strNewMap.getValue());
            } else if (strNewMap.getValue() instanceof ArrayNode) {
                getListTree(schemNewMap.getValue(), (ArrayNode) strNewMap.getValue());
            }

            JsonNode replaceMap = schemaNode.get(strNewMap.getKey());

            if (replaceMap instanceof ObjectNode) {
                if (StringHandlerUtils.isNull(replaceMap) || StringHandlerUtils.isNull(replaceMap.get("replace"))) {
                    continue;
                }
                strNewMap.setValue(replaceMap.get("replace"));
            }
        }
    }

    private static void getListTree(JsonNode schemaNode, ArrayNode strJson) {
        JsonNode schmaProperties = schemaNode.get("items");
        JsonNode schmaJsonNode = null;
        for (JsonNode schmaProList : schmaProperties) {
            if (schmaProList instanceof ObjectNode) {
                schmaJsonNode = schmaProList.get("properties");
            }
        }
        if (StringHandlerUtils.isNull(schmaJsonNode)) {
            return;
        }
        Iterator<Map.Entry<String, JsonNode>> map = schmaProperties.fields();
        Iterator<Map.Entry<String, JsonNode>> schemMap = schemaNode.fields();
        for (JsonNode jsonNode : strJson) {
            if (jsonNode instanceof ObjectNode) {
                getObjectTree(schmaJsonNode, jsonNode);
            }
        }
    }


    /**
     * @param schemaNode schemaNode 基准模版
     * @param strJson    待验证json
     * @descrption 只针对json 中 没有出现数组的部分数据替换
     */
    public static void getTreeAndList(JsonNode schemaNode, JsonNode strJson) {
        Iterator<Map.Entry<String, JsonNode>> schemMap = schemaNode.fields();
        Iterator<Map.Entry<String, JsonNode>> strMap = strJson.fields();
        while (strMap.hasNext()) {
            schemMap.hasNext();
            Map.Entry<String, JsonNode> strNewMap = strMap.next();
            Map.Entry<String, JsonNode> schemNewMap = schemMap.next();

            if (strNewMap.getValue() instanceof ObjectNode) {
                getTreeAndList(schemNewMap.getValue().get("properties"), strNewMap.getValue());
            }

            if (schemNewMap.getValue() instanceof ObjectNode) {
                if (StringHandlerUtils.isNull(schemNewMap.getValue().get("replace"))) {
                    continue;
                }
                strNewMap.setValue(schemNewMap.getValue().get("replace"));
            }
        }
    }


    /**
     * @param filePath json schema 文件path
     * @param jsonStr  json 待对比数据
     * @return JSONSchemaResult
     * <p>
     * json 架构验证工具
     */
    public static JSONSchemaResult verifyJsonSchemaByFileUrl(String filePath, String jsonStr) {

        JSONSchemaResult jsonSchemaResult = new JSONSchemaResult();
        if (StringHandlerUtils.isNull(filePath)) {
            jsonSchemaResult.setIsSuccess(false);
            jsonSchemaResult.setDesc("filePath is null");
            log.error("filePath is null");
            return jsonSchemaResult;
        }

        if (StringHandlerUtils.isNull(jsonStr)) {
            jsonSchemaResult.setIsSuccess(false);
            jsonSchemaResult.setDesc("jsonStr is null");
            log.error("jsonStr is null");
            return jsonSchemaResult;
        }
        JsonNode dataNode = getJsonNodeFromFile(filePath);
        return verifyJsonSchema(dataNode, jsonStr);
    }


    /**
     * @param jsonSchema json schema 文件
     * @param jsonStr    json 待对比数据
     * @return JSONSchemaResult
     * <p>
     * json 架构验证工具
     */
    public static JSONSchemaResult verifyJsonSchemaByString(String jsonSchema, String jsonStr) {

        JSONSchemaResult jsonSchemaResult = new JSONSchemaResult();
        if (StringHandlerUtils.isNull(jsonSchema)) {
            jsonSchemaResult.setIsSuccess(false);
            jsonSchemaResult.setDesc("jsonSchema is null");
            log.error("jsonSchema is null");
            return jsonSchemaResult;
        }

        if (StringHandlerUtils.isNull(jsonStr)) {
            jsonSchemaResult.setIsSuccess(false);
            jsonSchemaResult.setDesc("jsonStr is null");
            log.error("jsonStr is null");
            return jsonSchemaResult;
        }
        //通过jsonSchema获得对应JsonNode对象
        JsonNode schemaNode = null;

        try {
            //通过jsonSchema获得对应JsonNode对象
            schemaNode = JsonLoader.fromString(jsonSchema);
        } catch (IOException e) {
            jsonSchemaResult.setIsSuccess(false);
            jsonSchemaResult.setDesc("e = " + e.getMessage());
            log.error("jsonSchema = " + jsonSchema + "jsonStr" + jsonStr);
            log.error("e = " + e);
            return jsonSchemaResult;

        }
        return verifyJsonSchema(schemaNode, jsonStr);
    }

    /**
     * @param filePath json 文件URL地址
     * @return JsonNode
     * @descrption 通过url路径找到 JsonNode
     */
    public static JsonNode getJsonNodeFromFile(String filePath) {
        JsonNode jsonNode = null;
        try {
            jsonNode = new JsonNodeReader().fromReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException filePath = " + filePath + " e= " + e);
            return null;
        } catch (IOException e) {
            log.error("IOException filePath = " + filePath + " e= " + e);
            return null;
        }
        return jsonNode;
    }

    private static JSONSchemaResult verifyJsonSchema(JsonNode schemaNode, String jsonStr) {
        JSONSchemaResult jsonSchemaResult = new JSONSchemaResult();
        //创建JsonSchema工厂
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        try {
            //通过jsonStr字符串获得对应JsonNode对象
            JsonNode dataNode = JsonLoader.fromString(jsonStr);
            //通过jsonSchema的JsonNode对象获得JsonSchema对象
            JsonSchema schema = factory.getJsonSchema(schemaNode);
            //使用json-schema-validator中JsonSchema对象的validate方法对数据进行校验
            //获得处理的报告信息
            ProcessingReport processingReport = schema.validate(dataNode);
            //获取完整报告信息
            System.out.println(processingReport);
            //判断校验是否成功，如果为true表示成功，否则失败
            System.out.println();
            if (processingReport.isSuccess()) {
                jsonSchemaResult.setIsSuccess(true);
                return jsonSchemaResult;
            }
            jsonSchemaResult.setIsSuccess(false);
            jsonSchemaResult.setDesc(resultHandler(processingReport));
            return jsonSchemaResult;


        } catch (IOException e) {
            jsonSchemaResult.setIsSuccess(false);
            jsonSchemaResult.setDesc("e = " + e.getMessage());
            log.error("e = " + e);
            return jsonSchemaResult;
        } catch (ProcessingException e) {
            jsonSchemaResult.setIsSuccess(false);
            jsonSchemaResult.setDesc("e = " + e.getMessage());
            log.error("e = " + e);
            return jsonSchemaResult;
        }
    }

    private static String resultHandler(ProcessingReport processingReport) {

        StringBuilder str = new StringBuilder();
        Iterator<ProcessingMessage> object = processingReport.iterator();
        int count = 0;
        while (object.hasNext()) {

            ProcessingMessage processingMessage = object.next();
            if ("warning".equals(processingMessage.getLogLevel().toString())) {
                continue;
            }
            String resultMsg = "";
            JsonNode jsonNode = processingMessage.asJson();
            //获取丢失必要参数
            resultMsg = resultMsg + JSONSchenaReportUtils.getMissing(jsonNode);
            resultMsg = resultMsg + JSONSchenaReportUtils.getType(jsonNode);
            resultMsg = resultMsg + JSONSchenaReportUtils.getMinimum(jsonNode);
            resultMsg = resultMsg + JSONSchenaReportUtils.getMaximum(jsonNode);
            resultMsg = resultMsg + JSONSchenaReportUtils.getMultipleOf(jsonNode);

            resultMsg = resultMsg + "导致错误描述" + count + " : " + jsonNode.get("message");
            str.append("{" + resultMsg + "}");
            count++;
        }

        log.debug("校验结果：" + str);
        return str.toString();
    }
}