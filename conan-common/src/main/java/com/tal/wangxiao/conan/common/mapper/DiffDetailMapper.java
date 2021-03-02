package com.tal.wangxiao.conan.common.mapper;

import java.util.List;

import com.tal.wangxiao.conan.common.domain.DiffDetail;
import com.tal.wangxiao.conan.common.domain.DiffDetailViewInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 比对接口关系表Mapper接口
 *
 * @author dengkunan
 * @date 2021-01-08
 */
public interface DiffDetailMapper {
    /**
     * 查询比对接口关系表
     *
     * @param id 比对接口关系表ID
     * @return 比对接口关系表
     */
    public DiffDetail selectDiffDetailById(Integer id);




    /**
     * 查询比对接口关系表列表
     *
     * @param executionId 任务执行ID
     * @return 比对接口关系表集合
     */
    @Select("select ddt.*, api.name as api_name, dm.name as domain_name, u.user_name as api_add_name  from bss_diff_detail ddt \n" +
            "LEFT JOIN bss_api api on api.api_id = ddt.api_id\n" +
            "LEFT JOIN bss_domain dm on dm.id = api.domain_id\n" +
            "LEFT JOIN sys_user u on u.user_id = api.create_by\n" +
            "WHERE ddt.diff_id = (SELECT diff_id FROM bss_diff WHERE task_execution_id = #{executionId} and replay_id = #{replayId}) ")
    public List<DiffDetailViewInfo> selectDiffDetailViewList(@Param("executionId") Integer executionId,@Param("replayId") Integer replayId);

    /**
     * 查询比对接口关系表列表
     *
     * @param diffDetail 比对接口关系表
     * @return 比对接口关系表集合
     */
    public List<DiffDetail> selectDiffDetailList(DiffDetail diffDetail);

    /**
     * 新增比对接口关系表
     *
     * @param diffDetail 比对接口关系表
     * @return 结果
     */
    public int insertDiffDetail(DiffDetail diffDetail);

    /**
     * 修改比对接口关系表
     *
     * @param diffDetail 比对接口关系表
     * @return 结果
     */
    public int updateDiffDetail(DiffDetail diffDetail);

    /**
     * 删除比对接口关系表
     *
     * @param id 比对接口关系表ID
     * @return 结果
     */
    public int deleteDiffDetailById(Integer id);

    /**
     * 批量删除比对接口关系表
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDiffDetailByIds(Integer[] ids);
}