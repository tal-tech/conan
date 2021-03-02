package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 任务执行实体类
 * @author liujinsong
 * @date 2021/1/6
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bss_task_execution")
@Entity
public class TaskExecution extends BaseEntity {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name="execution_id")
    private Integer id;

    @Column(name = "task_id",nullable = false)
    private Integer taskId;

    @Column(nullable = false)
    private Integer status;

}
