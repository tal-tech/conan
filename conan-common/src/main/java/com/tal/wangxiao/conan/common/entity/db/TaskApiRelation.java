package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 任务关联接口实体类
 * @author conan
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bss_task_api_relation")
@Entity
public class TaskApiRelation {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name="task_api_relation_id")
    private Integer id;

    @Column(name = "task_id", nullable = false)
    private Integer taskId;

    @Column(name = "api_id", nullable = false)
    private Integer apiId;

    @Column(name = "record_count")
    private Integer recordCount;

    @Column(name = "diff_type")
    private Integer diffType;

    @Column(name = "position")
    private Integer position;


}
