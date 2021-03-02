package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 域名实体类
 * @author liujinsong
 * @date 2021/1/7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "bss_domain_auth")
@Entity
public class DomainAuth {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "auth_id",nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String curlUrl;

    @Column(nullable = false)
    private Integer domainId;

    private String responseGetCookieKey;

    @Column(nullable = false)
    private Integer keyType;

    private LocalDateTime updateTime;

    private String cookie;

}
