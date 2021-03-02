package com.tal.wangxiao.conan.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * response返回值枚举类
 * @author huyaoguo
 * @date 2020/12/02
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(200, "ok"),

    INVALID_DEPARTMENT_ID(1001,"无效的部门ID"),
    INVALID_PRODUCT_LINE_ID(1002,"无效的产品线ID"),
    EMPTY_API_LIST(1003, "任务缺少关联的接口信息"),
    INVALID_DOMAIN_ID(1004, "无效的域名ID"),
    OFFLINE_DOMAIN(1005, "域名已下线"),
    INVALID_API_ID(1006, "无效的接口ID"),
    OFFLINE_API(1007, "接口已下线"),
    DISABLED_API(1008, "接口已禁用"),
    INVALID_TASK_ID(1009, "无效的任务ID"),
    INVALID_RECORD_ID(1010, "无效的录制ID"),
    INVALID_REPLAY_ID(1011, "无效的回放ID"),
    INVALID_DIFF_ID(1012, "无效的比对ID"),
    RECORD_DETAIL_NOT_FOUND(1013, "未找到录制详情实体"),
    API_DOMAIN_NOT_FOUND(1014, "未找到该接口的域名"),
    INVALID_TASK_EXECUTION_ID(1015, "无效的任务执行ID"),
    INVALID_STATUS_FOR_REPLAY(1016, "当前任务执行状态不能回放流量"),
    INVALID_STATUS_FOR_DIFF(1017, "当前任务执行状态不能比对流量"),
    RECORD_NOT_FOUND(1018, "未找到录制实体"),
    NO_REQUEST_RECORDED(1019, "没有流量可供回放"),
    INVALID_USER_ID(1020, "错误的userID"),
    NO_VERSION_INFO(1021, "没有版本信息，无法进行打点"),
    INVALID_REDIS_KEY(1023, "Redis中查询不到对应结果"),
    REPLAY_DETAIL_NOT_FOUND(1023, "找不到对应的回放详情"),
    EMPTY_FILE_CONTENT(1024,"空文件或无效文件"),
    EXCEL_PARSE_FAIL(1025,"excel文件解析失败"),
    SCHEDULE_FAIL_REPLAY(1026,"定时任务回放失败"),
    SCHEDULE_FAIL_RECORD(1027,"定时任务录制失败"),
    SCHEDULE_FAIL_DIFF(1028,"定时任务比对失败"),
    FAILED_CREATE_TASKSCHEDULE(1029,"定时任务创建失败"),
    INVALID_CRON_EXPRESSION(1030,"无效的cron表达式"),
    INVALID_SCHEDULE_TASK_ID(1031,"无效的定时任务ID"),
    FAILED_FILE_DOWNLOAD(1032,"无效的定时任务ID"),


    MYSQL_EXCEPTION(80000,"mysql数据库异常"),
    REDIS_EXCEPTION(90000,"redis异常"),
    FAILED(100000,"unknown error, please contact administrator");

    /**
     * 内部返回码
     */
    private int code;
    /**
     * 返回码描述
     */
    private String desc;

    @Override
    public String toString() {
        return super.toString() + " - " + this.getDesc();
    }
}
