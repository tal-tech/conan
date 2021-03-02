package com.tal.wangxiao.conan.admin.service.impl;

import com.tal.wangxiao.conan.admin.schedule.config.ScheduleUtils;
import com.tal.wangxiao.conan.admin.service.TaskScheduleService;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.constant.ScheduleConstants;
import com.tal.wangxiao.conan.common.converter.ConvertUtil;
import com.tal.wangxiao.conan.common.converter.TaskScheduleConverter;
import com.tal.wangxiao.conan.common.entity.db.Task;
import com.tal.wangxiao.conan.common.entity.db.TaskSchedule;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.bo.TaskScheduleInfo;
import com.tal.wangxiao.conan.common.model.query.TaskScheduleQuery;
import com.tal.wangxiao.conan.common.model.vo.TaskScheduleVO;
import com.tal.wangxiao.conan.common.repository.db.TaskRepository;
import com.tal.wangxiao.conan.common.repository.db.TaskScheduleRepository;
import com.tal.wangxiao.conan.sys.auth.core.domain.model.LoginUser;
import com.tal.wangxiao.conan.sys.common.utils.ServletUtils;
import com.tal.wangxiao.conan.sys.framework.web.service.TokenService;
import com.tal.wangxiao.conan.sys.quartz.util.CronUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 定时任务Service
 *
 * @author huyaoguo
 * @data 2020/11/24
 */
@Slf4j
@Service
public class TaskScheduleServiceImpl implements TaskScheduleService {

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private TaskScheduleRepository taskScheduleRepository;

    @Resource
    private TokenService tokenService;


