package com.tal.wangxiao.conan.utils.str;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 字符串处理工具类
 *
 * @author ：huyaoguo
 * @date ：2020/1/1
 */
public class StringHandlerUtils {

    /**
     * Unicode转 汉字字符串
     *
     * @param str \u6728
     * @return '木'
     */
    public static String unicodeToString(String str) {
        if (str == null) {
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
     * 字符串首字母转小写
     *
     * @param str 需要转的字符串
     * @return 处理后的字符串
     */
    public static String lowerFirst(String str) {
        if (isNull(str)) {
            return "";
        }
        char[] chars = str.toCharArray();

        if (!charIs_AZ(chars[0]) && charIs_az(chars[0])) {
            return "";
        }
        if (chars[0] >= 65 && chars[0] <= 90) {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }


    /**
     * 字符串首字母转大写
     *
     * @param str 需要转的字符串
     * @return 处理后的字符串
     */
    public static String increaseFirst(String str) {
        if (isNull(str)) {
            return "";
        }
        char[] chars = str.toCharArray();

        if (!charIs_AZ(chars[0]) && charIs_az(chars[0])) {
            return "";
        }
        if (chars[0] >= 97 && chars[0] <= 122) {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    static boolean charIs_AZ(char a) {
        if (a >= 65 && a <= 90) {
            return true;
        }
        return false;
    }

    static boolean charIs_az(char a) {
        if (a >= 97 && a <= 122) {
            return true;
        }
        return false;
    }


    /**
     * @param str Object
     * @return true/false
     */
    public static boolean isNull(@Nullable Object str) {
        return str == null || "".equals(str);
    }

    /**
     * 获取字符串中的数字
     * @param str 需要传入的字符串
     * @return 得到的字符串中的数字
     */
    public static String getStringNum(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 得到唯一UUID码
     *
     * @return 唯一uuid码
     */
    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 处理特殊字符串
     *
     * @param str 待处理的数据
     * @return 处理后的数据
     */
    public static String replaceSpeStr(String str) {
        //字符串去掉空格、回车、换行符、制表符
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        } else {
            return null;
        }
        //处理流量中包含的特殊字符，例如nginx的unicode码
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
        if (str.contains("%2C")) {
            str = str.replace("%2C", ",");
        }
        if (str.contains("%22")) {
            str = str.replace("%22", "\"");
        }
        if (str.contains("%5B")) {
            str = str.replace("%5B", "[");
        }
        if (str.contains("%5D")) {
            str = str.replace("%5D", "]");
        }
        if (str.contains("%7B")) {
            str = str.replace("%7B", "{");
        }
        if (str.contains("%7D")) {
            str = str.replace("%7D", "}");
        }
        if (str.contains("%3A")) {
            str = str.replace("%3A", ":");
        }
        return str;
    }
}
