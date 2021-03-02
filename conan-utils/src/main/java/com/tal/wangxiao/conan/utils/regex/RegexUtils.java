package com.tal.wangxiao.conan.utils.regex;

import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据正则表达式获取对应字符串
 *
 * @author liujinsong
 * @date 2021/1/18
 **/
public class RegexUtils {

    public static String getMsgByRegex(String msg,String regex) {
        //正则表达式为空时直接返回传入字符串
        if("".equals(regex)||Objects.isNull(regex)){
            return msg;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher= pattern.matcher(msg);
        while (matcher.find()){
            return matcher.group();
        }
        return null;
    }

}
