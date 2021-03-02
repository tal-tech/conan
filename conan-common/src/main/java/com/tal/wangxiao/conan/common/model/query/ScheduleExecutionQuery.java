package com.tal.wangxiao.conan.common.model.query;


import com.tal.wangxiao.conan.common.entity.db.ScheduleExecution;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


/**
 * 定时任务查询
 *
 * @author liujinsong
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ScheduleExecutionQuery extends BaseQuery<ScheduleExecution> {


    @QueryWord
    private Integer taskScheduleId;


    @Override
    public Specification<ScheduleExecution> toSpec() {
        //所有条件用and连接
        Specification<ScheduleExecution> spec = super.toSpecWithAnd();
        return ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>(20);
            predicates.add(spec.toPredicate(root, criteriaQuery, criteriaBuilder));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }
}
