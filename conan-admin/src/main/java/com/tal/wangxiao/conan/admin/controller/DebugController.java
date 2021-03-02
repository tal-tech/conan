package com.tal.wangxiao.conan.admin.controller;

import com.tal.wangxiao.conan.admin.service.DebugService;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.bo.ApiInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 接口调试类
 * @author huyaoguo
 * @date 2019/8/13
 */
@Api(value = "Debug API", tags = "用例接口调试接口")
@RestController
@RequestMapping("api/1.0/debug")
public class DebugController {
    private static final Logger logger = LoggerFactory.getLogger(DebugController.class);

    @Resource
    private DebugService debugService;


    @ApiOperation(value = "单接口调试", notes = "根据url+method+parms发请求")
    @PostMapping(value = "")
    public ApiResponse<Object> oneApiDebug(@RequestBody @Param(value = "api_info") @Validated ApiInfo apiInfo) {
        logger.info("DebugController#oneApiDebug");
        Result<Object> result = debugService.debugOneApi(apiInfo);
        return new ApiResponse<>(result);
    }




}
