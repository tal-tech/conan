package com.tal.wangxiao.conan.admin.service.impl;

import com.tal.wangxiao.conan.admin.service.DiffService;
import com.tal.wangxiao.conan.admin.service.ReplayService;
import com.tal.wangxiao.conan.admin.service.TaskExecutionService;
import com.tal.wangxiao.conan.admin.service.TaskPipelineService;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.entity.db.Replay;
import com.tal.wangxiao.conan.common.entity.db.Task;
import com.tal.wangxiao.conan.common.entity.db.TaskExecution;
import com.tal.wangxiao.conan.common.exception.BaseException;
import com.tal.wangxiao.conan.common.exception.task.TaskException;
import com.tal.wangxiao.conan.common.kafaka.KafkaTaskData;
import com.tal.wangxiao.conan.common.kafaka.TaskMessage;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.repository.db.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 任务流水线服务实现类
 *
 * @author huyaoguo
 * @date 2019/9/23
 */
@Service
@Slf4j
public class TaskPipelineServiceImpl implements TaskPipelineService {

    @Resource
    private TaskExecutionRepository taskExecutionRepository;

    @Resource
    private ReplayService replayService;

    @Resource
    private ReplayRepository replayRepository;

    @Resource
    private DiffRepository diffRepository;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private DiffService diffService;

    @Resource
    private TaskExecutionService taskExecutionService;

    @Resource
    private ReplayDetailRepository replayDetailRepository;

    @PersistenceContext
    private EntityManager em;

    int scanTimes = 120;    //轮询次数

    Result<Object> result = new Result<>(ResponseCode.SUCCESS, null);

    //多线程执行(适用于任务回放+比对)
    @Async
    public Future<Result> asyncTaskReplayPipeline(Integer taskId, Integer operateBy, String replayEnv) {
        Result result = taskExecutionOnReplayAndDiff(taskId, operateBy, replayEnv);
        //消息汇总
        return new AsyncResult<>(result);
    }

    //多线程执行（适用于任务录制+回放+设置基准baseLine）
    @Async
    public Future<Result> asyncTaskRecordPipeline(Integer taskId, Integer operateBy, String replayEnv) {
        Result<Object> result = taskExecutionOnRecordAndReplay(taskId, operateBy, replayEnv);
        //消息汇总
        return new AsyncResult<>(result);
    }

    //录制+回放+设置为基准
    @Override
    public Result<Object> taskExecutionOnRecordAndReplay(Integer taskId, Integer operateBy, String replayEnv) {
        Integer recordId = null;
        Integer replayId = null;
        try {
            recordId = doRecord(taskId, operateBy);
        } catch (Exception e) {
            log.error("录制异常信息，err_msg = " + e.getMessage());
            return new Result<>(ResponseCode.SCHEDULE_FAIL_RECORD, e.getMessage());
        }
        if (Objects.isNull(recordId)) {
            return new Result<>(ResponseCode.SCHEDULE_FAIL_RECORD, "录制超过2小时，录制超时结束");
        }
        try {
            replayId = doReplay(taskId, operateBy, replayEnv);
            setBaseLine(replayId);
            log.info("该回放记录已设置为基准");
        } catch (Exception e) {
            log.error("执行回放异常信息，err_msg = " + e.getMessage());
            return new Result<>(ResponseCode.SCHEDULE_FAIL_REPLAY, e.getMessage());
        }
        if (Objects.isNull(replayId)) {
            return new Result<>(ResponseCode.SCHEDULE_FAIL_REPLAY, "回放超过半小时，回放超时");
        }
        return new Result<>(ResponseCode.SUCCESS, "定时任务执行完成");

    }

