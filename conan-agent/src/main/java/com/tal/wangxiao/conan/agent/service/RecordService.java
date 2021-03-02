package com.tal.wangxiao.conan.agent.service;


import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.bo.RecordInfo;

/**
 * 流量录制管理
 * @author liujinsong
 * @date 2019/6/27
 */
public interface RecordService {
    /**
     * 根据任务执行ID查找录制信息
     * @param taskExecutionId
     * @return
     * @throws Exception
     */
    Result findByTaskExecutionId(Integer taskExecutionId) throws Exception;

    /**
     * 开始录制
     * @param taskExecutionId 任务执行ID
     * @param recordId 任务录制ID
     * @throws Exception
     */
    void startRecord(Integer taskExecutionId, Integer recordId) throws Exception;

}
