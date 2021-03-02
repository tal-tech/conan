package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.DomainAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 域名鉴权DAO
 */
public interface DomainAuthRepository extends JpaRepository<DomainAuth, Integer>, JpaSpecificationExecutor<DomainAuth> {
    Optional<DomainAuth> findByDomainId(Integer domainId);
}
