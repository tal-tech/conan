package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.ReplayDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 流量回放详情DAO
 */
public interface ReplayDetailRepository extends JpaRepository<ReplayDetail, Integer> {
    /**
     * 根据回放ID和接口ID查询
     *
     * @param replayId
     * @param apiId
     * @return
     */
    Optional<ReplayDetail> findByReplayIdAndApiId(Integer replayId, Integer apiId);


    /**
     * 查询流量回放详情
     *
     * @param replayId
     * @return
     */
    List<ReplayDetail> findByReplayId(@Param("replay_id") Integer replayId);

    @Query("select sum(actualCount) from ReplayDetail where replayId = :replayId")
    Integer getFlowsByReplayId(Integer replayId);

    @Query("select sum(actualCount) from ReplayDetail")
    Integer getAllFlows();
}
