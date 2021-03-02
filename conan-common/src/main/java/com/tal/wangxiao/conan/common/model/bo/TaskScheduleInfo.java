package com.tal.wangxiao.conan.common.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用例组BO信息类，用于创建用例组从前端获取信息
 * @author huyaoguo
 * @data 2020/11/24
 */
@Data
@ApiModel(value = "TaskScheduleInfo", description = "从前端接收创建定时任务的数据")
public class TaskScheduleInfo {

    @NotBlank
    @ApiModelProperty(value = "定时任务名称", name = "name", required = true, position = 1)
    private String name;

    @Min(1)
    @JsonProperty("task_id")
    @ApiModelProperty(value = "任务ID", name = "task_id", required = true, position = 2)
    private Integer taskId;

    @JsonProperty("task_name")
    @ApiModelProperty(value = "任务名称", name = "task_name", position = 2)
    private String taskName;

    @Min(1)
    @Max(2)
    @ApiModelProperty(value = "定时任务类型（1-录制，2-回放）", name = "type", position = 3, required = true)
    private Integer type;

    @NotNull
    @ApiModelProperty(value = "cron表达式", name = "cron", position = 4, required = true)
    private String cron;

    @ApiModelProperty(value = "任务执行状态(0-运行，1暂停)", name = "status", position = 5)
    private Integer status;

    @Min(0)
    @Max(1)
    @ApiModelProperty(value = "执行策略(0-默认,1只执行一次)", name = "strategy", position = 6, required = true)
    private Integer strategy;

    @JsonProperty(value = "operate_by",index = 6)
    @ApiModelProperty(value = "执行人", name = "operate_by", position = 7)
    private String operateBy;



}
