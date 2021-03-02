package com.tal.wangxiao.conan.admin.service.impl;

import com.tal.wangxiao.conan.admin.service.ScheduleExecutionService;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.converter.ConvertUtil;
import com.tal.wangxiao.conan.common.converter.ScheduleExecutionConverter;
import com.tal.wangxiao.conan.common.entity.db.ScheduleExecution;
import com.tal.wangxiao.conan.common.entity.db.TaskSchedule;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.query.ScheduleExecutionQuery;
import com.tal.wangxiao.conan.common.model.vo.ScheduleExecutionVO;
import com.tal.wangxiao.conan.common.repository.db.ScheduleExecutionRepository;
import com.tal.wangxiao.conan.common.repository.db.TaskScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author huyaoguo
 * @date 2020/11/27
 * @description 定时任务执行日志信息实现类
 **/
@Slf4j
@Service
public class ScheduleExecutionServiceImpl implements ScheduleExecutionService {

    @Resource
    private TaskScheduleRepository taskScheduleRepository;

    @Resource
    private ScheduleExecutionRepository scheduleExecutionRepository;

    @Override
    public Result addScheduleExecution(ScheduleExecution scheduleExecution) {
        Optional<TaskSchedule> taskScheduleOptional = taskScheduleRepository.findById(scheduleExecution.getTaskScheduleId());
        if (!taskScheduleOptional.isPresent()) {
            log.error("无效的定时任务ID" + scheduleExecution.getTaskScheduleId());
            return new Result(ResponseCode.INVALID_SCHEDULE_TASK_ID, "无效的定时任务ID" + scheduleExecution.getTaskScheduleId());
        }
        scheduleExecution = scheduleExecutionRepository.save(scheduleExecution);
        return new Result(ResponseCode.SUCCESS, scheduleExecution);
    }

    @Override
    public Result findByConditions(ScheduleExecutionQuery scheduleExecutionQuery, Pageable pageable) {
        Page<ScheduleExecution> scheduleExecutions = scheduleExecutionRepository.findAll(scheduleExecutionQuery.toSpec(), pageable);
        return new Result(ResponseCode.SUCCESS, ConvertUtil.convert2PageVO(scheduleExecutions, ScheduleExecutionVO.class, new ScheduleExecutionConverter()));
    }


}
