package com.tal.wangxiao.conan.common.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tal.wangxiao.conan.common.mapper.ApiRuleMapper;
import com.tal.wangxiao.conan.common.domain.ApiRule;
import com.tal.wangxiao.conan.common.service.ApiRuleService;

import javax.annotation.Resource;

/**
 * 接口Schema规则Service业务层处理
 * 
 * @author dengkunan
 * @date 2020-12-22
 */
@Service
public class ApiRuleServiceImpl implements ApiRuleService
{
    @Resource
    private ApiRuleMapper apiRuleMapper;

    /**
     * 查询接口Schema规则
     * 
     * @param id 接口Schema规则ID
     * @return 接口Schema规则
     */
    @Override
    public ApiRule selectApiRuleById(Integer id)
    {
        return apiRuleMapper.selectApiRuleById(id);
    }

    /**
     * 查询接口Schema规则列表
     * 
     * @param apiRule 接口Schema规则
     * @return 接口Schema规则
     */
    @Override
    public List<ApiRule> selectApiRuleList(ApiRule apiRule)
    {
        return apiRuleMapper.selectApiRuleList(apiRule);
    }

    /**
     * 新增接口Schema规则
     * 
     * @param apiRule 接口Schema规则
     * @return 结果
     */
    @Override
    public int insertApiRule(ApiRule apiRule)
    {
        return apiRuleMapper.insertApiRule(apiRule);
    }

    /**
     * 修改接口Schema规则
     * 
     * @param apiRule 接口Schema规则
     * @return 结果
     */
    @Override
    public int updateApiRule(ApiRule apiRule)
    {
        return apiRuleMapper.updateApiRule(apiRule);
    }

    /**
     * 批量删除接口Schema规则
     * 
     * @param ids 需要删除的接口Schema规则ID
     * @return 结果
     */
    @Override
    public int deleteApiRuleByIds(Integer[] ids)
    {
        return apiRuleMapper.deleteApiRuleByIds(ids);
    }

    /**
     * 删除接口Schema规则信息
     * 
     * @param id 接口Schema规则ID
     * @return 结果
     */
    @Override
    public int deleteApiRuleById(Integer id)
    {
        return apiRuleMapper.deleteApiRuleById(id);
    }
}