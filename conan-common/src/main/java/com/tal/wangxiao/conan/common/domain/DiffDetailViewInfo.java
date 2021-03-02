package com.tal.wangxiao.conan.common.domain;


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
public class DiffDetailViewInfo   {
    private static final long serialVersionUID = 1L;

    /**
     * $id
     */
    private Integer diffDetailId;

    /**
     * apiCount
     */

    @ApiModelProperty("接口数量")
    private Integer apiCount;

    /**
     * $diffId
     */

    @ApiModelProperty("apiId")
    private Integer apiId;

    @ApiModelProperty("apiName")
    private String apiName;

    @ApiModelProperty(name = "actualCount")
    private Integer actualCount;

    @ApiModelProperty(name = "expectCount")
    private Integer expectCount;

    /**
     * $diffId
     */
    @ApiModelProperty("diffId")
    private Integer diffId;

    @ApiModelProperty("比对状态")
    private String diffStatus;

    @ApiModelProperty("域名名称")
    private String domainName;

    @ApiModelProperty("成功率")
    private Double successRate;

    @ApiModelProperty("task任务执行ID")
    private  Integer taskExecutionId;

    @ApiModelProperty("负责人姓名")
    private  String apiAddName;


}