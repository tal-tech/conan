package com.tal.wangxiao.conan.admin.service.impl;


import com.tal.wangxiao.conan.admin.service.ExcelService;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.constant.enums.HttpMethodConstants;
import com.tal.wangxiao.conan.common.entity.db.Api;
import com.tal.wangxiao.conan.common.entity.db.Department;
import com.tal.wangxiao.conan.common.entity.db.Domain;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.vo.ApiExcelVO;
import com.tal.wangxiao.conan.common.repository.db.ApiRepository;
import com.tal.wangxiao.conan.common.repository.db.DepartmentRepository;
import com.tal.wangxiao.conan.common.repository.db.DomainRepository;
import com.tal.wangxiao.conan.sys.auth.core.domain.model.LoginUser;
import com.tal.wangxiao.conan.sys.common.core.domain.entity.SysUser;
import com.tal.wangxiao.conan.sys.common.utils.ServletUtils;
import com.tal.wangxiao.conan.sys.framework.web.service.TokenService;
import com.tal.wangxiao.conan.utils.excel.ExcelHanderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {

    @Resource
    private ApiRepository apiRepository;

    @Resource
    private DepartmentRepository departmentRepository;

    @Resource
    private DomainRepository domainRepository;

    @Resource
    private TokenService tokenService;

    @Override
    public Result<String> importApiByExcel(MultipartFile excelFile) {
        if (Objects.isNull(excelFile) || excelFile.getSize() <= 0) {
            return new Result<>(ResponseCode.EMPTY_FILE_CONTENT, "空文件或者无效的文件");
        }
        File file = null;
        try {
            file = File.createTempFile("tmp", null);
            excelFile.transferTo(file);
        } catch (Exception e) {
            log.error("解析excel失败" + e.getMessage());
            return new Result<>(ResponseCode.EXCEL_PARSE_FAIL, "解析excel失败");
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        List<ApiExcelVO> apiLists = ExcelHanderUtil.readToList(2, 4, file, ApiExcelVO.class);
        int index = 5;
        StringBuilder sb = new StringBuilder();
        int successCount = 0;
        for (ApiExcelVO data : apiLists) {
            String apiName = data.getApiName().trim();
            String method = data.getMethod().trim();
            String domainName = data.getDomainName().trim();
            String isRead = data.getIsRead().trim();
            if ("".equals(isRead)) {
                if ("POST".equals(method)) {
                    isRead = "写";
                } else {
                    isRead = "读";
                }
            }
            String recordableCount = data.getRecordableCount().trim();
            String department = data.getDepartment().trim();
            Integer departmentId = null;
            Integer productLineId = null;
            //部门新增
            Optional<Department> departmentOptional = departmentRepository.findByDeptName(department);
            if (!departmentOptional.isPresent()) {
                //部门属于最外层
                Optional<Department> departmentOptional1 = departmentRepository.findFirstByParentId(0);
                if (!departmentOptional1.isPresent()) {
                    return new Result<>(ResponseCode.EXCEL_PARSE_FAIL, "找不到部门，解析失败");
                }
                departmentId = departmentOptional1.get().getId();
            } else {
                departmentId = departmentOptional.get().getId();
            }
            //域名新增
            Optional<Domain> domainOptional = domainRepository.findByName(domainName);
            Integer domainId = null;
            if (!domainOptional.isPresent()) {
                Domain domain1 = new Domain();
                domain1.setName(domainName);
                domain1.setCreateBy(user.getUserId().intValue());
                domain1.setCreateAt(LocalDateTime.now());
                domain1.setOnline(true);
                domain1.setIsAuth(false);
                Domain newDomain = domainRepository.save(domain1);
                domainId = newDomain.getId();
                log.info("新增域名 name = " + domainName);
            } else {
                domainId = domainOptional.get().getId();
            }
            //接口去重
            List<Api> apiList = apiRepository.findByNameAndMethodAndDomainId(data.getApiName(), HttpMethodConstants.valueOf(data.getMethod()).getValue(), domainId);
            if (apiList.size() > 0) {
                sb.append("第").append(index).append("行 - ").append(data.getApiName()).append("接口重复");
                sb.append("\\n");
                log.error("添加接口失败,库中已存在" + index + "." + data.getApiName());
                index++;
                continue;
            }

            Api newApi = new Api();
            newApi.setDepartmentId(departmentId);
            newApi.setDomainId(domainId);
            newApi.setIsEnable(true);
            newApi.setIsOnline(true);
            if ("读".equals(isRead)) {
                newApi.setIsRead(true);
            } else {
                newApi.setIsRead(false);
            }
            newApi.setMethod(HttpMethodConstants.valueOf(method).getValue());
            newApi.setName(apiName);
            int recordCount = 50;
            try {
                recordCount = Integer.parseInt(recordableCount);
            } catch (Exception e) {
                log.error("可录制条数不是整数，" + recordableCount);
            }
            newApi.setRecordableCount(recordCount);
            newApi.setCreateAt(LocalDateTime.now());
            newApi.setCreateBy(user.getUserId().intValue());
            apiRepository.save(newApi);
            log.info("添加接口成功" + index + "." + data.getApiName());
            index++;
            successCount++;

        }
        String msg = "添加接口完成，成功数" + successCount + ",总数" + apiLists.size();
        if (successCount != apiLists.size()) {
            msg = msg + "\\n" + "失败原因：" + sb;
        }
        return new Result<>(ResponseCode.SUCCESS, msg);
    }

}
