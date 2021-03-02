package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;

/**
 * ES 数据源
 *
 * @author liujinsong
 * @data 2021/01/15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bss_es_source")
@Entity
public class EsSource {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name="es_source_id")
    private Integer id;

    @Column(nullable = false,name = "es_name")
    private String esName;

    @Column(nullable = false,name = "es_bean_name")
    private String esBeanName;

    @Column(nullable = false,name = "es_ip")
    private String esIp;

    @Column(name = "es_port")
    private Integer esPort;
}
