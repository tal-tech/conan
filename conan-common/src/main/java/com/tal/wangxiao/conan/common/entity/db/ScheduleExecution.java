package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 定时任务执行记录
 *
 * @author huyaoguo
 * @data 2021/1/14
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bss_schedule_execution")
@Entity
public class ScheduleExecution {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "schedule_execution_id", nullable = false)
    private Integer id;

    @Column(name = "task_schedule_id", nullable = false)
    private Integer taskScheduleId;

    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "task_type", nullable = false)
    private Integer taskType;

    @Column(name = "operate_by")
    private Integer operateBy;

    private Integer status;

    private String message;

    @Column(name = "start_at",columnDefinition ="datetime default CURRENT_TIMESTAMP" )
    private LocalDateTime startAt;




}
