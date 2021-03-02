package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * 任务管理对象 bss_task
 *
 * @author dengkunnan
 * @date 2020-12-28
 */
@ApiModel
public class Task extends ConanBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer taskId;

    /**
     * 任务类型:0 普通任务，1带场次任务
     */

    @ApiModelProperty("任务类型:0 普通任务，1带场次任务")
    private Integer type;

    /**
     * 任务名
     */

    @ApiModelProperty("任务名")
    private String name;

    /**
     * 0-可执行，1-录制中，2-录制成功，3-录制失败，4-回放中， 5-回放成功， 6-回放失败， 7-比对中，8-比对成功， 9-比对失败
     */

    @ApiModelProperty("0-可执行，1-录制中，2-录制成功，3-录制失败，4-回放中， 5-回放成功， 6-回放失败， 7-比对中，8-比对成功， 9-比对失败")
    private Integer status;

    /**
     * 所属部门id
     */

    @ApiModelProperty("所属部门id")
    private Integer sysDeptId;

    /**
     * 是否演示任务，无权限人员可看（0-否，1-是）
     */

    @ApiModelProperty("是否演示任务，无权限人员可看")
    private Integer isDemo;

    /**
     * 管理员锁定任务（0-正常任务，1-异常锁定）
     */

    @ApiModelProperty("管理员锁定任务")
    private Integer isForceLock;

    /**
     * 扩展属性，用来记录task特殊处理的时候使用
     */
    private String extend;


    @ApiModelProperty(hidden = true, name = "部门名称")
    private String deptName;

    @ApiModelProperty(hidden = true, name = "添加用户名称")
    private String createByName;


    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }



    public String getDeptName() {
        return deptName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getCreateByName() {
        return createByName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setSysDeptId(Integer sysDeptId) {
        this.sysDeptId = sysDeptId;
    }

    public Integer getSysDeptId() {
        return sysDeptId;
    }

    public void setIsDemo(Integer isDemo) {
        this.isDemo = isDemo;
    }

    public Integer getIsDemo() {
        return isDemo;
    }

    public void setIsForceLock(Integer isForceLock) {
        this.isForceLock = isForceLock;
    }

    public Integer getIsForceLock() {
        return isForceLock;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getExtend() {
        return extend;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("taskId", getTaskId())
                .append("type", getType())
                .append("name", getName())
                .append("status", getStatus())
                .append("sysDeptId", getSysDeptId())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("isDemo", getIsDemo())
                .append("isForceLock", getIsForceLock())
                .append("extend", getExtend())
                .toString();
    }
}