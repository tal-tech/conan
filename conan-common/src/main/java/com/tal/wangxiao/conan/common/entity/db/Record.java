package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 录制实体类
 *
 * @author conan
 */
@Data
@Table(name = "bss_record")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Record {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "record_id")
    private Integer id;

    @Column(name = "task_execution_id", nullable = false)
    private Integer taskExecutionId;

    @Column(name = "api_count", nullable = false)
    private Integer apiCount;

    @Column(name = "operate_by", nullable = false)
    @CreatedBy
    private Integer operateBy;

    @Column(name = "start_time", nullable = false)
    @CreatedDate
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "success_rate")
    private Double successRate;
}
