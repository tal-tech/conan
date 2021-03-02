package com.tal.wangxiao.conan.admin.controller;

import java.util.List;

import com.tal.wangxiao.conan.common.domain.TaskApiRelationDbInfo;
import com.tal.wangxiao.conan.common.domain.TaskApiRelationView;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tal.wangxiao.conan.sys.common.annotation.Log;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.api.PageInfoResponse;
import com.tal.wangxiao.conan.common.controller.ConanBaseController;
import com.tal.wangxiao.conan.sys.common.core.domain.AjaxResult;
import com.tal.wangxiao.conan.sys.common.enums.BusinessType;
import com.tal.wangxiao.conan.common.domain.TaskApiRelation;
import com.tal.wangxiao.conan.common.service.TaskApiRelationService;
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * taskApiRelationController
 *
 * @author dengkunan
 * @date 2020-12-29
 */
@RestController
@RequestMapping("/api/1.0/common/taskApiRelation")
@Api(value = "taskApiRelation模块管理", tags = "taskApiRelation模块管理")
public class TaskApiRelationController extends ConanBaseController {
    @Autowired
    private TaskApiRelationService taskApiRelationService;

    /*   *//**
     * 查询taskApiRelation列表
     *//*
    @ApiOperation(value = "", notes = "查询taskApiRelation")
    @PreAuthorize("@ss.hasPermi('admin:task:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<TaskApiRelation>>> list( TaskApiRelation taskApiRelation) {
        startPage();
        List<TaskApiRelation> list = taskApiRelationService.selectTaskApiRelationList(taskApiRelation);
        return getDataTable(list);
    }*/

    /*   *//**
     * 导出taskApiRelation列表
     *//*
    @PreAuthorize("@ss.hasPermi('admin:task:export')")
    @Log(title = "taskApiRelation", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export( TaskApiRelation taskApiRelation) {
        List<TaskApiRelation> list = taskApiRelationService.selectTaskApiRelationList(taskApiRelation);
        ExcelUtil<TaskApiRelation> util = new ExcelUtil<TaskApiRelation>(TaskApiRelation.class);
        return util.exportExcel(list, "taskApiRelation");
    }*/

    /**
     * 获取taskApiRelation详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:task:query')")
    @GetMapping(value = "taskId/{taskId}")
    public ApiResponse<TaskApiRelationView> getInfoByTaskId(@PathVariable("taskId") Integer taskId) {
        List<TaskApiRelationView> list = taskApiRelationService.selectTaskApiRelationViewListByTaskId(taskId);
        return ApiResponse.success(list);
    }


    /**
     * 获取taskApiRelation详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:task:query')")
    @GetMapping(value = "deptId/{deptId}")
    public ApiResponse<TaskApiRelationView> getInfoByDeptId(@PathVariable("deptId") Integer deptId) {
        List<TaskApiRelationView> list = taskApiRelationService.selectTaskApiRelationViewListByDeptId(deptId);
        return ApiResponse.success(list);
    }


    /**
     * 获取taskApiRelation详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:task:query')")
    @GetMapping(value = "domainId/{domainId}")
    public ApiResponse<TaskApiRelationView> getInfoByDomainId(@PathVariable("domainId") Integer domainId) {
        List<TaskApiRelationView> list = taskApiRelationService.selectTaskApiRelationViewListByDomainId(domainId);
        return ApiResponse.success(list);
    }

    /**
     * 获取taskApiRelation详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:task:query')")
    @GetMapping(value = "/getInfo")
    public ApiResponse<TaskApiRelationView> getInfoByApiNameAndDomainName(TaskApiRelationDbInfo taskApiRelationDbInfo) {
        List<TaskApiRelationView> list = taskApiRelationService.selectTaskApiRelationViewListByApiNameAndDomainName(taskApiRelationDbInfo);
        return ApiResponse.success(list);
    }

    /**
     * 新增taskApiRelation
     */
    @PreAuthorize("@ss.hasPermi('admin:task:add')")
    @Log(title = "taskApiRelation", businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody List<TaskApiRelation> taskApiRelationList) {
        return toAjax(taskApiRelationService.insertTaskApiRelation(taskApiRelationList));
    }

    /*    *//**
     * 修改taskApiRelation
     *//*
    @PreAuthorize("@ss.hasPermi('admin:task:edit')")
    @Log(title = "taskApiRelation", businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody TaskApiRelation taskApiRelation) {
        return toAjax(taskApiRelationService.updateTaskApiRelation(taskApiRelation));
    }

    *//**
     * 删除taskApiRelation
     *//*
    @PreAuthorize("@ss.hasPermi('admin:task:remove')")
    @Log(title = "taskApiRelation", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public ApiResponse remove(@PathVariable Integer[] ids) {
        return toAjax(taskApiRelationService.deleteTaskApiRelationByIds(ids));
    }*/
}