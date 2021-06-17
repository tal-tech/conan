package com.tal.wangxiao.conan.common.service.impl;

import com.tal.wangxiao.conan.common.domain.EsConditionSetting;
import com.tal.wangxiao.conan.common.mapper.DomainMapper;
import com.tal.wangxiao.conan.common.mapper.EsConditionSettingMapper;
import com.tal.wangxiao.conan.common.service.EsConditionSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * esConditionSetting域名下ES 查询条件配置Service业务层处理
 * @author dengkunan
 * @date 2021-01-05
 */
@Service
public class EsConditionSettingServiceImpl implements EsConditionSettingService {
    @Resource
    private EsConditionSettingMapper esConditionSettingMapper;

    @Resource
    private DomainMapper domainMapper;

    /**
     * 查询esConditionSetting域名下ES 查询条件配置
     * @param esSettingId esConditionSetting域名下ES 查询条件配置ID
     * @return esConditionSetting域名下ES 查询条件配置
     */
    @Override
    public EsConditionSetting selectEsConditionSettingById(Long esSettingId) {
        return esConditionSettingMapper.selectEsConditionSettingById(esSettingId);
    }

    /**
     * 查询esConditionSetting域名下ES 查询条件配置列表
     * @param esConditionSetting esConditionSetting域名下ES 查询条件配置
     * @return esConditionSetting域名下ES 查询条件配置
     */
    @Override
    public List<EsConditionSetting> selectEsConditionSettingList(EsConditionSetting esConditionSetting) {
        return esConditionSettingMapper.selectEsConditionSettingList(esConditionSetting);
    }

    /**
     * 新增esConditionSetting域名下ES 查询条件配置
     * @param esConditionSetting esConditionSetting域名下ES 查询条件配置
     * @return 结果
     */
    @Override
    public int insertEsConditionSetting(EsConditionSetting esConditionSetting) {
        // 通过esConditionSetting中的domainId 查询bss_domain表中的es_source_id
        int DomainId = esConditionSetting.getDomainId();
        int esSourceId = domainMapper.selectEsSourceIdByDomainId(DomainId);
        // 将es_source_id 添加到esConditionSetting中
        esConditionSetting.setEsSourceId(esSourceId);
        return esConditionSettingMapper.insertEsConditionSetting(esConditionSetting);
    }

    /**
     * 修改esConditionSetting域名下ES 查询条件配置
     * @param esConditionSetting esConditionSetting域名下ES 查询条件配置
     * @return 结果
     */
    @Override
    public int updateEsConditionSetting(EsConditionSetting esConditionSetting) {
        // 通过esConditionSetting中的domainId 查询bss_domain表中的es_source_id
        int DomainId = esConditionSetting.getDomainId();
        int esSourceId = domainMapper.selectEsSourceIdByDomainId(DomainId);
        // 将es_source_id 添加到esConditionSetting中
        esConditionSetting.setEsSourceId(esSourceId);
        return esConditionSettingMapper.updateEsConditionSetting(esConditionSetting);
    }

    /**
     * 批量删除esConditionSetting域名下ES 查询条件配置
     * @param esSettingIds 需要删除的esConditionSetting域名下ES 查询条件配置ID
     * @return 结果
     */
    @Override
    public int deleteEsConditionSettingByIds(Long[] esSettingIds) {
        return esConditionSettingMapper.deleteEsConditionSettingByIds(esSettingIds);
    }

    /**
     * 删除esConditionSetting域名下ES 查询条件配置信息
     * @param esSettingId esConditionSetting域名下ES 查询条件配置ID
     * @return 结果
     */
    @Override
    public int deleteEsConditionSettingById(Long esSettingId) {
        return esConditionSettingMapper.deleteEsConditionSettingById(esSettingId);
    }
}