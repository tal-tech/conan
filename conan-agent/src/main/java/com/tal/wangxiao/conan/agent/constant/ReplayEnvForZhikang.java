package com.tal.wangxiao.conan.agent.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 智康回放环境枚举类型
 *
 * @author liujinsong
 * @date 2020/10/20
 */
@AllArgsConstructor
@Getter
public enum ReplayEnvForZhikang {
    ONLINE("线上", 0),
    PREONLINE("预上线", 1);


    private String label;
    private Integer value;
}
