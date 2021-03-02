package com.tal.wangxiao.conan.common.entity.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @author huyaoguo
 * @date 2020/11/5
 * @description 版本信息描述
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "sys_version")
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class Version extends BaseEntity  {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name = "version_id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String versionNum;

    private String currentInfo;

    private String platformInfo;

    private Integer pvCount;


}
