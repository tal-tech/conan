package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.ScheduleExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author huyaoguo
 * @date 2020/11/30
 * @description 定时任务执行记录Dao
 **/
public interface ScheduleExecutionRepository extends JpaRepository<ScheduleExecution, Integer>, JpaSpecificationExecutor<ScheduleExecution> {

    List<ScheduleExecution> findAllByTaskScheduleId(Integer scheduleId);
}
