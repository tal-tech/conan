package com.tal.wangxiao.conan.admin.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tal.wangxiao.conan.admin.cache.AdminCache;
import com.tal.wangxiao.conan.common.constant.enums.AgentStatus;
import com.tal.wangxiao.conan.common.entity.db.AgentNode;
import com.tal.wangxiao.conan.common.kafaka.KafkaType;
import com.tal.wangxiao.conan.common.kafaka.TaskMessage;
import com.tal.wangxiao.conan.common.repository.db.AgentNodeRepository;
import com.tal.wangxiao.conan.utils.entity.EntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Kafka消费者监听器
 *
 * @author liujinsong
 * @date 2019/5/24
 */
@Slf4j
public class KafkaConsumerListener {

    @Resource
    private AgentNodeRepository agentNodeRepository;

    @KafkaListener(topics = {"conan-task-result"})
    public void listen(ConsumerRecord<?, ?> record) {
        try {
            String body = record.value().toString();
            TaskMessage taskMessage = new ObjectMapper().readValue(body, TaskMessage.class);
            log.info("接收到任务队列消息：{}", taskMessage.toString());
        } catch (Exception e) {
            log.error("消费任务队列消息异常", e);
        }
    }

    /**
     * 处理kafka的收到的agent消息
     *
     * @param record
     */
    @KafkaListener(topics = {"conan-agent-message"})
    @Transactional(rollbackOn = Exception.class)
    public void listenAgentMessage(ConsumerRecord<?, ?> record) {
        try {
            String body = record.value().toString();
            TaskMessage taskMessage = new ObjectMapper().readValue(body, TaskMessage.class);
            log.info("接收到agent 任务队列消息：{}", taskMessage.toString());
            String agentEnv = taskMessage.getData().getRunEnv();
            String systemEnv = AdminCache.getEnv();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            Map<String, Object> kafkaData = (LinkedHashMap) taskMessage.getData().getData();
            AgentNode agentNode = EntityUtils.mapToEntity(kafkaData, AgentNode.class);
            System.out.println(kafkaData.toString());
            if (agentEnv.equals(systemEnv)) {
                if (taskMessage.getData().getType() == KafkaType.AGENT_REGISTER) {
                    //agent 服务注册
                    /*agentNode.setAgentId(kafkaData.get("agentId").toString());
                    agentNode.setName(kafkaData.get("name").toString());
                    agentNode.setIpAddress(kafkaData.get("ipAddress").toString());
                    agentNode.setStatus(Integer.valueOf(kafkaData.get("status").toString()));
                    agentNode.setUpdateTime(LocalDateTime.now());*/
                    agentNode.setUpdateTime(LocalDateTime.now());
                    agentNodeRepository.save(agentNode);
                } else if (taskMessage.getData().getType() == KafkaType.AGENT_CHECK_STOP) {
                    //agent 心跳停止
                    agentNodeRepository.deleteByAgentId(agentNode.getAgentId());
                } else if (taskMessage.getData().getType() == KafkaType.AGENT_CHECK_RUN) {
                    //心跳检测 收到更新该机器ID的更新时间
                    Optional<AgentNode> agentNodeOptional = agentNodeRepository.findFirstByAgentId(agentNode.getAgentId());
                    if (agentNodeOptional.isPresent()) {
                        //AgentNode agentNode = agentNodeOptional.get();
                        agentNode = agentNodeOptional.get();
                        agentNode.setStatus(AgentStatus.FREE.getValue());
                        agentNode.setUpdateTime(LocalDateTime.now());
                    } else {
                        log.info("无该Agent的配置，将重新注册");
                        agentNode.setUpdateTime(LocalDateTime.now());
                        agentNode.setAgentId(agentNode.getAgentId());
                        agentNode.setName(agentNode.getName());
                        agentNode.setIpAddress(agentNode.getIpAddress());
                        agentNode.setStatus(agentNode.getStatus());
                    }
                    agentNodeRepository.save(agentNode);
                }
            } else {
                log.info("消息环境不匹配，收到{}环境的agent消息，agent服务环境为{}，将不执行", agentEnv, systemEnv);
            }
        } catch (Exception e) {
            log.error("消费任务队列消息异常", e);
        }
    }
}
