package com.tal.wangxiao.conan.admin.controller;

import com.tal.wangxiao.conan.admin.service.ReplayService;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.controller.ConanBaseController;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.vo.ReplayDetailVO;
import com.tal.wangxiao.conan.common.model.vo.ReplayVO;
import com.tal.wangxiao.conan.common.redis.RedisTemplateTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务回放Controller
 *
 * @author huyaoguo
 * @date 2021/1/4
 */
@RestController
@RequestMapping("/api/1.0/admin/replay")
@Api(value = "流量回放", tags = "流量回放模块管理")
@Slf4j
public class ReplayController extends ConanBaseController {
    @Resource
    private ReplayService replayService;

    @Resource
    private RedisTemplateTool redisTemplateTool;

    /**
     * 执行一次回放
     */
    @ApiOperation(value = "执行回放", notes = "根据执行任务ID执行一次回放")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "task_execution_id", value = "任务执行ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "replay_env", value = "回放环境（online , test ,gray）", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "replay_type", value = "回放类型（0-定时任务，1-手动执行）", dataType = "string", paramType = "query")
    })
    @PreAuthorize("@ss.hasPermi('admin:execution:replay')")
    @PostMapping("")
    public ApiResponse<Object> startReplay(@RequestParam(value = "task_execution_id") Integer taskExecutionId, @RequestParam(value = "replay_env") String replayEnv, @RequestParam(value = "replay_type") Integer replayType) {
        log.info("ReplayController#startReplay");
        Result<Object> result = replayService.startReplay(taskExecutionId, replayEnv, replayType);
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "根据任务执行ID查询流量回放记录列表", notes = "根据任务执行ID查询流量回放记录列表")
    @PreAuthorize("@ss.hasPermi('admin:execution:replay:list')")
    @ApiImplicitParam(name = "task_execution_id", value = "任务执行ID", dataType = "int", paramType = "query")
    @GetMapping(value = "")
    public ApiResponse<List<ReplayVO>> findReplayListByTaskExecutionId(@RequestParam(value = "task_execution_id") Integer taskExecutionId) {
        log.info("ReplayController#findReplayListByTaskExecutionId:" + taskExecutionId);
        Result<Object> result = replayService.findReplaysByTaskExecutionId(taskExecutionId);
        return new ApiResponse(result);
    }

    @ApiOperation(value = "根据回放ID获取最新比对ID", notes = "根据回放ID获取最新比对ID")
    @ApiImplicitParam(name = "replayId", value = "replayId", dataType = "int", paramType = "query")
    @GetMapping(value = "/getDiffIdByReplayId")
    public ApiResponse<Integer> findDiffIdByReplayId(@RequestParam(value = "replayId") Integer replayId) {
        log.info("replayId:" + replayId);
        Integer diffId = replayService.findDiffIdByReplayId(replayId);
        return new ApiResponse(new Result<>(ResponseCode.SUCCESS, diffId));
    }

    @ApiOperation(value = "查询回放执行详情", notes = "根据回放ID查询回放执行详情")
    @Cacheable(value = "replay-info")
    @GetMapping(value = "detail")
    @PreAuthorize("@ss.hasPermi('admin:replay:detail')")
    public ApiResponse<List<ReplayDetailVO>> findDetailByReplayId(@RequestParam(value = "replay_id") Integer replayId) {
        log.info("ReplayController#findDetailByReplayId, replay_id = {}", replayId);
        Result<Object> result = replayService.findDetailByReplayId(replayId);
        return new ApiResponse(result);
    }

    @ApiOperation(value = "单接口的回放数据", notes = "根据ApiID查询单接口的流量回放数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "replay_id", value = "回放Id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "api_id", value = "接口Id", dataType = "int", paramType = "query")
    })
    @GetMapping(value = "oneApiDetail")
    public ApiResponse<Object> findOneApiDetailByReplayIdAndApiId(@RequestParam(value = "replay_id") Integer replayId, @RequestParam(value = "api_id") Integer apiId) {
        log.info("ReplayController#findOneApiDetailByReplayIdAndApiId");
        Result<Object> result = replayService.findOneApiDetailByReplayIdAndApiId(replayId, apiId);
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "根据回放ID查询回放日志", notes = "根据回放ID查询回放日志")
    @ApiImplicitParam(name = "replay_id", value = "回放Id", dataType = "int", paramType = "query")
    @GetMapping(value = "/log")
    @PreAuthorize("@ss.hasPermi('admin:replay:log')")
    public ApiResponse<String> findReplayLog(@RequestParam(value = "replay_id") Integer replayId) {
        log.info("ReplayController#findReplayLog:" + replayId);
        Result<String> result = replayService.findLogByReplayId(replayId);
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "根据回放ID查询回放进度", notes = "根据回放ID查询回放进度")
    @ApiImplicitParam(name = "replay_id", value = "回放Id", dataType = "int", paramType = "query")
    @GetMapping(value = "/progress")
    public ApiResponse<Object> findReplayProgress(@RequestParam(value = "replay_id") Integer replayId) {
        log.info("ReplayController#findReplayProgress:" + replayId);
        Result<Object> result = replayService.findReplayProgress(replayId);
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "设置某次回放记录作为比对基准", notes = "设置某次回放记录作为比对基准")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "replayId", value = "回放ID", dataType = "int", paramType = "query")})
    @GetMapping(value = "/setBaseLine")
    public ApiResponse<String> setReplayAsBaseLine(
            @RequestParam(value = "replayId") Integer replayId) {
        log.info("ReplayController#setReplayAsBaseLine:replay_id=" + replayId);
        Result<String> result = replayService.setBaseLine(replayId);
        return new ApiResponse<>(result);
    }

    @ApiOperation(value = "根据回放ID写入回放进度（上线后清除）", notes = "根据回放ID写入回放进度")
    @ApiImplicitParam(name = "replay_id", value = "回放Id", dataType = "int", paramType = "query")
    @GetMapping(value = "/setProgress")
    public ApiResponse<Object> setReplayProgress(@RequestParam(value = "replay_id") Integer replayId, @RequestParam(value = "progress") String progress) {
        log.info("ReplayController#setRecordProgress:" + replayId);
        redisTemplateTool.setReplayProgress(replayId, progress);
        return new ApiResponse<>(200, "ok");
    }

}