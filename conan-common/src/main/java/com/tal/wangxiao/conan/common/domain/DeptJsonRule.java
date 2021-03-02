package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * 部门Schema规则配置对象 bss_dept_json_rule
 *
 * @author dengkunan
 * @date 2021-01-05
 */
@ApiModel
public class DeptJsonRule extends ConanBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Integer id;

    /**
     * Schema规则
     */

    @ApiModelProperty("Schema规则")
    private String ruleJson;

    /**
     * 部门Id
     */

    @ApiModelProperty("部门Id")
    private Integer sysDeptId;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setRuleJson(String ruleJson) {
        this.ruleJson = ruleJson;
    }

    public String getRuleJson() {
        return ruleJson;
    }

    public void setSysDeptId(Integer sysDeptId) {
        this.sysDeptId = sysDeptId;
    }

    public Integer getSysDeptId() {
        return sysDeptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("ruleJson", getRuleJson())
                .append("sysDeptId", getSysDeptId())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .toString();
    }
}