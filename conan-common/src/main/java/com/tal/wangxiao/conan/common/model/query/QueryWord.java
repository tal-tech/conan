package com.tal.wangxiao.conan.common.model.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用枚举类表示Criteria查询连接条件
 * @author conan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryWord {

    /**
     * 数据库中字段名,默认为空字符串,则Query类中的字段要与数据库中字段一致
     */
    String column() default "";

    /**
     * equal, like, gt, lt...
     */
    MatchType func() default MatchType.EQUAL;

    /**
     * object是否可以为null
     */
    boolean nullable() default false;

    /**
     * 字符串是否可为空
     */
    boolean emptyable() default false;
}
