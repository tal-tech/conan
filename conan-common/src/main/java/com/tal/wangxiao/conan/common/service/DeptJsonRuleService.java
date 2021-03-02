package com.tal.wangxiao.conan.common.service;

import java.util.List;
import com.tal.wangxiao.conan.common.domain.DeptJsonRule;

/**
 * 部门Schema规则配置Service接口
 * 
 * @author dengkunan
 * @date 2020-12-22
 */
public interface DeptJsonRuleService
{
    /**
     * 查询部门Schema规则配置
     * 
     * @param id 部门Schema规则配置ID
     * @return 部门Schema规则配置
     */
    public DeptJsonRule selectDeptJsonRuleById(Integer id);

    /**
     * 查询部门Schema规则配置列表
     * 
     * @param deptJsonRule 部门Schema规则配置
     * @return 部门Schema规则配置集合
     */
    public List<DeptJsonRule> selectDeptJsonRuleList(DeptJsonRule deptJsonRule);

    /**
     * 新增部门Schema规则配置
     * 
     * @param deptJsonRule 部门Schema规则配置
     * @return 结果
     */
    public int insertDeptJsonRule(DeptJsonRule deptJsonRule);

    /**
     * 修改部门Schema规则配置
     * 
     * @param deptJsonRule 部门Schema规则配置
     * @return 结果
     */
    public int updateDeptJsonRule(DeptJsonRule deptJsonRule);

    /**
     * 批量删除部门Schema规则配置
     * 
     * @param ids 需要删除的部门Schema规则配置ID
     * @return 结果
     */
    public int deleteDeptJsonRuleByIds(Integer[] ids);

    /**
     * 删除部门Schema规则配置信息
     * 
     * @param id 部门Schema规则配置ID
     * @return 结果
     */
    public int deleteDeptJsonRuleById(Integer id);
}