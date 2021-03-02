package com.tal.wangxiao.conan.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tal.wangxiao.conan.admin.service.DiffService;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.controller.ConanBaseController;
import com.tal.wangxiao.conan.common.domain.ApiDiffDetailInfo;
import com.tal.wangxiao.conan.common.exception.CustomException;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.vo.DiffDetailVO;
import com.tal.wangxiao.conan.sys.auth.core.domain.model.LoginUser;
import com.tal.wangxiao.conan.sys.common.utils.ServletUtils;
import com.tal.wangxiao.conan.sys.framework.web.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 比对记录Controller
 *
 * @author dengkunan
 * @date 2021-01-08
 */
@Slf4j
@RestController
@RequestMapping("/api/1.0/common/diff")
@Api(value = "流量比对", tags = "流量比对模块管理")
public class DiffController extends ConanBaseController {
    @Resource
    private DiffService diffService;

    @Resource
    private TokenService tokenService;

    @ApiOperation(value = "执行diff命令", notes = "执行diff")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "replayId", value = "回放ID", dataType = "int", paramType = "query")
    })
    @GetMapping(value = "/start")
    public ApiResponse<Object> startDiff(
            @RequestParam(value = "replayId") Integer replayId
    ) throws CustomException {
        log.info("DiffController#startDiff");
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Result<Object> result = diffService.startDiff(replayId,loginUser.getUser().getUserId().intValue());
        return new ApiResponse<>(result);
    }


    @ApiOperation(value = "查询diff列表", notes = "查询diff列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "replayId", value = "回放ID", dataType = "int", paramType = "query")})
    @GetMapping(value = "/list")
    public ApiResponse<List<DiffDetailVO>> getDiffResult(
            @RequestParam(value = "replayId") Integer replayId) {
        Result<Object> result = diffService.getDiffResultList(replayId);
        return new ApiResponse(result);
    }

    @ApiOperation(value = "查询单接口diff详情", notes = "查询单接口diff详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "diffId", value = "diffID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "apiId", value = "接口ID", dataType = "int", paramType = "query")})
    @Cacheable(value = "diff-detail")
    @GetMapping(value = "/detail")
    public ApiResponse<ApiDiffDetailInfo> getDiffDetail(
            @RequestParam(value = "diffId") Integer diffId,
            @RequestParam(value = "apiId") Integer apiId
    ) throws JsonProcessingException {
        ApiResponse result = diffService.getDiffDetail(apiId, diffId);
        return result;
    }


    @ApiOperation(value = "查询比对进度", notes = "根据比对ID查询比对进度")
    @ApiImplicitParam(name = "diff_id", value = "比对Id", dataType = "int", paramType = "query")
    @GetMapping(value = "/progress")
    public ApiResponse<Object> findDiffProgress(@RequestParam(value = "diff_id") Integer diffId){
        log.info("ReplayController#findDiffProgress:"+diffId);
        Result<Object> result = diffService.findDiffProgress(diffId);
        return  new ApiResponse<>(result);
    }

    @ApiOperation(value = "查询比对日志", notes = "根据比对ID查询比对日志")
    @ApiImplicitParam(name = "diff_id", value = "比对Id", dataType = "int", paramType = "query")
    @GetMapping(value = "/log")
    public ApiResponse<String> findDiffLog(@RequestParam(value = "diff_id") Integer diffId){
        log.info("ReplayController#findDiffLog:"+diffId);
        Result<String> result = diffService.findDiffLog(diffId);
        return  new ApiResponse<>(result);
    }
}