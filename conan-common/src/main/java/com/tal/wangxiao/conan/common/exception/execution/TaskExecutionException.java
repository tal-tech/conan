package com.tal.wangxiao.conan.common.exception.execution;

import com.tal.wangxiao.conan.common.exception.BaseException;

/**
 * 回放相关自定义异常
 *
 * @author huyaoguo
 * @date 2021/1/7
 **/
public class TaskExecutionException extends BaseException {

    private TaskExecutionException.Code code;

    public TaskExecutionException(String msg, TaskExecutionException.Code code) {
        super(code.toString(),msg);
        this.code = code;
    }

    public TaskExecutionException.Code getCode() {
        return code;
    }

    public enum Code {
        NO_EXECUTION_EXISTS, UNKNOWN
    }
}