package com.tal.wangxiao.conan.agent.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tal.wangxiao.conan.agent.cache.CodeCache;
import com.tal.wangxiao.conan.agent.service.RecordService;
import com.tal.wangxiao.conan.agent.service.ReplayService;
import com.tal.wangxiao.conan.agent.service.impl.DiffServiceImpl;
import com.tal.wangxiao.conan.common.constant.enums.TaskStatus;
import com.tal.wangxiao.conan.common.exception.BaseException;
import com.tal.wangxiao.conan.common.kafaka.KafkaTaskData;
import com.tal.wangxiao.conan.common.kafaka.KafkaType;
import com.tal.wangxiao.conan.common.kafaka.TaskMessage;
import com.tal.wangxiao.conan.common.redis.RedisTemplateTool;
import com.tal.wangxiao.conan.common.service.common.AgentCommonService;
import com.tal.wangxiao.conan.common.service.common.TaskStatusService;
import com.tal.wangxiao.conan.utils.entity.EntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.aop.framework.AopContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Kafka消费者监听器
 *
 * @author conan
 */
@Slf4j
public class KafkaConsumerListener {

    @Resource
    private ReplayService replayService;

    @Resource
    private RecordService recordService;

    @Resource
    private RedisTemplateTool redisTemplateTool;

    @Resource
    private AgentCommonService agentCommonService;

    @Resource
    private DiffServiceImpl diffService;

    @Resource
    private TaskStatusService taskStatusService;

    @Transactional
    @KafkaListener(topics = {"conan-task-dist"})
    public void listen(ConsumerRecord<?, ?> record) {
        String body = record.value().toString();
        TaskMessage taskMessage = null;
        try {
            taskMessage = new ObjectMapper().readValue(body, TaskMessage.class);
        } catch (Exception e) {
            log.error("消费任务队列消息异常", e);
        }
        Map<String, Object> messageMap = (LinkedHashMap) taskMessage.getData().getData();
        KafkaTaskData taskData = EntityUtils.mapToEntity(messageMap, KafkaTaskData.class);
        KafkaType taskType = taskMessage.getData().getType();
        String runEnv = taskMessage.getData().getRunEnv();
        //匹配运行环境
        if (!CodeCache.getEnv().equals(runEnv)) {
            log.info("该任务执行的环境[{}]于该服务环境不匹配[{}]", runEnv, CodeCache.getEnv());
            return;
        }
        //匹配指定机器agentId执行
        if (!CodeCache.getAgentId().equals(taskData.getAgentId())) {
            log.info("Agent 任务不是本机器执行agentID = {}", taskData.getAgentId());
            return;
        }
        if (KafkaType.RECORD.equals(taskType)) {
            log.info("接收到流量录制任务 {}", taskMessage);
            KafkaConsumerListener agentListener = (KafkaConsumerListener) AopContext.currentProxy();
            agentListener.record(taskData);
        } else if (KafkaType.REPLAY.equals(taskType)) {
            log.info("接收到流量回放任务 {}", taskMessage);
            KafkaConsumerListener agentListener = (KafkaConsumerListener) AopContext.currentProxy();
            agentListener.replay(taskData);
        } else if (KafkaType.DIFF.equals(taskType)) {
            log.info("接收到流量比对任务 {}", taskMessage);
            KafkaConsumerListener agentListener = (KafkaConsumerListener) AopContext.currentProxy();
            agentListener.diff(taskData);
        }

    }


    @Async
    void record(KafkaTaskData taskData) {
        try {
            agentCommonService.updateAgentStatus(CodeCache.getAgentId(), true);
            recordService.startRecord(taskData.getTaskExecutionId(), taskData.getRecordId());
            agentCommonService.updateAgentStatus(CodeCache.getAgentId(), false);
        } catch (BaseException e) {
            log.error("录制异常，异常code:" + e.getRetCode());
            redisTemplateTool.setLogByRecordId_END(taskData.getRecordId(), e.getMsgDesc());
            //更新任务状态
            taskStatusService.updateTaskStatus(taskData.getTaskExecutionId(), TaskStatus.RECORD_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("录制异常，异常code:" + e.getMessage());
            redisTemplateTool.setLogByRecordId_END(taskData.getRecordId(), e.getMessage());
            //更新任务状态
            taskStatusService.updateTaskStatus(taskData.getTaskExecutionId(), TaskStatus.RECORD_FAIL);
        }
        redisTemplateTool.setRecordProgress(taskData.getRecordId(), "100");
    }

    @Async
    void replay(KafkaTaskData taskData) {
        try {
            agentCommonService.updateAgentStatus(CodeCache.getAgentId(), true);
            replayService.replay(taskData.getTaskExecutionId(), taskData.getRecordId(), taskData.getReplayId());
            agentCommonService.updateAgentStatus(CodeCache.getAgentId(), false);
        } catch (BaseException e) {
            redisTemplateTool.setLogByReplayId_END(taskData.getReplayId(), e.getMsgDesc());
            log.error("回放异常，异常code:" + e.getRetCode());
            //更新任务状态
            taskStatusService.updateTaskStatus(taskData.getTaskExecutionId(), TaskStatus.REPLAY_FAIL);
        } catch (Exception e) {
            redisTemplateTool.setLogByReplayId_END(taskData.getReplayId(), e.getMessage());
            //更新任务状态
            taskStatusService.updateTaskStatus(taskData.getTaskExecutionId(), TaskStatus.REPLAY_FAIL);
        }
        redisTemplateTool.setReplayProgress(taskData.getReplayId(), "100");
    }


    @Async
    void diff(KafkaTaskData taskData) {
        try {
            agentCommonService.updateAgentStatus(CodeCache.getAgentId(), true);
            diffService.startDiff(taskData.getTaskExecutionId(), taskData.getRecordId(), taskData.getReplayId(), taskData.getDiffId());
            agentCommonService.updateAgentStatus(CodeCache.getAgentId(), false);
        } catch (BaseException e) {
            redisTemplateTool.setLogByDiffId_END(taskData.getDiffId(), e.getMsgDesc());
            log.error("比对异常，异常code:" + e.getRetCode());
            //更新任务状态
            taskStatusService.updateTaskStatus(taskData.getTaskExecutionId(), TaskStatus.DIFF_FAIL);
        } catch (Exception e) {
            redisTemplateTool.setLogByDiffId_END(taskData.getDiffId(), e.getMessage());
            //更新任务状态
            taskStatusService.updateTaskStatus(taskData.getTaskExecutionId(), TaskStatus.DIFF_FAIL);
        }
        redisTemplateTool.setDiffProgress(taskData.getDiffId(), "100");
    }


}
   

