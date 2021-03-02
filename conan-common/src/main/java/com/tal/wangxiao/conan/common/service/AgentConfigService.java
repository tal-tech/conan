//package com.tal.wangxiao.conan.common.service;
//
//import java.util.List;
//import com.tal.wangxiao.conan.common.domain.AgentConfig;
//
///**
// * work机管理Service接口
// *
// * @author dengkunan
// * @date 2020-12-23
// */
//public interface AgentConfigService
//{
//    /**
//     * 查询work机管理
//     *
//     * @param id work机管理ID
//     * @return work机管理
//     */
//    public AgentConfig selectAgentConfigById(Integer id);
//
//    /**
//     * 查询work机管理列表
//     *
//     * @param agentConfig work机管理
//     * @return work机管理集合
//     */
//    public List<AgentConfig> selectAgentConfigList(AgentConfig agentConfig);
//
//    /**
//     * 新增work机管理
//     *
//     * @param agentConfig work机管理
//     * @return 结果
//     */
//    public int insertAgentConfig(AgentConfig agentConfig);
//
//    /**
//     * 修改work机管理
//     *
//     * @param agentConfig work机管理
//     * @return 结果
//     */
//    public int updateAgentConfig(AgentConfig agentConfig);
//
//    /**
//     * 批量删除work机管理
//     *
//     * @param ids 需要删除的work机管理ID
//     * @return 结果
//     */
//    public int deleteAgentConfigByIds(Integer[] ids);
//
//    /**
//     * 删除work机管理信息
//     *
//     * @param id work机管理ID
//     * @return 结果
//     */
//    public int deleteAgentConfigById(Integer id);
//}