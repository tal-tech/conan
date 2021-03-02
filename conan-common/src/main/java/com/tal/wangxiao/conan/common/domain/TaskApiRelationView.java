package com.tal.wangxiao.conan.common.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * taskApiRelation对象 bss_task_api_relation
 *
 * @author dengkunan
 * @date 2020-12-29
 */
@ApiModel
public class TaskApiRelationView  {
    private static final long serialVersionUID = 1L;

    /**
     * 关联表ID
     */
    private Integer taskApiRelationId;

    /**
     * Task任务ID
     */
/*
    @ApiModelProperty(name = "Task任务ID", hidden = true)
    private Integer taskId;*/

    /**
     * 接口ID
     */

    @ApiModelProperty("接口ID")
    private Integer apiId;

    /**
     * 最大录制数
     */

    @ApiModelProperty("最大录制数")
    private Integer recordCount;

    /**
     * diff 类型
     */

    @ApiModelProperty("diff 类型 ")
    private Integer diffType;


    @ApiModelProperty(name = "域名", hidden = true)
    private String domainName;

    @ApiModelProperty(hidden = true, name = "部门名称")
    private String deptName;

    @ApiModelProperty(hidden = true, name = "添加用户名称")
    private String createByName;

    /**
     * 接口名
     */

    @ApiModelProperty("接口名")
    private String apiName;


    /**
     * 执行顺序
     */

    @ApiModelProperty("执行顺序")
    private Integer position;

    public Integer getTaskApiRelationId() {
        return taskApiRelationId;
    }

    public void setTaskApiRelationId(Integer taskApiRelationId) {
        this.taskApiRelationId = taskApiRelationId;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }


    public String getDomainName() {
        return domainName;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiName() {
        return apiName;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getDiffType() {
        return diffType;
    }

    public void setDiffType(Integer diffType) {
        this.diffType = diffType;
    }

  /*  public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskId() {
        return taskId;
    }*/

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getTaskApiRelationId())
                //.append("taskId", getTaskId())
                .append("apiId", getApiId())
                .append("recordCount", getRecordCount())
                .toString();
    }
}