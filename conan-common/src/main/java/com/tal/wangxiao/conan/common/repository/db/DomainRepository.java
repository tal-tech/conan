package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 域名DAO
 */
public interface DomainRepository extends JpaRepository<Domain, Integer>, JpaSpecificationExecutor<Domain> {
    /**
     * 根据域名字符串查找
     *
     * @param domain
     * @return
     */
    Optional<Domain> findByName(@Param("domain") String domain);

    /**
     * 根据状态返回域名列表（用于创建/编辑任务页面）
     *
     * @param online
     * @return
     */
    List<Domain> findByOnline(@Param("online") Boolean online);

    /**
     * 根据姓名进行模糊查询
     *
     * @param name
     * @return
     */
    List<Domain> findAllByNameContains(@Param("name") String name);
}
