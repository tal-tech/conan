package com.tal.wangxiao.conan.common.service.impl;

import java.util.List;
import com.tal.wangxiao.conan.sys.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tal.wangxiao.conan.common.mapper.TestUserMapper;
import com.tal.wangxiao.conan.common.domain.TestUser;
import com.tal.wangxiao.conan.common.service.TestUserService;

/**
 * 测试账号管理Service业务层处理
 * 
 * @author dengkunan
 * @date 2020-12-30
 */
@Service
public class TestUserServiceImpl implements TestUserService
{
    @Autowired
    private TestUserMapper testUserMapper;

    /**
     * 查询测试账号管理
     * 
     * @param id 测试账号管理ID
     * @return 测试账号管理
     */
    @Override
    public TestUser selectTestUserById(Integer id)
    {
        return testUserMapper.selectTestUserById(id);
    }

    /**
     * 查询测试账号管理列表
     * 
     * @param testUser 测试账号管理
     * @return 测试账号管理
     */
    @Override
    public List<TestUser> selectTestUserList(TestUser testUser)
    {
        return testUserMapper.selectTestUserList(testUser);
    }

    /**
     * 新增测试账号管理
     * 
     * @param testUser 测试账号管理
     * @return 结果
     */
    @Override
    public int insertTestUser(TestUser testUser)
    {
        testUser.setCreateTime(DateUtils.getNowDate());
        return testUserMapper.insertTestUser(testUser);
    }

    /**
     * 修改测试账号管理
     * 
     * @param testUser 测试账号管理
     * @return 结果
     */
    @Override
    public int updateTestUser(TestUser testUser)
    {
        testUser.setUpdateTime(DateUtils.getNowDate());
        return testUserMapper.updateTestUser(testUser);
    }

    /**
     * 批量删除测试账号管理
     * 
     * @param ids 需要删除的测试账号管理ID
     * @return 结果
     */
    @Override
    public int deleteTestUserByIds(Integer[] ids)
    {
        return testUserMapper.deleteTestUserByIds(ids);
    }

    /**
     * 删除测试账号管理信息
     * 
     * @param id 测试账号管理ID
     * @return 结果
     */
    @Override
    public int deleteTestUserById(Integer id)
    {
        return testUserMapper.deleteTestUserById(id);
    }
}