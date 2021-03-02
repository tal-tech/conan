package com.tal.wangxiao.conan.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 返回前端任务信息VO类
 *
 * @author huyaoguo
 * @date 2021/1/25
 **/
@Data
public class TaskVO {

    @JsonProperty(value = "task_id", index = 1)
    private Integer taskId;

    @JsonProperty(value = "task_name", index = 2)
    private String taskName;

    @JsonProperty(value = "dept_name", index = 3)
    private String deptName;

    @JsonProperty(value = "record_id", index = 1)
    private Integer recordId;

    @JsonProperty(value = "replay_id", index = 1)
    private Integer replayId;

    @JsonProperty(value = "diff_id", index = 1)
    private Integer diff_id;

    @JsonProperty(value = "dept_id", index = 4)
    private Integer deptId;

    @JsonProperty(value = "api_count", index = 5)
    private Integer apiCount;

    @ApiModelProperty("执行人")
    private String operateBy;

    @JsonProperty(value = "start_at", index = 6)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startAt;

    @JsonProperty(value = "end_at", index = 7)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endAt;

    @JsonProperty(value = "success_rate", index = 8)
    @ApiModelProperty(value = "成功率", name = "success_rate")
    private Double successRate;
}

