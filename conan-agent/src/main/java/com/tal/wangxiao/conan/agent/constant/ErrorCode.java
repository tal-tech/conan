package com.tal.wangxiao.conan.agent.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码枚举类
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {
    SUCCESS(0, "成功"),
    INVALID_DEPARTMENT_ID(1001, "无效的部门ID"),
    INVALID_PRODUCT_LINE_ID(1002, "无效的产品线ID"),
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
    KAFKA_SEND_FAILURE(1018, "发送kafka消息失败"),
    RECORD_NOT_FOUND(1019, "未找到录制实体"),
    NO_REQUEST_RECORDED(1020, "没有流量可供回放"),
    REPLAY_DETAIL_NOT_FOUND(1021, "未找到流量回放详情实体"),
    API_NOT_FOUND(1022, "未找到接口实体"),
    INVALID_REDIS_KEY(1023, "Redis中查询不到对应结果"),
    FAILED_ADD_USER(1024, "添加用户失败"),
    FAILED_CREATE_TASKSCHEDULE(1025, "创建定时任务失败"),
    INVALID_SCHEDULETASK_ID(1026, "无效的定时任务ID"),
    INVALID_CRON_EXPRESSION(1027, "无效的Cron表达式"),
    INVALID_CASE_ID(1101, "无效的用例ID"),
    INVALID_USER_ID(1102, "无效的用户ID"),
    INVALID_CASEGROUP_ID(1103, "无效的用例组ID"),
    EMPTY_CASE_LIST(1104, "任务缺少关联的用例信息"),
    INVALID_PROTO_ID(1105, "无效的协议模板ID"),
    EMPTY_FILE_CONTENT(1106, "上传文件大小为空"),
    FAIL_DATE_DYED(1107, "数据染色失败"),
    INVALID_ACCOUNT_ID(1108, "无效的账号信息"),
    FAILED_BUY_COURSE(1109, "自动买课失败"),
    FAILED(1200, "执行失败");

    /**
     * 内部返回值
     */
    private int ret;
    /**
     * 返回值描述
     */
    private String desc;

    @Override
    public String toString() {
        return super.toString() + " - " + this.getDesc();
    }
}
