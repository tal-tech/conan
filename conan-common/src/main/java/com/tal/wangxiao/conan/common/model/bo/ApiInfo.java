package com.tal.wangxiao.conan.common.model.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 接口信息类，用于单接口调试的时候从前端接受数据
 * @author: huyaoguo
 * @date: 2019-11-26 16:40
 **/
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@ApiModel(value = "ApiInfo", description = "接口信息类")
public class ApiInfo {

    @JsonProperty("url")
    @ApiModelProperty(value = "接口地址", name = "url", position = 1)
    private String url;

    @JsonProperty(value = "method")
    @ApiModelProperty(value = "请求方式", name = "method", position = 3)
    private String method;

    @JsonProperty(value = "parms")
    @ApiModelProperty(value = "参数", name = "parms", position = 4)
    private String parms;

    @JsonProperty(value = "header")
    @ApiModelProperty(value = "header", name = "header", position = 5)
    private String header;

    @JsonProperty(value = "cookie")
    @ApiModelProperty(value = "cookie", name = "cookie", position = 6)
    private String cookie;
}
