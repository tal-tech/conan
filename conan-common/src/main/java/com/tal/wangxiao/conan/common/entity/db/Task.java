package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * 任务实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bss_task")
@Entity
public class Task extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "task_id")
    private Integer id;

    @Column(nullable = false)
    private Integer type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer status;

    @Column(name = "sys_dept_id", nullable = false)
    private Integer departmentId;

    @Column(name = "is_demo", nullable = false)
    private Integer isDemo;

    @Column(name = "is_force_lock", nullable = false)
    private Integer isForceLock;

    @Column(nullable = false)
    private Integer extend;


}
