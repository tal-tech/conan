package com.tal.wangxiao.conan.common.service;

import java.util.List;

import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.domain.ReplaySchemaError;

/**
 * 回放schema错误记录Service接口
 *
 * @author dengkunan
 * @date 2021-01-07
 */
public interface ReplaySchemaErrorService {
    /**
     * 查询回放schema错误记录
     *
     * @param schemaErrorId 回放schema错误记录ID
     * @return 回放schema错误记录
     */
    public ReplaySchemaError selectReplaySchemaErrorById(Integer schemaErrorId);

    /**
     * 查询回放schema错误记录列表
     *
     * @param replaySchemaError 回放schema错误记录
     * @return 回放schema错误记录集合
     */
    public List<ReplaySchemaError> selectReplaySchemaErrorList(ReplaySchemaError replaySchemaError);


    /**
     * 查询回放schema错误记录列表
     *
     * @param rePlayid 回放rePlayid
     * @return 回放schema错误记录集合
     */
    ApiResponse getResponseInterfaceNumber(Integer rePlayid);

    /**
     * 新增回放schema错误记录
     *
     * @param replaySchemaError 回放schema错误记录
     * @return 结果
     */
    public int insertReplaySchemaError(ReplaySchemaError replaySchemaError);

    /**
     * 修改回放schema错误记录
     *
     * @param replaySchemaError 回放schema错误记录
     * @return 结果
     */
    public int updateReplaySchemaError(ReplaySchemaError replaySchemaError);

    /**
     * 批量删除回放schema错误记录
     *
     * @param schemaErrorIds 需要删除的回放schema错误记录ID
     * @return 结果
     */
    public int deleteReplaySchemaErrorByIds(Integer[] schemaErrorIds);

    /**
     * 删除回放schema错误记录信息
     *
     * @param schemaErrorId 回放schema错误记录ID
     * @return 结果
     */
    public int deleteReplaySchemaErrorById(Integer schemaErrorId);
}