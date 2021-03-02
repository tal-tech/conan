package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 接口管理对象 bss_api
 *
 * @author dengkunan
 * @date 2020-12-21
 */
@ApiModel
public class ApiInfo extends ConanBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Integer apiId;

    /** $column.columnComment */

    @ApiModelProperty("${comment}")
    private Integer sysDeptId;

    /** 所属域名id */

    @ApiModelProperty("所属域名id")
    private Integer domainId;

    /** 接口名 */

    @ApiModelProperty("接口名")
    private String name;

    /** 校验规则 */


    /** http方法
GET("GET", 0),
    POST("POST", 1),
    PUT("PUT", 2),
    DELETE("DELETE", 3),
    HEAD("HEAD", 4),
    CONNECT("CONNECT", 5),
    OPTIONS("OPTIONS", 6),
    TRACE("TRACE", 7); */

    @ApiModelProperty("http方法\n" +
            "GET(\"GET\", 0),\n" +
            "    POST(\"POST\", 1),\n" +
            "    PUT(\"PUT\", 2),\n" +
            "    DELETE(\"DELETE\", 3),\n" +
            "    HEAD(\"HEAD\", 4),\n" +
            "    CONNECT(\"CONNECT\", 5),\n" +
            "    OPTIONS(\"OPTIONS\", 6),\n" +
            "    TRACE(\"TRACE\", 7);")
    private Integer method;

    /** 是否已上线(0-已下线，1-已上线) */

    @ApiModelProperty("是否已上线(0-已下线，1-已上线)")
    private Integer isOnline;

    /** 是否读接口(0-否，1-是) */

    @ApiModelProperty("是否读接口(0-否，1-是)")
    private Integer isRead;

    /** 是否启用(0-禁用，1-启用) */

    @ApiModelProperty("是否启用(0-禁用，1-启用)")
    private Integer isEnable;

    /** 可录制请求条数 */

    @ApiModelProperty("可录制请求条数")
    private Integer recordableCount;

    @ApiModelProperty("接口响应是否是JSON")
    private Integer responseIsJson;


    public Integer getResponseIsJson() {
        return responseIsJson;
    }

    public void setResponseIsJson(Integer responseIsJson) {
        this.responseIsJson = responseIsJson;
    }

    @ApiModelProperty(name = "域名", hidden = true)
    private String domainName;

    @ApiModelProperty(hidden = true, name = "部门名称")
    private String deptName;

    @ApiModelProperty(hidden = true, name = "添加用户名称")
    private String createByName;

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getDeptName() {
        return deptName;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public void setSysDeptId(Integer sysDeptId)
    {
        this.sysDeptId = sysDeptId;
    }

    public Integer getSysDeptId()
    {
        return sysDeptId;
    }
    public void setDomainId(Integer domainId)
    {
        this.domainId = domainId;
    }

    public Integer getDomainId()
    {
        return domainId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setMethod(Integer method)
    {
        this.method = method;
    }

    public Integer getMethod()
    {
        return method;
    }
    public void setIsOnline(Integer isOnline)
    {
        this.isOnline = isOnline;
    }

    public Integer getIsOnline()
    {
        return isOnline;
    }
    public void setIsRead(Integer isRead)
    {
        this.isRead = isRead;
    }

    public Integer getIsRead()
    {
        return isRead;
    }
    public void setIsEnable(Integer isEnable)
    {
        this.isEnable = isEnable;
    }

    public Integer getIsEnable()
    {
        return isEnable;
    }
    public void setRecordableCount(Integer recordableCount)
    {
        this.recordableCount = recordableCount;
    }

    public Integer getRecordableCount()
    {
        return recordableCount;
    }

    public String getDomainName() {
        return domainName;
    }


    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("apiId", getApiId())
            .append("sysDeptId", getSysDeptId())
            .append("domainId", getDomainId())
            .append("name", getName())
            .append("method", getMethod())
            .append("isOnline", getIsOnline())
            .append("isRead", getIsRead())
            .append("isEnable", getIsEnable())
            .append("recordableCount", getRecordableCount())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}