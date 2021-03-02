package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 任务DAO
 * @author huyaoguo
 * @date 2020/1/21
 */
public interface TaskRepository extends JpaRepository<Task, Integer> {


}
