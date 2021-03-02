package com.tal.wangxiao.conan.common.repository.db;

import com.tal.wangxiao.conan.common.entity.db.AgentNode;
import org.apache.ibatis.annotations.Delete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * agent配置Dao
 * @author huyaoguo
 * @date 2020/6/28
 */

public interface AgentNodeRepository  extends JpaRepository<AgentNode, Integer> {

    Optional<AgentNode> findFirstByStatusAndAgentEnv(Integer status,String agentEnv);

    List<AgentNode> findByStatusAndAgentEnv(Integer status,String agentEnv);


    List<AgentNode> findByStatus(Integer status);


    Optional<AgentNode> findFirstByAgentId(String agentId);

    void deleteByAgentId(String agentId);

    List<AgentNode> findByUpdateTimeBefore(LocalDateTime time);

}
