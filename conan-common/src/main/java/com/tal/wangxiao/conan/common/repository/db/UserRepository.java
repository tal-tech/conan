package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 用户DAO
 */

public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * 根据姓名进行模糊查询
     * @param name
     * @return
     */
    Optional<User> findByNickName(@Param("name") String name);
}
