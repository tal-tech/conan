package com.tal.wangxiao.conan.agent.cache;

import com.tal.wangxiao.conan.agent.schedule.AgentHeartSchedule;
import com.tal.wangxiao.conan.common.constant.enums.AgentStatus;
import com.tal.wangxiao.conan.common.entity.db.AgentNode;
import com.tal.wangxiao.conan.common.kafaka.KafkaType;
import com.tal.wangxiao.conan.common.service.common.KafkaMessageService;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
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
public class CodeCache {

    /**
     * 公共缓存Map
     */
    public static Map<String,Object> commonMap = new HashMap<>();

    /**
     * 环境变量
     */
    @Value("${system.env}")
    private String env;

    /**
     * 环境变量
     */
    @Value("${system.redis.cacheTime}")
    private int redisCacheTime;

    @Resource
    private AgentHeartSchedule agentHeartSchedule;

    @Resource
    KafkaMessageService kafkaMessageService;

    @PostConstruct
    public void init() {
        //系统初始化执行
        String agentId =  StringHandlerUtils.getUniqueId();
        log.info("程序启动，初始化缓存类 正在上报注册中心 机器ID : "+agentId);
        commonMap.put("agentId",agentId);
        commonMap.put("env", env);
        commonMap.put("redisCacheTime", redisCacheTime);
        agentHeartRegister(agentId);
    }

    //初始化上报注册中心
    public void agentHeartRegister(String agentId) {
        //初始化上报注册中心
        log.info("Agent服务 Heart Beat Start -- agentId :"+ agentId);
        AgentNode agentNode = new AgentNode();
        agentNode.setAgentId(agentId);
        initAgentConfig(agentNode, log, AgentStatus.FREE);
        AgentHeartSchedule.agentHeartMessage(agentNode, KafkaType.AGENT_REGISTER, kafkaMessageService);
    }

    //初始化agent配置
    public static void initAgentConfig(AgentNode agentNode, Logger log, AgentStatus free) {
        String ipAddress = "unknown ip address";
        String hostName = "unknown host name";
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error("获取ip地址异常："+e.getMessage());
        }
        agentNode.setAgentEnv(getEnv());
        agentNode.setStatus(free.getValue());
        agentNode.setIpAddress(ipAddress);
        agentNode.setUpdateTime(LocalDateTime.now());
        agentNode.setName(hostName);
    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
        log.info("Agent服务 Heart STOP，上报注册中心 -- agentId : "+ CodeCache.commonMap.get("agentId"));
        AgentNode agentNode = new AgentNode();
        agentNode.setAgentId(CodeCache.commonMap.get("agentId").toString());
        AgentHeartSchedule.agentHeartMessage(agentNode, KafkaType.AGENT_CHECK_STOP, kafkaMessageService);
    }

    //获取机器ID的快捷方法
    public static String getAgentId(){
        return commonMap.get("agentId").toString();
    }

    //获取环境的快捷方法
    public static String getEnv(){
        return commonMap.get("env").toString();
    }

    //获取redis缓存过期时间的快捷方法
    public static Integer getRedisCacheTime(){
        return (Integer) commonMap.get("redisCacheTime");
    }

}

