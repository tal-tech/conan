package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


/**
 * 各事业部流量回放业务接入比例
 * @author liujinsong
 * @date 2020/11/09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "department_access_rate")
@Entity
public class DepartmentAccessRate {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(nullable = false)
    private String department;

    @Column(name = "access_rate", nullable = false)
    private int accessRate;
}
