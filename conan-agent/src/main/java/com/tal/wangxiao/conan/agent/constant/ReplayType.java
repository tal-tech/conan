package com.tal.wangxiao.conan.agent.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 回放类型枚举类型
 *
 * @author huyaoguo
 * @date 2020/8/14
 */
@AllArgsConstructor
@Getter
public enum ReplayType {
    CHECK("定时任务巡检", 0),
    DEPLOY("发布系统触发", 1),
    MANUAL("平台手动执行", 2);

    private String label;
    private Integer value;
}
