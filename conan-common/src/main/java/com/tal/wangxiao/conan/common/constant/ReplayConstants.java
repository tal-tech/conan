package com.tal.wangxiao.conan.common.constant;

/**
 * 回放相关常量
 *
 * @author huyaoguo
 * @date 2021/1/15
 **/
public interface ReplayConstants {

    //定时巡检回放
    Integer REPLAY_SCHEDULE = 0;

    //手动执行回放
    Integer REPLAY_MANUAL = 1;

    //外部触发（例如发布系统）
    Integer REPLAY_DEPLOY = 2;

}
