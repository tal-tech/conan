package com.tal.wangxiao.conan.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tal.wangxiao.conan.common.mapper.EsSourceMapper;
import com.tal.wangxiao.conan.common.domain.EsSource;
import com.tal.wangxiao.conan.common.service.EsSourceService;

/**
 * ES 数据源配置，域名需要绑定ES数据源Service业务层处理
 *
 * @author dengkunan
 * @date 2021-01-05
 */
@Service
public class EsSourceServiceImpl implements EsSourceService {
    @Autowired
    private EsSourceMapper esSourceMapper;

    /**
     * 查询ES 数据源配置，域名需要绑定ES数据源
     *
     * @param esSourceId ES 数据源配置，域名需要绑定ES数据源ID
     * @return ES 数据源配置，域名需要绑定ES数据源
     */
    @Override
    public EsSource selectEsSourceById(Integer esSourceId) {
        return esSourceMapper.selectEsSourceById(esSourceId);
    }

    /**
     * 查询ES 数据源配置，域名需要绑定ES数据源列表
     *
     * @param esSource ES 数据源配置，域名需要绑定ES数据源
     * @return ES 数据源配置，域名需要绑定ES数据源
     */
    @Override
    public List<EsSource> selectEsSourceList(EsSource esSource) {
        return esSourceMapper.selectEsSourceList(esSource);
    }

    /**
     * 新增ES 数据源配置，域名需要绑定ES数据源
     *
     * @param esSource ES 数据源配置，域名需要绑定ES数据源
     * @return 结果
     */
    @Override
    public int insertEsSource(EsSource esSource) {
        return esSourceMapper.insertEsSource(esSource);
    }

    /**
     * 修改ES 数据源配置，域名需要绑定ES数据源
     *
     * @param esSource ES 数据源配置，域名需要绑定ES数据源
     * @return 结果
     */
    @Override
    public int updateEsSource(EsSource esSource) {
        return esSourceMapper.updateEsSource(esSource);
    }

    /**
     * 批量删除ES 数据源配置，域名需要绑定ES数据源
     *
     * @param esSourceIds 需要删除的ES 数据源配置，域名需要绑定ES数据源ID
     * @return 结果
     */
    @Override
    public int deleteEsSourceByIds(Integer[] esSourceIds) {
        return esSourceMapper.deleteEsSourceByIds(esSourceIds);
    }

    /**
     * 删除ES 数据源配置，域名需要绑定ES数据源信息
     *
     * @param esSourceId ES 数据源配置，域名需要绑定ES数据源ID
     * @return 结果
     */
    @Override
    public int deleteEsSourceById(Integer esSourceId) {
        return esSourceMapper.deleteEsSourceById(esSourceId);
    }
}