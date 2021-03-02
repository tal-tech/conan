package com.tal.wangxiao.conan.common.service.common;

import com.tal.wangxiao.conan.common.entity.db.Domain;
import com.tal.wangxiao.conan.common.entity.db.EsConditionSetting;
import com.tal.wangxiao.conan.common.entity.db.EsSource;
import com.tal.wangxiao.conan.common.repository.db.DomainRepository;
import com.tal.wangxiao.conan.common.repository.db.EsConditionSettingRepository;
import com.tal.wangxiao.conan.common.repository.db.EsSourceRepository;
import com.tal.wangxiao.conan.common.utils.DynamicEsUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Es相关公共服务类
 *
 * @author huyaoguo
 * @date 2021/2/23
 **/
@Slf4j
@Service
public class ElasticCommonService {

    @Resource
    private DomainRepository domainRepository;

    @Resource
    private EsConditionSettingRepository esConditionSettingRepository;

    @Resource
    private EsSourceRepository esSourceRepository;

    /**
     * 根据domain获取restHighLevelClient
     * @param domainName
     * @return restHighLevelClient
     */
    public RestHighLevelClient getRestHighLevelClient(String domainName) {
        Optional<Domain> domainOptional = domainRepository.findByName(domainName);
        if (domainOptional.isPresent()) {
            Optional<EsConditionSetting> esConditionSetting = esConditionSettingRepository.findByDomainId(domainOptional.get().getId());
            if (esConditionSetting.isPresent()) {
                Optional<EsSource> esSource = esSourceRepository.findById(esConditionSetting.get().getEsSourceId());
                if (esSource.isPresent()) {
                    String esIp = esSource.get().getEsIp();
                    Integer esPort = esSource.get().getEsPort();
                    String beanName= esSource.get().getEsBeanName();
                    return DynamicEsUtils.getRestHighLevelClient(beanName,esIp, esPort);
                }else{
                    log.error("该ES的配置esSourceId:{}无效，请检查esCondition是否配置成功",esConditionSetting.get().getEsSourceId());
                }
            }else{
                log.error("该域名{}获取ES的数据源无效，请检查该域名是否配置",domainName);

            }
        }
        return null;
    }
}
