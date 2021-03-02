package com.tal.wangxiao.conan.common.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huyaoguo
 * @date 2021/1/25
 */
@Data
@ApiModel(value = "ReplayDetailVO", description = "流量回放详情")
public class ReplayDetailVO {

    @ApiModelProperty(value = "域名id", name = "domain_id")
    @JsonProperty(value = "domain_id", index = 1)
    private Integer domainId;

    @ApiModelProperty(value = "域名名称", name = "domain_name")
    @JsonProperty(value = "domain_name", index = 2)
    private String domainName;

    @ApiModelProperty(value = "接口id", name = "api_id")
    @JsonProperty(value = "api_id", index = 3)
    private Integer apiId;

    @ApiModelProperty(value = "接口url", name = "api_name")
    @JsonProperty(value = "api_name", index = 4)
    private String apiName;

    @ApiModelProperty(value = "接口方法", name = "api_method")
    @JsonProperty(value = "api_method", index = 5)
    private String apiMethod;

    @ApiModelProperty(value = "预期回放条数", name = "expect_count")
    @JsonProperty(value = "expect_count", index = 7)
    private Integer expectCount;

    @ApiModelProperty(value = "实际回放条数", name = "actual_count")
    @JsonProperty(value = "actual_count", index = 8)
    private Integer actualCount;

    @ApiModelProperty(value = "回放成功率", name = "success_rate")
    @JsonProperty(value = "success_rate", index = 9)
    private Double successRate;
}
