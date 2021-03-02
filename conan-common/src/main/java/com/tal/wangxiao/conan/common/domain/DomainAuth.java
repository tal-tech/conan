package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * 域名鉴权对象 bss_domain_auth
 *
 * @author dengkunan
 * @date 2021-02-22
 */
@ApiModel
public class DomainAuth extends ConanBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer authId;

    /** 请求URL */

    @ApiModelProperty("请求URL")
    private String curlUrl;

    /** 获取cookie的header头的key,Cookie\Token */

    @ApiModelProperty("获取cookie的header头的key,Cookie｜Token")
    private String responseGetCookieKey;

    /** 获取cookie的类型（1-body中jsonpath获取，2-header头获取） */

    @ApiModelProperty("获取cookie的类型")
    private Integer keyType;

    /** 关联的domain_id */

    @ApiModelProperty("关联的domain_id")
    private Integer domainId;

    /** cookie信息 */

    @ApiModelProperty("cookie信息")
    private String cookie;

    public void setAuthId(Integer authId)
    {
        this.authId = authId;
    }

    public Integer getAuthId()
    {
        return authId;
    }
    public void setCurlUrl(String curlUrl)
    {
        this.curlUrl = curlUrl;
    }

    public String getCurlUrl()
    {
        return curlUrl;
    }
    public void setResponseGetCookieKey(String responseGetCookieKey)
    {
        this.responseGetCookieKey = responseGetCookieKey;
    }

    public String getResponseGetCookieKey()
    {
        return responseGetCookieKey;
    }
    public void setKeyType(Integer keyType)
    {
        this.keyType = keyType;
    }

    public Integer getKeyType()
    {
        return keyType;
    }
    public void setDomainId(Integer domainId)
    {
        this.domainId = domainId;
    }

    public Integer getDomainId()
    {
        return domainId;
    }
    public void setCookie(String cookie)
    {
        this.cookie = cookie;
    }

    public String getCookie()
    {
        return cookie;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("authId", getAuthId())
                .append("curlUrl", getCurlUrl())
                .append("responseGetCookieKey", getResponseGetCookieKey())
                .append("keyType", getKeyType())
                .append("domainId", getDomainId())
                .append("updateTime", getUpdateTime())
                .append("cookie", getCookie())
                .toString();
    }
}