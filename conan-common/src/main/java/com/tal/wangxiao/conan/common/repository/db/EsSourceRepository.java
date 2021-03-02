package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.EsSource;
import com.tal.wangxiao.conan.common.entity.db.TaskApiRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ES数据源配置
 * @author liujisong
 * @date 2019/5/27
 */
@Repository
public interface EsSourceRepository extends JpaRepository<EsSource, Integer> {
    /**
     * 根据任务ID查询关联的接口
     * @param id
     * @return
     */
    Optional <EsSource> findById(Integer id);


}
