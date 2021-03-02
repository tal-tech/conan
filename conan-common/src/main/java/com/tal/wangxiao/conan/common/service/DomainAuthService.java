package com.tal.wangxiao.conan.common.service;

import java.util.List;
import com.tal.wangxiao.conan.common.domain.DomainAuth;

/**
 * 域名鉴权Service接口
 *
 * @author dengkunan
 * @date 2021-02-22
 */
public interface DomainAuthService
{
    /**
     * 查询域名鉴权
     *
     * @param authId 域名鉴权ID
     * @return 域名鉴权
     */
    public DomainAuth selectDomainAuthById(Integer authId);

    /**
     * 查询域名鉴权列表
     *
     * @param domainAuth 域名鉴权
     * @return 域名鉴权集合
     */
    public List<DomainAuth> selectDomainAuthList(DomainAuth domainAuth);

    /**
     * 新增域名鉴权
     *
     * @param domainAuth 域名鉴权
     * @return 结果
     */
    public int insertDomainAuth(DomainAuth domainAuth);

    /**
     * 修改域名鉴权
     *
     * @param domainAuth 域名鉴权
     * @return 结果
     */
    public int updateDomainAuth(DomainAuth domainAuth);

    /**
     * 批量删除域名鉴权
     *
     * @param authIds 需要删除的域名鉴权ID
     * @return 结果
     */
    public int deleteDomainAuthByIds(Integer[] authIds);

    /**
     * 删除域名鉴权信息
     *
     * @param authId 域名鉴权ID
     * @return 结果
     */
    public int deleteDomainAuthById(Integer authId);
}