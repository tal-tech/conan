package com.tal.wangxiao.conan.common.exception;

import lombok.Getter;

/**
 * 自定义基础异常
 * @author huyaoguo
 * @date 2020/12/2
 **/
@Getter
public class BaseException extends RuntimeException {

    private String retCode; //异常对应的返回码
    private String msgDesc; //异常对应的描述信息

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
        msgDesc = message;
    }

    public BaseException(String retCode,String message) {
        super();
        this.retCode = retCode;
        this.msgDesc = message;
    }

    public BaseException(ExceptionCode exceptionCode) {
        super();
        this.retCode = exceptionCode.getCode().toString();
        this.msgDesc = exceptionCode.getDesc();
    }
}
