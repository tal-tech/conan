package com.tal.wangxiao.conan.admin.cache;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

/**
 * 写字典缓存全局变量
 *
 * @author huyaoguo
 * @date 2020/12/15
 **/
@Component
@Repository("cache")
@Data
@Slf4j
public class AdminCache {

    /**
     * 公共缓存Map
     */
    public static Map<String,String> commonMap = new HashMap<>();

    /**
     * 环境变量
     */
    @Value("${system.env}")
    private String env;

    /**
     * redis环境时间
     */
    @Value("${system.redis.cacheTime}")
    private String redisCacheTime;

    @PostConstruct
    public void init() {
        //系统初始化执行
        commonMap.put("env", env);
    }



    @PreDestroy
    public void destroy() {
        //系统运行结束
        //log.info("Agent服务 Heart STOP，上报注册中心 -- agentId : "+CodeCache.commonMap.get("agentId"));
    }

    //获取环境的快捷方法
    public static String getEnv(){
        return commonMap.get("env");
    }

}

