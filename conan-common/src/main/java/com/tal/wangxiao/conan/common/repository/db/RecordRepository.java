package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 录制DAO
 * @author sunzhihua
 * @date 2019/6/4
 */
public interface RecordRepository extends JpaRepository<Record, Integer>, JpaSpecificationExecutor<Record> {
    /**
     * 根据任务执行ID查询
     * @param taskExecutionId
     * @return
     */
    Optional<Record> findFirstByTaskExecutionId(Integer taskExecutionId);
}
