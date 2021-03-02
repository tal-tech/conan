package com.tal.wangxiao.conan.admin.schedule.config;

import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.constant.ScheduleConstants;
import com.tal.wangxiao.conan.common.entity.db.TaskSchedule;
import com.tal.wangxiao.conan.common.exception.task.TaskException;
import com.tal.wangxiao.conan.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * 定时任务工具类
 *
 * @author liujinsong
 * @data 2019/10/15
 */
@Slf4j
public class ScheduleUtils {

    /**
     * 获取触发器key
     */
    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId);
    }

    /**
     * 获取jobKey
     */
    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId);
    }

    /**
     * 获取表达式触发器
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            log.info("定时cron任务：{},任务名称：{}",scheduler.getSchedulerName(), getTriggerKey(jobId).getName());
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            log.error("getCronTrigger 异常：" + e);
        }
        return null;
    }

    /**
     * 创建定时任务的 JobDetail调度程序 和 时间表达式触发器
     */
    public static Result createScheduleJob(Scheduler scheduler, TaskSchedule taskSchedule) {
        try {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(taskSchedule.getId().longValue())).build();
            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = null;
            try {
                cronScheduleBuilder = CronScheduleBuilder.cronSchedule(taskSchedule.getCron());
                cronScheduleBuilder = handleCronScheduleMisfirePolicy(taskSchedule, cronScheduleBuilder);
            } catch (TaskException e) {
                log.error("定时任务表达式构建异常");
                return new Result<>(ResponseCode.FAILED_CREATE_TASKSCHEDULE,"定时任务表达式构建异常");
            }catch (RuntimeException e){
                log.error("cron表达式格式不正确");
                return new Result<>(ResponseCode.INVALID_CRON_EXPRESSION,"cron表达式格式不正确");
            }
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(taskSchedule.getId().longValue())).withSchedule(cronScheduleBuilder).build();
            // 放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, taskSchedule);
            scheduler.scheduleJob(jobDetail, trigger);
            // 暂停任务
            if (taskSchedule.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
                pauseJob(scheduler, taskSchedule.getId().longValue());
            }
        } catch (SchedulerException e) {
            log.error("createScheduleJob 异常：" + e);
            return new Result<>(ResponseCode.FAILED_CREATE_TASKSCHEDULE,"创建定时任务任务异常 ："+e.getMessage());
        }
        return new Result<>(ResponseCode.SUCCESS,"创建定时任务成功");
    }

    /**
     * 更新定时任务
     */
    public static void updateScheduleJob(Scheduler scheduler, TaskSchedule taskSchedule, CronTrigger trigger) throws Exception {
        try {
            TriggerKey triggerKey = getTriggerKey(taskSchedule.getId().longValue());

            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(taskSchedule.getCron());
            try {
                cronScheduleBuilder = handleCronScheduleMisfirePolicy(taskSchedule, cronScheduleBuilder);
            } catch (TaskException e) {
                e.printStackTrace();
            }

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

            // 参数
            trigger.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, taskSchedule);

            scheduler.rescheduleJob(triggerKey, trigger);

            // 暂停任务
            if (taskSchedule.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
                pauseJob(scheduler, taskSchedule.getId().longValue());
            }

        } catch (Exception e) {
            log.error("更新定时任务异常：" + e.getMessage());
            throw new Exception("更新定时任务异常 ："+e.getMessage());
        }

    }

    /**
     * 立即执行任务
     */
    public static int run(Scheduler scheduler, TaskSchedule taskSchedule){
        int rows = 0;
        try {
            // 参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(ScheduleConstants.TASK_PROPERTIES, taskSchedule);
            scheduler.triggerJob(getJobKey(taskSchedule.getId().longValue()), dataMap);
            rows = 1;
        } catch (SchedulerException e) {
            log.error("定时任务run执行失败:"+e.getMessage());
        }
        return rows;
    }

    /**
     * 暂停任务
     */
    public static void pauseJob(Scheduler scheduler, Long taskScheduleId) {
        try {
            scheduler.pauseJob(getJobKey(taskScheduleId));
        } catch (SchedulerException e) {
            System.err.println("pauseJob 异常：" + e);
        }
    }

    /**
     * 恢复任务
     */
    public static void resumeJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            System.err.println("resumeJob 异常：" + e);
        }
    }

    /**
     * 删除定时任务
     */
    public static void deleteScheduleJob(Scheduler scheduler, Long jobId) throws Exception {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            log.error("删除定时任务失败，"+e.getMessage());
            throw new Exception("删除定时任务失败，"+e.getMessage());
        }
    }

    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(TaskSchedule taskSchedule, CronScheduleBuilder cb)
            throws TaskException {
        return cb.withMisfireHandlingInstructionDoNothing();
//            switch (taskSchedule.getStrategy().toString())
//            {
//                case ScheduleConstants.MISFIRE_DEFAULT:
//                    return cb;
//                case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
//                    return cb.withMisfireHandlingInstructionIgnoreMisfires();
//                case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
//                    return cb.withMisfireHandlingInstructionFireAndProceed();
//                case ScheduleConstants.MISFIRE_DO_NOTHING:
//                    return cb.withMisfireHandlingInstructionDoNothing();
//                default:
        //                   throw new TaskException("The task misfire policy '" + taskSchedule.getStrategy()+ "' cannot be used in cron schedule tasks", TaskException.Code.CONFIG_ERROR);
        //           }
    }
}
