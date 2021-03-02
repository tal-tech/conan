package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.EsConditionSetting;
import com.tal.wangxiao.conan.common.entity.db.EsSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ES配置相关DAO
 * @author liujisong
 * @date 2019/5/27
 */
@Repository
public interface EsConditionSettingRepository extends JpaRepository<EsConditionSetting, Integer> {
    /**
     * 根据ID查询配置信息
     * @param id
     * @return
     */
    Optional <EsConditionSetting> findById(Integer id);

    /**
     * 根据ID查询配置信息
     * @param domainId
     * @return
     */
    Optional <EsConditionSetting> findByDomainId(Integer domainId);


}
