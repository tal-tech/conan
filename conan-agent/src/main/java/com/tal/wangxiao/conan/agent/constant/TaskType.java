package com.tal.wangxiao.conan.agent.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务类型枚举类
 *
 * @author conan
 */
@Getter
@AllArgsConstructor
public enum TaskType {
    REPLAY("流量回放", 0),
    CUSTOM("自定义", 1);

    private String label;
    private Integer value;
}
