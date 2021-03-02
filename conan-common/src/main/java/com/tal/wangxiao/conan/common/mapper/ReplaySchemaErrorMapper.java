package com.tal.wangxiao.conan.common.mapper;

import java.util.List;

import com.tal.wangxiao.conan.common.domain.DeptJsonRule;
import com.tal.wangxiao.conan.common.domain.ReplaySchemaError;
import org.apache.ibatis.annotations.Select;

/**
 * 回放schema错误记录Mapper接口
 *
 * @author dengkunan
 * @date 2021-01-07
 */
public interface ReplaySchemaErrorMapper {


    @Select("select * from bss_dept_json_rule where sys_dept_id = \n" +
            "(select sys_dept_id from bss_task where id =(\n" +
            "select task_id from bss_task_execution where id =\n" +
            "            (select task_execution_id from bss_replay where id = = #{replayId}))) ")
    List<DeptJsonRule> getRuleByDeptId(Integer replayId);
    /**
     * 查询回放schema错误记录
     *
     * @param schemaErrorId 回放schema错误记录ID
     * @return 回放schema错误记录
     */
    public ReplaySchemaError selectReplaySchemaErrorById(Integer schemaErrorId);

    /**
     * 查询回放schema错误记录列表
     *
     * @param replaySchemaError 回放schema错误记录
     * @return 回放schema错误记录集合
     */
    public List<ReplaySchemaError> selectReplaySchemaErrorList(ReplaySchemaError replaySchemaError);

    /**
     * 新增回放schema错误记录
     *
     * @param replaySchemaError 回放schema错误记录
     * @return 结果
     */
    public int insertReplaySchemaError(ReplaySchemaError replaySchemaError);

    /**
     * 修改回放schema错误记录
     *
     * @param replaySchemaError 回放schema错误记录
     * @return 结果
     */
    public int updateReplaySchemaError(ReplaySchemaError replaySchemaError);

    /**
     * 删除回放schema错误记录
     *
     * @param schemaErrorId 回放schema错误记录ID
     * @return 结果
     */
    public int deleteReplaySchemaErrorById(Integer schemaErrorId);

    /**
     * 批量删除回放schema错误记录
     *
     * @param schemaErrorIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteReplaySchemaErrorByIds(Integer[] schemaErrorIds);
}