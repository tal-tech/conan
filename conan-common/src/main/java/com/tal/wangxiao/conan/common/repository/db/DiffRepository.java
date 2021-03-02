package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.Diff;
import com.tal.wangxiao.conan.common.entity.db.Replay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 流量比对DAO
 * @author huyaoguo
 * @date 2021/1/14
 */
public interface DiffRepository extends JpaRepository<Diff, Integer> {

    /**
     * 根据任务执行ID查找最近一次对比记录
     * @param taskExecutionId
     * @return
     */
    Optional<Diff> findFirstByTaskExecutionIdOrderByCreateTimeDesc(@Param("task_execution_id") Integer taskExecutionId);

    /**
     * 根据任务执行ID与replayI 查找最近一次对比记录
     * @param taskExecutionId replayId
     * @return
     */
    Optional<Diff> findFirstByTaskExecutionIdAndReplayIdOrderByCreateTimeDesc(Integer taskExecutionId,Integer replayId );


}
