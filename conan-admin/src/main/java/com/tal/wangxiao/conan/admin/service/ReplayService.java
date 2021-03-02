package com.tal.wangxiao.conan.admin.service;

import com.tal.wangxiao.conan.common.model.Result;

/**
 * 流量回放服务类
 *
 * @author huyaoguo
 * @date 2021/1/6
 **/
public interface ReplayService {

    /**
     * 根据任务执行ID获取流量回放记录列表
     * @param taskExecutionId 任务执行ID
     * @return
     */
    Result<Object> findReplaysByTaskExecutionId(Integer taskExecutionId);

    /**
     * 查询流量回放详情
     * @param replayId 回放ID
     * @return
     */
    Result<Object> findDetailByReplayId(Integer replayId);

    /**
     * 开始新一次流量回放任务
     * @param taskExecutionId 任务执行ID
     * @param replayEnv 回放环境（线上，灰度，测试）
     * @param replayType 回放类型（0-定时任务，1-手动执行，3-其它路径例如发布系统触发）
     * @return
     */
    Result<Object> startReplay(Integer taskExecutionId, String replayEnv, Integer replayType);

    /**
     * 查询单个接口的回放详情
     * @param replayId 回放ID
     * @param apiId 接口ID
     * @return Result
     */
    Result<Object> findOneApiDetailByReplayIdAndApiId(Integer replayId, Integer apiId);

    /**
     * 查询回放日志
     * @param replayId 回放ID
     * @return Result
     */
    Result<String> findLogByReplayId(Integer replayId);

    /**
     * 根据回放Id查看回放进度
     * @param replayId 回放ID
     * @return Result
     */
    Result<Object> findReplayProgress(Integer replayId);

    /**
     * 设置该回放记录作为比对基准
     * @param replayId 回放ID
     * @return Result
     */
    Result<String> setBaseLine(Integer replayId);
}
