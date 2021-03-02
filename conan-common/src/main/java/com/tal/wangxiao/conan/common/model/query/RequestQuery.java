package com.tal.wangxiao.conan.common.model.query;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * 流量查询类，用于从ES中获取请求流量
 * @author liujinsong
 */
@Data
@Builder
public class RequestQuery {
    String domain;

    String api;

    Integer method;

    Integer count;

    LocalDate dateFrom;

    LocalDate dateTo;

    //type=0  预留属性后续版本计划支持不同纬度流量采集
    Integer type;

}
