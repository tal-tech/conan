package com.tal.wangxiao.conan.common.entity.db;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * agent服务节点实体
 * @author huyaoguo
 * @date 2020/6/28
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "os_agent_node")
@Entity
public class AgentNode {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private Integer status;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    @Column(nullable = false)
    private String  agentId;

    @Column(nullable = false)
    private String agentEnv;

}
