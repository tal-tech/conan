package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.swagger.annotations.ApiModel;

/**
 * 回放schema错误记录对象 bss_replay_schema_error
 *
 * @author dengkunan
 * @date 2021-01-07
 */
@ApiModel
public class ReplaySchemaError extends ConanBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer schemaErrorId;

    /** 回放ID */

    @ApiModelProperty("回放ID")
    private Integer replayId;

    /** $column.columnComment */

    @ApiModelProperty("回放ID")
    private Integer apiId;

    @ApiModelProperty("回放ID")
    private String apiName;

    /** 请求参数 */

    @ApiModelProperty("请求参数")
    private String requst;

    /** 请求头 */

    @ApiModelProperty("请求头")
    private String header;

    /** 放回参数 */

    @ApiModelProperty("放回参数")
    private String response;

    /** 请求参数 */

    @ApiModelProperty("请求参数")
    private String errorDesc;


    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public void setSchemaErrorId(Integer schemaErrorId)
    {
        this.schemaErrorId = schemaErrorId;
    }

    public Integer getSchemaErrorId()
    {
        return schemaErrorId;
    }
    public void setReplayId(Integer replayId)
    {
        this.replayId = replayId;
    }

    public Integer getReplayId()
    {
        return replayId;
    }
    public void setApiId(Integer apiId)
    {
        this.apiId = apiId;
    }

    public Integer getApiId()
    {
        return apiId;
    }
    public void setRequst(String requst)
    {
        this.requst = requst;
    }

    public String getRequst()
    {
        return requst;
    }
    public void setHeader(String header)
    {
        this.header = header;
    }

    public String getHeader()
    {
        return header;
    }
    public void setResponse(String response)
    {
        this.response = response;
    }

    public String getResponse()
    {
        return response;
    }
    public void setErrorDesc(String errorDesc)
    {
        this.errorDesc = errorDesc;
    }

    public String getErrorDesc()
    {
        return errorDesc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("schemaErrorId", getSchemaErrorId())
                .append("replayId", getReplayId())
                .append("apiId", getApiId())
                .append("requst", getRequst())
                .append("header", getHeader())
                .append("response", getResponse())
                .append("errorDesc", getErrorDesc())
                .toString();
    }
}