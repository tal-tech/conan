package com.tal.wangxiao.conan.common.mapper;

import java.util.List;
import com.tal.wangxiao.conan.common.domain.TestUser;

/**
 * 测试账号管理Mapper接口
 * 
 * @author dengkunan
 * @date 2020-12-30
 */
public interface TestUserMapper 
{
    /**
     * 查询测试账号管理
     * 
     * @param id 测试账号管理ID
     * @return 测试账号管理
     */
    public TestUser selectTestUserById(Integer id);

    /**
     * 查询测试账号管理列表
     * 
     * @param testUser 测试账号管理
     * @return 测试账号管理集合
     */
    public List<TestUser> selectTestUserList(TestUser testUser);

    /**
     * 新增测试账号管理
     * 
     * @param testUser 测试账号管理
     * @return 结果
     */
    public int insertTestUser(TestUser testUser);

    /**
     * 修改测试账号管理
     * 
     * @param testUser 测试账号管理
     * @return 结果
     */
    public int updateTestUser(TestUser testUser);

    /**
     * 删除测试账号管理
     * 
     * @param id 测试账号管理ID
     * @return 结果
     */
    public int deleteTestUserById(Integer id);

    /**
     * 批量删除测试账号管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTestUserByIds(Integer[] ids);
}