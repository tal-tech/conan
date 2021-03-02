package com.tal.wangxiao.conan.common.converter;


import com.tal.wangxiao.conan.common.constant.enums.HttpMethodConstants;
import com.tal.wangxiao.conan.common.entity.db.Api;
import com.tal.wangxiao.conan.common.entity.db.Domain;
import com.tal.wangxiao.conan.common.entity.db.ReplayDetail;
import com.tal.wangxiao.conan.common.model.vo.ReplayDetailVO;
import com.tal.wangxiao.conan.common.repository.db.ApiRepository;
import com.tal.wangxiao.conan.common.repository.db.DomainRepository;
import com.tal.wangxiao.conan.common.utils.spring.SpringBeanUtil;
import com.tal.wangxiao.conan.utils.enumutils.EnumUtil;

import java.util.Optional;

/**
 * 流量回放详情DO转VO工具类
 * @author huyaoguo
 * @date 2021/1/25
 */
public class ReplayDetailConverter extends AbstractObjectConverter<ReplayDetail, ReplayDetailVO> {
    private ApiRepository apiRepository;
    private DomainRepository domainRepository;


    public ReplayDetailConverter() {
        apiRepository = SpringBeanUtil.getBean(ApiRepository.class);
        domainRepository = SpringBeanUtil.getBean(DomainRepository.class);
    }

    @Override
    public void extraHandle(ReplayDetail replayDetail, ReplayDetailVO replayDetailVO) {
        //转换为接口url
        Optional<Api> apiOptional = apiRepository.findById(replayDetail.getApiId());
        if(apiOptional.isPresent()){
            Api api = apiOptional.get();
            replayDetailVO.setApiName(api.getName());
            replayDetailVO.setDomainId(api.getDomainId());
            //转化域名name
            Optional<Domain> domainOptional = domainRepository.findById(api.getDomainId());
            domainOptional.ifPresent(domain -> replayDetailVO.setDomainName(domain.getName()));
            //转换接口请求方法
            replayDetailVO.setApiMethod(EnumUtil.getByField(HttpMethodConstants.class, "getValue", String.valueOf(api.getMethod())).getLabel());
        }
        //成功率转化
        replayDetailVO.setSuccessRate((replayDetail.getActualCount()*100.0)/replayDetail.getExpectCount());


    }
}
