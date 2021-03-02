package com.tal.wangxiao.conan.common.converter;


import com.tal.wangxiao.conan.common.entity.db.Api;
import com.tal.wangxiao.conan.common.entity.db.DiffDetail;
import com.tal.wangxiao.conan.common.model.vo.DiffDetailVO;
import com.tal.wangxiao.conan.common.repository.db.ApiRepository;
import com.tal.wangxiao.conan.common.repository.db.DomainRepository;
import com.tal.wangxiao.conan.common.repository.db.UserRepository;
import com.tal.wangxiao.conan.common.utils.spring.SpringBeanUtil;

import java.util.Optional;

/**
 * 流量回放详情DO转VO工具类
 * @author huyaoguo
 * @date 2021/1/25
 */
public class DiffDetailDetailConverter extends AbstractObjectConverter<DiffDetail, DiffDetailVO> {
    private ApiRepository apiRepository;
    private DomainRepository domainRepository;
    private UserRepository userRepository;

    public DiffDetailDetailConverter() {
        apiRepository = SpringBeanUtil.getBean(ApiRepository.class);
        domainRepository = SpringBeanUtil.getBean(DomainRepository.class);
        userRepository = SpringBeanUtil.getBean(UserRepository.class);
    }

    @Override
    public void extraHandle(DiffDetail diffDetail, DiffDetailVO diffDetailVO) {
        diffDetailVO.setApiCount(diffDetail.getExpectCount());
        diffDetailVO.setDiffDetailId(diffDetail.getId());
        //计算成功率
        diffDetailVO.setDiffSuccessRate((diffDetail.getActualCount()*100.0)/diffDetail.getExpectCount());
        //获取域名和接口名
        Optional<Api> apiOptional = apiRepository.findById(diffDetail.getApiId());
        if(apiOptional.isPresent()){
            Api api = apiOptional.get();
            //转换负责人
            userRepository.findById(api.getCreateBy()).ifPresent(user -> diffDetailVO.setOwnerName(user.getNickName()));
            diffDetailVO.setApiName(api.getName());
            //转换域名名称
            domainRepository.findById(api.getDomainId()).ifPresent(domain -> diffDetailVO.setDomainName(domain.getName()));
        }

    }
}
