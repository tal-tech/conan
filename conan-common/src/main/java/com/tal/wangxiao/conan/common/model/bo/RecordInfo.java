package com.tal.wangxiao.conan.common.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 录制信息
 */
@Data
@ApiModel(value = "RecordInfo", description = "流量录制信息")
public class RecordInfo {

    @ApiModelProperty(value = "录制Id", name = "record_id")
    @JsonProperty(value = "record_id", index = 1)
    private Integer recordId;

    @ApiModelProperty(value = "任务Id", name = "task_id")
    @JsonProperty(value = "task_id", index = 2)
    private Integer taskId;

    @ApiModelProperty(value = "任务名称", name = "task_name")
    @JsonProperty(value = "task_name", index = 3)
    private String taskName;

    @ApiModelProperty(value = "任务执行ID", name = "task_execution_id")
    @JsonProperty(value = "task_execution_id", index = 4)
    private Integer taskExecutionId;

    @ApiModelProperty(value = "部门Id", name = "department_id")
    @JsonProperty(value = "department_id", index = 5)
    private Integer departmentId;

    @ApiModelProperty(value = "部门名称", name = "department")
    @JsonProperty(index = 6)
    private String department;

    @ApiModelProperty(value = "接口个数", name = "api_count")
    @JsonProperty(value = "api_count", index = 7)
    private Integer apiCount;

    @ApiModelProperty(value = "执行人", name = "operate_by")
    @JsonProperty(value = "operate_by", index = 8)
    private String operateBy;

    @ApiModelProperty(value = "开始时间", name = "start_at")
    @JsonProperty(value = "start_at", index = 9)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startAt;

    @ApiModelProperty(value = "结束时间", name = "end_at")
    @JsonProperty(value = "end_at", index = 10)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endAt;

    @ApiModelProperty(value = "录制成功率", name = "success_rate")
    @JsonProperty(value = "success_rate", index = 11)
    private Double successRate;

    @ApiModelProperty(value = "录制详情实体", name = "record_detail_list")
    @JsonProperty(value = "record_detail_list", index = 12)
    private List<RecordDetailInfo> recordDetailInfoList;
}
