package com.tal.wangxiao.conan.admin.service;


import com.tal.wangxiao.conan.common.domain.TaskExecutionInfo;
import com.tal.wangxiao.conan.common.model.Result;

import java.util.List;


/**
 * 执行任务管理服务
 *
 * @author liujinsong
 * @date 2021/1/21
 */
public interface TaskExecutionService {
    /**
     * 创建任务执行
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    Result<Object> initExcAndRecord(Integer taskId,Integer operateBy) throws Exception;


    /**
     * 任务执行列表
     *
     * @param taskExecutionInfo
     * @return
     * @throws Exception
     */
    List<TaskExecutionInfo> findTaskExecutionList(TaskExecutionInfo taskExecutionInfo);


}
