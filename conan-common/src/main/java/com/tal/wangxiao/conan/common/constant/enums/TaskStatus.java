package com.tal.wangxiao.conan.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态枚举类
 * @author conan
 */
@Getter
@AllArgsConstructor
public enum TaskStatus {
    READY("可执行", 0),
    RECORD("录制中", 1),
    RECORD_SUCCESS("录制成功", 2),
    RECORD_FAIL("录制失败", 3),
    REPLAY("回放中", 4),
    REPLAY_SUCCESS("回放成功", 5),
    REPLAY_FAIL("回放失败", 6),
    DIFF("比对中", 7),
    DIFF_SUCCESS("比对成功", 8),
    DIFF_FAIL("比对失败", 9);

    private String label;
    private Integer value;
}
