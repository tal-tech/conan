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
import com.tal.wangxiao.conan.common.domain.EsConditionSetting;
import com.tal.wangxiao.conan.common.service.EsConditionSettingService;
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * esConditionSetting域名下ES 查询条件配置Controller
 *
 * @author dengkunan
 * @date 2021-01-05
 */
@RestController
@RequestMapping("/api/1.0/common/esConditionSetting")
@Api(value = "esConditionSetting模块管理", tags = "esConditionSetting模块管理")
public class EsConditionSettingController extends ConanBaseController {
    @Autowired
    private EsConditionSettingService esConditionSettingService;

    /**
     * 查询esConditionSetting域名下ES 查询条件配置列表
     */
    @ApiOperation(value = "", notes = "查询esConditionSetting域名下ES 查询条件配置")
    @PreAuthorize("@ss.hasPermi('admin:domain:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<EsConditionSetting>>> list( EsConditionSetting esConditionSetting) {
        startPage();
        List<EsConditionSetting> list = esConditionSettingService.selectEsConditionSettingList(esConditionSetting);
        return getDataTable(list);
    }

    /**
     * 导出esConditionSetting域名下ES 查询条件配置列表
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:export')")
    @Log(title = "esConditionSetting域名下ES 查询条件配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export( EsConditionSetting esConditionSetting) {
        List<EsConditionSetting> list = esConditionSettingService.selectEsConditionSettingList(esConditionSetting);
        ExcelUtil<EsConditionSetting> util = new ExcelUtil<EsConditionSetting>(EsConditionSetting.class);
        return util.exportExcel(list, "esConditionSetting");
    }

    /**
     * 获取esConditionSetting域名下ES 查询条件配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:query')")
    @GetMapping(value = "/{esSettingId}")
    public ApiResponse<EsConditionSetting> getInfo(@PathVariable("esSettingId") Long esSettingId) {
        return ApiResponse.success(esConditionSettingService.selectEsConditionSettingById(esSettingId));
    }

    /**
     * 新增esConditionSetting域名下ES 查询条件配置
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:add')")
    @Log(title = "esConditionSetting域名下ES 查询条件配置", businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody EsConditionSetting esConditionSetting) {
        return toAjax(esConditionSettingService.insertEsConditionSetting(esConditionSetting));
    }

    /**
     * 修改esConditionSetting域名下ES 查询条件配置
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:edit')")
    @Log(title = "esConditionSetting域名下ES 查询条件配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody EsConditionSetting esConditionSetting) {
        return toAjax(esConditionSettingService.updateEsConditionSetting(esConditionSetting));
    }

    /**
     * 删除esConditionSetting域名下ES 查询条件配置
     */
    @PreAuthorize("@ss.hasPermi('admin:domain:remove')")
    @Log(title = "esConditionSetting域名下ES 查询条件配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{esSettingIds}")
    public ApiResponse remove(@PathVariable Long[] esSettingIds) {
        return toAjax(esConditionSettingService.deleteEsConditionSettingByIds(esSettingIds));
    }
}