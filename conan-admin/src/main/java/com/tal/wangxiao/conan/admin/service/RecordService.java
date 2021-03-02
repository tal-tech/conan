package com.tal.wangxiao.conan.admin.service;

import com.google.common.collect.Lists;
import com.tal.wangxiao.conan.admin.constant.HttpMethod;
import com.tal.wangxiao.conan.common.entity.db.Record;
import com.tal.wangxiao.conan.common.model.Result;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 流量录制管理
 * @author liujinsong
 * @date 2021/1/21
 */
public interface RecordService {
    /**
     * 根据录制ID查找录制进度
     * @param taskExecutionId 任务执行ID
     * @return
     */
    Result<String> findRecordProgress(Integer taskExecutionId);


    Result<Object> findLogByExecutionId(Integer taskExecutionId);

    Result<Object> findByTaskExecutionId(Integer taskExecutionId);


    //导出域名的所有流量
    @Async
    void getGetFlowsByDomain(String domainName, HttpServletResponse response) throws Exception;

}
