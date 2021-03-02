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
import com.tal.wangxiao.conan.common.domain.EsSource;
import com.tal.wangxiao.conan.common.service.EsSourceService;
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * ES 数据源配置，域名需要绑定ES数据源Controller
 *
 * @author dengkunan
 * @date 2021-01-05
 */
@RestController
@RequestMapping("/api/1.0/common/esDataSource")
@Api(value = "esDataSource模块管理", tags = "esDataSource模块管理")
public class EsSourceController extends ConanBaseController {
    @Autowired
    private EsSourceService esSourceService;

    /**
     * 查询ES 数据源配置，域名需要绑定ES数据源列表
     */
    @ApiOperation(value = "", notes = "查询ES 数据源配置，域名需要绑定ES数据源")
    @PreAuthorize("@ss.hasPermi('common:esDataSource:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<EsSource>>> list(EsSource esSource) {
        startPage();
        List<EsSource> list = esSourceService.selectEsSourceList(esSource);
        return getDataTable(list);
    }

    /**
     * 导出ES 数据源配置，域名需要绑定ES数据源列表
     */
    @PreAuthorize("@ss.hasPermi('common:esDataSource:export')")
    @Log(title = "ES 数据源配置，域名需要绑定ES数据源", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(EsSource esSource) {
        List<EsSource> list = esSourceService.selectEsSourceList(esSource);
        ExcelUtil<EsSource> util = new ExcelUtil<EsSource>(EsSource.class);
        return util.exportExcel(list, "esDataSource");
    }

    /**
     * 获取ES 数据源配置，域名需要绑定ES数据源详细信息
     */
    @PreAuthorize("@ss.hasPermi('common:esDataSource:query')")
    @GetMapping(value = "/{esSourceId}")
    public ApiResponse<EsSource> getInfo(@PathVariable("esSourceId") Integer esSourceId) {
        return ApiResponse.success(esSourceService.selectEsSourceById(esSourceId));
    }

    /**
     * 新增ES 数据源配置，域名需要绑定ES数据源
     */
    @PreAuthorize("@ss.hasPermi('common:esDataSource:add')")
    @Log(title = "ES 数据源配置，域名需要绑定ES数据源", businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody EsSource esSource) {
        return toAjax(esSourceService.insertEsSource(esSource));
    }

    /**
     * 修改ES 数据源配置，域名需要绑定ES数据源
     */
    @PreAuthorize("@ss.hasPermi('common:esDataSource:edit')")
    @Log(title = "ES 数据源配置，域名需要绑定ES数据源", businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody EsSource esSource) {
        return toAjax(esSourceService.updateEsSource(esSource));
    }

    /**
     * 删除ES 数据源配置，域名需要绑定ES数据源
     */
    @PreAuthorize("@ss.hasPermi('common:esDataSource:remove')")
    @Log(title = "ES 数据源配置，域名需要绑定ES数据源", businessType = BusinessType.DELETE)
    @DeleteMapping("/{esSourceIds}")
    public ApiResponse remove(@PathVariable Integer[] esSourceIds) {
        return toAjax(esSourceService.deleteEsSourceByIds(esSourceIds));
    }
}