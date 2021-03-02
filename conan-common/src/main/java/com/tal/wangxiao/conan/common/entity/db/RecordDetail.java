package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;

import javax.persistence.*;

/**
 * 录制实体类
 * @author conan
 */
@Data
@Table(name = "bss_record_detail")
@Entity
public class RecordDetail {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name="record_detail_id")
    private Integer id;

    @Column(name = "record_id", nullable = false)
    private Integer recordId;


    @Column(name = "api_id", nullable = false)
    private Integer apiId;

    @Column(name = "expect_count", nullable = false)
    private Integer expectCount;

    @Column(name = "actual_count")
    private Integer actualCount;
}
