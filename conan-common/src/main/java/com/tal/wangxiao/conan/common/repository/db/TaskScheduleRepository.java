package com.tal.wangxiao.conan.common.repository.db;


import com.tal.wangxiao.conan.common.entity.db.TaskSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 定时任务DAO
 * @author liujinsong
 * @date 2019/9/29
 */

public interface TaskScheduleRepository extends JpaRepository<TaskSchedule, Integer>,JpaSpecificationExecutor<TaskSchedule> {

}






