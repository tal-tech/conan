package com.tal.wangxiao.conan.utils.str;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 关于字符串的处理工具类
 * @author: huyaoguo
 * @date: 2019-11-26
 **/
public class StringUtil {
    /**
     * Header字符串转化为HeadMap
     * @param headerString
     * @return map
     */
    public static Map<String,Object> headersConvertMap(String headerString){
        if(Objects.isNull(headerString)||"".equals(headerString)){
            return new HashMap<>();
        }
        Map<String,Object> headersMap=new HashMap<String,Object>();
        String[] headerSplit = headerString.split(";");
        for(String headerModel:headerSplit){
            String[] headerModelSplit = headerModel.split(":");
            if(headerModelSplit.length>1){
                headersMap.put(headerModelSplit[0],headerModelSplit[1]);
            }

        }
        return headersMap;
    }

    /**
     * Unicode转 汉字字符串
     * @param str \u6728
     * @return '木'
     */
    public static String unicodeToString(String str) {
        if(str==null){
            return "";
        }
        Pattern pattern = compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            //group 6728
            String group = matcher.group(2);
            //ch:'木' 26408
            ch = (char) Integer.parseInt(group, 16);
            //group1 \u6728
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
    }
    /**
     * 字符串数据转化为str[]
     * @param arrStr  [1000,10001]
     * @return str[] = {1000,10001}
     */
    public static String[] strToArrayStr(String arrStr){
        if(Objects.isNull(arrStr)){
            return null;
        }
        //校验字符串格式格式 []
        if(!arrStr.startsWith("[")&&!arrStr.endsWith("]")){
            return null;
        }else {
            if("[]".equals(arrStr)){
                return null;
            }
            arrStr = arrStr.substring(1,arrStr.length()-1);
            return arrStr.split(",");

        }
    }
    /**
     * 字符串去掉空格、回车、换行符、制表符
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }





}
