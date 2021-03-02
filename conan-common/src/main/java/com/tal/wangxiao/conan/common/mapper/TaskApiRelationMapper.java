package com.tal.wangxiao.conan.common.mapper;

import java.util.List;

import com.tal.wangxiao.conan.common.domain.TaskApiRelation;
import com.tal.wangxiao.conan.common.domain.TaskApiRelationDbInfo;
import com.tal.wangxiao.conan.common.domain.TaskApiRelationView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * taskApiRelationMapper接口
 *
 * @author dengkunan
 * @date 2020-12-29
 */
@Mapper
public interface TaskApiRelationMapper {
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
     * @return taskApiRelation集合
     */
  /*  @Select("SELECT re.id, api.api_id as api_id,api.name as api_name,domain.name as domain_name, d.dept_name as dept_name, u.user_name as create_by_name from bss_api api " +
            " LEFT JOIN bss_task_api_relation re on  api.api_id = re.api_id" +
            " LEFT JOIN bss_domain  domain ON domain.id = api.domain_id\n" +
            "        LEFT  JOIN sys_dept d on api.sys_dept_id = d.dept_id where re.task_id = #{taskId}")*/
    public List<TaskApiRelationView> selectTaskApiRelationViewListByTaskId(Integer taskId);


    /**
     * 查询taskApiRelation列表 通过部门，添加部门下所有接口列表
     *
     * @param deptId deptId
     * @return taskApiRelation集合
     */
    public List<TaskApiRelationView> selectTaskApiRelationViewListByDeptId(Integer deptId);


    /**
     * 查询taskApiRelation列表 通过部门，添加域名下所有接口列表
     *
     * @param domainId domainId
     * @return taskApiRelation集合
     */
    public List<TaskApiRelationView> selectTaskApiRelationViewListByDomainId(Integer domainId);


    /**
     * 查询taskApiRelation列表 通过api name 及domain
     *
     * @param taskApiRelationDbInfo taskApiRelationDbInfo
     * @return taskApiRelation集合
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
     * 查询taskExcutionId 获取Task id
     *
     * @param taskExcutionId taskExcutionId
     * @return taskID
     */
    @Select("SELECT task_id from bss_task_execution where id = #{taskExcutionId}")
    public Integer  getTaskIdByExcutionId(Integer taskExcutionId);



    /**
     * 查询taskExcutionId 获取Task id
     *
     * @param taskExcutionId taskExcutionId
     * @return taskID
     */
    @Select("SELECT task_id from bss_task_execution where id = #{taskExcutionId}")
    public Integer  getApiResponseIsJSonListBy(Integer taskExcutionId);



    /**
     * 新增taskApiRelation
     *
     * @param taskApiRelations taskApiRelations
     * @return 结果
     */
    public int insertTaskApiRelation(@Param(value = "taskApiRelations") List<TaskApiRelation> taskApiRelations);




    /**
     * 修改taskApiRelation
     *
     * @param taskApiRelation taskApiRelation
     * @return 结果
     */
    public int updateTaskApiRelation(TaskApiRelation taskApiRelation);

    /**
     * 删除taskApiRelation
     *
     * @param id taskApiRelationID
     * @return 结果
     */
    public int deleteTaskApiRelationById(Integer id);

    /**
     * 批量删除taskApiRelation
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTaskApiRelationByIds(Integer[] ids);

}