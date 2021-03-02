package com.tal.wangxiao.conan.admin.schedule;

import com.tal.wangxiao.conan.common.constant.enums.AgentStatus;
import com.tal.wangxiao.conan.common.entity.db.AgentNode;
import com.tal.wangxiao.conan.common.repository.db.AgentNodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 注册中心
 *
 * @author huyaoguo
 * @date 2020/12/15
 **/
@Slf4j
@Component
public class RegisterCenterSchedule {

    @Resource
    private AgentNodeRepository agentNodeRepository;

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void agentCheck() {
        //每3分钟kill掉10分钟心跳停止的服务
        /*DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String durationTime = LocalDateTime.now().minus(10, ChronoUnit.MINUTES).format(df);*/
        //暂时先改状态
        List<AgentNode> agentNodeList = agentNodeRepository.findByUpdateTimeBefore(LocalDateTime.now().minus(10, ChronoUnit.MINUTES));
        for(AgentNode agentNode:agentNodeList){
            agentNode.setStatus(AgentStatus.READY.getValue());
        }
        agentNodeRepository.saveAll(agentNodeList);
        //log.info("Agent服务 Heart Killed -- count :");
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getCanonicalHostName());

    }
}
