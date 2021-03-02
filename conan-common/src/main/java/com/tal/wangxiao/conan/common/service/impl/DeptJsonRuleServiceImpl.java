package com.tal.wangxiao.conan.common.service.impl;

import java.util.List;

import com.tal.wangxiao.conan.common.domain.DeptJsonRule;
import com.tal.wangxiao.conan.common.mapper.DeptJsonRuleMapper;
import com.tal.wangxiao.conan.common.service.DeptJsonRuleService;
import com.tal.wangxiao.conan.sys.common.annotation.DataScope;
import com.tal.wangxiao.conan.sys.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 部门Schema规则配置Service业务层处理
 *
 * @author dengkunan
 * @date 2021-01-05
 */
@Service
public class DeptJsonRuleServiceImpl implements DeptJsonRuleService {
    @Autowired
    private DeptJsonRuleMapper deptJsonRuleMapper;

    /**
     * 查询部门Schema规则配置
     *
     * @param id 部门Schema规则配置ID
     * @return 部门Schema规则配置
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public DeptJsonRule selectDeptJsonRuleById(Integer id) {
        return deptJsonRuleMapper.selectDeptJsonRuleById(id);
    }

    /**
     * 查询部门Schema规则配置列表
     *
     * @param deptJsonRule 部门Schema规则配置
     * @return 部门Schema规则配置
     */
    @Override
    public List<DeptJsonRule> selectDeptJsonRuleList(DeptJsonRule deptJsonRule) {
        return deptJsonRuleMapper.selectDeptJsonRuleList(deptJsonRule);
    }

    /**
     * 新增部门Schema规则配置
     *
     * @param deptJsonRule 部门Schema规则配置
     * @return 结果
     */
    @Override
    public int insertDeptJsonRule(DeptJsonRule deptJsonRule) {
        deptJsonRule.setCreateTime(DateUtils.getNowDate());
        return deptJsonRuleMapper.insertDeptJsonRule(deptJsonRule);
    }

    /**
     * 修改部门Schema规则配置
     *
     * @param deptJsonRule 部门Schema规则配置
     * @return 结果
     */
    @Override
    public int updateDeptJsonRule(DeptJsonRule deptJsonRule) {
        deptJsonRule.setUpdateTime(DateUtils.getNowDate());
        return deptJsonRuleMapper.updateDeptJsonRule(deptJsonRule);
    }

    /**
     * 批量删除部门Schema规则配置
     *
     * @param ids 需要删除的部门Schema规则配置ID
     * @return 结果
     */
    @Override
    public int deleteDeptJsonRuleByIds(Integer[] ids) {
        return deptJsonRuleMapper.deleteDeptJsonRuleByIds(ids);
    }

    /**
     * 删除部门Schema规则配置信息
     *
     * @param id 部门Schema规则配置ID
     * @return 结果
     */
    @Override
    public int deleteDeptJsonRuleById(Integer id) {
        return deptJsonRuleMapper.deleteDeptJsonRuleById(id);
    }
}