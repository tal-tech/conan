package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 定时任务实体类
 * @author liujinsong
 * @data 2019/09/27
 */


@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bss_task_schedule")
@Entity

public class TaskSchedule extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "schedule_id",nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer type;

    private String scheduleName;

    @Column(nullable = false)
    private Integer taskId;

    private String cron;

    @Column (nullable = false)
    private Integer strategy;

    private String methodName;

    private String cronTaskName;

    private Integer status;



}
