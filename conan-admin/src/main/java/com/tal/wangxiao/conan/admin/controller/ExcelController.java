package com.tal.wangxiao.conan.admin.controller;

import com.tal.wangxiao.conan.admin.service.ExcelService;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.utils.DownloadFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel工具类(流量的导入导出)
 * @author huyaoguo
 * @date 2020/7/6
 */
@Api(value = "Excel API", tags = "excel文件相关接口")
@RestController
@RequestMapping("api/1.0/excel")
@Slf4j
public class ExcelController {

    @Resource
    private ExcelService excelService;

    @ApiOperation(value = "解析excel导入API接口", notes = "导入excel")
    @PostMapping(value = "")
    public ApiResponse<String> importIntoApi(@RequestParam(value = "excel",required = false) MultipartFile excelFile){
        log.info("ExcelController#importIntoApi");
        Result<String> result = excelService.importApiByExcel(excelFile);
        return  new ApiResponse<>(result);
    }

    @ApiOperation(value = "根据domainName下载文件(不删除生成到文件)", notes = "根据domainName下载文件(不删除生成到文件)",produces = "application/json")
    @GetMapping(value = "/downloadFileNoDeleteByDomain")
    public void downloadFileNoDeleteByDomain(@RequestParam(value = "domain",required = false) String domainName,
                                             HttpServletResponse response) throws Exception {
        log.info("EsConroller#downloadFileNoDeleteByDomain");
        String path = ResourceUtils.getURL("classpath:").getPath() + "static/";//+domain+"域名接口.xlsx";
        String name = domainName+".xlsx";
        File file = new File(path + name);
        DownloadFileUtil.downloadFileNoDelete(name,file,response);
    }

    @ApiOperation(value = "删除文件", notes = "删除文件",produces = "application/json")
    @GetMapping(value = "/deleteFileByDomain")
    public ApiResponse<String> deleteFileByDomain(@RequestParam(value = "domain",required = false) String domainName) throws Exception {
        log.info("EsConroller#deleteFileByDomain");
        String path = ResourceUtils.getURL("classpath:").getPath() + "static/";//+domain+"域名接口.xlsx";
        String name = domainName+".xlsx";
        File file = new File(path + name);
        try {
            if(!file.delete()){
                return  new ApiResponse<>(new Result<>(ResponseCode.SUCCESS,"文件删除失败"));
            }
            return  new ApiResponse<>(new Result<>(ResponseCode.SUCCESS,"文件删除成功"));
        } catch (Exception e) {
            log.error("删除文件" + e);
            return  new ApiResponse<>(new Result<>(ResponseCode.SUCCESS,"文件删除异常"));
        }
    }

    @ApiOperation(value = "获取文件列表", notes = "获取文件列表",produces = "application/json")
    @GetMapping(value = "/getFileNameList")
    public ApiResponse<List<String>> getFileNameList() throws Exception{
        log.info("ExcelController#getFileNameList");
        String path = ResourceUtils.getURL("classpath:").getPath() + "static/";//+domain+"域名接口.xlsx";
        File file = new File(path);
        List<String> fileNAmeList = new ArrayList<>();
        try {
            for(File newFile :file.listFiles()) {
                fileNAmeList.add(newFile.getName());
            }
        } catch (Exception e) {
            log.error("获取文件列表失败" + e);
            return  new ApiResponse(new Result<>(ResponseCode.EXCEL_PARSE_FAIL,"获取文件列表失败"));
        }
        return  new ApiResponse<>(new Result<>(ResponseCode.SUCCESS,fileNAmeList));
    }
}
