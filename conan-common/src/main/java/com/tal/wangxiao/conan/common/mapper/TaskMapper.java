package com.tal.wangxiao.conan.common.mapper;

import java.util.List;
import com.tal.wangxiao.conan.common.domain.Task;

import java.util.List;
import com.tal.wangxiao.conan.common.domain.Task;

/**
 * 任务管理Mapper接口
 *
 * @author dengkunnan
 * @date 2020-12-28
 */
public interface TaskMapper
{
    /**
     * 查询任务管理
     *
     * @param id 任务管理ID
     * @return 任务管理
     */
    public Task selectTaskById(Integer id);

    /**
     * 查询任务管理列表
     *
     * @param task 任务管理
     * @return 任务管理集合
     */
    public List<Task> selectTaskList(Task task);

    /**
     * 新增任务管理
     *
     * @param task 任务管理
     * @return 结果
     */
    public int insertTask(Task task);

    /**
     * 修改任务管理
     *
     * @param task 任务管理
     * @return 结果
     */
    public int updateTask(Task task);

    /**
     * 删除任务管理
     *
     * @param id 任务管理ID
     * @return 结果
     */
    public int deleteTaskById(Integer id);

    /**
     * 批量删除任务管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTaskByIds(Integer[] ids);
}