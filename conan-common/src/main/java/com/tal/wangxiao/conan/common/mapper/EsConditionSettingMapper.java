package com.tal.wangxiao.conan.common.mapper;

import java.util.List;

import com.tal.wangxiao.conan.common.domain.EsConditionSetting;

/**
 * esConditionSetting域名下ES 查询条件配置Mapper接口
 *
 * @author dengkunan
 * @date 2021-01-05
 */
public interface EsConditionSettingMapper {
    /**
     * 查询esConditionSetting域名下ES 查询条件配置
     *
     * @param esSettingId esConditionSetting域名下ES 查询条件配置ID
     * @return esConditionSetting域名下ES 查询条件配置
     */
    public EsConditionSetting selectEsConditionSettingById(Long esSettingId);

    /**
     * 查询esConditionSetting域名下ES 查询条件配置列表
     *
     * @param esConditionSetting esConditionSetting域名下ES 查询条件配置
     * @return esConditionSetting域名下ES 查询条件配置集合
     */
    public List<EsConditionSetting> selectEsConditionSettingList(EsConditionSetting esConditionSetting);

    /**
     * 新增esConditionSetting域名下ES 查询条件配置
     *
     * @param esConditionSetting esConditionSetting域名下ES 查询条件配置
     * @return 结果
     */
    public int insertEsConditionSetting(EsConditionSetting esConditionSetting);

    /**
     * 修改esConditionSetting域名下ES 查询条件配置
     *
     * @param esConditionSetting esConditionSetting域名下ES 查询条件配置
     * @return 结果
     */
    public int updateEsConditionSetting(EsConditionSetting esConditionSetting);

    /**
     * 删除esConditionSetting域名下ES 查询条件配置
     *
     * @param esSettingId esConditionSetting域名下ES 查询条件配置ID
     * @return 结果
     */
    public int deleteEsConditionSettingById(Long esSettingId);

    /**
     * 批量删除esConditionSetting域名下ES 查询条件配置
     *
     * @param esSettingIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEsConditionSettingByIds(Long[] esSettingIds);
}