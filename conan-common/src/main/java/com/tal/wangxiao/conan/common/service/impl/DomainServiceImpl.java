package com.tal.wangxiao.conan.common.service.impl;

import java.util.List;

import com.tal.wangxiao.conan.sys.common.annotation.DataScope;
import com.tal.wangxiao.conan.sys.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tal.wangxiao.conan.common.mapper.DomainMapper;
import com.tal.wangxiao.conan.common.domain.Domain;
import com.tal.wangxiao.conan.common.service.DomainService;

/**
 * 域名信息Service业务层处理
 * 
 * @author dengkunan
 * @date 2020-12-21
 */
@Service
public class DomainServiceImpl implements DomainService
{
    @Autowired
    private DomainMapper domainMapper;

    /**
     * 查询域名信息
     * 
     * @param id 域名信息ID
     * @return 域名信息
     */
    @Override
    public Domain selectDomainById(Long id)
    {
        return domainMapper.selectDomainById(id);
    }

    /**
     * 查询域名信息列表
     * 
     * @param domain 域名信息
     * @return 域名信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<Domain> selectDomainList(Domain domain)
    {
        return domainMapper.selectDomainList(domain);
    }

    /**
     * 新增域名信息
     * 
     * @param domain 域名信息
     * @return 结果
     */
    @Override
    public int insertDomain(Domain domain)
    {
        domain.setCreateTime(DateUtils.getNowDate());
        return domainMapper.insertDomain(domain);
    }

    /**
     * 修改域名信息
     * 
     * @param domain 域名信息
     * @return 结果
     */
    @Override
    public int updateDomain(Domain domain)
    {
        domain.setUpdateTime(DateUtils.getNowDate());
        return domainMapper.updateDomain(domain);
    }

    /**
     * 批量删除域名信息
     * 
     * @param ids 需要删除的域名信息ID
     * @return 结果
     */
    @Override
    public int deleteDomainByIds(Long[] ids)
    {
        return domainMapper.deleteDomainByIds(ids);
    }

    /**
     * 删除域名信息信息
     * 
     * @param id 域名信息ID
     * @return 结果
     */
    @Override
    public int deleteDomainById(Long id)
    {
        return domainMapper.deleteDomainById(id);
    }
}