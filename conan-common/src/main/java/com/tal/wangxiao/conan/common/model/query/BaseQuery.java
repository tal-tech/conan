package com.tal.wangxiao.conan.common.model.query;


import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * JPA动态查询抽象类，实现通用的or/and字句的构造
 *
 * @author conan
 */
@Data
public abstract class BaseQuery<T> {

    /**
     * 页码从0开始记
     */
    protected Integer pageNum = 0;
    protected Integer pageSize = 10;

    /**
     * 将查询转换成Specification
     *
     * @return
     */
    public abstract Specification<T> toSpec();

    /**
     * 动态查询and连接
     *
     * @return
     */
    protected Specification<T> toSpecWithAnd() {
        return this.toSpecWithLogicType("and");
    }

    /**
     * 动态查询or连接
     *
     * @return
     */
    protected Specification<T> toSpecWithOr() {
        return this.toSpecWithLogicType("or");
    }


    private Specification<T> toSpecWithLogicType(String logicType) {
        BaseQuery outerThis = this;
        return (root, criteriaQuery, criteriaBuilder) -> {
            Class clazz = outerThis.getClass();
            //获取查询类Query的所有字段,包括父类字段
            List<Field> fields = getAllFieldsWithRoot(clazz);
            List<Predicate> predicates = new ArrayList<>(fields.size());
            for (Field field : fields) {
                //获取字段上的@QueryWord注解
                QueryWord qw = field.getAnnotation(QueryWord.class);
                if (qw == null) {
                    continue;
                }
                // 获取字段名
                String column = qw.column();
                //如果主注解上column为默认值"",则以field为准
                if ("".equals(column)) {
                    column = field.getName();
                }

                field.setAccessible(true);

                try {

                    // 对象可为null
                    Object value = field.get(outerThis);
                    //如果值为null,注解未标注nullable,跳过
                    if (value == null && !qw.nullable()) {
                        continue;
                    }

                    // 字符串可为空串
                    if (value != null && String.class.isAssignableFrom(value.getClass())) {
                        String s = (String) value;
                        //如果值为"",且注解未标注emptyable,跳过
                        if ("".equals(s) && !qw.emptyable()) {
                            continue;
                        }
                    }

                    //通过注解上func属性,构建路径表达式
                    Path path = root.get(column);
                    switch (qw.func()) {
                        case EQUAL:
                            predicates.add(criteriaBuilder.equal(path, value));
                            break;
                        case LIKE:
                            predicates.add(criteriaBuilder.like(path, "%" + value + "%"));
                            break;
                        case IN:
                            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                            assert value != null;
                            for (Integer obj : (List<Integer>) value) {
                                in.value(obj);
                            }
                            predicates.add(in);
                            break;
                        case GT:
                            predicates.add(criteriaBuilder.gt(path, (Number) value));
                            break;
                        case LT:
                            predicates.add(criteriaBuilder.lt(path, (Number) value));
                            break;
                        case GE:
                            predicates.add(criteriaBuilder.ge(path, (Number) value));
                            break;
                        case LE:
                            predicates.add(criteriaBuilder.le(path, (Number) value));
                            break;
                        case NOT_EQUAL:
                            predicates.add(criteriaBuilder.notEqual(path, value));
                            break;
                        case NOT_LIKE:
                            predicates.add(criteriaBuilder.notLike(path, "%" + value + "%"));
                            break;
                        case GREATER_THAN:
                            predicates.add(criteriaBuilder.greaterThan(path, (Comparable) value));
                            break;
                        case GREATER_THAN_EQUAL:
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, (Comparable) value));
                            break;
                        case LESS_THAN:
                            predicates.add(criteriaBuilder.lessThan(path, (Comparable) value));
                            break;
                        case LESS_THAN_EQUAL:
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(path, (Comparable) value));
                            break;
                        default:
                    }
                } catch (Exception ignored) {
                }
            }
            Predicate p = null;
            if (logicType == null || "".equals(logicType) || "and".equals(logicType)) {
                //and连接
                p = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            } else if ("or".equals(logicType)) {
                //or连接
                p = criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
            }
            return p;
        };
    }

    private List<Field> getAllFieldsWithRoot(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        //获取本类所有字段
        Field[] dFields = clazz.getDeclaredFields();
        if (Objects.nonNull(dFields) && dFields.length > 0) {
            fieldList.addAll(Arrays.asList(dFields));
        }

        // 若父类是Object，则直接返回当前Field列表
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == Object.class) {
            return Arrays.asList(dFields);
        }

        // 递归查询父类的field列表
        List<Field> superFields = getAllFieldsWithRoot(superClass);

        //不重复字段
        if (Objects.nonNull(superFields) && !superFields.isEmpty()) {
            superFields.stream().filter(field -> !fieldList.contains(field)).forEach(fieldList::add);
        }
        return fieldList;
    }
}
