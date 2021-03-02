package com.tal.wangxiao.conan.admin.controller;

import com.tal.wangxiao.conan.admin.service.TaskExecutionService;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.controller.ConanBaseController;
import com.tal.wangxiao.conan.common.domain.TaskExecutionInfo;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.sys.common.utils.ServletUtils;
import com.tal.wangxiao.conan.sys.framework.web.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 任务执行管理接口类
 * @author sunzhihua
 * @date 2019/6/3
 */
@Api(value = "Task Execution API", tags = "任务执行接口")
@RestController
@RequestMapping("api/1.0/taskExecution")
public class TaskExecutionController  extends ConanBaseController {

    private static final Logger logger = LoggerFactory.getLogger(TaskExecutionController.class);

    @Resource
    private TaskExecutionService taskExecutionService;

    @Resource
    private TokenService tokenService;

    @ApiOperation(value = "任务执行创建并录制流量", notes = "创建任务执行并录制流量")
    @PostMapping(value = "")
    public ApiResponse<Object> createTaskExecution(@RequestParam(value = "task_id") Integer taskId) throws Exception {
        logger.info("TaskExecutionController#createTaskExecution, task_id = {}", taskId);
        Result<Object> result = taskExecutionService.initExcAndRecord(taskId,tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId().intValue());
        return  new ApiResponse<>(result);
    }

    /**
     * 查询任务管理列表
     */
    @ApiOperation(value = "任务执行详情", notes = "查询任务执行详情")
    @PreAuthorize("@ss.hasPermi('admin:execution:list')")
    @GetMapping("/list")
    public ApiResponse<List<TaskExecutionInfo>> list(TaskExecutionInfo taskExecutionInfo) {
        startPage();
        List<TaskExecutionInfo> list = taskExecutionService.findTaskExecutionList(taskExecutionInfo);
        return getDataTable(list);
    }

}
