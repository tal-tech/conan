package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 首页留言版Dao
 * @author huyaoguo
 * @date 2020/11/9
 */
public interface MessagesRepository extends JpaRepository<Messages, Integer> {
}
