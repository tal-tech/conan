package com.tal.wangxiao.conan.common.exception.replay;

import com.tal.wangxiao.conan.common.exception.BaseException;

/**
 * 回放相关自定义异常
 *
 * @author huyaoguo
 * @date 2021/1/7
 **/
public class ReplayException extends BaseException {

    private ReplayException.Code code;

    public ReplayException(String msg, ReplayException.Code code) {
        super(code.toString(),msg);
        this.code = code;
    }

    public ReplayException.Code getCode() {
        return code;
    }

    public enum Code {
        NO_REPLAY_EXISTS, UNKNOWN, REPLAY_TIMEOUT, REPLAY_THREAD_ERROR, NOT_FOUND_REPLAY_DETAIL, REPLAY_REQUEST_FAIL
    }
}