    //回放+比对
    @Override
    public Result<Object> taskExecutionOnReplayAndDiff(Integer taskId, Integer operateBy, String replayEnv) {
        Integer replayId = null;
        Integer diffId = null;
        try {
            replayId = doReplay(taskId, operateBy, replayEnv);
        } catch (Exception e) {
            log.error("回放异常信息，err_msg = " + e.getMessage());
            return new Result<>(ResponseCode.SCHEDULE_FAIL_REPLAY, e.getMessage());
        }
        if (Objects.isNull(replayId)) {
            return new Result<>(ResponseCode.SCHEDULE_FAIL_REPLAY, "回放超过2小时，回放超时");
        }
        try {
            diffId = doDiff(replayId, operateBy);
        } catch (Exception e) {
            log.error("比对异常信息，err_msg = " + e.getMessage());
            return new Result<>(ResponseCode.SCHEDULE_FAIL_DIFF, e.getMessage());
        }
        if (Objects.isNull(diffId)) {
            return new Result<>(ResponseCode.SCHEDULE_FAIL_DIFF, "比对超过半小时，比对超时");
        }
        return new Result<>(ResponseCode.SUCCESS, "执行完成");
    }

    //根据任务ID执行回放, 成功返回replayId
    public Integer doRecord(int taskId, int operateBy) throws Exception {
        Integer recordId = null;
        Integer taskExecutionId = null;
        Result result = taskExecutionService.initExcAndRecord(taskId, 0);
        if (!result.getRespCode().equals(ResponseCode.SUCCESS)) {
            log.error("录制任务执行失败: {}", result.getDesc());
            throw new Exception("录制任务执行失败: " + result.getDesc());

        }
        try {
            TaskMessage<KafkaTaskData> taskMessage = (TaskMessage<KafkaTaskData>) result.getDesc();
            recordId = taskMessage.getData().getData().getRecordId();
            taskExecutionId = taskMessage.getData().getData().getTaskExecutionId();
            log.info("开始自动录制，taskId = {},operateBy = {}, agentId = {}", taskId, operateBy, taskMessage.getData().getData().getAgentId());
        } catch (Exception e) {
            log.error("数据转换异常err_msg：" + e.getMessage());
        }
        //执行录制完成后开始轮询任务状态
        for (int i = 0; i < scanTimes; i++) {
            TimeUnit.MINUTES.sleep(1);
            em.clear();
            if (taskExecutionRepository.findById(taskExecutionId).get().getStatus() == 2) {
                //表示录制成功
                log.info("录制成功 taskExecutionID = {} recordId = {}", taskExecutionId, recordId);
                return recordId;
            } else if (taskExecutionRepository.findById(taskExecutionId).get().getStatus() == 3) {
                //表示录制失败，跳出
                log.error("录制失败 taskExecutionID = {} replayID = {}", taskExecutionId, recordId);
                throw new Exception("该任务状态为回放失败");
            }
        }
        return recordId;
    }

