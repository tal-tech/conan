package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.RecordDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 录制DAO
 * @author liujinsong
 * @date 2019/6/4
 */
public interface RecordDetailRepository extends JpaRepository<RecordDetail, Integer> {
    /**
     * 根据录制ID查找录制
     * @param recordId
     * @return
     */
    List<RecordDetail> findByRecordId(Integer recordId);
}
