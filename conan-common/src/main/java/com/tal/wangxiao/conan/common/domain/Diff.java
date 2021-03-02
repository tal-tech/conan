package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * 比对记录对象 bss_diff
 *
 * @author dengkunan
 * @date 2021-01-08
 */
@ApiModel
public class Diff extends ConanBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer diffId;

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
     * 回放id
     */

    @ApiModelProperty("回放id")
    private Integer replayId;

    /**
     * 比对成功率
     */

    @ApiModelProperty("比对成功率")
    private Double successRate;

    /**
     * $column.columnComment
     */

    @ApiModelProperty("比对成功率")
    private String diffErrorMsg;

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

    public void setReplayId(Integer replayId) {
        this.replayId = replayId;
    }

    public Integer getReplayId() {
        return replayId;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public void setDiffErrorMsg(String diffErrorMsg) {
        this.diffErrorMsg = diffErrorMsg;
    }

    public String getDiffErrorMsg() {
        return diffErrorMsg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("diffId" , getDiffId())
                .append("taskExecutionId" , getTaskExecutionId())
                .append("recordId" , getRecordId())
                .append("replayId" , getReplayId())
                .append("createBy" , getCreateBy())
                .append("createTime" , getCreateTime())
                .append("updateTime" , getUpdateTime())
                .append("successRate" , getSuccessRate())
                .append("diffErrorMsg" , getDiffErrorMsg())
                .toString();
    }

    public Integer getDiffId() {
        return diffId;
    }

    public void setDiffId(Integer diffId) {
        this.diffId = diffId;
    }
}