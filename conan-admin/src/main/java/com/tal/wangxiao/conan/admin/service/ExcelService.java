package com.tal.wangxiao.conan.admin.service;

import com.tal.wangxiao.conan.common.model.Result;
import org.springframework.web.multipart.MultipartFile;


public interface ExcelService {

    //导入Excel 解析api
    Result<String> importApiByExcel(MultipartFile harFile);
}
