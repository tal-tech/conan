package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.sys.common.annotation.Excel;
import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * esConditionSetting域名下ES 查询条件配置对象 bss_es_condition_setting
 * 
 * @author dengkunan
 * @date 2021-01-05
 */
@ApiModel
public class EsConditionSetting extends ConanBaseEntity
{
    private static final long serialVersionUID = 1L;


    /** $column.columnComment */
    @Excel(name = "es_setting_id")
    private Long esSettingId;

    /** ES对应日志索引名称 */

    @Excel(name = "index_name")
    @ApiModelProperty("ES对应日志索引名称")
    private String indexName;

    /** 模版关联域名 */

    @Excel(name = "domain_id")
    @ApiModelProperty("模版关联域名")
    private Integer domainId;

    /** ES中_source内接口对应的key名称  */

    @Excel(name = "api")
    @ApiModelProperty("ES中_source内接口对应的key名称 ")
    private String api;

    /** 接口正则表达式 */

    @Excel(name = "api_regex")
    @ApiModelProperty("接口正则表达式")
    private String apiRegex;

    /** ES中_source内域名对应的key名称  */
    @Excel(name = "domain")
    @ApiModelProperty("ES中_source内域名对应的key名称 ")
    private String domain;

    /** 域名正则表达式 */
    @Excel(name = "domain_regex")
    @ApiModelProperty("域名正则表达式")
    private String domainRegex;

    /** ES中_source内请求方法对应的key名称 */
    @Excel(name = "method")
    @ApiModelProperty("ES中_source内请求方法对应的key名称")
    private String method;

    /** 请求方法的正则表达式 */
    @Excel(name = "method_regex")
    @ApiModelProperty("请求方法的正则表达式")
    private String methodRegex;

    /** ES中_source内请求体对应的key名称 */
    @Excel(name = "request_body")
    @ApiModelProperty("ES中_source内请求体对应的key名称")
    private String requestBody;

    /** 请求体正则表达式 */
    @Excel(name = "request_body_regex")
    @ApiModelProperty("请求体正则表达式")
    private String requestBodyRegex;

    /** ES中_source内请求体对应的key名称 */
    @Excel(name = "header")
    @ApiModelProperty("ES中_source内请求体对应的key名称")
    private String header;

    /** Header的正则表达式 */
    @Excel(name = "header_regex")
    @ApiModelProperty("Header的正则表达式")
    private String headerRegex;

    /** es_source_id */
    @Excel(name = "es_source_id")
    @ApiModelProperty("es_source_id")
    private Integer esSourceId;

    public void setEsSettingId(Long esSettingId)
    {
        this.esSettingId = esSettingId;
    }

    public Long getEsSettingId()
    {
        return esSettingId;
    }
    public void setIndexName(String indexName)
    {
        this.indexName = indexName;
    }

    public String getIndexName()
    {
        return indexName;
    }
    public void setDomainId(Integer domainId)
    {
        this.domainId = domainId;
    }

    public Integer getDomainId()
    {
        return domainId;
    }
    public void setApi(String api)
    {
        this.api = api;
    }

    public String getApi()
    {
        return api;
    }
    public void setApiRegex(String apiRegex)
    {
        this.apiRegex = apiRegex;
    }

    public String getApiRegex()
    {
        return apiRegex;
    }
    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getDomain()
    {
        return domain;
    }
    public void setDomainRegex(String domainRegex)
    {
        this.domainRegex = domainRegex;
    }

    public String getDomainRegex()
    {
        return domainRegex;
    }
    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getMethod()
    {
        return method;
    }
    public void setMethodRegex(String methodRegex)
    {
        this.methodRegex = methodRegex;
    }

    public String getMethodRegex()
    {
        return methodRegex;
    }
    public void setRequestBody(String requestBody)
    {
        this.requestBody = requestBody;
    }

    public String getRequestBody()
    {
        return requestBody;
    }
    public void setRequestBodyRegex(String requestBodyRegex)
    {
        this.requestBodyRegex = requestBodyRegex;
    }

    public String getRequestBodyRegex()
    {
        return requestBodyRegex;
    }
    public void setHeader(String header)
    {
        this.header = header;
    }

    public String getHeader()
    {
        return header;
    }
    public void setHeaderRegex(String headerRegex)
    {
        this.headerRegex = headerRegex;
    }

    public String getHeaderRegex()
    {
        return headerRegex;
    }

    public void setEsSourceId(Integer esSourceId)
    {
        this.esSourceId = esSourceId;
    }

    public Integer getEsSourceId()
    {
        return esSourceId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("esSettingId", getEsSettingId())
            .append("indexName", getIndexName())
            .append("domainId", getDomainId())
            .append("api" , getApi())
            .append("apiRegex", getApiRegex())
            .append("domain", getDomain())
            .append("domainRegex", getDomainRegex())
            .append("method", getMethod())
            .append("methodRegex", getMethodRegex())
            .append("requestBody", getRequestBody())
            .append("requestBodyRegex", getRequestBodyRegex())
            .append("header", getHeader())
            .append("headerRegex", getHeaderRegex())
            .append("esSourceId", getEsSourceId())
            .toString();
    }
}