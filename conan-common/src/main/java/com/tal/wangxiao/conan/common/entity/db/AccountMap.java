package com.tal.wangxiao.conan.common.entity.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 账号信息映射类
 * @author huyaoguo
 * @date 2020/2/18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "account_map")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AccountMap {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "stu_id_old")
    private String stuIdOld;

    @Column(name = "stu_cou_id")
    private String stuCouId;

    @Column(name = "stuIRC_id")
    private String stuIRCId;

    @Column(name = "account_id", nullable = false)
    private Integer accountId;

    @Column(name = "live_basic_infos_id", nullable = false)
    private Integer liveBasicInfosId;

    @Column(name = "replay_id")
    private Integer replayId;

    @Column(name = "team_id")
    private Integer teamId;

}
