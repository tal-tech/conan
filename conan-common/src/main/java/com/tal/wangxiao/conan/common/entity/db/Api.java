package com.tal.wangxiao.conan.common.entity.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 接口实体类
 *
 * @author huyaoguo
 * @date 2020/1/7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bss_api")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Api extends BaseEntity {


    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "api_id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "domain_id", nullable = false)
    private Integer domainId;

    @Column(nullable = false)
    private Integer method;

    @Column(name = "sys_dept_id", nullable = false)
    private Integer departmentId;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @Column(name = "is_enable", nullable = false)
    private Boolean isEnable;

    @Column(name = "recordable_count")
    private Integer recordableCount;

    @Column(name = "response_is_json")
    private Integer responseIsJson;
}
