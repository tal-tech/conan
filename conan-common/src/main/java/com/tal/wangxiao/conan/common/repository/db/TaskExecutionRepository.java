package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.TaskExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 任务执行DAO
 * @author huyaoguo
 * @date 2021/1/1
 */
public interface TaskExecutionRepository extends JpaRepository<TaskExecution, Integer> , JpaSpecificationExecutor<TaskExecution> {

    @Query(value = "select a.task_id as taskId,t.name as taskName,count(b.replay_id) as replayCount  from bss_task_execution a LEFT JOIN bss_replay b ON a.execution_id = b.task_execution_id LEFT JOIN bss_task t ON a.task_id = t.task_id where b.start_at >:beginTime and a.status>= :status GROUP BY b.task_execution_id ORDER BY count(b.replay_id) desc limit :count",nativeQuery = true)
    List<Map<String,Object>> getTaskRankByTimeAndStatus(@Param("beginTime") String beginTime, @Param("status") Integer status, @Param("count") Integer count);

    List<TaskExecution> findByTaskIdAndCreateByOrderByUpdateAtDesc(Integer taskId,Integer createBy);
}
