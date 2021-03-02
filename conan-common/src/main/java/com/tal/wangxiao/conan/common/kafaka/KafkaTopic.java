package com.tal.wangxiao.conan.common.kafaka;

/**
 * kafka topic常量
 *
 * @author huyaoguo
 * @date 2020/12/15
 **/
public interface KafkaTopic {

    /**
     * admin下发至agent的任务消息
     */
    String CONAN_TASK_DIST = "conan-task-dist";

    /**
     * agent上报至admin的消息
     */
    String CONAN_AGENT_MESSAGE = "conan-agent-message";

}
