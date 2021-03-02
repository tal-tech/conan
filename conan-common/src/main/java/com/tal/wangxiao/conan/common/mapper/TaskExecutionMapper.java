package com.tal.wangxiao.conan.common.mapper;

import java.util.List;
import com.tal.wangxiao.conan.common.domain.TaskExecutionInfo;

/**
 * 任务执行记录Mapper接口
 *
 * @author huyaoguo
 * @date 2021-01-22
 */
public interface TaskExecutionMapper
{
    /**
     * 查询任务执行记录
     *
     * @param executionId 任务执行记录ID
     * @return 任务执行记录
     */
    public TaskExecutionInfo selectTaskExecutionById(Long executionId);

    /**
     * 查询任务执行记录列表
     *
     * @param TaskExecutionInfo 任务执行记录
     * @return 任务执行记录集合
     */
    public List<TaskExecutionInfo> selectTaskExecutionList(TaskExecutionInfo TaskExecutionInfo);

    /**
     * 新增任务执行记录
     *
     * @param TaskExecutionInfo 任务执行记录
     * @return 结果
     */
    public int insertTaskExecution(TaskExecutionInfo TaskExecutionInfo);

    /**
     * 修改任务执行记录
     *
     * @param TaskExecutionInfo 任务执行记录
     * @return 结果
     */
    public int updateTaskExecution(TaskExecutionInfo TaskExecutionInfo);

    /**
     * 删除任务执行记录
     *
     * @param executionId 任务执行记录ID
     * @return 结果
     */
    public int deleteTaskExecutionById(Long executionId);

    /**
     * 批量删除任务执行记录
     *
     * @param executionIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteTaskExecutionByIds(Long[] executionIds);
}