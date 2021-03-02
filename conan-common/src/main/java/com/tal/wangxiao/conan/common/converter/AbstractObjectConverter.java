package com.tal.wangxiao.conan.common.converter;

import org.springframework.beans.BeanUtils;

/**
 * DO转VO抽象类，完成分页对象的转换
 * @author huyaoguo
 * @date 2021/1/25
 */
public abstract class AbstractObjectConverter<S, T> {
    /**
     * 转换
     * @param s DO
     * @param t VO
     */
    void convert(S s, T t) throws Exception {
        BeanUtils.copyProperties(s, t);
        extraHandle(s, t);
    }

    /**
     * 其他处理
     * @param s
     * @param t
     */
    public abstract void extraHandle(S s, T t) throws Exception;
}
