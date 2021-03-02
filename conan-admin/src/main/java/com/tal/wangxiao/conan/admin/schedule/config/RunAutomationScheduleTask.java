package com.tal.wangxiao.conan.admin.schedule.config;


import com.tal.wangxiao.conan.admin.service.impl.TaskPipelineServiceImpl;
import com.tal.wangxiao.conan.common.constant.enums.ScheduleTaskTypeConstants;
import com.tal.wangxiao.conan.common.entity.db.TaskSchedule;
import com.tal.wangxiao.conan.common.repository.db.TaskScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author huyaoguo
 * @date 2020/11/30
 * @description 定时任务调度执行
 */
@Component("runAutomationScheduleTask")
@Slf4j
public class RunAutomationScheduleTask {

    @Resource
    private TaskScheduleRepository taskScheduleRepository;

    @Resource
    private TaskPipelineServiceImpl taskPipelineService;

    //定时任务执行
    @Transactional(rollbackOn = Exception.class)
    public void runTask(String params) throws Exception {
        //根据taskSchedule传递过来的参数为taskScheduleId
        Integer taskScheduleId = null;
        try{
            taskScheduleId = Integer.parseInt(params);
        }catch (Exception e){
            log.error("定时任务参数异常，请检查参数类型: "+params);
            return;
        }

        Optional<TaskSchedule> taskScheduleOptional = taskScheduleRepository.findById(taskScheduleId);
        if(!taskScheduleOptional.isPresent()){
            log.error("该定时任务ID："+taskScheduleId+"不存在记录，不会执行定时任务");
            return;
        }
        TaskSchedule taskSchedule = taskScheduleOptional.get();
        Integer taskType = taskScheduleOptional.get().getType();
        if(ScheduleTaskTypeConstants.RECORD.getValue().equals(taskType)){
            //录制+回放+设置回放基准,采用管理员执行
            taskPipelineService.asyncTaskRecordPipeline(taskSchedule.getTaskId(),0,"online");
            log.info("定时任务执行成功 - taskScheduleId : "+ taskSchedule.getId()+" 执行类型：RECORD");
        }else if(ScheduleTaskTypeConstants.REPLAY.getValue().equals(taskType)){
            //回放+比对,采用管理员执行
            taskPipelineService.asyncTaskReplayPipeline(taskSchedule.getTaskId(),0,"online");
            log.info("定时任务执行成功 - taskScheduleId : "+ taskSchedule.getId()+" 执行类型：REPLAY");
        }else{
            log.info("不支持该定时任务类型，taskType = "+taskType);
        }


    }

}
