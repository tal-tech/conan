package com.tal.wangxiao.conan.admin.schedule.config;

import com.tal.wangxiao.conan.admin.service.ScheduleExecutionService;
import com.tal.wangxiao.conan.admin.service.TaskScheduleService;
import com.tal.wangxiao.conan.admin.service.impl.ScheduleExecutionServiceImpl;
import com.tal.wangxiao.conan.admin.service.impl.TaskScheduleServiceImpl;
import com.tal.wangxiao.conan.common.constant.ScheduleConstants;
import com.tal.wangxiao.conan.common.entity.db.ScheduleExecution;
import com.tal.wangxiao.conan.common.entity.db.TaskSchedule;
import com.tal.wangxiao.conan.config.core.spring.SpringUtils;
import com.tal.wangxiao.conan.sys.common.utils.bean.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author huyaoguo
 * @date 2020/11/30
 * @description 定时任务处理
 **/
@Slf4j
@DisallowConcurrentExecution
public class ScheduleJob extends QuartzJobBean {

    //单线程池创建多想，防止资源浪费
    private ExecutorService service = Executors.newSingleThreadExecutor();
    //    private ThreadPoolTaskExecutor executor = SpringUtils.getBean("threadPoolScheduleTaskExecutor");


    private final static ScheduleExecutionService scheduleService = (ScheduleExecutionService) SpringUtils.getBean(ScheduleExecutionServiceImpl.class);

    private final static TaskScheduleService taskScheduleService = (TaskScheduleService)SpringUtils.getBean(TaskScheduleServiceImpl.class);


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        TaskSchedule taskSchedule = new TaskSchedule();
        BeanUtils.copyBeanProp(taskSchedule, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        //jobLog.setMethodParams(taskSchedule.getMethodParams());
        ScheduleExecution executionLog = new ScheduleExecution();
        executionLog.setOperateBy(taskSchedule.getUpdateBy());
        executionLog.setTaskScheduleId(taskSchedule.getId());
        executionLog.setTaskId(taskSchedule.getTaskId());
        executionLog.setStartAt(LocalDateTime.now());
        executionLog.setTaskType(taskSchedule.getType());
        long startTime = System.currentTimeMillis();
        try {
            // 执行任务
            log.info("任务开始执行 - 名称：{} 方法：{} 参数：{}", taskSchedule.getCronTaskName(), taskSchedule.getMethodName(), taskSchedule.getId());
            ScheduleRunnable task = new ScheduleRunnable(taskSchedule.getCronTaskName(), taskSchedule.getMethodName(), taskSchedule.getId().toString());
            //将任务委托给ExecutorService线程池
            Future<?> future = service.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            // 任务状态 0：成功 1：失败
            executionLog.setStatus(0);
            executionLog.setMessage(taskSchedule.getScheduleName() + " 总共耗时：" + times + "毫秒");
            log.info("任务执行结束 - 名称：{} 耗时：{} 毫秒", taskSchedule.getScheduleName(), times);
            //执行策略如果是1，运行后暂停
            if(1 == taskSchedule.getStrategy()){
                taskScheduleService.pauseJob(taskSchedule);
            }
        } catch (Exception e) {
            log.info("任务执行失败 - 名称：{} 方法：{} 参数: {}", taskSchedule.getScheduleName(),taskSchedule.getMethodName(),taskSchedule.getId());
            log.error("任务执行异常  - ：", e);
            long times = System.currentTimeMillis() - startTime;
            executionLog.setMessage("异常信息："+e.getMessage()+"，任务执行结束 - 名称："+taskSchedule.getScheduleName() + ", 总共耗时：" + times + "毫秒");
            // 任务状态 0：成功 1：失败
            executionLog.setStatus(1);
        } finally {
            //添加定时任务执行记录
            scheduleService.addScheduleExecution(executionLog);
        }
    }
}

