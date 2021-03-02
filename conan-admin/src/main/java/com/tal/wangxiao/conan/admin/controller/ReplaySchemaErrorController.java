package com.tal.wangxiao.conan.admin.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.tal.wangxiao.conan.common.domain.ReplayErrorInterfaceListInfo;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.tal.wangxiao.conan.sys.common.annotation.Log;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.api.PageInfoResponse;
import com.tal.wangxiao.conan.common.controller.ConanBaseController;
import com.tal.wangxiao.conan.sys.common.core.domain.AjaxResult;
import com.tal.wangxiao.conan.sys.common.enums.BusinessType;
import com.tal.wangxiao.conan.common.domain.ReplaySchemaError;
import com.tal.wangxiao.conan.common.service.ReplaySchemaErrorService;
import com.tal.wangxiao.conan.sys.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 回放schema错误记录Controller
 *
 * @author dengkunan
 * @date 2021-01-07
 */
@RestController
@RequestMapping("/api/1.0/common/replaySchemaError")
@Api(value = "replaySchemaError模块管理", tags = "replaySchemaError模块管理")
@Slf4j
public class ReplaySchemaErrorController extends ConanBaseController {
    @Autowired
    private ReplaySchemaErrorService replaySchemaErrorService;

    /**
     * 查询回放schema错误记录列表
     */
    @ApiOperation(value = "", notes = "查询回放schema错误记录")
   // @PreAuthorize("@ss.hasPermi('common:replaySchemaError:list')")
    @GetMapping("/list")
    public ApiResponse<PageInfoResponse<List<ReplaySchemaError>>> list( ReplaySchemaError replaySchemaError) {
        startPage();
        List<ReplaySchemaError> list = replaySchemaErrorService.selectReplaySchemaErrorList(replaySchemaError);
        return getDataTable(list);
    }

    @ApiOperation(value = "错误接口统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "api接口名", dataType = "Integer", paramType = "query"),
    })
    @GetMapping(value = "response/getResponseInterfaceNumber")
    public ApiResponse<ReplayErrorInterfaceListInfo> getResponseInterfaceNumber(
            @RequestParam(value = "rePlayid", required = false) Integer rePlayid) {
        if (StringHandlerUtils.isNull(rePlayid)) {
            log.error("请求参数错误");
            return ApiResponse.error("请求参数错误, id is null");
        }
        return replaySchemaErrorService.getResponseInterfaceNumber(rePlayid);

    }

/*    *//**
     * 导出回放schema错误记录列表
     *//*
    @PreAuthorize("@ss.hasPermi('common:replaySchemaError:export')")
    @Log(title = "回放schema错误记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export( ReplaySchemaError replaySchemaError) {
        List<ReplaySchemaError> list = replaySchemaErrorService.selectReplaySchemaErrorList(replaySchemaError);
        ExcelUtil<ReplaySchemaError> util = new ExcelUtil<ReplaySchemaError>(ReplaySchemaError.class);
        return util.exportExcel(list, "replaySchemaError");
    }*/

    /**
     * 获取回放schema错误记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('common:replaySchemaError:query')")
    @GetMapping(value = "/{schemaErrorId}")
    public ApiResponse<ReplaySchemaError> getInfo(@PathVariable("schemaErrorId") Integer schemaErrorId) {
        return ApiResponse.success(replaySchemaErrorService.selectReplaySchemaErrorById(schemaErrorId));
    }
/*
    *//**
     * 新增回放schema错误记录
     *//*
    @PreAuthorize("@ss.hasPermi('common:replaySchemaError:add')")
    @Log(title = "回放schema错误记录", businessType = BusinessType.INSERT)
    @PostMapping
    public ApiResponse add(@RequestBody ReplaySchemaError replaySchemaError) {
        return toAjax(replaySchemaErrorService.insertReplaySchemaError(replaySchemaError));
    }*/

  /*  *//**
     * 修改回放schema错误记录
     *//*
    @PreAuthorize("@ss.hasPermi('common:replaySchemaError:edit')")
    @Log(title = "回放schema错误记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public ApiResponse edit(@RequestBody ReplaySchemaError replaySchemaError) {
        return toAjax(replaySchemaErrorService.updateReplaySchemaError(replaySchemaError));
    }*/

    /**
     * 删除回放schema错误记录
     */
   // @PreAuthorize("@ss.hasPermi('common:replaySchemaError:remove')")
    @Log(title = "回放schema错误记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{schemaErrorIds}")
    public ApiResponse remove(@PathVariable Integer[] schemaErrorIds) {
        return toAjax(replaySchemaErrorService.deleteReplaySchemaErrorByIds(schemaErrorIds));
    }
}