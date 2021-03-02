package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.TaskApiRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 任务关联接口DAO
 * @author liujisong
 * @date 2019/5/27
 */
@Repository
public interface TaskApiRelationRepository extends JpaRepository<TaskApiRelation, Integer> {
    /**
     * 根据任务ID查询关联的接口
     * @param taskId
     * @return
     */
    Optional<List<TaskApiRelation>> findAllByTaskId(Integer taskId);

    /**
     * 根据任务ID和关联接口ID查询
     * @param taskId
     * @param apiId
     * @return
     */
    Optional<TaskApiRelation> findByTaskIdAndApiId(Integer taskId, Integer apiId);
    
    /**
     * 根据任务ID和获取对应接口
     * @param taskId
     * @return
     */
    Optional<TaskApiRelation> findByTaskId(Integer taskId);
    
}
