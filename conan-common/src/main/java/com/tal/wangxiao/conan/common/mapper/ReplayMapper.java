package com.tal.wangxiao.conan.common.mapper;

import java.util.List;

import com.tal.wangxiao.conan.common.domain.Replay;

/**
 * 回放记录Mapper接口
 *
 * @author dengkunan
 * @date 2021-01-18
 */
public interface ReplayMapper {
    /**
     * 查询回放记录
     *
     * @param id 回放记录ID
     * @return 回放记录
     */
    public Replay selectReplayById(Integer id);

    /**
     * 查询回放记录列表
     *
     * @param replay 回放记录
     * @return 回放记录集合
     */
    public List<Replay> selectReplayList(Replay replay);

    /**
     * 新增回放记录
     *
     * @param replay 回放记录
     * @return 结果
     */
    public int insertReplay(Replay replay);

    /**
     * 修改回放记录
     *
     * @param replay 回放记录
     * @return 结果
     */
    public int updateReplay(Replay replay);

    /**
     * 删除回放记录
     *
     * @param id 回放记录ID
     * @return 结果
     */
    public int deleteReplayById(Integer id);

    /**
     * 批量删除回放记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteReplayByIds(Integer[] ids);
}