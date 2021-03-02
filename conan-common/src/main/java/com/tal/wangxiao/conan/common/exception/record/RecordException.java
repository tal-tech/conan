package com.tal.wangxiao.conan.common.exception.record;

import com.tal.wangxiao.conan.common.exception.BaseException;

/**
 * 录制相关自定义异常
 *
 * @author huyaoguo
 * @date 2021/1/7
 **/
public class RecordException extends BaseException {

    private RecordException.Code code;

    public RecordException(String msg, RecordException.Code code) {
        super(code.toString(),msg);
        this.code = code;
    }

    public RecordException.Code getCode() {
        return code;
    }

    public enum Code {
        NO_RECORD_EXISTS, NO_RECORD_DETAIL_EXISTS , UNKNOWN
    }
}