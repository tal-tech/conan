package com.tal.wangxiao.conan.common.kafaka;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Kafka消息环境枚举类
 * @author huyaoguo
 * @date 2020/12/03
 */
@Getter
@AllArgsConstructor
public enum KafkaRunEnv {

    ONLINE("ONLINE","线上环境"),
    GRAY("GRAY","灰度环境"),
    TEST("TEST","测试环境"),
    LOCAL("LOCAL","本地环境");

    private String name;
    private String desc;
}
