package com.tal.wangxiao.conan.admin.service;

import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.bo.ApiInfo;

/**
 * 接口调试服务
 * @author huyaoguo
 * @date 2021-1-16
 **/
public interface DebugService {
    /**
     * 根据接口的信息获取运行结果
     * @param apiInfo
     * @return
     */
    Result<Object> debugOneApi(ApiInfo apiInfo);
}
