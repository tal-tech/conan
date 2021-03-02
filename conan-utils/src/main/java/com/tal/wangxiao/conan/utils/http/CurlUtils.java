package com.tal.wangxiao.conan.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * curl请求工具类
 *
 * @author huyaoguo
 * @date 2021/2/22
 **/
public class CurlUtils {

    public static String getBodyByCurl(String curlUrl) {
        String[] cmd = curlUrl.split(" ");
        ProcessBuilder process = new ProcessBuilder(cmd);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();

        } catch (IOException e) {
            System.out.print("error，"+e.getMessage());
        }
        return null;
    }

    public static String getCookieByCurl(String curlUrl,String headerKey) {
        String cookieFromHead = "";
        String[] cmd = curlUrl.split(" ");
        ProcessBuilder process = new ProcessBuilder(cmd);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
                if(line.contains(":")){
                    String[] str = line.split(":");
                    if(headerKey.equals(str[0])){
                        cookieFromHead += str[1];
                    }
                }
            }
            return cookieFromHead.trim();

        } catch (IOException e) {
            System.out.print("error，"+e.getMessage());
        }
        return null;
    }







    public static void main(String[] args) {

        String s = "curl -s -D- -o/dev/null https://teacherlive.xueersi.com/v1/teacher/login -H Content-Type:application/json -d {\"password\":\"java2019.\",\"userName\":\"huyaoguo\",\"teacherType\":2}";
//        System.out.println(execCurl(s));
//        System.out.println(getCookieByCurl(s,"set-cookie"));


    }

}
