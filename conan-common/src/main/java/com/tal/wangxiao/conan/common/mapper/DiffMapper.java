package com.tal.wangxiao.conan.common.mapper;

import java.util.List;

import com.tal.wangxiao.conan.common.domain.ApiInfo;
import com.tal.wangxiao.conan.common.domain.Diff;
import com.tal.wangxiao.conan.common.entity.db.Replay;
import com.tal.wangxiao.conan.common.entity.db.TaskExecution;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 比对记录Mapper接口
 *
 * @author dengkunan
 * @date 2021-01-08
 */
public interface DiffMapper {
    /**
     * 查询比对记录
     *
     * @param id 比对记录ID
     * @return 比对记录
     */
    public Diff selectDiffById(Integer id);

    /**
     * 查询比对记录列表
     *
     * @param diff 比对记录
     * @return 比对记录集合
     */
    public List<Diff> selectDiffList(Diff diff);


    /**
     * 获取TaskExecution
     * */
    @Select("select * from bss_task_execution where execution_id = #{taskExecutionId}")
    public TaskExecution getApiResponseById(Integer taskExecutionId);


    /**
     * @param taskExecutionId
     * @param isBaseline
     * 获判断任务是否有基准信息
     * */
    @Select("select count(1) from bss_replay  where task_execution_id = #{taskExecutionId} and is_baseline = #{isBaseline}")
    public Integer getReplayCountByTaskExecutionIdAndIsBaseline(Integer taskExecutionId, Integer isBaseline);



    /**
     * @param taskExecutionId
     * @param isBaseline
     * 获判断任务是否有基准信息
     * */
    @Select("select * from bss_replay  where task_execution_id = #{taskExecutionId} and is_baseline = #{isBaseline}")
    public Replay getReplayInfoByTaskExecutionIdAndIsBaseline(Integer taskExecutionId, Integer isBaseline);

    /**
     * @param taskExecutionId
     * @param isBaseline
     * 获判断任务是否有基准信息
     * */
    @Select("select replay_id from bss_replay  where task_execution_id = #{taskExecutionId} and is_baseline = #{isBaseline}")
    public Integer getReplayIdByTaskExecutionIdAndIsBaseline(Integer taskExecutionId, Integer isBaseline);




    /**
     * @param recordId
     * get recordId api list Id
     * */
    @Select("SELECT api_id from bss_record_result WHERE record_id = #{recordId} GROUP BY api_id")
    public List<Integer> getRecordIdApiById(Integer recordId);



    /**
     * @param recordId
     * get recordId api list Id
     * */
    @Select("SELECT distinct request_id from bss_record_result WHERE record_id = #{recordId} and api_id = #{apiId}")
    public List<String> getRecordRequestIdApiById(Integer recordId, Integer apiId);


    /**
     * @param taskExecutionId
     * @param apiId
     * get recordId api list Id
     * */
    @Select("SELECT request_id from bss_record_result WHERE api_id = #{apiId} and record_id = (select record_id from bss_record where task_execution_id = #{taskExecutionId})")
    public List<String> getRecordRequestIdApiByTaskexcuteId(@Param("taskExecutionId") Integer taskExecutionId,@Param("apiId") Integer apiId);

    /**
     * @param recordId
     * get recordId api list Id
     * */
    @Select(" SELECT  api.response_is_json ,api.api_id ,api.name as name from bss_api api \n" +
            "LEFT JOIN bss_record_result rs on rs.api_id = api.api_id WHERE rs.record_id = #{recordId}")
    public List<ApiInfo> getApiInfoById(Integer recordId);


    /**
     * 新增比对记录
     *
     * @param diff 比对记录
     * @return 结果
     */
    public int insertDiff(Diff diff);

    /**
     * 修改比对记录
     *
     * @param diff 比对记录
     * @return 结果
     */
    public int updateDiff(Diff diff);

    /**
     * 删除比对记录
     *
     * @param id 比对记录ID
     * @return 结果
     */
    public int deleteDiffById(Integer id);

    /**
     * 批量删除比对记录
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDiffByIds(Integer[] ids);
}