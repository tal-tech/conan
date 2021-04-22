package com.tal.wangxiao.conan.common.mapper;

import com.tal.wangxiao.conan.common.domain.Domain;

import java.util.List;

/**
 * 域名信息Mapper接口
 * 
 * @author dengkunan
 * @date 2020-12-21
 */
public interface DomainMapper 
{
    /**
     * 查询es_source_id
     * @param id 域名信息ID
     * @return es_source_id
     */
    public int selectEsSourceIdByDomainId(int id);

    /**
     * 查询域名信息
     * 
     * @param id 域名信息ID
     * @return 域名信息
     */
    public Domain selectDomainById(Long id);

    /**
     * 查询域名信息列表
     * 
     * @param domain 域名信息
     * @return 域名信息集合
     */
    public List<Domain> selectDomainList(Domain domain);

    /**
     * 新增域名信息
     * 
     * @param domain 域名信息
     * @return 结果
     */
    public int insertDomain(Domain domain);

    /**
     * 修改域名信息
     * 
     * @param domain 域名信息
     * @return 结果
     */
    public int updateDomain(Domain domain);

    /**
     * 删除域名信息
     * 
     * @param id 域名信息ID
     * @return 结果
     */
    public int deleteDomainById(Long id);

    /**
     * 批量删除域名信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDomainByIds(Long[] ids);
}