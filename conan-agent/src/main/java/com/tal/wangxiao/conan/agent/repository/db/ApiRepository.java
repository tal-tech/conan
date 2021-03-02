package com.tal.wangxiao.conan.agent.repository.db;

import com.tal.wangxiao.conan.common.entity.db.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 接口DAO
 */
public interface ApiRepository extends JpaRepository<Api, Integer> {
    /**
     * 查询某一域名下得接口列表（全量返回，用于接口标记页面）
     *
     * @param domainId
     * @return
     */
    List<Api> findByDomainId(Integer domainId);

    List<Api> findByNameAndMethod(@Param("name") String name, @Param("method") Integer method);
}
