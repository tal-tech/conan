package com.tal.wangxiao.conan.common.entity.db;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 首页留言版实体类
 * @author huyaoguo
 * @date 2020/11/9
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "sys_messages")
@Entity
public class Messages extends BaseEntity {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name="message_id")
    private Integer id;

    private String content;
}
