package com.tal.wangxiao.conan.common.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.util.List;

/**
 * 比对接口关系表对象
 *
 * @author dengkunan
 * @date 2021-01-08
 */
@Data
@ApiModel
public class ApiDiffDetailInfo  {

    @ApiModelProperty("域名")
    String domainName;

    @ApiModelProperty("接口名")
    String apiName;

    @ApiModelProperty("比对日志列表")
    List<DiffApiLogInfo> diffApiLogInfoList;
}