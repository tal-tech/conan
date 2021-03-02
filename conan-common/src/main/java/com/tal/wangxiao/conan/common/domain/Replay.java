package com.tal.wangxiao.conan.common.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * 回放记录对象 bss_replay
 *
 * @author dengkunan
 * @date 2021-01-18
 */
@ApiModel
public class Replay extends ConanBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 任务执行id
     */

    @ApiModelProperty("任务执行id")
    private Integer taskExecutionId;

    /**
     * 录制id
     */

    @ApiModelProperty("录制id")
    private Integer recordId;

    /**
     * 操作人
     */

    @ApiModelProperty("操作人")
    private Integer operateBy;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")

    @ApiModelProperty("开始时间")
    private Date startAt;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endAt;

    /**
     * 流量回放成功率
     */

    @ApiModelProperty("流量回放成功率")
    private String successRate;

    /**
     * 0 对应的replayId非 baseLine
     * 1 对应的replayId为 baseLine
     */

    @ApiModelProperty("0 对应的replayId非 baseLine 对应的replayId为 baseLine")
    private Integer isBaseline;

    /**
     * 回放环境
     */

    @ApiModelProperty("回放环境")
    private String replayEnv;

    /**
     * 回放类型（0-定时巡检,2-手动执行,3-外部触发）
     */

    @ApiModelProperty("回放类型")
    private Integer replayType;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setTaskExecutionId(Integer taskExecutionId) {
        this.taskExecutionId = taskExecutionId;
    }

    public Integer getTaskExecutionId() {
        return taskExecutionId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setOperateBy(Integer operateBy) {
        this.operateBy = operateBy;
    }

    public Integer getOperateBy() {
        return operateBy;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setIsBaseline(Integer isBaseline) {
        this.isBaseline = isBaseline;
    }

    public Integer getIsBaseline() {
        return isBaseline;
    }

    public void setReplayEnv(String replayEnv) {
        this.replayEnv = replayEnv;
    }

    public String getReplayEnv() {
        return replayEnv;
    }

    public void setReplayType(Integer replayType) {
        this.replayType = replayType;
    }

    public Integer getReplayType() {
        return replayType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskExecutionId", getTaskExecutionId())
                .append("recordId", getRecordId())
                .append("operateBy", getOperateBy())
                .append("startAt", getStartAt())
                .append("endAt", getEndAt())
                .append("successRate", getSuccessRate())
                .append("isBaseline", getIsBaseline())
                .append("replayEnv", getReplayEnv())
                .append("replayType", getReplayType())
                .toString();
    }
}