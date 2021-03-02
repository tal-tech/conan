package com.tal.wangxiao.conan.common.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 比对接口关系表对象 diff 单个接口详细信息
 *
 * @author dengkunan
 * @date 2021-01-18
 */
@Data
@ApiModel
public class DiffApiLogInfo {
    private static final long serialVersionUID = 1L;

    /**
     * baseData
     */
    @ApiModelProperty("baseData")
    private String baseData;

    /**
     * compareData
     */
    @ApiModelProperty("compareData")
    private String compareData;

    /**
     * compareData
     */
    @ApiModelProperty("diffMsg")
    private String diffMsg;

    /**
     * compareData
     */
    @ApiModelProperty("requestBody")
    private String requestBody;

    /**
     * compareData
     */
    @ApiModelProperty("requestId")
    private String requestId;




}