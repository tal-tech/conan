package com.tal.wangxiao.conan.common.service.impl;

import java.util.List;

import com.tal.wangxiao.conan.sys.common.annotation.DataScope;
import com.tal.wangxiao.conan.sys.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tal.wangxiao.conan.common.mapper.ApiMapper;
import com.tal.wangxiao.conan.common.domain.ApiInfo;
import com.tal.wangxiao.conan.common.service.ApiService;

/**
 * 接口管理Service业务层处理
 * 
 * @author dengkunan
 * @date 2020-12-21
 */
@Service
public class ApiServiceImpl implements ApiService
{
    @Autowired
    private ApiMapper apiMapper;

    /**
     * 查询接口管理
     * 
     * @param id 接口管理ID
     * @return 接口管理
     */
    @Override
    public ApiInfo selectApiById(Integer id)
    {
        return apiMapper.selectApiById(id);
    }

    /**
     * 查询接口管理列表
     * 
     * @param api 接口管理
     * @return 接口管理
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<ApiInfo> selectApiList(ApiInfo api)
    {
        return apiMapper.selectApiList(api);
    }

    /**
     * 新增接口管理
     * 
     * @param api 接口管理
     * @return 结果
     */
    @Override
    public int insertApi(ApiInfo api)
    {
        api.setCreateTime(DateUtils.getNowDate());
        return apiMapper.insertApi(api);
    }

    /**
     * 修改接口管理
     * 
     * @param api 接口管理
     * @return 结果
     */
    @Override
    public int updateApi(ApiInfo api)
    {
        api.setUpdateTime(DateUtils.getNowDate());
        return apiMapper.updateApi(api);
    }

    /**
     * 批量删除接口管理
     * 
     * @param ids 需要删除的接口管理ID
     * @return 结果
     */
    @Override
    public int deleteApiByIds(Integer[] ids)
    {
        return apiMapper.deleteApiByIds(ids);
    }

    /**
     * 删除接口管理信息
     * 
     * @param id 接口管理ID
     * @return 结果
     */
    @Override
    public int deleteApiById(Integer id)
    {
        return apiMapper.deleteApiById(id);
    }
}