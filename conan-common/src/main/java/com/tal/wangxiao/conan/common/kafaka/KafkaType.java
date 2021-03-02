package com.tal.wangxiao.conan.common.kafaka;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息队列类型，按用途划分
 * @author huyaoguo
 * @date 2020/12/03
 */
@Getter
@AllArgsConstructor
public enum KafkaType {
    RECORD("RECORD", "录制"),
    REPLAY("REPLAY", "回放"),
    DIFF("DIFF", "比对"),
    AGENT_REGISTER("AGENT_REGISTER","agent服务注册"),
    AGENT_CHECK_RUN("AGENT_BEAT_RUN","agent心跳检测"),
    AGENT_CHECK_STOP("AGENT_BEAT_STOP","agent心跳停止");

    private String name;

    private String desc;
}


