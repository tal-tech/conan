package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 基础实体类
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract class BaseEntity {

    @Column(name = "create_time", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime createAt;

    @Column(name = "create_by")
    @CreatedBy
    private Integer createBy;

    @Column(name = "update_time", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    @LastModifiedDate
    private LocalDateTime updateAt;

    @Column(name = "update_by", columnDefinition = "int default 0")
    @LastModifiedBy
    private Integer updateBy;
}
