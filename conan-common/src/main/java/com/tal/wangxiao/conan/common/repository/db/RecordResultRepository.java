package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.RecordResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 流量录制结果DAO
 * @author conan
 */
public interface RecordResultRepository extends JpaRepository<RecordResult, Integer> {
    /**
     * 根据录制ID查询
     * @param recordId
     * @return
     */
    List<RecordResult> findByRecordId(Integer recordId);
    
    /**
     * 根据录制ID查询
     * @param recordId
     * @return
     */
    List<RecordResult> findByRecordIdAndApiId(Integer recordId,Integer apiId);
    
    /**
     * 根据录制ID查询
     * @param recordId
     * @return
     */
    List<RecordResult> findByApiId(Integer recordId);

    /**
     * 根据录制ID查询根据position排序
     * @param recordId
     * @return
     */


    @Query(value = "select count(id) as totalActualCount   from record_result  where record_id = ?1", nativeQuery = true)
    Integer findByCountIdAnd(Integer recordId);
}