    //根据任务ID执行回放, 成功返回replayId
    public Integer doReplay(int taskId, int operateBy, String replayEnv) throws BaseException {
        log.info("开始自动回放，taskId = {},operateBy = {},replayEnv = {}", taskId, operateBy, replayEnv);
        Integer replayId = null;
        List<TaskExecution> taskExecutionList = taskExecutionRepository.findByTaskIdAndCreateByOrderByUpdateAtDesc(taskId, 0);
        if (taskExecutionList.size() == 0) {
            //代表没有管理员的录制记录不进行回放
            throw new BaseException("该任务没有管理员录制执行记录，回放失败");
        }
        int taskExecutionId = taskExecutionList.get(0).getId();
        int taskStatus = taskExecutionList.get(0).getStatus();
        //任务状态为录制完成或者回放完成比对,开启回放
        if (taskStatus == 2 || taskStatus >= 5) {
            log.info("开始自动回放 taskExecutionId={}", taskExecutionId);
            try {
                if (0 == operateBy) {
                    //定时巡检
                    result = replayService.startReplay(taskExecutionId, replayEnv, 0);
                } else {
                    //发布系统触发
                    result = replayService.startReplay(taskExecutionId, replayEnv, 1);
                }
            } catch (Exception e) {
                throw new BaseException("执行回放任务失败 taskID=" + taskId + " replayID=" + replayId + ",err_msg=" + e.getMessage());
            }
            if (result.getRespCode().equals(ResponseCode.SUCCESS)) {
                try {
                    TaskMessage<KafkaTaskData> map = (TaskMessage<KafkaTaskData>) result.getDesc();
                    replayId = map.getData().getData().getReplayId();
                } catch (Exception e) {
                    log.error("数据转换异常err_msg：" + e.getMessage());
                }
                log.info("定时任务执行 replayID={}", replayId);
            }
        }
        //执行回放完成后开始轮询任务状态
        for (int i = 0; i < scanTimes; i++) {
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                throw new BaseException("回放轮巡异常");
            }
            em.clear();
            if (taskExecutionRepository.findById(taskExecutionId).get().getStatus() == 5) {
                //表示回放成功
                log.info("回放成功 taskExecutionID={} replayID={}", taskExecutionId, replayId);
                Optional<Task> taskOptional = taskRepository.findById(taskId);
                if (!taskOptional.isPresent()) {
                    //找不到任务信息轮巡失败
                    throw new TaskException("找不到该任务信息，轮巡回放失败", TaskException.Code.NO_TASK_EXISTS);
                }
                return replayId;
            } else if (taskExecutionRepository.findById(taskExecutionId).get().getStatus() == 6) {
                //回放失败，跳出
                log.error("回放失败 taskExecutionID={} replayID={}", taskExecutionId, replayId);
                throw new BaseException("该任务状态为回放失败");
            }
        }
        return replayId;
    }

    //设置单个回放任务为基准回放
    public Integer setBaseLine(Integer replayId) throws Exception {
        Optional<Replay> replayOptional = replayRepository.findById(replayId);
        if (!replayOptional.isPresent()) {
            throw new Exception("该回放ID不存在");
        }
        Replay replay = replayOptional.get();
        replay.setIsBaseline(true);
        replay = replayRepository.save(replay);
        return replay.getId();
    }

    //根据相关参数执行比对
    public Integer doDiff(Integer replayId, Integer operateBy) throws BaseException {
        Optional<Replay> replayOptional = replayRepository.findById(replayId);
        if (!replayOptional.isPresent()) {
            throw new BaseException("执行比对异常 找不到有效的回放信息 replayId = " + replayId);
        }
        Replay replay = replayOptional.get();
        Integer recordId = replay.getRecordId();
        Integer taskExecutionId = null;
        Integer diffId = null;
        try {
            log.info("开始执行自动比对 ,recordId={},replayId={}", recordId, replayId);
            result = diffService.startDiff(replayId, operateBy);
            if (result.getRespCode().equals(ResponseCode.SUCCESS)) {
                //如果比对成功 发送比对成功通知
                try {
                    TaskMessage<KafkaTaskData> map = (TaskMessage<KafkaTaskData>) result.getDesc();
                    diffId = map.getData().getData().getDiffId();
                    taskExecutionId = map.getData().getData().getTaskExecutionId();
                } catch (Exception e) {
                    log.error("数据转换异常err_msg：" + e.getMessage());
                }
            } else {
                throw new Exception("发送diff执行请求失败");
            }
        } catch (Exception e) {
            log.error("执行比对异常 err_msg = {},recordId={},replayId={}", e.getMessage(), recordId, recordId);
            throw new BaseException("执行比对异常 err_msg = " + e.getMessage());
        }
        for (int i = 0; i < scanTimes; i++) {
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                log.error("执行比对异常 比对论询超时");
            }
            em.clear();
            if (taskExecutionRepository.findById(taskExecutionId).get().getStatus() == 8 && !Objects.isNull(diffRepository.findById(diffId).get().getSuccessRate())) {
                return diffId;
            } else if (taskExecutionRepository.findById(taskExecutionId).get().getStatus() == 9) {
                //轮询任务执行状态为比对失败
                throw new BaseException("该任务执行比对状态失败 taskExecutionId = " + taskExecutionId);
            }
        }
        return null;
    }

}
