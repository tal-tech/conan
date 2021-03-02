package com.tal.wangxiao.conan.admin.service;

import com.tal.wangxiao.conan.common.model.Result;

/**
 * 任务流水线服务
 *
 * @author huyaoguo
 * @date 2019/9/23
 */
public interface TaskPipelineService {

    //任务的执行流水线，涉及到回放比对
    Result taskExecutionOnReplayAndDiff(Integer taskId, Integer createBy, String replayEnv);

    //任务的执行流水线，涉及到录制+回放+设置为基准
    Result taskExecutionOnRecordAndReplay(Integer taskId, Integer createBy, String replayEnv);
}
