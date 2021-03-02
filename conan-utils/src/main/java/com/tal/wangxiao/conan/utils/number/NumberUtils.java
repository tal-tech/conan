package com.tal.wangxiao.conan.utils.number;

import java.text.DecimalFormat;

/**
 * 数字工具类
 *
 * @author huyaoguo
 * @date 2021/1/14
 **/
public class NumberUtils {

    /**
     * 数字格式化3位数加逗号分隔（1000->1，000）
     * @param number 被转化数字
     * @return
     */
    public static String numberFormat(int number){
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(number);
    }
}
