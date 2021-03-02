package com.tal.wangxiao.conan.admin.controller;

import java.util.List;

import com.tal.wangxiao.conan.common.domain.ApiInfo;
import com.tal.wangxiao.conan.common.service.ApiService;
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
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 接口管理Controller
 *
 * @author dengkunan
 * @date 2020-12-29
 */
@RestController
@RequestMapping("/api/1.0/admin/api")
@Api(value = "api模块管理", tags = "api模块管理")
public class ApiController extends ConanBaseController {
    @Autowired
    private ApiService apiService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询接口管理列表
     */
    @ApiOperation(value = "", notes = "查询接口管理")
    @PreAuthorize("@ss.hasPermi('admin:api:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<ApiInfo>>>  list(ApiInfo api) {
        startPage();
        List<ApiInfo> list = apiService.selectApiList(api);
        return getDataTable(list);
    }

    /**
     * 导出接口管理列表
     */
    @PreAuthorize("@ss.hasPermi('admin:api:export')")
    @Log(title = "接口管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export( ApiInfo api) {
        List<ApiInfo> list = apiService.selectApiList(api);
        ExcelUtil<ApiInfo> util = new ExcelUtil<ApiInfo>(ApiInfo.class);
        return util.exportExcel(list, "com/tal/wangxiao/conan/utils");
    }

    /**
     * 获取接口管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:api:query')")
    @GetMapping(value = "/{id}")
    public ApiResponse<ApiInfo> getInfo(@PathVariable("id") Integer id) {
        return ApiResponse.success(apiService.selectApiById(id));
    }

    /**
     * 新增接口管理
     */
    @PreAuthorize("@ss.hasPermi('admin:api:add')")
    @Log(title = "接口管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody ApiInfo api) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        api.setCreateBy(loginUser.getUser().getUserId().intValue());
        return toAjax(apiService.insertApi(api));
    }

    /**
     * 修改接口管理
     */
    @PreAuthorize("@ss.hasPermi('admin:api:edit')")
    @Log(title = "接口管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody ApiInfo api) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        api.setUpdateBy(loginUser.getUser().getUserId().intValue());
        return toAjax(apiService.updateApi(api));
    }

    /**
     * 删除接口管理
     */
    @PreAuthorize("@ss.hasPermi('admin:api:remove')")
    @Log(title = "接口管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public ApiResponse remove(@PathVariable Integer[] ids) {
        return toAjax(apiService.deleteApiByIds(ids));
    }
}