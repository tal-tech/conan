package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 流量回放实体类
 * @author conan
 */
@Data
@Table(name = "bss_replay")
@Entity
public class Replay {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "replay_id", nullable = false)
    private Integer id;

    @Column(name = "task_execution_id", nullable = false)
    private Integer taskExecutionId;

    @Column(name = "record_id", nullable = false)
    private Integer recordId;

    @Column(name = "operate_by", nullable = false)
    @CreatedBy
    private Integer operateBy;

    @Column(name = "start_at", nullable = false)
    @CreatedDate
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "success_rate",columnDefinition="N/A")
    private Double successRate;
    
    @Column(name = "is_baseline",nullable = false)
    private Boolean isBaseline;

    @Column(name = "replay_env",nullable = false)
    private String replayEnv;

    @Column(name = "replay_type",nullable = false)
    private Integer replayType;
}
