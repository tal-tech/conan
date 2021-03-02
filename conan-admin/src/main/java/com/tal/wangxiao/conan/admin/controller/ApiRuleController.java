package com.tal.wangxiao.conan.admin.controller;

import java.util.List;
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
import com.tal.wangxiao.conan.common.domain.ApiRule;
import com.tal.wangxiao.conan.common.service.ApiRuleService;
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 接口Schema规则Controller
 * 
 * @author dengkunan
 * @date 2020-12-22
 */
@RestController
@RequestMapping("/api/1.0/admin/api/rule")
@Api(value = "Api rule模块管理", tags = "rule模块管理")
public class ApiRuleController extends ConanBaseController {
    @Autowired
    private ApiRuleService apiRuleService;

    /**
     * 查询接口Schema规则列表
     */
    @ApiOperation(value = "", notes = "查询接口Schema规则")
    @PreAuthorize("@ss.hasPermi('admin:api:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<ApiRule>>> list( ApiRule apiRule) {
        startPage();
        List<ApiRule> list = apiRuleService.selectApiRuleList(apiRule);
        return getDataTable(list);
    }

    /**
     * 导出接口Schema规则列表
     */
    @PreAuthorize("@ss.hasPermi('admin:api:export')")
    @Log(title = "接口Schema规则", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export( ApiRule apiRule) {
        List<ApiRule> list = apiRuleService.selectApiRuleList(apiRule);
        ExcelUtil<ApiRule> util = new ExcelUtil<ApiRule>(ApiRule.class);
        return util.exportExcel(list, "rule");
    }

    /**
     * 获取接口Schema规则详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:api:query')")
    @GetMapping(value = "/{id}")
    public ApiResponse<ApiRule> getInfo(@PathVariable("id") Integer id) {
        return ApiResponse.success(apiRuleService.selectApiRuleById(id));
    }

    /**
     * 新增接口Schema规则
     */
    @PreAuthorize("@ss.hasPermi('admin:api:add')")
    @Log(title = "接口Schema规则", businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody ApiRule apiRule) {
        return toAjax(apiRuleService.insertApiRule(apiRule));
    }

    /**
     * 修改接口Schema规则
     */
    @PreAuthorize("@ss.hasPermi('admin:api:edit')")

    @Log(title = "接口Schema规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody ApiRule apiRule) {
        return toAjax(apiRuleService.updateApiRule(apiRule));
    }

    /**
     * 删除接口Schema规则
     */
    @PreAuthorize("@ss.hasPermi('admin:api:remove')")
    @Log(title = "接口Schema规则", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public ApiResponse remove(@PathVariable Integer[] ids) {
        return toAjax(apiRuleService.deleteApiRuleByIds(ids));
    }
}