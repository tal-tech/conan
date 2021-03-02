package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * ES mapping对应关系 维护
 * @author liujinsong
 * @data 2020/02/19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "es_setting")
@Entity
public class EsSetting {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(nullable = false,name = "index_name")
    private String indexName;

    @Column(nullable = false,name = "domain_id")
    private String domainId;

    @Column(nullable = false,name = "com.tal.wangxiao.conan.utils.schema.api")
    private Integer api;

    @Column(name = "api_regex")
    private String apiRegex;

    @Column(name = "domain")
    private String domain;

    @Column(name = "domain_regex")
    private String domainRegex;

    @Column(name = "method")
    private String method;

    @Column(name = "method_regex")
    private String methodRegex;

    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "request_body_regex")
    private String requestBodyRegex;

    @Column(name = "header")
    private String header;

    @Column(name = "header_regex")
    private String headerRegex;



}
