package com.tal.wangxiao.conan.utils.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间相关处理类
 *
 * @author huyaoguo
 * @date 2021/1/15
 **/
public class DateHandleUtils {

    /**
     * 指定时间格式处理
     *
     * @param time 传入需要处理时间
     * @return 返回月-日格式（一月一日 -> 01-01）
     */
    public static String dateToStrWithMMdd(LocalDate time) {
        return time.format(DateTimeFormatter.ofPattern("MM-dd"));
    }

    /**
     * 根据时间格式返回字符串
     *
     * @param formatter 时间格式
     * @return 根据时间格式返回字符串
     */
    public static String getNowWithFormatter(String formatter) {
        String res = "";
        try {
            res = LocalDateTime.now().format(DateTimeFormatter.ofPattern(formatter));
        } catch (Exception e) {
            System.out.println("格式转化异常：" + e);
            return res;
        }
        return res;
    }

}
