package com.tal.wangxiao.conan.admin.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.domain.Diff;
import com.tal.wangxiao.conan.common.exception.CustomException;
import com.tal.wangxiao.conan.common.model.Result;

/**
 * 比对记录Service接口
 *
 * @author dengkunan
 * @date 2021-01-08
 */
public interface DiffService {

    Result<Object> findDiffProgress(Integer diffId);
        /**
         * 查询比对记录
         *
         * @param id 比对记录ID
         * @return 比对记录
         */
    public Diff selectDiffById(Integer id);

    /**
     * 查询比对记录列表
     *
     * @param diff 比对记录
     * @return 比对记录集合
     */
    public List<Diff> selectDiffList(Diff diff);


    /**
     * 流量Diff服务
     *
     * @param replayId
     */
    Result<Object> startDiff(Integer replayId,Integer operateBy) throws CustomException;


    /**
     * 获取 diff 结果列表
     *
     * @param replayId
     */
    Result<Object> getDiffResultList(Integer replayId);

    /**
     * 查询单接口diff详情
     */
    /**
     * 获取相同api不同requestId的diff详情
     *
     * @param apiId          apiId
     * @param diffId         diffId
     */
    ApiResponse getDiffDetail( Integer apiId, Integer diffId) throws JsonProcessingException;


    /**
     * 获取diff log
     */
    Result<String> findDiffLog(Integer replayId);

    /**
     * 新增比对记录
     *
     * @param diff 比对记录
     * @return 结果
     */
    public int insertDiff(Diff diff);



    /**
     * 修改比对记录
     *
     * @param diff 比对记录
     * @return 结果
     */
    public int updateDiff(Diff diff);

    /**
     * 批量删除比对记录
     *
     * @param ids 需要删除的比对记录ID
     * @return 结果
     */
    public int deleteDiffByIds(Integer[] ids);

    /**
     * 删除比对记录信息
     *
     * @param id 比对记录ID
     * @return 结果
     */
    public int deleteDiffById(Integer id);
}