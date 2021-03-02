package com.tal.wangxiao.conan.common.service.common;


import com.tal.wangxiao.conan.common.constant.enums.TaskStatus;
import com.tal.wangxiao.conan.common.entity.db.Record;
import com.tal.wangxiao.conan.common.entity.db.Task;
import com.tal.wangxiao.conan.common.entity.db.TaskExecution;
import com.tal.wangxiao.conan.common.repository.db.RecordRepository;
import com.tal.wangxiao.conan.common.repository.db.TaskExecutionRepository;
import com.tal.wangxiao.conan.common.repository.db.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 修改任务及任务执行状态接口类
 * @author liujinsong
 * @date 2020/1/7
 */
@Component
@Slf4j
public class TaskStatusService {
    @Resource
    private TaskRepository taskRepository;

    @Resource
    private TaskExecutionRepository taskExecutionRepository;

    @Resource
    private RecordRepository recordRepository;


    public void updateTaskStatus(Integer taskExecutionId, TaskStatus taskStatus){
        Optional<TaskExecution> taskExecutionOptional = taskExecutionRepository.findById(taskExecutionId);
        if (!taskExecutionOptional.isPresent()) {
            log.error("无效的任务执行ID， " + taskExecutionId);
            return;
        }
        taskExecutionOptional.get().setStatus(taskStatus.getValue());
        taskExecutionRepository.saveAndFlush(taskExecutionOptional.get());

        Optional<Task> taskOptional = taskRepository.findById(taskExecutionOptional.get().getTaskId());
        if (!taskOptional.isPresent()) {
            log.error("未找到任务，taskExecuteId = " + taskExecutionId);
            return;
        }
        taskOptional.get().setStatus(taskStatus.getValue());
        taskRepository.saveAndFlush(taskOptional.get());
    }
    public int getTaskExecutionIdByRecordId(int recordId){
        Record re=new Record();
        Optional<Record> byId = recordRepository.findById(recordId);
        if(byId.isPresent()){
            re=byId.get();
        }
        return  re.getTaskExecutionId();
    }
}
