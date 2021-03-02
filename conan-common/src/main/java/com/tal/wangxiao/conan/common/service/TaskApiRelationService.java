package com.tal.wangxiao.conan.common.service;

import java.util.List;
import com.tal.wangxiao.conan.common.domain.TaskApiRelation;
import com.tal.wangxiao.conan.common.domain.TaskApiRelationDbInfo;
import com.tal.wangxiao.conan.common.domain.TaskApiRelationView;

/**
 * taskApiRelationService接口
 * 
 * @author dengkunan
 * @date 2020-12-29
 */
public interface TaskApiRelationService
{
    /**
     * 查询taskApiRelation
     * 
     * @param id taskApiRelationID
     * @return taskApiRelation
     */
    public TaskApiRelation selectTaskApiRelationById(Integer id);


    /**
     * 查询taskApiRelation列表
     *
     * @param taskId taskId
     * @return TaskApiRelationView集合
     */
    public List<TaskApiRelationView> selectTaskApiRelationViewListByTaskId(Integer taskId);


    /**
     * 查询taskApiRelation列表
     *
     * @param deptId taskId
     * @return TaskApiRelationView集合
     */
    public List<TaskApiRelationView> selectTaskApiRelationViewListByDeptId(Integer deptId);


    /**
     * 查询taskApiRelation列表
     *
     * @param domainId domainId
     * @return TaskApiRelationView集合
     */
    public List<TaskApiRelationView> selectTaskApiRelationViewListByDomainId(Integer domainId);



    /**
     * 查询taskApiRelation列表
     *
     * @param taskApiRelationDbInfo taskApiRelationDbInfo
     * @return TaskApiRelationView集合
     */
    public List<TaskApiRelationView> selectTaskApiRelationViewListByApiNameAndDomainName(TaskApiRelationDbInfo taskApiRelationDbInfo);

    /**
     * 查询taskApiRelation列表
     * 
     * @param taskApiRelation taskApiRelation
     * @return taskApiRelation集合
     */
    public List<TaskApiRelation> selectTaskApiRelationList(TaskApiRelation taskApiRelation);

    /**
     * 新增taskApiRelation
     * 
     * @param taskApiRelation taskApiRelation
     * @return 结果
     */
    public int insertTaskApiRelation(List<TaskApiRelation>  taskApiRelation);

    /**
     * 修改taskApiRelation
     * 
     * @param taskApiRelation taskApiRelation
     * @return 结果
     */
    public int updateTaskApiRelation(TaskApiRelation taskApiRelation);

    /**
     * 批量删除taskApiRelation
     * 
     * @param ids 需要删除的taskApiRelationID
     * @return 结果
     */
    public int deleteTaskApiRelationByIds(Integer[] ids);

    /**
     * 删除taskApiRelation信息
     * 
     * @param id taskApiRelationID
     * @return 结果
     */
    public int deleteTaskApiRelationById(Integer id);
}