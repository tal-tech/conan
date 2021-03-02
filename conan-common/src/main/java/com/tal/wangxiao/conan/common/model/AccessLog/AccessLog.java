package com.tal.wangxiao.conan.common.model.AccessLog;

import com.tal.wangxiao.conan.sys.common.enums.HttpMethod;
import lombok.Data;

/**
 * ES或其他存储中的流量实体
 *
 * @author liujinsong
 */
@Data
public class AccessLog {

    private String domain;

    private String api;

    private HttpMethod httpMethod;

    private String body;

    private String cookie;



}
