package com.tal.wangxiao.conan.common.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 比对接口关系表对象 diff结果详细信息列表
 *
 * @author dengkunan
 * @date 2021-01-08
 */
@Data
@ApiModel
@ToString
public class DiffDetailVO {

    private Integer diffDetailId;

    @ApiModelProperty("接口Id")
    private Integer apiId;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("接口条数")
    private Integer apiCount;

    @ApiModelProperty("比对成功率")
    private Double diffSuccessRate;

    @ApiModelProperty("比对Id")
    private Integer diffId;

    @ApiModelProperty("域名名称")
    private String domainName;

    @ApiModelProperty("负责人姓名")
    private  String ownerName;


}