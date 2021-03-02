package com.tal.wangxiao.conan.admin.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HTTP方法枚举类
 *
 * @author conan
 */
@Getter
@AllArgsConstructor
public enum HttpMethod {
    GET("GET", 0),
    POST("POST", 1),
    PUT("PUT", 2),
    DELETE("DELETE", 3),
    HEAD("HEAD", 4),
    CONNECT("CONNECT", 5),
    OPTIONS("OPTIONS", 6),
    TRACE("TRACE", 7);

    private String label;
    private Integer value;
}
