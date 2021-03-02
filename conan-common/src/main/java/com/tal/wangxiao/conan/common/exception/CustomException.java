package com.tal.wangxiao.conan.common.exception;


import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.model.Result;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ：dengkunnan
 * @date ：2021.01.07
 * @description：自定义异常处理类，可以采用throws 方式中断任务，避免方法过长
 * @modified By：
 * @version: 1.0.0.1
 */
@Slf4j
@Data
public class CustomException extends Exception {


    /**
     * 错误码描述
     */
    private ResponseCode code;
    /**
     * 详细信息
     */
    private Object desc;

    /**
     * 当前系统使用等错误返回格式
     */
    private Result result;

    public CustomException(ResponseCode code, Object desc) {
        super(desc.toString());
        this.code = code;
        result = new Result(code, desc);
        log.error("出现异常：error desc ={0} ,;  code ={1}" , desc.toString(), code);
        logPringtStackTrace();
    }

    public CustomException(ResponseCode code) {
        this.code = code;
        result = new Result(code, "");
        log.error("出现异常：error code ={}" , code);
        logPringtStackTrace();
    }

    public CustomException(Object desc) {
        this.code = null;
        this.desc = desc;
        result = new Result(code, "");
        log.error("出现异常：error code ={}" , code);
        logPringtStackTrace();
    }

    /**
     * @descrption 打印堆栈信息，最多打印50行，
     */
    private void logPringtStackTrace() {
        int count = 0;
        StackTraceElement[] trace = this.getStackTrace();
        for (StackTraceElement traceElement : trace) {
            count++;
            if (count >= 50) {
                return;
            }
            log.error(traceElement.toString());
        }
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }
}