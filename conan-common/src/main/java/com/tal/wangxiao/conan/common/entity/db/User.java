package com.tal.wangxiao.conan.common.entity.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "sys_user")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "dept_id", nullable = false)
    private Integer deptId;


    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column
    private String nickName;

    @Column
    private String userType;

    @Column
    private String email;

    @Column
    private String phonenumber;

    @Column
    private Integer sex;

    @Column
    private String avatar;

    @Column
    private String password;

    @Column
    private Integer status;

    @Column
    private Integer delFlag;

    @Column
    private String loginIp;

    @Column
    private LocalDateTime loginDate;

    @Column(name = "create_time", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private LocalDateTime createAt;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_time", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private LocalDateTime updateAt;

    @Column(name = "update_by", columnDefinition = "int default 0")
    private String updateBy;
}
