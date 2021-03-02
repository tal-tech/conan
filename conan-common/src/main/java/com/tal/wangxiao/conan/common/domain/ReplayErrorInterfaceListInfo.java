package com.tal.wangxiao.conan.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：dengkunnan
 * @date ：2021.01.23
 * @description：  错误接口列表
 * @version: 1.0.0.1
 */
@Data
@ApiModel(value = "ReplayErrorInterfaceListInfo", description = "回放错误接口信息")
public class ReplayErrorInterfaceListInfo {

    @ApiModelProperty(value = "错误请求数量统计"  ,example ="")
    Integer count;

    @ApiModelProperty(value = "错误接口数量统计"  ,example ="")
    Integer apiCount;

    @ApiModelProperty(value = "接口名称列表"  ,example ="")
    Map<String,Integer> apiNameMap = new HashMap<String,Integer>();
}
