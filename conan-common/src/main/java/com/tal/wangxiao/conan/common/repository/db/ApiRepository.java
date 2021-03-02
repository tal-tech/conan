package com.tal.wangxiao.conan.common.repository.db;


import com.tal.wangxiao.conan.common.entity.db.Api;
import com.tal.wangxiao.conan.common.entity.db.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 接口DAO
 * @author huyaoguo
 * @date 2021/1/7
 */
public interface ApiRepository extends JpaRepository<Api, Integer>, JpaSpecificationExecutor<Api> {
    List<Api> findByNameAndMethod(@Param("name") String name, @Param("method") Integer method);

    /**
     * 根据部门ID统计归属该部门下的接口个数
     * @param deptId
     * @return
     */
    @Query(value = "SELECT count(1) from bss_api WHERE sys_dept_id IN (select dept_id from sys_dept where FIND_IN_SET(:deptId,ancestors));",nativeQuery = true)
    Integer countApiCountByDeptId(Integer deptId);

    /**
     * 根据域名、接口名、方法查询接口列表，用于去重
     * @param name api url
     * @param method 请求方法
     * @param domainId 域名ID
     * @return
     */
    List<Api> findByNameAndMethodAndDomainId(String name,Integer method,Integer domainId);

}
