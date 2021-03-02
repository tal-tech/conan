package com.tal.wangxiao.conan.common.domain;

import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * Task任务与接口关联关系对象 bss_task_api_relation
 *
 * @author dengkunan
 * @date 2020-12-29
 */
@ApiModel
public class TaskApiRelation extends ConanBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关联表ID */
    @ApiModelProperty("任务接口关联ID")
    private Integer taskApiRelationId;

    /** Task任务ID */

    @ApiModelProperty("Task任务ID")
    private Integer taskId;

    /** 接口ID */

    @ApiModelProperty("接口ID")
    private Integer apiId;

    /** 最大录制数 */

    @ApiModelProperty("最大录制数")
    private Integer recordCount;

    /** 比对类型 */

    @ApiModelProperty("比对类型")
    private Integer diffType;

    /** 执行顺序 */

    @ApiModelProperty("执行顺序")
    private Integer position;

    public Integer getTaskApiRelationId() {
        return taskApiRelationId;
    }

    public void setTaskApiRelationId(Integer taskApiRelationId) {
        this.taskApiRelationId = taskApiRelationId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskId()
    {
        return taskId;
    }
    public void setApiId(Integer apiId)
    {
        this.apiId = apiId;
    }

    public Integer getApiId()
    {
        return apiId;
    }
    public void setRecordCount(Integer recordCount)
    {
        this.recordCount = recordCount;
    }

    public Integer getRecordCount()
    {
        return recordCount;
    }
    public void setDiffType(Integer diffType)
    {
        this.diffType = diffType;
    }

    public Integer getDiffType()
    {
        return diffType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("taskApiRelationId", getTaskApiRelationId())
                .append("taskId", getTaskId())
                .append("apiId", getApiId())
                .append("recordCount", getRecordCount())
                .append("diffType", getDiffType())
                .append("position", getPosition())
                .toString();
    }
}