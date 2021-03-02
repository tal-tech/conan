package com.tal.wangxiao.conan.common.model.query;

/**
 * 用枚举类表示Criteria查询连接条件
 * @author conan
 */
public enum MatchType {
    /**
     * filed = value
     */
    EQUAL,

    /**
     * field != value
     */
    NOT_EQUAL,

    /**
     * 下面四个用于Number类型的比较
     *
     * GT: filed > value
     * GE: field >= value
     * LT: field < value
     * LE: field <= value
     */
    GT,
    GE,
    LT,
    LE,

    /**
     *
     * field like value
     */
    LIKE,

    /**
     * field like value
     */
    NOT_LIKE,

    /**
     * field in value
     */
    IN,

    /**
     * 下面四个用于可比较类型(Comparable)的比较
     *
     * GREATER_THAN: field > value
     * GREATER_THAN_EQUAL: field >= value
     * LESS_THAN: field < value
     * LESS_THAN_EQUAL: field <= value
     */
    GREATER_THAN,
    GREATER_THAN_EQUAL,
    LESS_THAN,
    LESS_THAN_EQUAL
}
