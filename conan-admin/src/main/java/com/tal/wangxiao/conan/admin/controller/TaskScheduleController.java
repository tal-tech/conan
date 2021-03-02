package com.tal.wangxiao.conan.admin.controller;

import com.tal.wangxiao.conan.admin.service.TaskScheduleService;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.bo.TaskScheduleInfo;
import com.tal.wangxiao.conan.common.model.query.TaskScheduleQuery;
import com.tal.wangxiao.conan.common.model.vo.TaskScheduleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 定时任务接口类
 *
 * @author liujinsong
 * @data 2019/09/27
 */

@Api(value = "Task Schedule API", tags = "定时任务接口")
@RestController
@Slf4j
@RequestMapping("api/1.0/taskSchedule")
public class TaskScheduleController {

    @Resource
    private TaskScheduleService taskScheduleService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "create_by", value = "创建人", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "task_id", value = "任务Id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "定时任务类型", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "date_from", value = "时间范围查询", dataType = "LocalDate", paramType = "query", example = "2019-01-23"),
            @ApiImplicitParam(name = "date_to", value = "时间范围查询", dataType = "LocalDate", paramType = "query", example = "2019-01-23"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "size", value = "页面大小", dataType = "int", paramType = "query", example = "10")
    })
    @ApiOperation(value = "查询任务", notes = "定时任务查询")
    @GetMapping(value = "")
    public ApiResponse<TaskScheduleVO> searchScheduleTask(@RequestParam(value = "keyword", required = false) String keyword,
                                                          @RequestParam(value = "create_by", required = false) Integer createBy,
                                                          @RequestParam(value = "task_id", required = false) Integer taskId,
                                                          @RequestParam(value = "type", required = false) Integer type,
                                                          @RequestParam(value = "date_from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dateFrom,
                                                          @RequestParam(value = "date_to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dateTo,
                                                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNum,
                                                          @RequestParam(value = "size", required = false, defaultValue = "10") Integer pageSize
    ) {
        log.info("TaskScheduleController#searchScheduleTask");
        TaskScheduleQuery taskQuery = TaskScheduleQuery.builder()
                .name(keyword).createBy(createBy)
                .taskId(taskId).type(type).dateFrom(dateFrom).dateTo(dateTo).build();
        //JPA页数从0开始计数
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.DESC, "id");
        Result<Object> result = taskScheduleService.findByConditions(taskQuery, pageable);
        return new ApiResponse(result);
    }

    @ApiOperation(value = "任务创建", notes = "创建定时任务")
    @PostMapping(value = "")
    public ApiResponse<Object> createScheduleTask(@RequestBody @Param(value = "task_info") @Validated TaskScheduleInfo taskInfo) {
        log.info("TaskScheduleController#createScheduleTask");
        Result result;
        try{
            result = taskScheduleService.createScheduleTask(taskInfo);
        }catch (Exception e){
            result = new Result(ResponseCode.FAILED_CREATE_TASKSCHEDULE,e.getMessage());
        }
        return new ApiResponse(result);
    }

    @ApiOperation(value = "任务更新", notes = "更新定时任务")
    @PostMapping(value = "/updateTask/{task_id}")
    public ApiResponse<String> updateScheduleTask(@PathVariable(value = "task_id") Integer taskId, @RequestBody @Param(value = "task_info") @Validated TaskScheduleInfo taskInfo) {
        log.info("TaskScheduleController#updateScheduleTask");
        Result result;
        try{
            result = taskScheduleService.updateScheduleTask(taskInfo,taskId);
        }catch (Exception e){
            result = new Result(ResponseCode.FAILED_CREATE_TASKSCHEDULE,e.getMessage());
        }
        return new ApiResponse(result);
    }

    @ApiOperation(value = "暂停或恢复定时任务", notes = "更新任务状态开启或关闭定时任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "task_id", value = "定时任务ID", dataType = "int", paramType = "path"),
    })
    @PostMapping(value = "/execTask/{task_id}")
    public ApiResponse<String> changeStatus(@PathVariable(value = "task_id" ) Integer taskId) {
        log.info("TaskScheduleController#changeStatus");
        Result result = taskScheduleService.changeStatus(taskId);
        return new  ApiResponse(result);
    }

    @ApiOperation(value = "删除任务", notes = "通过任务ID删除定时任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "task_id", value = "定时任务ID", dataType = "int", paramType = "path")
    })
    @PostMapping(value = "/deleteTask/{task_id}")
    public ApiResponse<Object> deleteScheduleTask(@PathVariable(value = "task_id") Integer taskId) {
        log.info("TaskScheduleController#deleteScheduleTask");
        Result result = taskScheduleService.deleteScheduleTask(taskId);
        return new ApiResponse(result);
    }

    @ApiOperation(value = "单个定时任务详情", notes = "查询定时任务详情")
    @GetMapping(value = "/{task_id}")
    public ApiResponse<TaskScheduleInfo> findByTaskScheduleId(@PathVariable(value = "task_id") Integer taskId) {
        log.info("TaskScheduleController#findByTaskScheduleId");
        Result result = taskScheduleService.findByTaskScheduleId(taskId);
        return new ApiResponse(result);
    }

    @ApiOperation(value = "检查cron表达式", notes = "根据cron表达式判断是否正确")
    @GetMapping(value = "/cronValid/{cron_expression}")
    public ApiResponse<Object> checkCronExpressionIsValid(@PathVariable(value = "cron_expression") String cronExpression) {
        log.info("TaskScheduleController#checkCronExpressionIsValid");
        Result result = taskScheduleService.checkCronExpressionIsValid(cronExpression);
        return new ApiResponse(result);
    }


}

