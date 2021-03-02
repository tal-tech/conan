package com.tal.wangxiao.conan.common.entity.db;

import com.tal.wangxiao.conan.utils.core.ConanBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * 部门实体类
 * @author liujinsong
 * @date 2021/1/6
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "sys_dept")
@Entity
public class Department  {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "dept_id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer parentId;

    @Column
    private String ancestors;

    @Column
    private String deptName;

    @Column
    private Integer orderNum;

    @Column
    private String leader;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private Integer status;

    @Column
    private Integer delFlag;

    @Column(name = "create_by")
    @CreatedBy
    private String createBy;

    @Column(name = "update_by",columnDefinition ="int default 0")
    @LastModifiedBy
    private String updateBy;


}
