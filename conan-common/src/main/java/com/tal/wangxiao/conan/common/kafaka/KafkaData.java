package com.tal.wangxiao.conan.common.kafaka;

import lombok.Data;

/**
 * Kafka消息数据实体
 *
 * @author huyaoguo
 * @date 2020/12/03
 */

@Data
public class KafkaData<T> {

    /**
     * 消息类型
     */
    private KafkaType type;

    /**
     * 消息环境
     */
    private String runEnv;

    /**
     * 消息体
     */
    private T data;

    @Override
    public String toString() {
        return "kafkaType = " + type.getName() + " - " + "runEnv = " + runEnv;
    }
}

