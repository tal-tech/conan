package com.tal.wangxiao.conan.utils.es;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网关ES的工具类
 * @author huyaoguo
 * @data 2020/03/27
 */
public class EsUtils {

    //替换es中存储的特殊字符 \x0A  \n.  \x0D \r.   \x22 ".
    public static String replaceSpeStr(String str) {
        if (str.contains("\\x22")) {
            str = str.replace("\\x22", "\"");
        }
        if (str.contains("\\x0A")) {
            str = str.replace("\\x0A", "\n");
        }
        if (str.contains("\\x0D")) {
            str = str.replace("\\x0D", "\r");
        }
        if (str.contains("\\x5C")) {
            str = str.replace("\\x5C", "\\");
        }
        if(str.contains("%2A")){
            str = str.replace("%2A","*");
        }
        if(str.contains("%2B")){
            str = str.replace("%2B","+");
        }
        if(str.contains("%2C")){
            str = str.replace("%2C",",");
        }
        if(str.contains("%2F")){
            str = str.replace("%2F","/");
        }
        if(str.contains("%22")){
            str = str.replace("%22","\"");
        }
        if(str.contains("%24")){
            str = str.replace("%24","$");
        }
        if(str.contains("%25")){
            str = str.replace("%25","%");
        }
        if(str.contains("%26")){
            str = str.replace("%26","&");
        }
        if(str.contains("%3A")){
            str = str.replace("%3A",":");
        }
        if(str.contains("%3D")){
            str = str.replace("%3D","=");
        }
        if(str.contains("%3F")){
            str = str.replace("%3F","?");
        }
        if(str.contains("%5B")){
            str = str.replace("%5B","[");
        }
        if(str.contains("%5D")){
            str = str.replace("%5D","]");
        }
        if(str.contains("%7B")){
            str = str.replace("%7B","{");
        }
        if(str.contains("%7D")){
            str = str.replace("%7D","}");
        }
        return str;
    }
    //获取es中request字段的url
    public static String getUrlByRequest(String request){
        try {
            if (request.contains(" ")) {
                request = request.split(" ")[1];
            }
            return request.trim();
        }catch (Exception e){
            return null;
        }
    }

    //获取es中的request字段的方法
    public static String getMethodByRequest(String request) {
        try {
            if (request.contains(" ")) {
                request = request.split(" ")[0];
            }
            return request;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 忽略静态资源
     * @param uri
     * @return
     */
    public static Boolean shouldIgnore(String uri) {
        String regex = "(^/static/\\w+)|(\\.(jpg|jpeg|json|svg|png|gif|js|css|ttf|ico|map|html|htm|woff2|woff)$)|(__webpack_hmr)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 将路径参数替换为占位符"{pp}"
     * @param url
     * @return
     */
    public static String replacePathParam(String url) {
        String uri = truncateUrl(url);
        String regex = "/(([0-9]|-|_|,)+|([0-9a-zA-Z]){32})(/|$)";
        Pattern pattern = Pattern.compile(regex);
        while (pattern.matcher(uri).find()) {
            uri = uri.replaceAll(regex, "/\\{pp\\}/");
        }
        String parsedUri = uri.replaceAll("/$", "");
        if (parsedUri.isEmpty()) {
            parsedUri = "/";
        }
        return parsedUri;
    }


    /**
     * 返回URL中的URI部分
     * @param url
     * @return
     */
    public static String truncateUrl(String url) {
        String urlStr = url.trim();
        String[] arrSplit = urlStr.split("[?]");
        String newStr = arrSplit[0];
        if (newStr.contains("http")) {
            newStr = newStr.replaceAll("^http(s)?://[a-z0-9.:]+/", "/");
        }
        return newStr.trim();
    }

    //通过网关Es的request格式来处理路径参数的匹配
    public static String getRequestWithMatchEs(String url,String method){
        String result = "";
        String regex = "\\{\\S+}";
        String urlScheme = url.replaceAll(regex, "*");
        if("POST".equals(method)){
            result = method+" "+urlScheme+" *";
        }else{
            result = method+" "+urlScheme+"*";

        }
        return result;
    }

    //es正则获取
    public static String getParamIdByRequestBody(String requestBody, String paramId){
        String planId = "";
        //int index = requestBody.indexOf("plan_id=");
        //String s1 = "\"liveId\":\"(\\d+)\"";
        String s1 =paramId+".(\\d+)";
        Pattern p1 = Pattern.compile(s1);
        Matcher m1 = p1.matcher(requestBody);
        if (m1.find()) {
            return m1.group(1);
        }
        return planId;
    }
    //body参数替换
    public static String replaceParamIdByRequestBody(String requestBody, String paramId, String replaceValue){
        //String s1 =paramId+"=.(\\d+)";
        //String s1 =paramId+"=.([\\s\\S])";
        requestBody += "&";
        String s1 = paramId+"=.*?(?=&)";
        Pattern p1 = Pattern.compile(s1);
        Matcher m1 = p1.matcher(requestBody);
        if (m1.find()) {
            System.out.println(m1.group(0));
            requestBody = requestBody.replace(m1.group(0),paramId+"="+replaceValue);
        }
        return requestBody.substring(0,requestBody.length()-1);
    }

}

