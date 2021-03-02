package com.tal.wangxiao.conan.admin.service;

import com.tal.wangxiao.conan.common.entity.db.ScheduleExecution;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.query.ScheduleExecutionQuery;
import org.springframework.data.domain.Pageable;

/**
 * 定时任务执行日志信息Service
 * @author huyaoguo
 * @date 2020/11/27
 */

public interface ScheduleExecutionService {


    /**
     * 添加一条定时任务执行记录
     * @param scheduleExecution
     * @return
     */
    Result addScheduleExecution(ScheduleExecution scheduleExecution);

    /**
     * 查询定时任务执行记录列表
     * @param scheduleExecutionQuery 查询条件
     * @param pageable 分页标准
     * @return
     */
    Result findByConditions(ScheduleExecutionQuery scheduleExecutionQuery, Pageable pageable);


}
