package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * 任务执行记录对象 bss_task_execution
 *
 * @author huyaoguo
 * @date 2021-01-22
 */
@ApiModel
public class TaskExecutionInfo extends ConanBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Integer executionId;

    /** 任务id */

    @ApiModelProperty("任务id")
    private Integer taskId;

    @ApiModelProperty("任务名称关键字")
    private String keyword;

    @ApiModelProperty("部门Id")
    private Integer deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("任务名称")
    private String taskName;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyWord) {
        this.keyword = keyWord;
    }

    /** 任务状态（0-可执行，1-录制中，2-录制成功，3-录制失败，4-回放中， 5-回放成功， 6-回放失败， 7-比对中，8-比对成功， 9-比对失败） */

    @ApiModelProperty("任务状态（0-可执行，1-录制中，2-录制成功，3-录制失败，4-回放中， 5-回放成功， 6-回放失败， 7-比对中，8-比对成功， 9-比对失败）")
    private Integer status;


    public Integer getExecutionId() {
        return executionId;
    }

    public void setExecutionId(Integer executionId) {
        this.executionId = executionId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("executionId", getExecutionId())
                .append("taskId", getTaskId())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .toString();
    }
}