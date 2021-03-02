package com.tal.wangxiao.conan.common.kafaka;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义Kafka消息格式
 *
 * @author huyaoguo
 * @date 2020/12/03
 */
@Data
@NoArgsConstructor
public class TaskMessage<T> {
    /**
     * 消息数据实体
     */
    private KafkaData<T> data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public TaskMessage(KafkaData<T> data){
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public TaskMessage(KafkaData<T> data,Long timestamp){
        this.data = data;
        this.timestamp = timestamp;
    }

}
