package com.tal.wangxiao.conan.common.kafaka;

import lombok.Data;
import lombok.ToString;

/**
 * Kafka发送的消息Model
 *
 * @author huyaoguo
 * @date 2020/12/03
 */
@Data
@ToString
public class KafkaTaskData {
    /**
     * 任务Id
     */
    private Integer taskId;

    /**
     * 任务执行ID
     */
    private Integer taskExecutionId;

    /**
     * 任务录制ID
     */
    private Integer recordId;

    /**
     * 任务回放ID
     */
    private Integer replayId;

    /**
     * 任务指定的agentId
     */
    private String agentId;

    /**
     * 任务指定的agentId
     */
    private Integer diffId;
}
