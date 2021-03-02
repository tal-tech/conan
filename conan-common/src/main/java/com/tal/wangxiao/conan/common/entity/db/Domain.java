package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 域名实体类
 * @author liujinsong
 * @date 2021/1/7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bss_domain")
@Entity
public class Domain extends BaseEntity {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean online;

    @Column(name = "is_auth",nullable = false,columnDefinition = "0")
    private Boolean isAuth;

    @Column(name="sys_dept_id")
    private Boolean sysDeptId;

    @Column(name="es_source_id")
    private Boolean esSourceId;
}
