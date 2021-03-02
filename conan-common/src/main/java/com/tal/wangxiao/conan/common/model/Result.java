package com.tal.wangxiao.conan.common.model;

import com.tal.wangxiao.conan.common.api.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 统一服务端接口返回信息格式
 * @author huyaoguo
 * @date 2020/12/02
 */
@Data
@AllArgsConstructor
public class Result<T> {
    /**
     * 错误码描述
     */
    private ResponseCode respCode;
    /**
     * 详细信息
     */
    private T desc;
}
