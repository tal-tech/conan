package com.tal.wangxiao.conan.utils.math;

import java.text.NumberFormat;

/**
 * json工具类
 *
 * @author huyaoguo
 * @date 2021/1/7
 **/

public class MathUtils {


    /**
     * 计算比例
     *
     * @param molecule    分子
     * @param denominator 分母
     */
    public static String getRate(int molecule, int denominator) {
        if (denominator == 0) {
            return "0";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String successRate = numberFormat.format((float) molecule / (float) denominator * 100);
        return successRate + "%";
    }
}