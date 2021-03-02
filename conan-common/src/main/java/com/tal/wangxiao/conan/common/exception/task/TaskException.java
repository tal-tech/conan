package com.tal.wangxiao.conan.common.exception.task;

import com.tal.wangxiao.conan.common.exception.BaseException;

/**
 * 回放相关自定义异常
 *
 * @author huyaoguo
 * @date 2021/1/7
 **/
public class TaskException extends BaseException {

    private TaskException.Code code;

    public TaskException(String msg, TaskException.Code code) {
        super(code.toString(),msg);
        this.code = code;
    }

    public TaskException.Code getCode() {
        return code;
    }

    public enum Code {
        NO_TASK_EXISTS, UNKNOWN
    }
}