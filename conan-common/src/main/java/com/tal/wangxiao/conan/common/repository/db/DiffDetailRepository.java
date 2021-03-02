package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.DiffDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 流量比对详情DAO
 * @author huyaoguo
 * @date 2021/1/25
 */
public interface DiffDetailRepository extends JpaRepository<DiffDetail, Integer> {
	List <DiffDetail> findByDiffId(Integer diffId);

	//通过diffID与apiId获取diff结果
	Optional<DiffDetail> findByApiIdAndDiffId(Integer apiId, Integer diffId);
}
