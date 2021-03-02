package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author huyaoguo
 * @date 2020/11/5
 * @description 版本信息dao层
 **/
public interface VersionRepository extends JpaRepository<Version, Integer> {

    Optional<Version> findFirstByOrderByIdDesc();

}
