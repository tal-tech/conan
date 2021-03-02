package com.tal.wangxiao.conan.agent.service;

/**
 * 流量回放管理服务
 * @author conan
 */
public interface ReplayService {
    /**
     * 流量回放
     * @param taskExecutionId 任务执行ID
     * @param recordId 任务录制ID
     * @param replayId 任务回放ID
     * @throws Exception
     */
    void replay(Integer taskExecutionId, Integer recordId, Integer replayId) throws Exception;
}
