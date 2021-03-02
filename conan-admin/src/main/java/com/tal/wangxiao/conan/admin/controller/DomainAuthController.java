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
import com.tal.wangxiao.conan.common.domain.DomainAuth;
import com.tal.wangxiao.conan.common.service.DomainAuthService;
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 域名鉴权Controller
 * 
 * @author dengkunan
 * @date 2020-12-30
 */
@RestController
@RequestMapping("/api/1.0/common/domainAuth")
@Api(value = "domainAuth模块管理", tags = "domainAuth模块管理")
public class DomainAuthController extends ConanBaseController {
    @Autowired
    private DomainAuthService domainAuthService;

    /**
     * 查询域名鉴权列表
     */
    @ApiOperation(value = "", notes = "查询域名鉴权")
    @PreAuthorize("@ss.hasPermi('admin:domain:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<DomainAuth>>> list( DomainAuth domainAuth) {
        startPage();
        List<DomainAuth> list = domainAuthService.selectDomainAuthList(domainAuth);
        return getDataTable(list);
    }

    /**
     * 导出域名鉴权列表
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:export')")
    @Log(title = "域名鉴权", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export( DomainAuth domainAuth) {
        List<DomainAuth> list = domainAuthService.selectDomainAuthList(domainAuth);
        ExcelUtil<DomainAuth> util = new ExcelUtil<DomainAuth>(DomainAuth.class);
        return util.exportExcel(list, "domainAuth");
    }

    /**
     * 获取域名鉴权详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:add')")
    @GetMapping(value = "/{id}")
    public ApiResponse<DomainAuth> getInfo(@PathVariable("id") Integer id) {
        return ApiResponse.success(domainAuthService.selectDomainAuthById(id));
    }

    /**
     * 新增域名鉴权
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:add')")
    @Log(title = "域名鉴权", businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody DomainAuth domainAuth) {
        return toAjax(domainAuthService.insertDomainAuth(domainAuth));
    }

    /**
     * 修改域名鉴权
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:edit')")
    @Log(title = "域名鉴权", businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody DomainAuth domainAuth) {
        return toAjax(domainAuthService.updateDomainAuth(domainAuth));
    }

    /**
     * 删除域名鉴权
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:remove')")
    @Log(title = "域名鉴权", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public ApiResponse remove(@PathVariable Integer[] ids) {
        return toAjax(domainAuthService.deleteDomainAuthByIds(ids));
    }
}