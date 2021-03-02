package com.tal.wangxiao.conan.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tal.wangxiao.conan.common.mapper.EsConditionSettingMapper;
import com.tal.wangxiao.conan.common.domain.EsConditionSetting;
import com.tal.wangxiao.conan.common.service.EsConditionSettingService;

/**
 * esConditionSetting域名下ES 查询条件配置Service业务层处理
 *
 * @author dengkunan
 * @date 2021-01-05
 */
@Service
public class EsConditionSettingServiceImpl implements EsConditionSettingService {
    @Autowired
    private EsConditionSettingMapper esConditionSettingMapper;

    /**
     * 查询esConditionSetting域名下ES 查询条件配置
     *
     * @param esSettingId esConditionSetting域名下ES 查询条件配置ID
     * @return esConditionSetting域名下ES 查询条件配置
     */
    @Override
    public EsConditionSetting selectEsConditionSettingById(Long esSettingId) {
        return esConditionSettingMapper.selectEsConditionSettingById(esSettingId);
    }

    /**
     * 查询esConditionSetting域名下ES 查询条件配置列表
     *
     * @param esConditionSetting esConditionSetting域名下ES 查询条件配置
     * @return esConditionSetting域名下ES 查询条件配置
     */
    @Override
    public List<EsConditionSetting> selectEsConditionSettingList(EsConditionSetting esConditionSetting) {
        return esConditionSettingMapper.selectEsConditionSettingList(esConditionSetting);
    }

    /**
     * 新增esConditionSetting域名下ES 查询条件配置
     *
     * @param esConditionSetting esConditionSetting域名下ES 查询条件配置
     * @return 结果
     */
    @Override
    public int insertEsConditionSetting(EsConditionSetting esConditionSetting) {
        return esConditionSettingMapper.insertEsConditionSetting(esConditionSetting);
    }

    /**
     * 修改esConditionSetting域名下ES 查询条件配置
     *
     * @param esConditionSetting esConditionSetting域名下ES 查询条件配置
     * @return 结果
     */
    @Override
    public int updateEsConditionSetting(EsConditionSetting esConditionSetting) {
        return esConditionSettingMapper.updateEsConditionSetting(esConditionSetting);
    }

    /**
     * 批量删除esConditionSetting域名下ES 查询条件配置
     *
     * @param esSettingIds 需要删除的esConditionSetting域名下ES 查询条件配置ID
     * @return 结果
     */
    @Override
    public int deleteEsConditionSettingByIds(Long[] esSettingIds) {
        return esConditionSettingMapper.deleteEsConditionSettingByIds(esSettingIds);
    }

    /**
     * 删除esConditionSetting域名下ES 查询条件配置信息
     *
     * @param esSettingId esConditionSetting域名下ES 查询条件配置ID
     * @return 结果
     */
    @Override
    public int deleteEsConditionSettingById(Long esSettingId) {
        return esConditionSettingMapper.deleteEsConditionSettingById(esSettingId);
    }
}