//package com.tal.wangxiao.conan.common.service.impl;
//
//import java.util.List;
//
//import com.tal.wangxiao.conan.common.service.AgentConfigService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.tal.wangxiao.conan.common.mapper.AgentConfigMapper;
//import com.tal.wangxiao.conan.common.domain.AgentConfig;
//
//
///**
// * work机管理Service业务层处理
// *
// * @author dengkunan
// * @date 2020-12-23
// */
//@Service
//public class AgentConfigServiceImpl implements AgentConfigService
//{
//    @Autowired
//    private AgentConfigMapper agentConfigMapper;
//
//    /**
//     * 查询work机管理
//     *
//     * @param id work机管理ID
//     * @return work机管理
//     */
//    @Override
//    public AgentConfig selectAgentConfigById(Integer id)
//    {
//        return agentConfigMapper.selectAgentConfigById(id);
//    }
//
//    /**
//     * 查询work机管理列表
//     *
//     * @param agentConfig work机管理
//     * @return work机管理
//     */
//    @Override
//    public List<AgentConfig> selectAgentConfigList(AgentConfig agentConfig)
//    {
//        return agentConfigMapper.selectAgentConfigList(agentConfig);
//    }
//
//    /**
//     * 新增work机管理
//     *
//     * @param agentConfig work机管理
//     * @return 结果
//     */
//    @Override
//    public int insertAgentConfig(AgentConfig agentConfig)
//    {
//        return agentConfigMapper.insertAgentConfig(agentConfig);
//    }
//
//    /**
//     * 修改work机管理
//     *
//     * @param agentConfig work机管理
//     * @return 结果
//     */
//    @Override
//    public int updateAgentConfig(AgentConfig agentConfig)
//    {
//        return agentConfigMapper.updateAgentConfig(agentConfig);
//    }
//
//    /**
//     * 批量删除work机管理
//     *
//     * @param ids 需要删除的work机管理ID
//     * @return 结果
//     */
//    @Override
//    public int deleteAgentConfigByIds(Integer[] ids)
//    {
//        return agentConfigMapper.deleteAgentConfigByIds(ids);
//    }
//
//    /**
//     * 删除work机管理信息
//     *
//     * @param id work机管理ID
//     * @return 结果
//     */
//    @Override
//    public int deleteAgentConfigById(Integer id)
//    {
//        return agentConfigMapper.deleteAgentConfigById(id);
//    }
//}