package com.tal.wangxiao.conan.common.entity.db;

import lombok.*;

import javax.persistence.*;

/**
 * 录制结果实体类
 * @author conan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "bss_record_result")
@Entity
public class RecordResult {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    @Column(name="record_result_id")
    private Integer id;

    @Column
    private Integer recordId;

    @Column
    private Integer apiId;

    @Column
    private String body;

    @Column
    private Integer isDataDye;

    @Column(name = "scene_id")
    private Integer sceneId;

    @Column
    private String header;

    @Column
    private Integer sceneCondition;

    @Column
    private String requestId;

}
