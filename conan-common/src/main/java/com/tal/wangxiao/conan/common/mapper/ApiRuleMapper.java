package com.tal.wangxiao.conan.common.mapper;

import java.util.List;

import com.tal.wangxiao.conan.common.domain.ApiRule;

/**
 * 接口Schema规则Mapper接口
 *
 * @author dengkunan
 * @date 2020-12-22
 */
public interface ApiRuleMapper {
    /**
     * 查询接口Schema规则
     *
     * @param id 接口Schema规则ID
     * @return 接口Schema规则
     */
    public ApiRule selectApiRuleById(Integer id);

    /**
     * 查询接口Schema规则列表
     *
     * @param apiRule 接口Schema规则
     * @return 接口Schema规则集合
     */
    public List<ApiRule> selectApiRuleList(ApiRule apiRule);

    /**
     * 新增接口Schema规则
     *
     * @param apiRule 接口Schema规则
     * @return 结果
     */
    public int insertApiRule(ApiRule apiRule);

    /**
     * 修改接口Schema规则
     *
     * @param apiRule 接口Schema规则
     * @return 结果
     */
    public int updateApiRule(ApiRule apiRule);

    /**
     * 删除接口Schema规则
     *
     * @param id 接口Schema规则ID
     * @return 结果
     */
    public int deleteApiRuleById(Integer id);

    /**
     * 批量删除接口Schema规则
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteApiRuleByIds(Integer[] ids);
}