package com.tal.wangxiao.conan.common.model.query;

import com.tal.wangxiao.conan.common.entity.db.TaskSchedule;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
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
public class TaskScheduleQuery extends BaseQuery<TaskSchedule> {

    @QueryWord(column = "name", func = MatchType.LIKE)
    private String name;

    @QueryWord
    private Integer id;

    @QueryWord
    private Integer type;

    @QueryWord
    private Integer taskId;

    @QueryWord
    private Integer createBy;

    /**
     * 创建时间查询范围
     */
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;


    @Override
    public Specification<TaskSchedule> toSpec() {
        //所有条件用and连接
        Specification<TaskSchedule> spec = super.toSpecWithAnd();
        return ((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>(20);
            predicates.add(spec.toPredicate(root, criteriaQuery, criteriaBuilder));

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }
}