    @Resource
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        try {
            scheduler.clear();
        } catch (Exception e) {
            log.error("初始化定时任务失败" + e.getMessage());
        }
        List<TaskSchedule> taskScheduleList = taskScheduleRepository.findAll();
        for (TaskSchedule taskSchedule : taskScheduleList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, taskSchedule.getId().longValue());
            // 如果不存在，则创建
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, taskSchedule);
            } else {
                try {
                    ScheduleUtils.updateScheduleJob(scheduler, taskSchedule, cronTrigger);
                } catch (Exception e) {
                    log.error("定时任务初始化错误 : taskScheduleId = " + taskSchedule.getId());
                }
            }
        }
    }

    @Override
    public Result findByConditions(TaskScheduleQuery taskQuery, Pageable pageable) {
        Page<TaskSchedule> tasks = taskScheduleRepository.findAll(taskQuery.toSpec(), pageable);
        return new Result<>(ResponseCode.SUCCESS, ConvertUtil.convert2PageVO(tasks, TaskScheduleVO.class, new TaskScheduleConverter()));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Result createScheduleTask(TaskScheduleInfo taskScheduleInfo) throws Exception {
        TaskSchedule taskSchedule = new TaskSchedule();
        taskSchedule.setScheduleName(taskScheduleInfo.getName());
        Optional<Task> taskOptional = taskRepository.findById(taskScheduleInfo.getTaskId());
        if (!taskOptional.isPresent()) {
            log.error("创建定时任务失败：无效的任务ID taskId= " + taskScheduleInfo.getTaskId());
            return new Result<>(ResponseCode.INVALID_TASK_ID, "创建定时任务失败: 无效的任务ID taskId = " + taskScheduleInfo.getTaskId());
        }
        taskSchedule.setTaskId(taskScheduleInfo.getTaskId());
        taskSchedule.setType(taskScheduleInfo.getType());
        taskSchedule.setCron(taskScheduleInfo.getCron());
        taskSchedule.setStrategy(taskScheduleInfo.getStrategy());
        taskSchedule.setStatus(0);
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        taskSchedule.setCreateBy(loginUser.getUser().getUserId().intValue());
        taskSchedule.setCreateAt(LocalDateTime.now());
        taskSchedule.setUpdateBy(loginUser.getUser().getUserId().intValue());
        taskSchedule.setUpdateAt(LocalDateTime.now());
        //定时任务执行的类
        taskSchedule.setCronTaskName(ScheduleConstants.JOB_JOBNAME_FOR_TASKSCHEDULING);
        //定时任务执行的方法
        taskSchedule.setMethodName(ScheduleConstants.JOB_METHODNAME_FOR_TASKSCHEDULING);
        taskSchedule = taskScheduleRepository.save(taskSchedule);
        //创建定时任务Job
        Result<Object> result = ScheduleUtils.createScheduleJob(scheduler, taskSchedule);
        if (result.getRespCode() != ResponseCode.SUCCESS) {
            throw new Exception(result.getDesc().toString());
        }
        return new Result<>(ResponseCode.SUCCESS, taskSchedule.getId());
    }


    @Override
    @Transactional(rollbackOn = Exception.class)
    public Result updateScheduleTask(TaskScheduleInfo taskScheduleInfo, Integer taskScheduleId) throws Exception {
        Optional<TaskSchedule> taskScheduleOptional = taskScheduleRepository.findById(taskScheduleId);
        if (!taskScheduleOptional.isPresent()) {
            return new Result<>(ResponseCode.INVALID_SCHEDULE_TASK_ID, "无效的任务ID ScheduleID ：" + taskScheduleId);
        }
        TaskSchedule taskSchedule = taskScheduleOptional.get();
        taskSchedule.setScheduleName(taskScheduleInfo.getName());
        taskSchedule.setTaskId(taskScheduleInfo.getTaskId());
        taskSchedule.setType(taskScheduleInfo.getType());
        taskSchedule.setCron(taskScheduleInfo.getCron());
        taskSchedule.setStrategy(taskScheduleInfo.getStrategy());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        taskSchedule.setUpdateBy(loginUser.getUser().getUserId().intValue());
        taskSchedule.setUpdateAt(LocalDateTime.now());
        try {
            CronTrigger trigger = ScheduleUtils.getCronTrigger(scheduler, taskScheduleId.longValue());
            ScheduleUtils.updateScheduleJob(scheduler, taskSchedule, trigger);
        } catch (Exception e) {
            throw new Exception("更新定时任务异常：" + e.getMessage());
        }
        taskScheduleRepository.saveAndFlush(taskSchedule);
        return new Result<>(ResponseCode.SUCCESS, "更新成功");
    }

    @Override
    public Result deleteScheduleTask(Integer taskScheduleId) {
        Optional<TaskSchedule> taskScheduleOptional = taskScheduleRepository.findById(taskScheduleId);
        if (!taskScheduleOptional.isPresent()) {
            return new Result<>(ResponseCode.INVALID_SCHEDULE_TASK_ID, "无效的任务ID ScheduleID ：" + taskScheduleId);
        }
        TaskSchedule taskSchedule = taskScheduleOptional.get();
        try {
            ScheduleUtils.deleteScheduleJob(scheduler, taskScheduleId.longValue());
        } catch (Exception e) {
            log.error("删除定时任务失败，id = " + taskScheduleId + ":" + e.getMessage());
            return new Result<>(ResponseCode.FAILED, "删除定时任务失败");
        }
        taskScheduleRepository.delete(taskSchedule);
        return new Result<>(ResponseCode.SUCCESS, "删除定时任务操作成功");

    }

    @Override
    public Result changeStatus(Integer taskScheduleId) {
        Optional<TaskSchedule> taskScheduleOptional = taskScheduleRepository.findById(taskScheduleId);
        if (!taskScheduleOptional.isPresent()) {
            return new Result(ResponseCode.INVALID_SCHEDULE_TASK_ID, "无效的任务ID ScheduleID ：" + taskScheduleId);
        }
        TaskSchedule taskSchedule = taskScheduleOptional.get();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(taskSchedule.getStatus())) {
            //任务状态为0执行暂停任务
            pauseJob(taskSchedule);
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(taskSchedule.getStatus())) {
            //任务状态为2恢复暂停任务
            resumeJob(taskSchedule);
        }
        return new Result(ResponseCode.SUCCESS, "执行定时任务操作成功");

    }

    @Override
    public Result run(Integer taskScheduleId) {
        Optional<TaskSchedule> taskScheduleOptional = taskScheduleRepository.findById(taskScheduleId);
        if (!taskScheduleOptional.isPresent()) {
            return new Result(ResponseCode.INVALID_SCHEDULE_TASK_ID, "无效的任务ID ScheduleID ：" + taskScheduleId);
        }
        int status = ScheduleUtils.run(scheduler, taskScheduleOptional.get());
        if (status == 0) {
            return new Result(ResponseCode.INVALID_SCHEDULE_TASK_ID, "定时任务执行失败 ：id = " + taskScheduleId);
        }
        return new Result(ResponseCode.SUCCESS, "执行定时任务操作成功");
    }

    @Override
    public Result pauseJob(TaskSchedule taskSchedule) {
        taskSchedule.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        TaskSchedule save = taskScheduleRepository.save(taskSchedule);
        ScheduleUtils.pauseJob(scheduler, save.getId().longValue());
        return new Result(ResponseCode.SUCCESS, "定时任务暂停成功");
    }

    @Override
    public Result resumeJob(TaskSchedule taskSchedule) {
        taskSchedule.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        TaskSchedule save = taskScheduleRepository.save(taskSchedule);
        ScheduleUtils.resumeJob(scheduler, save.getId().longValue());
        return new Result(ResponseCode.SUCCESS, "定时任务恢复成功");
    }

    @Override
    public Result findByTaskScheduleId(Integer taskScheduleId) {
        Optional<TaskSchedule> taskScheduleOptional = taskScheduleRepository.findById(taskScheduleId);
        if (!taskScheduleOptional.isPresent()) {
            return new Result(ResponseCode.INVALID_SCHEDULE_TASK_ID, "无效的任务ID ScheduleID ：" + taskScheduleId);
        }
        TaskSchedule taskSchedule = taskScheduleOptional.get();
        TaskScheduleInfo taskScheduleInfo = new TaskScheduleInfo();
        taskScheduleInfo.setCron(taskSchedule.getCron());
        taskScheduleInfo.setName(taskSchedule.getScheduleName());
        taskScheduleInfo.setStrategy(taskSchedule.getStrategy());
        taskScheduleInfo.setTaskId(taskSchedule.getTaskId());
        taskScheduleInfo.setType(taskSchedule.getType());
        taskScheduleInfo.setStatus(taskSchedule.getStatus());
        Optional<Task> taskOptional = taskRepository.findById(taskSchedule.getTaskId());
        taskOptional.ifPresent(task -> taskScheduleInfo.setTaskName(task.getName()));
        return new Result(ResponseCode.SUCCESS, taskScheduleInfo);
    }

    @Override
    public Result checkCronExpressionIsValid(String cronExpression) {
        boolean isValid = CronUtils.isValid(cronExpression);
        return new Result(ResponseCode.SUCCESS, isValid);
    }


}


