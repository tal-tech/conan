package com.tal.wangxiao.conan.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tal.wangxiao.conan.common.mapper.DiffDetailMapper;
import com.tal.wangxiao.conan.common.domain.DiffDetail;
import com.tal.wangxiao.conan.common.service.DiffDetailService;

/**
 * 比对接口关系表Service业务层处理
 *
 * @author dengkunan
 * @date 2021-01-08
 */
@Service
public class DiffDetailServiceImpl implements DiffDetailService {
    @Autowired
    private DiffDetailMapper diffDetailMapper;

    /**
     * 查询比对接口关系表
     *
     * @param id 比对接口关系表ID
     * @return 比对接口关系表
     */
    @Override
    public DiffDetail selectDiffDetailById(Integer id) {
        return diffDetailMapper.selectDiffDetailById(id);
    }

    /**
     * 查询比对接口关系表列表
     *
     * @param diffDetail 比对接口关系表
     * @return 比对接口关系表
     */
    @Override
    public List<DiffDetail> selectDiffDetailList(DiffDetail diffDetail) {
        return diffDetailMapper.selectDiffDetailList(diffDetail);
    }

    /**
     * 新增比对接口关系表
     *
     * @param diffDetail 比对接口关系表
     * @return 结果
     */
    @Override
    public int insertDiffDetail(DiffDetail diffDetail) {
        return diffDetailMapper.insertDiffDetail(diffDetail);
    }

    /**
     * 修改比对接口关系表
     *
     * @param diffDetail 比对接口关系表
     * @return 结果
     */
    @Override
    public int updateDiffDetail(DiffDetail diffDetail) {
        return diffDetailMapper.updateDiffDetail(diffDetail);
    }

    /**
     * 批量删除比对接口关系表
     *
     * @param ids 需要删除的比对接口关系表ID
     * @return 结果
     */
    @Override
    public int deleteDiffDetailByIds(Integer[] ids) {
        return diffDetailMapper.deleteDiffDetailByIds(ids);
    }

    /**
     * 删除比对接口关系表信息
     *
     * @param id 比对接口关系表ID
     * @return 结果
     */
    @Override
    public int deleteDiffDetailById(Integer id) {
        return diffDetailMapper.deleteDiffDetailById(id);
    }
}