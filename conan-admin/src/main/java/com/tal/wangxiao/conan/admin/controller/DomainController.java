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
import com.tal.wangxiao.conan.common.domain.Domain;
import com.tal.wangxiao.conan.common.service.DomainService;
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 域名信息Controller
 *
 * @author dengkunan
 * @date 2020-12-22
 */
@RestController
@RequestMapping("/api/1.0/admin/domain")
@Api(value = "domain模块管理", tags = "domain模块管理")
public class DomainController extends ConanBaseController {
    @Autowired
    private DomainService domainService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询域名信息列表
     */
    @ApiOperation(value = "", notes = "查询域名信息")
    @PreAuthorize("@ss.hasPermi('admin:domain:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<Domain>>> list(Domain domain) {
        startPage();
        List<Domain> list = domainService.selectDomainList(domain);
        return getDataTable(list);
    }

    /**
     * 导出域名信息列表
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:export')")
    @Log(title = "域名信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Domain domain) {
        List<Domain> list = domainService.selectDomainList(domain);
        ExcelUtil<Domain> util = new ExcelUtil<Domain>(Domain.class);
        return util.exportExcel(list, "domain");
    }

    /**
     * 获取域名信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:query')")
    @GetMapping(value = "/{id}")
    public ApiResponse<Domain> getInfo(@PathVariable("id") Long id) {
        return ApiResponse.success(domainService.selectDomainById(id));
    }

    /**
     * 新增域名信息
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:add')")
    @Log(title = "域名信息", businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody Domain domain) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        domain.setCreateBy(loginUser.getUser().getUserId().intValue());
        return toAjax(domainService.insertDomain(domain));
    }

    /**
     * 修改域名信息
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:edit')")
    @Log(title = "域名信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody Domain domain) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        domain.setUpdateBy(loginUser.getUser().getUserId().intValue());
        return toAjax(domainService.updateDomain(domain));
    }

    /**
     * 删除域名信息
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:remove')")
    @Log(title = "域名信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public ApiResponse remove(@PathVariable Long[] ids) {
        return toAjax(domainService.deleteDomainByIds(ids));
    }
}