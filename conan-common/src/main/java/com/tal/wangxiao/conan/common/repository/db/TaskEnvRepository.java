package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.TaskEnv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 任务环境配置Dao
 *
 * @author huyaoguo
 * @date 2019/10/16
 */
public interface TaskEnvRepository extends JpaRepository<TaskEnv, Integer>  {

    /**
     * 获取任务环境的所有信息
     * @param taskId
     * @return
     */
    List<TaskEnv> findAllByTaskId(@Param("task_id") Integer taskId);

    List<TaskEnv> findAllByTaskIdAndEnvType(Integer taskId, String envType);

}
