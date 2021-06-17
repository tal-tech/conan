package com.tal.wangxiao.conan.common.service.common;

import com.tal.wangxiao.conan.common.constant.enums.AgentStatus;
import com.tal.wangxiao.conan.common.entity.db.AgentNode;
import com.tal.wangxiao.conan.common.exception.BaseException;
import com.tal.wangxiao.conan.common.repository.db.AgentNodeRepository;
import com.tal.wangxiao.conan.utils.random.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Agent公共服务类
 *
 * @author huyaoguo
 * @date 2021/1/6
 **/
@Service
public class AgentCommonService {

    @Resource
    private AgentNodeRepository agentNodeRepository;

    /**
     * 获取执行的AgentId
     *
     * @return agentId
     */
    public String getAgentId(String agentEnv) throws BaseException {
        Optional<AgentNode> agentNodeOptional = agentNodeRepository.findFirstByStatusAndAgentEnv(AgentStatus.FREE.getValue(), agentEnv);

        if (agentNodeOptional.isPresent()) {
            return agentNodeOptional.get().getAgentId();
        } else {
            List<AgentNode> agentNodeList = agentNodeRepository.findByStatusAndAgentEnv(AgentStatus.BUSY.getValue(), agentEnv);
            int size = agentNodeList.size();
            if (size <= 0) {
                throw new BaseException("has not free agent machine（没有可以使用的Agent机器，请检查Agent是否启动或全部忙碌中）");
            } else {
                return agentNodeList.get(RandomUtils.getRandom(0, size)).getAgentId();
            }
        }
    }

    /**
     * 更改Agent的状态
     *
     * @param
     * @return agentId
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAgentStatus(String agentId, Boolean isRun) {
        Optional<AgentNode> agentNodeOptional = agentNodeRepository.findFirstByAgentId(agentId);
        if (agentNodeOptional.isPresent()) {
            AgentNode agentNode = agentNodeOptional.get();
            if (isRun) {
                agentNode.setStatus(AgentStatus.BUSY.getValue());
            } else {
                agentNode.setStatus(AgentStatus.FREE.getValue());
            }
            agentNode.setUpdateTime(LocalDateTime.now());
            agentNodeRepository.save(agentNode);
        }
    }
}
