package com.tal.wangxiao.conan.common.service.impl;

import java.util.List;
import java.util.Optional;

import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.entity.db.*;
import com.tal.wangxiao.conan.common.exception.BaseException;
import com.tal.wangxiao.conan.common.exception.execution.TaskExecutionException;
import com.tal.wangxiao.conan.common.exception.task.TaskException;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.vo.TaskVO;
import com.tal.wangxiao.conan.common.repository.db.*;
import com.tal.wangxiao.conan.sys.common.annotation.DataScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tal.wangxiao.conan.common.mapper.TaskMapper;
import com.tal.wangxiao.conan.common.domain.Task;
import com.tal.wangxiao.conan.common.service.TaskService;

import javax.annotation.Resource;

/**
 * 任务管理Service业务层处理
 *
 * @author dengkunnan
 * @date 2020-12-17
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Resource
    private TaskMapper taskMapper;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private TaskExecutionRepository taskExecutionRepository;

    @Resource
    private RecordRepository recordRepository;

    @Resource
    private DepartmentRepository departmentRepository;

    @Resource
    private ReplayRepository replayRepository;

    @Resource
    private ReplayDetailRepository replayDetailRepository;

    @Resource
    private DiffRepository diffRepository;

    @Resource
    private DiffDetailRepository diffDetailRepository;

    /**
     * 查询任务管理
     *
     * @param id 任务管理ID
     * @return 任务管理
     */
    @Override
    public Task selectTaskById(Integer id) {
        return taskMapper.selectTaskById(id);
    }

    /**
     * 查询任务管理列表
     *
     * @param task 任务管理
     * @return 任务管理
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @Override
    public List<Task> selectTaskList(Task task) {
        return taskMapper.selectTaskList(task);
    }

    /**
     * 新增任务管理
     *
     * @param task 任务管理
     * @return 结果
     */
    @Override
    public int insertTask(Task task) {
        return taskMapper.insertTask(task);
    }

    /**
     * 修改任务管理
     *
     * @param task 任务管理
     * @return 结果
     */
    @Override
    public int updateTask(Task task) {
        return taskMapper.updateTask(task);
    }

    /**
     * 批量删除任务管理
     *
     * @param ids 需要删除的任务管理ID
     * @return 结果
     */
    @Override
    public int deleteTaskByIds(Integer[] ids) {
        return taskMapper.deleteTaskByIds(ids);
    }

    /**
     * 删除任务管理信息
     *
     * @param id 任务管理ID
     * @return 结果
     */
    @Override
    public int deleteTaskById(Integer id) {
        return taskMapper.deleteTaskById(id);
    }

    @Override
    public Result<Object> findTaskInfoById(Integer id, String type) {
        com.tal.wangxiao.conan.common.entity.db.Task task;
        //前端返回任务信息
        TaskVO taskVO = new TaskVO();
        Integer taskExecutionId = null;
        //根据类型传时间
        switch (type){
            case "record":
                Optional<Record> recordOptional = recordRepository.findFirstByTaskExecutionId(id);
                if(!recordOptional.isPresent()){
                    return new Result<>(ResponseCode.INVALID_TASK_EXECUTION_ID,"查询录制信息失败，无效的任务执行ID:"+id);
                }
                taskExecutionId = recordOptional.get().getTaskExecutionId();
                taskVO.setApiCount(recordOptional.get().getApiCount());
                taskVO.setStartAt(recordOptional.get().getStartTime());
                taskVO.setEndAt(recordOptional.get().getEndTime());
                taskVO.setRecordId(recordOptional.get().getId());
                taskVO.setSuccessRate(recordOptional.get().getSuccessRate());
                Optional<User> userOptional = userRepository.findById(recordOptional.get().getOperateBy());
                userOptional.ifPresent(user -> taskVO.setOperateBy(user.getNickName()));
                break;
            case "replay":
                Optional<Replay> replayOptional = replayRepository.findById(id);
                if(!replayOptional.isPresent()){
                    return new Result<>(ResponseCode.INVALID_REPLAY_ID,"查询失败，无效的回放ID:"+id);
                }
                taskExecutionId = replayOptional.get().getTaskExecutionId();
                List<ReplayDetail> replayDetailList = replayDetailRepository.findByReplayId(id);
                taskVO.setApiCount(replayDetailList.size());
                taskVO.setStartAt(replayOptional.get().getStartAt());
                taskVO.setEndAt(replayOptional.get().getEndAt());
                taskVO.setRecordId(replayOptional.get().getId());
                taskVO.setSuccessRate(replayOptional.get().getSuccessRate());
                Optional<User> userOptional1 = userRepository.findById(replayOptional.get().getOperateBy());
                userOptional1.ifPresent(user -> taskVO.setOperateBy(user.getNickName()));
                break;
            case "diff":
                //id还是replayId
              /*  Optional<Replay> replayOptional1 = replayRepository.findById(id);
                if(!replayOptional1.isPresent()){
                    return new Result<>(ResponseCode.INVALID_REPLAY_ID,"查询失败，无效的回放ID:"+id);
                }
                Optional<Diff> diffOptional = diffRepository.findFirstByTaskExecutionIdAndReplayIdOrderByCreateTimeDesc(replayOptional1.get().getTaskExecutionId(), id);
                */
                Optional<Diff> diffOptional = diffRepository.findById(id);
                if(!diffOptional.isPresent()){
                    return new Result<>(ResponseCode.INVALID_REPLAY_ID,"查询失败，无效的比对ID:"+id);
                }

                if(!diffOptional.isPresent()){
                    return new Result<>(ResponseCode.INVALID_REPLAY_ID,"查询失败，无效的比对ID:"+id);
                }
                taskExecutionId = diffOptional.get().getTaskExecutionId();
                List<DiffDetail> findByDiffId = diffDetailRepository.findByDiffId(id);
                taskVO.setApiCount(findByDiffId.size());
                taskVO.setStartAt(diffOptional.get().getCreateTime());
                taskVO.setEndAt(diffOptional.get().getUpdateTime());
                taskVO.setRecordId(diffOptional.get().getId());
                taskVO.setSuccessRate(diffOptional.get().getSuccessRate());
                Optional<User> userOptional2 = userRepository.findById(diffOptional.get().getCreateBy());
                userOptional2.ifPresent(user -> taskVO.setOperateBy(user.getNickName()));
                break;
            default:
                return new Result<>(ResponseCode.SUCCESS,"");
        }
        try {
            task = findTaskByTaskExecutionId(taskExecutionId);
        }catch (TaskExecutionException e){
            log.info("查询任务详情失败："+e.getMsgDesc());
            return new Result<>(ResponseCode.INVALID_TASK_EXECUTION_ID,e.getMsgDesc());
        }catch (TaskException e){
            log.info("查询任务详情失败："+e.getMsgDesc());
            return new Result<>(ResponseCode.INVALID_TASK_ID,e.getMsgDesc());
        }catch (Exception e){
            log.info("查询任务详情失败："+e.getMessage());
            return new Result<>(ResponseCode.INVALID_TASK_ID,e.getMessage());
        }
        Optional<Department> departmentOptional = departmentRepository.findById(task.getDepartmentId());
        if(!departmentOptional.isPresent()){
            return new Result<>(ResponseCode.INVALID_DEPARTMENT_ID,"查询失败，无效的部门ID:"+task.getDepartmentId());
        }
        taskVO.setDeptId(task.getDepartmentId());
        taskVO.setTaskName(task.getName());
        taskVO.setTaskId(task.getId());
        taskVO.setDeptName(departmentOptional.get().getDeptName());
        return new Result<>(ResponseCode.SUCCESS,taskVO);
    }

    private com.tal.wangxiao.conan.common.entity.db.Task findTaskByTaskExecutionId(int taskExecutionId) {
        Optional<TaskExecution> taskExecutionOptional = taskExecutionRepository.findById(taskExecutionId);
        if(!taskExecutionOptional.isPresent()){
            throw new TaskExecutionException("无效的执行ID"+taskExecutionId,TaskExecutionException.Code.NO_EXECUTION_EXISTS);
        }
        Optional<com.tal.wangxiao.conan.common.entity.db.Task> taskOptional = taskRepository.findById(taskExecutionOptional.get().getTaskId());
        if(!taskOptional.isPresent()){
            throw new TaskException("无效的任务ID"+taskExecutionOptional.get().getTaskId(),TaskException.Code.NO_TASK_EXISTS);
        }
        return taskOptional.get();
    }

}
