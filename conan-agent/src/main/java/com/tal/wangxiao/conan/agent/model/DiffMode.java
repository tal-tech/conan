package com.tal.wangxiao.conan.agent.model;

import com.tal.wangxiao.conan.common.domain.TaskApiRelation;
import lombok.Data;

import java.util.HashMap;

/**
 * @author: dengkunnan
 * @date: 2021-01-14 11:29
 * @description:
 **/
@Data
public class DiffMode {

    private Integer baseReplayId;

    private Integer taskExcutionId;

    private Integer recordId;

    private Integer replayId;

    private Integer diffId;

    private Integer taskId;

    /**
     * task 下所有API
     */
    HashMap<Integer, TaskApiRelation> taskApiRelationMap;


    /**
     * task 下所有API
     */
    HashMap<Integer, Integer> taskApiRespnseIsJsonMap;


    /**
     * task 下所有API
     */
    HashMap<Integer, String> taskApiNameMap;
}
