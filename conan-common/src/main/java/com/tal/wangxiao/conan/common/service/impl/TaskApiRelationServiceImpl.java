package com.tal.wangxiao.conan.common.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.tal.wangxiao.conan.common.constant.enums.TaskStatus;
import com.tal.wangxiao.conan.common.domain.Task;
import com.tal.wangxiao.conan.common.domain.TaskApiRelationDbInfo;
import com.tal.wangxiao.conan.common.domain.TaskApiRelationView;
import com.tal.wangxiao.conan.common.mapper.TaskMapper;
import com.tal.wangxiao.conan.sys.common.annotation.DataScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tal.wangxiao.conan.common.mapper.TaskApiRelationMapper;
import com.tal.wangxiao.conan.common.domain.TaskApiRelation;
import com.tal.wangxiao.conan.common.service.TaskApiRelationService;

/**
 * taskApiRelationService业务层处理
 *
 * @author dengkunan
 * @date 2020-12-29
 */
@Service
public class TaskApiRelationServiceImpl implements TaskApiRelationService {
    @Autowired
    private TaskApiRelationMapper taskApiRelationMapper;

    @Autowired
    private TaskMapper taskMapper;

    /**
     * 查询taskApiRelation
     *
     * @param id taskApiRelationID
     * @return taskApiRelation
     */
    @Override
    public TaskApiRelation selectTaskApiRelationById(Integer id) {
        return taskApiRelationMapper.selectTaskApiRelationById(id);
    }

    @DataScope(deptAlias = "d", userAlias = "u")
    @Override
    public List<TaskApiRelationView> selectTaskApiRelationViewListByTaskId(Integer taskId) {
        return taskApiRelationMapper.selectTaskApiRelationViewListByTaskId(taskId);
    }

    @DataScope(deptAlias = "d", userAlias = "u")
    @Override
    public List<TaskApiRelationView> selectTaskApiRelationViewListByDeptId(Integer deptId) {
        return taskApiRelationMapper.selectTaskApiRelationViewListByDeptId(deptId);
    }

    @DataScope(deptAlias = "d", userAlias = "u")
    @Override
    public List<TaskApiRelationView> selectTaskApiRelationViewListByDomainId(Integer domainId) {
        return taskApiRelationMapper.selectTaskApiRelationViewListByDomainId(domainId);
    }

    @DataScope(deptAlias = "d", userAlias = "u")
    @Override
    public List<TaskApiRelationView> selectTaskApiRelationViewListByApiNameAndDomainName(TaskApiRelationDbInfo taskApiRelationDbInfo) {
        return taskApiRelationMapper.selectTaskApiRelationViewListByApiNameAndDomainName(taskApiRelationDbInfo);
    }

    /**
     * 查询taskApiRelation列表
     *
     * @param taskApiRelation taskApiRelation
     * @return taskApiRelation
     */
    @Override
    public List<TaskApiRelation> selectTaskApiRelationList(TaskApiRelation taskApiRelation) {
        return taskApiRelationMapper.selectTaskApiRelationList(taskApiRelation);
    }

    /**
     * 新增taskApiRelation
     *
     * @param taskApiRelationList taskApiRelationList
     * @return 结果
     */
    @Override
    public int insertTaskApiRelation(List<TaskApiRelation> taskApiRelationList) {
        List<TaskApiRelation> taskApiRelationListByfor = new ArrayList<TaskApiRelation>();

        Integer taskId = 0;
        if (taskApiRelationList.size() < 1) {
            return 1;
        } else {
            taskId = taskApiRelationList.get(0).getTaskId();
        }
        List<TaskApiRelationView> taskApiRelationListBydb = taskApiRelationMapper.selectTaskApiRelationViewListByTaskId(taskId);
        HashSet<Integer> dbApiList = new HashSet<Integer>(taskApiRelationListBydb.size());
        for (TaskApiRelationView taskApiRelationView : taskApiRelationListBydb) {
            dbApiList.add(taskApiRelationView.getTaskApiRelationId());
        }

        for (TaskApiRelation taskApiRelation : taskApiRelationList) {
            if (taskApiRelation.getTaskApiRelationId() != null && taskApiRelation.getTaskApiRelationId() != 0) {
                taskApiRelationMapper.updateTaskApiRelation(taskApiRelation);
                taskApiRelationListByfor.add(taskApiRelation);
                dbApiList.remove(taskApiRelation.getTaskApiRelationId());
            }
        }

        for(TaskApiRelation taskApiRelation :taskApiRelationListByfor) {
            taskApiRelationList.remove(taskApiRelation);
        }

        for (Integer api : dbApiList) {
            taskApiRelationMapper.deleteTaskApiRelationById(api);
        }

        //本次没有新增接口操作不需要重新录制，普通修改不影响任务, 新加减少接口需要重新录制
        if (taskApiRelationList.size() > 0 || dbApiList.size() > 0) {
            Task task = taskMapper.selectTaskById(taskId);
            task.setStatus(TaskStatus.READY.getValue());
        }
        if(taskApiRelationList.size() < 1) {
            return 1;
        }
        return taskApiRelationMapper.insertTaskApiRelation(taskApiRelationList);
    }

    /**
     * 修改taskApiRelation
     *
     * @param taskApiRelation taskApiRelation
     * @return 结果
     */
    @Override
    public int updateTaskApiRelation(TaskApiRelation taskApiRelation) {
        return taskApiRelationMapper.updateTaskApiRelation(taskApiRelation);
    }

    /**
     * 批量删除taskApiRelation
     *
     * @param ids 需要删除的taskApiRelationID
     * @return 结果
     */
    @Override
    public int deleteTaskApiRelationByIds(Integer[] ids) {
        return taskApiRelationMapper.deleteTaskApiRelationByIds(ids);
    }

    /**
     * 删除taskApiRelation信息
     *
     * @param id taskApiRelationID
     * @return 结果
     */
    @Override
    public int deleteTaskApiRelationById(Integer id) {
        return taskApiRelationMapper.deleteTaskApiRelationById(id);
    }
}