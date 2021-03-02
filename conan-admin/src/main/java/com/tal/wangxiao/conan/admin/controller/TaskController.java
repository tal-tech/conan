package com.tal.wangxiao.conan.admin.controller;

import java.util.List;

import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.sys.auth.core.domain.model.LoginUser;
import com.tal.wangxiao.conan.sys.common.utils.ServletUtils;
import com.tal.wangxiao.conan.sys.framework.web.service.TokenService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tal.wangxiao.conan.sys.common.annotation.Log;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.api.PageInfoResponse;
import com.tal.wangxiao.conan.common.controller.ConanBaseController;
import com.tal.wangxiao.conan.sys.common.core.domain.AjaxResult;
import com.tal.wangxiao.conan.sys.common.enums.BusinessType;
import com.tal.wangxiao.conan.common.domain.Task;
import com.tal.wangxiao.conan.common.service.TaskService;
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 任务管理Controller
 *
 * @author dengkunnan
 * @date 2020-12-28
 */
@RestController
@RequestMapping("/api/1.0/admin/task")
@Api(value = "task模块管理", tags = "task模块管理")
public class TaskController extends ConanBaseController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询任务管理列表
     */
    @ApiOperation(value = "", notes = "查询任务管理")
    @PreAuthorize("@ss.hasPermi('admin:task:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<Task>>> list( Task task) {
        startPage();
        List<Task> list = taskService.selectTaskList(task);
        return getDataTable(list);
    }

    /**
     * 导出任务管理列表
     */
    @PreAuthorize("@ss.hasPermi('admin:task:export')")
    @Log(title = "任务管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export( Task task) {
        List<Task> list = taskService.selectTaskList(task);
        ExcelUtil<Task> util = new ExcelUtil<Task>(Task.class);
        return util.exportExcel(list, "task");
    }

    /**
     * 获取任务管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:task:query')")
    @GetMapping(value = "/{id}")
    public ApiResponse<Task> getInfo(@PathVariable("id") Integer id) {
        return ApiResponse.success(taskService.selectTaskById(id));
    }

    /**
     * 新增任务管理
     */
    @PreAuthorize("@ss.hasPermi('admin:task:add')")
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody Task task) {

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        task.setCreateBy(loginUser.getUser().getUserId().intValue());
        return toAjax(taskService.insertTask(task));
    }

    /**
     * 修改任务管理
     */
    @PreAuthorize("@ss.hasPermi('admin:task:edit')")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody Task task) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        task.setUpdateBy(loginUser.getUser().getUserId().intValue());

        return toAjax(taskService.updateTask(task));
    }

    /**
     * 删除任务管理
     */
    @PreAuthorize("@ss.hasPermi('admin:task:remove')")
    @Log(title = "任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public ApiResponse remove(@PathVariable Integer[] ids) {
        return toAjax(taskService.deleteTaskByIds(ids));
    }

    /**
     * 查看任务信息通过录制回放比对记录
     */
    @ApiOperation(value = "任务相关信息", notes = "查看任务信息通过录制回放比对记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "参数Id(录制传executionId,回放传replayId,比对传diffId)", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "任务信息类型（record,replay,diff）", dataType = "string", paramType = "query")})
    @GetMapping("/info")
    public ApiResponse<Object> taskInfo(@RequestParam(value = "id") Integer id,
                                        @RequestParam(value = "type") String replayId) {
        Result<Object> result = taskService.findTaskInfoById(id,replayId);
        return new ApiResponse<>(result);
    }
}