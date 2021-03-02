package com.tal.wangxiao.conan.common.exception.api;

import com.tal.wangxiao.conan.common.exception.BaseException;

/**
 * 接口自定义异常
 *
 * @author huyaoguo
 * @date 2021/1/7
 **/
public class ApiException extends BaseException {

    private Code code;

    public ApiException(String msg, ApiException.Code code) {
        super(code.toString(),msg);
        this.code = code;
    }

    public ApiException.Code getCode() {
        return code;
    }

    public enum Code {
        NO_API_EXISTS, UNKNOWN
    }
}
