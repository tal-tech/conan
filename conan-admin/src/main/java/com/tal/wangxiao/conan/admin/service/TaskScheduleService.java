package com.tal.wangxiao.conan.admin.service;


import com.tal.wangxiao.conan.common.entity.db.TaskSchedule;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.bo.TaskScheduleInfo;
import com.tal.wangxiao.conan.common.model.query.TaskScheduleQuery;
import org.springframework.data.domain.Pageable;

/**
 * 定时任务Service
 *
 * @author liujinsong
 * @date 2019/9/27
 */

public interface TaskScheduleService {

    /**
     * 创建定时任务
     *
     * @param taskScheduleInfo 定时任务BO
     * @return
     */
    Result createScheduleTask(TaskScheduleInfo taskScheduleInfo) throws Exception;

    /**
     * 多条件查询定时任务
     *
     * @param taskQuery 查询条件
     * @param pageable  分页标准
     * @return
     */
    Result<Object> findByConditions(TaskScheduleQuery taskQuery, Pageable pageable);

    /**
     * 更新定时任务
     *
     * @param taskScheduleInfo 定时任务BO
     * @param taskScheduleId   定时任务ID
     * @return
     */
    Result updateScheduleTask(TaskScheduleInfo taskScheduleInfo, Integer taskScheduleId) throws Exception;

    /**
     * 暂停恢复定时任务
     *
     * @param taskScheduleId 定时任务ID
     * @return
     */
    Result changeStatus(Integer taskScheduleId);

    /**
     * 立即运行任务
     *
     * @param taskScheduleId 定时任务ID
     * @return
     */
    Result run(Integer taskScheduleId);

    /**
     * 暂停定时任务
     *
     * @param taskSchedule 定时任务Do
     * @return
     */
    Result pauseJob(TaskSchedule taskSchedule);

    /**
     * 恢复定时任务
     *
     * @param taskSchedule 定时任务Do
     * @return
     */
    Result resumeJob(TaskSchedule taskSchedule);

    /**
     * 定时任务删除
     *
     * @param taskScheduleId 定时任务ID
     * @return
     */
    Result deleteScheduleTask(Integer taskScheduleId);

    /**
     * 定时任务详情
     *
     * @param taskScheduleId 定时任务ID
     * @return
     */
    Result findByTaskScheduleId(Integer taskScheduleId);

    /**
     * 校验cron表达式是否有效
     *
     * @param cronExpression cron表达式
     * @return
     */
    Result checkCronExpressionIsValid(String cronExpression);


}
