package com.tal.wangxiao.conan.admin.controller;

import java.util.List;

import com.tal.wangxiao.conan.sys.auth.core.domain.model.LoginUser;
import com.tal.wangxiao.conan.sys.common.utils.ServletUtils;
import com.tal.wangxiao.conan.sys.framework.web.service.TokenService;
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
import com.tal.wangxiao.conan.common.domain.DeptJsonRule;
import com.tal.wangxiao.conan.common.service.DeptJsonRuleService;
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 部门Schema规则配置Controller
 * 
 * @author dengkunan
 * @date 2020-12-22
 */
@RestController
@RequestMapping("/api/1.0/admin/deptRule")
@Api(value = "deptRule模块管理", tags = "deptRule模块管理")
public class DeptJsonRuleController extends ConanBaseController {
    @Autowired
    private DeptJsonRuleService deptJsonRuleService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询部门Schema规则配置列表
     */
    @ApiOperation(value = "", notes = "查询部门Schema规则配置")
    @PreAuthorize("@ss.hasPermi('admin:deptRule:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<DeptJsonRule>>> list( DeptJsonRule deptJsonRule) {
        startPage();
        List<DeptJsonRule> list = deptJsonRuleService.selectDeptJsonRuleList(deptJsonRule);
        return getDataTable(list);
    }

    /**
     * 导出部门Schema规则配置列表
     */
    @PreAuthorize("@ss.hasPermi('admin:deptRule:export')")
    @Log(title = "部门Schema规则配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export( DeptJsonRule deptJsonRule) {
        List<DeptJsonRule> list = deptJsonRuleService.selectDeptJsonRuleList(deptJsonRule);
        ExcelUtil<DeptJsonRule> util = new ExcelUtil<DeptJsonRule>(DeptJsonRule.class);
        return util.exportExcel(list, "deptRule");
    }

    /**
     * 获取部门Schema规则配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:deptRule:query')")
    @GetMapping(value = "/{id}")
    public ApiResponse<DeptJsonRule> getInfo(@PathVariable("id") Integer id) {
        return ApiResponse.success(deptJsonRuleService.selectDeptJsonRuleById(id));
    }

    /**
     * 新增部门Schema规则配置
     */
    @PreAuthorize("@ss.hasPermi('admin:deptRule:add')")
    @Log(title = "部门Schema规则配置", businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody DeptJsonRule deptJsonRule) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        deptJsonRule.setCreateBy(loginUser.getUser().getUserId().intValue());
        return toAjax(deptJsonRuleService.insertDeptJsonRule(deptJsonRule));
    }

    /**
     * 修改部门Schema规则配置
     */
    @PreAuthorize("@ss.hasPermi('admin:deptRule:edit')")
    @Log(title = "部门Schema规则配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody DeptJsonRule deptJsonRule) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        deptJsonRule.setUpdateBy(loginUser.getUser().getUserId().intValue());
        return toAjax(deptJsonRuleService.updateDeptJsonRule(deptJsonRule));
    }

    /**
     * 删除部门Schema规则配置
     */
    @PreAuthorize("@ss.hasPermi('admin:deptRule:remove')")
    @Log(title = "部门Schema规则配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public ApiResponse remove(@PathVariable Integer[] ids) {
        return toAjax(deptJsonRuleService.deleteDeptJsonRuleByIds(ids));
    }
}