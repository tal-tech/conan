package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.Replay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 流量回放DAO
 *
 * @author conan
 */
public interface ReplayRepository extends JpaRepository<Replay, Integer> {

    /**
     * 根据任务执行ID反馈流量回放记录列表，按开始时间倒排
     *
     * @param taskExecutionId 任务执行ID
     * @return
     */
    List<Replay> findByTaskExecutionIdOrderByStartAtDesc(@Param("task_execution_id") Integer taskExecutionId);

    /**
     * 根据任务执行ID查找最近一次回放记录
     *
     * @param taskExecutionId 任务执行ID
     * @return 回放记录
     */
    Optional<Replay> findFirstByTaskExecutionIdOrderByStartAtDesc(@Param("task_execution_id") Integer taskExecutionId);

    /**
     * 根据部门ID统计归属该部门下的回放总数
     *
     * @param deptId 部门ID
     * @return 回放总数
     */
    @Query(value = "SELECT count(1) from bss_replay WHERE task_execution_id IN (SELECT execution_id FROM bss_task_execution WHERE task_id IN (SELECT task_id from bss_task WHERE sys_dept_id IN (select dept_id from sys_dept where FIND_IN_SET(:deptId,ancestors))))", nativeQuery = true)
    Integer countReplayCountByDeptId(Integer deptId);

    /**
     * 根据时间端来统计回放次数
     *
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param replayType 回放类型
     * @return 回放次数
     */
    Integer countByStartAtBetweenAndReplayType(LocalDateTime startTime, LocalDateTime endTime, Integer replayType);

    /**
     * 根据任务执行Id来设置该回放记录为基准
     *
     * @param taskExecutionId 任务执行Id
     * @param isBaseLine      基准标识
     * @return 回放记录
     */
    List<Replay> findAllByTaskExecutionIdAndIsBaseline(Integer taskExecutionId, Boolean isBaseLine);

    /**
     * 统计回放基准条数
     *
     * @param taskExecutionId 任务执行Id
     * @param isBaseLine      基准标识
     * @return 基准个数
     */
    Integer countByTaskExecutionIdAndIsBaseline(Integer taskExecutionId, Boolean isBaseLine);

}
