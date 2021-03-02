package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

/**
 * ES mapping对应关系 维护
 * @author liujinsong
 * @data 2020/02/19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bss_es_condition_setting")
@Entity
public class EsConditionSetting {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name="es_setting_id")
    private int id;

    @Column(name = "index_name", nullable = false)
    private String indexName;

    @Column(name = "domain_id", nullable = false)
    private int domainId;

    @Column(name = "api", nullable = false)
    private String api;

    @Column(name = "api_regex", nullable = false)
    private String apiRegex;

    @Column(name = "domain", nullable = false)
    private String domain;

    @Column(name = "domain_regex", nullable = false)
    private String domainRegex;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "method_regex", nullable = false)
    private String methodRegex;

    @Column(name = "request_body", nullable = false)
    private String requestBody;

    @Column(name = "request_body_regex", nullable = false)
    private String requestBodyRegex;

    @Column(name = "header", nullable = false)
    private String header;

    @Column(name = "header_regex", nullable = false)
    private String headerRegex;

    @Column(name = "es_source_id", nullable = false)
    private int esSourceId;

}
