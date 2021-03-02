package com.tal.wangxiao.conan.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务类型枚举类
 */
@Getter
@AllArgsConstructor
public enum ScheduleTaskTypeConstants {

    //录制并回放一次后作为基准
    RECORD("录制", 1),
    //回放一次并与基准进行比对
    REPLAY("回放", 2);
    /*RECORD_REPLAY("录制并回放", 3),
    DIFF("比对",4),
    REPLAY_DIFF("回放并比对", 5),
    RECORD_REPLAY_DIFF("录制回放并比对", 6);*/

    private String label;
    private Integer value;
}
