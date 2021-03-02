package com.tal.wangxiao.conan.admin.schedule.config;


import com.tal.wangxiao.conan.config.core.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 执行定时任务
 * @author liujinsong
 * @data 2019/10/15
 */
@Slf4j
public class ScheduleRunnable implements Runnable {
    private Object target;
    private Method method;
    private String params;

    public ScheduleRunnable(String beanName, String methodName, String params)//String methodName, String params
            throws NoSuchMethodException, SecurityException {
        System.out.println("------------ScheduleRunnable");
        this.target = SpringUtils.getBean(beanName);
        this.params = params;
        if (StringUtils.isNotEmpty(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    @Override
    public void run() {
        try {
            ReflectionUtils.makeAccessible(method);
            if (StringUtils.isNotEmpty(params)) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception e) {
            log.error("定时任务执行失败: {}",e.getMessage());
        }
    }
}