package com.tal.wangxiao.conan.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.tal.wangxiao.conan.admin.schedule.RedisSchedule;
import com.tal.wangxiao.conan.admin.service.HomeService;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.utils.core.constant.DataType;
import com.tal.wangxiao.conan.utils.date.DateHandleUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 首页大盘Controller
 *
 * @author huyaoguo
 * @date 2021/1/14
 **/
@RestController
@RequestMapping("/api/1.0/admin/home")
@Api(value = "首页大盘", tags = "首页大盘数据管理")
@Slf4j
public class HomeController {

    @Resource
    private HomeService homeService;

    @Resource
    private RedisSchedule redisSchedule;

    @ApiOperation(value = "首页总数据统计（包括已接入接口、自动回归次数、累计回放流量、比对次数等）")
    @GetMapping(value = "/getAllCount")
    public ApiResponse<Object> getAllCount() {
        log.info("HomeController#getAllCount");
        String cacheId = DateHandleUtils.getNowWithFormatter("MM-DD-HH");
        Result<Object> result = homeService.getAllCount(cacheId);
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "核心监控数据")
    @GetMapping(value = "/getImportantData")
    public ApiResponse<Object> getImportantData() {
        log.info("HomeController#getImportantData");
        String cacheId = DateHandleUtils.getNowWithFormatter("MM-DD-HH");
        Result<Object> result = homeService.getImportantData(cacheId);
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "一周任务累计排行")
    @GetMapping(value = "/getTaskRank")
    public ApiResponse<Object> getTaskRank(@RequestParam(value = "count") Integer count) {
        log.info("HomeController#getTaskRank params:count=" + count);
        String cacheId = DateHandleUtils.getNowWithFormatter("MM-DD-HH");
        Result<Object> result = homeService.getTaskRank(count, cacheId);
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "获取agent节点状态")
    @GetMapping(value = "/getAgentNode")
    public ApiResponse<Object> getAgentNode() {
        //log.info("HomeController#getAgentNode");
        Result<Object> result = homeService.getAgentNode();
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "获取部门统计数据")
    @GetMapping(value = "/getDepartmentData")
    public ApiResponse<Object> getDepartmentData() {
        log.info("HomeController#getDepartmentData");
        Result<Object> result = homeService.getDepartmentData();
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "平台打点数据，收集用户访问轨迹")
    @GetMapping(value = "/addDot")
    public ApiResponse<Object> addDot() {
        log.info("HomeController#addDot");
        Result<Object> result = homeService.addDot();
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "test")
    @GetMapping(value = "/test")
    public ApiResponse<Object> test() {
        log.info("HomeController#test");
        redisSchedule.setHomeProductData();
        return new ApiResponse<>(new Result<>(ResponseCode.SUCCESS, ""));
    }

}
