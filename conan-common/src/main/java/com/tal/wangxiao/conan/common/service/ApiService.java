package com.tal.wangxiao.conan.common.service;

import java.util.List;
import com.tal.wangxiao.conan.common.domain.ApiInfo;

/**
 * 接口管理Service接口
 * 
 * @author dengkunan
 * @date 2020-12-21
 */
public interface ApiService
{
    /**
     * 查询接口管理
     * 
     * @param id 接口管理ID
     * @return 接口管理
     */
    public ApiInfo selectApiById(Integer id);

    /**
     * 查询接口管理列表
     * 
     * @param api 接口管理
     * @return 接口管理集合
     */
    public List<ApiInfo> selectApiList(ApiInfo api);

    /**
     * 新增接口管理
     * 
     * @param api 接口管理
     * @return 结果
     */
    public int insertApi(ApiInfo api);

    /**
     * 修改接口管理
     * 
     * @param api 接口管理
     * @return 结果
     */
    public int updateApi(ApiInfo api);

    /**
     * 批量删除接口管理
     * 
     * @param ids 需要删除的接口管理ID
     * @return 结果
     */
    public int deleteApiByIds(Integer[] ids);

    /**
     * 删除接口管理信息
     * 
     * @param id 接口管理ID
     * @return 结果
     */
    public int deleteApiById(Integer id);
}