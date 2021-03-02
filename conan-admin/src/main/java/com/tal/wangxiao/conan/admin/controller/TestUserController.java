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
import com.tal.wangxiao.conan.common.domain.TestUser;
import com.tal.wangxiao.conan.common.service.TestUserService;
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 测试账号管理Controller
 *
 * @author dengkunan
 * @date 2020-12-30
 */
@RestController
@RequestMapping("/api/1.0/common/testUser")
@Api(value = "testUser模块管理" , tags = "testUser模块管理")
public class TestUserController extends ConanBaseController {

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询测试账号管理列表
     */
    @ApiOperation(value = "" , notes = "查询测试账号管理")
    @PreAuthorize("@ss.hasPermi('common:testUser:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<TestUser>>> list(TestUser testUser) {
        startPage();
        List<TestUser> list = testUserService.selectTestUserList(testUser);
        return getDataTable(list);
    }

    /**
     * 导出测试账号管理列表
     */
    @PreAuthorize("@ss.hasPermi('common:testUser:export')")
    @Log(title = "测试账号管理" , businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TestUser testUser) {
        List<TestUser> list = testUserService.selectTestUserList(testUser);
        ExcelUtil<TestUser> util = new ExcelUtil<TestUser>(TestUser.class);
        return util.exportExcel(list, "testUser");
    }

    /**
     * 获取测试账号管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('common:testUser:query')")
    @GetMapping(value = "/{id}")
    public ApiResponse<TestUser> getInfo(@PathVariable("id") Integer id) {
        return ApiResponse.success(testUserService.selectTestUserById(id));
    }

    /**
     * 新增测试账号管理
     */
    @PreAuthorize("@ss.hasPermi('common:testUser:add')")
    @Log(title = "测试账号管理" , businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody TestUser testUser) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        testUser.setCreateBy(loginUser.getUser().getUserId().intValue());
        return toAjax(testUserService.insertTestUser(testUser));
    }

    /**
     * 修改测试账号管理
     */
    @PreAuthorize("@ss.hasPermi('common:testUser:edit')")
    @Log(title = "测试账号管理" , businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody TestUser testUser) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        testUser.setUpdateBy(loginUser.getUser().getUserId().intValue());
        return toAjax(testUserService.updateTestUser(testUser));
    }

    /**
     * 删除测试账号管理
     */
    @PreAuthorize("@ss.hasPermi('common:testUser:remove')")
    @Log(title = "测试账号管理" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public ApiResponse remove(@PathVariable Integer[] ids) {
        return toAjax(testUserService.deleteTestUserByIds(ids));
    }
}