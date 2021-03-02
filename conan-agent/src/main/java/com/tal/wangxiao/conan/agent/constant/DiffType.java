package com.tal.wangxiao.conan.agent.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 比对模式类型枚举类
 */
@AllArgsConstructor
@Getter
public enum DiffType {
    JSON("JSON比对", 0),
    FIELD("字段匹配", 1);

    private String label;
    private Integer value;
}
