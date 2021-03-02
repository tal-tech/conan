package com.tal.wangxiao.conan.common.service;

import java.util.List;

import com.tal.wangxiao.conan.common.domain.DiffDetail;

/**
 * 比对接口关系表Service接口
 *
 * @author dengkunan
 * @date 2021-01-08
 */
public interface DiffDetailService {
    /**
     * 查询比对接口关系表
     *
     * @param id 比对接口关系表ID
     * @return 比对接口关系表
     */
    public DiffDetail selectDiffDetailById(Integer id);

    /**
     * 查询比对接口关系表列表
     *
     * @param diffDetail 比对接口关系表
     * @return 比对接口关系表集合
     */
    public List<DiffDetail> selectDiffDetailList(DiffDetail diffDetail);

    /**
     * 新增比对接口关系表
     *
     * @param diffDetail 比对接口关系表
     * @return 结果
     */
    public int insertDiffDetail(DiffDetail diffDetail);

    /**
     * 修改比对接口关系表
     *
     * @param diffDetail 比对接口关系表
     * @return 结果
     */
    public int updateDiffDetail(DiffDetail diffDetail);

    /**
     * 批量删除比对接口关系表
     *
     * @param ids 需要删除的比对接口关系表ID
     * @return 结果
     */
    public int deleteDiffDetailByIds(Integer[] ids);

    /**
     * 删除比对接口关系表信息
     *
     * @param id 比对接口关系表ID
     * @return 结果
     */
    public int deleteDiffDetailById(Integer id);
}