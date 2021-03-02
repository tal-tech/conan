package com.tal.wangxiao.conan.common.converter;

import com.tal.wangxiao.conan.common.constant.enums.ScheduleTaskTypeConstants;
import com.tal.wangxiao.conan.common.entity.db.ScheduleExecution;
import com.tal.wangxiao.conan.common.entity.db.Task;
import com.tal.wangxiao.conan.common.entity.db.TaskSchedule;
import com.tal.wangxiao.conan.common.entity.db.User;
import com.tal.wangxiao.conan.common.model.vo.ScheduleExecutionVO;
import com.tal.wangxiao.conan.common.repository.db.TaskRepository;
import com.tal.wangxiao.conan.common.repository.db.TaskScheduleRepository;
import com.tal.wangxiao.conan.common.repository.db.UserRepository;
import com.tal.wangxiao.conan.common.utils.spring.SpringBeanUtil;
import com.tal.wangxiao.conan.utils.enumutils.EnumUtil;

import java.util.Optional;

/**
 * 定时任务DO转VO工具类
 * @author huyaoguo
 * @date 2020/11/25
 */
public class ScheduleExecutionConverter extends AbstractObjectConverter<ScheduleExecution, ScheduleExecutionVO> {

    private UserRepository userRepository;

    private TaskRepository taskRepository;

    private TaskScheduleRepository taskScheduleRepository;

    public ScheduleExecutionConverter() {
        userRepository = SpringBeanUtil.getBean(UserRepository.class);
        taskRepository = SpringBeanUtil.getBean(TaskRepository.class);
        taskScheduleRepository = SpringBeanUtil.getBean(TaskScheduleRepository.class);
    }

    @Override
    public void extraHandle(ScheduleExecution execution, ScheduleExecutionVO executionVO) {
        //转换成定时任务类型描述
        if(execution.getStatus()==0){
            executionVO.setStatus("成功");
        }else{
            executionVO.setStatus("失败");
        }
        //转换为任务类型
        executionVO.setTaskType(EnumUtil.getByField(ScheduleTaskTypeConstants.class,"getValue", String.valueOf(execution.getTaskType())).getLabel());
        //转换为定时任务名称
        Optional<TaskSchedule> taskScheduleOptional = taskScheduleRepository.findById(execution.getTaskScheduleId());
        taskScheduleOptional.ifPresent(taskSchedule -> executionVO.setTaskScheduleName(taskSchedule.getScheduleName()));
        //转换为任务名称
        Optional<Task> taskOptional = taskRepository.findById(execution.getTaskId());
        taskOptional.ifPresent(task -> executionVO.setTaskName(task.getName()));
        //转换为执行人名称
        Optional<User> createByOptional = userRepository.findById(execution.getOperateBy());
        createByOptional.ifPresent(user -> executionVO.setOperateBy(user.getNickName()));
    }
}
