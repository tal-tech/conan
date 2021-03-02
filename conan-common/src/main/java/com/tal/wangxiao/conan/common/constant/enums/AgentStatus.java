package com.tal.wangxiao.conan.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举类
 * @author conan
 */
@Getter
@AllArgsConstructor
public enum AgentStatus {

    READY("失活", 0),
    FREE("空闲", 1),
    BUSY("忙碌", 2);

    private String label;
    private Integer value;
}
