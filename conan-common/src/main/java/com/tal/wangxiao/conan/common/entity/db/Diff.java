package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 流量比对实体
 * @author huyaoguo
 * @date 2021/1/14
 */
@Data
@Table(name = "bss_diff")
@Entity
public class Diff {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "diff_id", nullable = false)
    private Integer id;

    @Column(name = "task_execution_id", nullable = false)
    private Integer taskExecutionId;

    @Column(name = "record_id", nullable = false)
    private Integer recordId;

    private Integer replayId;

    @Column(name = "create_by", nullable = false)
    @CreatedBy
    private Integer createBy;

    @Column(name = "create_time", nullable = false)
    @CreatedDate
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @Column(name = "success_rate",columnDefinition="N/A")
    private Double successRate;

    @Column(name = "diff_error_msg")
    private String diffErrorMsg;

}
