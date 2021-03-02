package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 任务回放环境配置表
 * @author huyaoguo
 * @date 2020/8/10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "task_env")
@Entity
public class TaskEnv {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(nullable = false)
    private Integer taskId;

    @Column(nullable = false)
    private String domainName;

    @Column(nullable = false)
    private String envIp;

    @Column(nullable = false)
    private String envType;
}
