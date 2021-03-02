package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * 接口Schema规则对象 bss_api_rule
 *
 * @author dengkunan
 * @date 2020-12-22
 */
@ApiModel
public class ApiRule extends ConanBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** api id */

    @ApiModelProperty("api id")
    private Integer apiId;

    /** 接口结构规则 */

    @ApiModelProperty("接口结构规则")
    private String ruleJson;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setApiId(Integer apiId)
    {
        this.apiId = apiId;
    }

    public Integer getApiId()
    {
        return apiId;
    }
    public void setRuleJson(String ruleJson)
    {
        this.ruleJson = ruleJson;
    }

    public String getRuleJson()
    {
        return ruleJson;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("apiId", getApiId())
                .append("ruleJson", getRuleJson())
                .toString();
    }
}