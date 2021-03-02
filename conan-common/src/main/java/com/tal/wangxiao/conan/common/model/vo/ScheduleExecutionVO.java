package com.tal.wangxiao.conan.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author huyaoguo
 * @date 2020/11/25
 * @description 定时任务执行记录VO
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class ScheduleExecutionVO implements Serializable {

    private Integer id;

    @ApiModelProperty(value = "任务执行状态", name = "status")
    private String status;

    @JsonProperty(value = "task_schedule_id")
    @ApiModelProperty(value = "定时任务ID", name = "task_schedule_id")
    private Integer taskScheduleId;

    @JsonProperty(value = "task_schedule_name")
    @ApiModelProperty(value = "定时任务名称", name = "task_schedule_name")
    private String taskScheduleName;

    @JsonProperty(value = "task_id")
    @ApiModelProperty(value = "任务ID", name = "department_id")
    private Integer taskId;

    @JsonProperty(value = "task_name")
    @ApiModelProperty(value = "任务名称", name = "department_id")
    private String taskName;

    @JsonProperty(value = "task_type")
    @ApiModelProperty(value = "定时任务类型", name = "task_type")
    private String taskType;

    @JsonProperty(value = "message")
    @ApiModelProperty(value = "执行消息", name = "message")
    private String message;

    @JsonProperty(value = "start_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间", name = "start_at")
    private LocalDateTime startAt;

    @JsonProperty(value = "operate_by")
    @ApiModelProperty(value = "执行人", name = "operate_by")
    private String operateBy;

}
