package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;

import javax.persistence.*;

/**
 * @author conan
 */
@Data
@Table(name = "bss_replay_detail")
@Entity
public class ReplayDetail {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "replay_detail_id")
    private Integer id;

    @Column(name = "replay_id", nullable = false)
    private Integer replayId;

    @Column(name = "api_id", nullable = false)
    private Integer apiId;

    @Column(name = "expect_count", nullable = false)
    private Integer expectCount;

    @Column(name = "actual_count")
    private Integer actualCount;
}
