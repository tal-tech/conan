package com.tal.wangxiao.conan.common.service.impl;

import com.tal.wangxiao.conan.common.domain.DomainAuth;
import com.tal.wangxiao.conan.common.mapper.DomainAuthMapper;
import com.tal.wangxiao.conan.common.service.DomainAuthService;
import com.tal.wangxiao.conan.sys.common.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 域名鉴权Service业务层处理
 *
 * @author dengkunan
 * @date 2021-02-22
 */
@Service
public class DomainAuthServiceImpl implements DomainAuthService {

    @Resource
    private DomainAuthMapper domainAuthMapper;

    /**
     * 查询域名鉴权
     *
     * @param authId 域名鉴权ID
     * @return 域名鉴权
     */
    @Override
    public DomainAuth selectDomainAuthById(Integer authId) {
        return domainAuthMapper.selectDomainAuthById(authId);
    }

    /**
     * 查询域名鉴权列表
     *
     * @param domainAuth 域名鉴权
     * @return 域名鉴权
     */
    @Override
    public List<DomainAuth> selectDomainAuthList(DomainAuth domainAuth) {
        return domainAuthMapper.selectDomainAuthList(domainAuth);
    }

    /**
     * 新增域名鉴权
     *
     * @param domainAuth 域名鉴权
     * @return 结果
     */
    @Override
    public int insertDomainAuth(DomainAuth domainAuth) {
        return domainAuthMapper.insertDomainAuth(domainAuth);
    }

    /**
     * 修改域名鉴权
     *
     * @param domainAuth 域名鉴权
     * @return 结果
     */
    @Override
    public int updateDomainAuth(DomainAuth domainAuth) {
        domainAuth.setUpdateTime(DateUtils.getNowDate());
        return domainAuthMapper.updateDomainAuth(domainAuth);
    }

    /**
     * 批量删除域名鉴权
     *
     * @param authIds 需要删除的域名鉴权ID
     * @return 结果
     */
    @Override
    public int deleteDomainAuthByIds(Integer[] authIds) {
        return domainAuthMapper.deleteDomainAuthByIds(authIds);
    }

    /**
     * 删除域名鉴权信息
     *
     * @param authId 域名鉴权ID
     * @return 结果
     */
    @Override
    public int deleteDomainAuthById(Integer authId) {
        return domainAuthMapper.deleteDomainAuthById(authId);
    }
}