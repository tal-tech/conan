package com.tal.wangxiao.conan.common.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 录制详情
 * @author liujinsong
 * @date 2021/1/7
 */
@Data
@ApiModel(value = "RecordDetailInfo", description = "流量录制详情")
public class RecordDetailInfo {
    @ApiModelProperty(value = "域名Id", name = "domain_id")
    @JsonProperty(value = "domain_id", index = 1)
    private Integer domainId;

    @ApiModelProperty(value = "域名名称", name = "domain_name")
    @JsonProperty(value = "domain_name", index = 2)
    private String domainName;

    @ApiModelProperty(value = "接口Id", name = "api_id")
    @JsonProperty(value = "api_id", index = 3)
    private Integer apiId;

    @ApiModelProperty(value = "接口名称", name = "api_name")
    @JsonProperty(value = "api_name", index = 4)
    private String apiName;

    @ApiModelProperty(value = "接口方法", name = "end_at")
    @JsonProperty(value = "api_method", index = 5)
    private String apiMethod;

    @ApiModelProperty(value = "结束时间", name = "end_at")
    @JsonProperty(value = "recordable_count", index = 6)
    private Integer recordableCount;

    @ApiModelProperty(value = "结束时间", name = "end_at")
    @JsonProperty(value = "expect_count", index = 7)
    private Integer expectCount;

    @ApiModelProperty(value = "结束时间", name = "end_at")
    @JsonProperty(value = "actual_count", index = 8)
    private Integer actualCount;

    @ApiModelProperty(value = "结束时间", name = "end_at")
    @JsonProperty(value = "success_rate", index = 9)
    private String successRate;
}
