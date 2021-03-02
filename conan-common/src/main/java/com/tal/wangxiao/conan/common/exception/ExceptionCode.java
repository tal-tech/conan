package com.tal.wangxiao.conan.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义异常枚举类
 * @author huyaoguo
 * @date 2020/12/02
 */
@AllArgsConstructor
@Getter
public enum ExceptionCode {
    UNKNOWN_ERROR(100000, "未知的错误");

    /**
     * 内部返回错误码
     */
    private Integer code;
    /**
     * 返回码描述
     */
    private String desc;

    @Override
    public String toString() {
        return super.toString() + " - " + this.getDesc();
    }
}
