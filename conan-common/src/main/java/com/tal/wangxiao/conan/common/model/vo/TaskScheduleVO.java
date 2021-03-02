package com.tal.wangxiao.conan.common.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author huyaoguo
 * @date 2020/11/25
 * @description 定时任务VO
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class TaskScheduleVO extends BaseVO implements Serializable {

    private Integer id;

    private String name;

    @ApiModelProperty(value = "任务类型", name = "type")
    private String type;

    @JsonProperty(value = "task_id")
    @ApiModelProperty(value = "任务ID", name = "department_id")
    private Integer taskId;

    @JsonProperty(value = "task_name")
    @ApiModelProperty(value = "任务名称", name = "department_id")
    private String taskName;

    @JsonProperty(value = "cron")
    @ApiModelProperty(value = "cron表达式", name = "cron")
    private String cron;

    @JsonProperty(value = "strategy")
    @ApiModelProperty(value = "定时策略", name = "strategy")
    private String strategy;

    /*@JsonProperty(value = "status_name")
    @ApiModelProperty(value = "执行状态名称", name = "status_name")
    private String statusName;*/

    @JsonProperty(value = "status")
    @ApiModelProperty(value = "执行状态", name = "status")
    private String status;

}
