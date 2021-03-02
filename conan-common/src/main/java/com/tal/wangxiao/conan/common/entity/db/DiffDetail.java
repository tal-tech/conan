package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;

import javax.persistence.*;

/**
 * 流量比对详情实体类
 * @author huyaoguo
 * @date 2021/1/25
 */
@Data
@Table(name = "bss_diff_detail")
@Entity
public class DiffDetail {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "diff_detail_id", nullable = false)
    private Integer id;

    @Column(name = "diff_id", nullable = false)
    private Integer diffId;

    @Column(name = "api_id", nullable = false)
    private Integer apiId;

    @Column(name = "expect_count", nullable = false)
    private Integer expectCount;

    @Column(name = "actual_count")
    private Integer actualCount;
}
