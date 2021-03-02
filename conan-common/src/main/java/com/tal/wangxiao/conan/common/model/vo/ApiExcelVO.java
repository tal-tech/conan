package com.tal.wangxiao.conan.common.model.vo;

import lombok.Data;

/**
 * 接口Excel解析Model
 *
 * @author huyaoguo
 * @date 2021/1/28
 **/
@Data
public class ApiExcelVO {
    //接口域名
    String domainName;
    //接口url
    String apiName;
    //接口请求方法（大写）
    String method;
    //接口读写标识
    String isRead;
    //可录制条数
    String recordableCount;
    //部门名称
    String department;
}
