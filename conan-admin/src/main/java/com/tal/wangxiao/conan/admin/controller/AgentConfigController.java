/*
package com.tal.wangxiao.conan.admin.controller;

import java.util.List;

import com.tal.wangxiao.conan.common.controller.ConanBaseController;
import com.tal.wangxiao.conan.common.service.AgentConfigService;
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
import com.tal.wangxiao.conan.sys.common.core.domain.AjaxResult;
import com.tal.wangxiao.conan.sys.common.enums.BusinessType;
import com.tal.wangxiao.conan.common.domain.AgentConfig;
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

*/
/**
 * work机管理Controller
 *
 * @author dengkunan
 * @date 2020-12-23
 *//*

@RestController
@RequestMapping("/api/1.0/common/agent_config")
@Api(value = "agent_config模块管理", tags = "agent_config模块管理")
public class AgentConfigController extends ConanBaseController {
    @Autowired
    private AgentConfigService agentConfigService;

    */
/**
     * 查询work机管理列表
     *//*

    @ApiOperation(value = "", notes = "查询work机管理")
    @PreAuthorize("@ss.hasPermi('common:agent_config:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<AgentConfig>>> list( AgentConfig agentConfig) {
        startPage();
        List<AgentConfig> list = agentConfigService.selectAgentConfigList(agentConfig);
        return getDataTable(list);
    }

    */
/**
     * 导出work机管理列表
     *//*

    @PreAuthorize("@ss.hasPermi('common:agent_config:export')")
    @Log(title = "work机管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export( AgentConfig agentConfig) {
        List<AgentConfig> list = agentConfigService.selectAgentConfigList(agentConfig);
        ExcelUtil<AgentConfig> util = new ExcelUtil<AgentConfig>(AgentConfig.class);
        return util.exportExcel(list, "agent_config");
    }

    */
/**
     * 获取work机管理详细信息
     *//*

    @PreAuthorize("@ss.hasPermi('common:agent_config:query')")
    @GetMapping(value = "/{id}")
    public ApiResponse<AgentConfig> getInfo(@PathVariable("id") Integer id) {
        return ApiResponse.success(agentConfigService.selectAgentConfigById(id));
    }

    */
/**
     * 新增work机管理
     *//*

    @PreAuthorize("@ss.hasPermi('common:agent_config:add')")
    @Log(title = "work机管理", businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody AgentConfig agentConfig) {
        return toAjax(agentConfigService.insertAgentConfig(agentConfig));
    }

    */
/**
     * 修改work机管理
     *//*

    @PreAuthorize("@ss.hasPermi('common:agent_config:edit')")
    @Log(title = "work机管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody AgentConfig agentConfig) {
        return toAjax(agentConfigService.updateAgentConfig(agentConfig));
    }

    */
/**
     * 删除work机管理
     *//*

    @PreAuthorize("@ss.hasPermi('common:agent_config:remove')")
    @Log(title = "work机管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public ApiResponse remove(@PathVariable Integer[] ids) {
        return toAjax(agentConfigService.deleteAgentConfigByIds(ids));
    }
}*/
