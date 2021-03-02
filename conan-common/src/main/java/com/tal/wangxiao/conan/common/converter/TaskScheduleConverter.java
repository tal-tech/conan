package com.tal.wangxiao.conan.common.converter;

import com.tal.wangxiao.conan.common.constant.ScheduleConstants;
import com.tal.wangxiao.conan.common.constant.enums.ScheduleTaskTypeConstants;
import com.tal.wangxiao.conan.common.entity.db.Task;
import com.tal.wangxiao.conan.common.entity.db.TaskSchedule;
import com.tal.wangxiao.conan.common.entity.db.User;
import com.tal.wangxiao.conan.common.model.vo.TaskScheduleVO;
import com.tal.wangxiao.conan.common.repository.db.TaskRepository;
import com.tal.wangxiao.conan.common.repository.db.UserRepository;
import com.tal.wangxiao.conan.common.utils.spring.SpringBeanUtil;
import com.tal.wangxiao.conan.utils.enumutils.EnumUtil;

import java.util.Optional;

/**
 * 定时任务DO转VO工具类
 * @author huyaoguo
 * @date 2020/11/25
 */
public class TaskScheduleConverter extends AbstractObjectConverter<TaskSchedule, TaskScheduleVO> {

    private UserRepository userRepository;

    private TaskRepository taskRepository;

    public TaskScheduleConverter() {
        userRepository = SpringBeanUtil.getBean(UserRepository.class);
        taskRepository = SpringBeanUtil.getBean(TaskRepository.class);
    }

    @Override
    public void extraHandle(TaskSchedule task, TaskScheduleVO taskVO) {
        //转换成定时任务类型描述
        taskVO.setType(EnumUtil.getByField(ScheduleTaskTypeConstants.class,"getValue", String.valueOf(task.getType())).getLabel());
        //转换为定时策略
        if(task.getStrategy()==1){
            taskVO.setStrategy(ScheduleConstants.MISFIRE_ONCE_PROCEED);
        }else{
            taskVO.setStrategy(ScheduleConstants.MISFIRE_DEFAULT);
        }
        //转换为任务名称
        Optional<Task> taskOptional = taskRepository.findById(task.getTaskId());
        taskOptional.ifPresent(task1 -> taskVO.setTaskName(task1.getName()));
        //转换为创建人姓名
        Optional<User> createByOptional = userRepository.findById(task.getCreateBy());
        createByOptional.ifPresent(user -> taskVO.setCreateBy(user.getNickName()));
        //转换为更新人姓名
        Optional<User> updateByOptional = userRepository.findById(task.getUpdateBy());
        updateByOptional.ifPresent(user -> taskVO.setUpdateBy(user.getNickName()));
        //转换定时任务执行状态
        taskVO.setStatus(EnumUtil.getByField(ScheduleConstants.Status.class,"getValue", String.valueOf(task.getStatus())).getLabel());
    }
}
