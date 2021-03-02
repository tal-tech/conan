package com.tal.wangxiao.conan.common.domain;


import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * taskApiRelation对象 bss_task_api_relation
 *
 * @author dengkunan
 * @date 2020-12-29
 */
@ApiModel
public class TaskApiRelationDbInfo extends ConanBaseEntity {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("域名")
    private String domainName;


    @ApiModelProperty("接口名")
    private String apiName;


    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }


    public String getDomainName() {
        return domainName;
    }


}