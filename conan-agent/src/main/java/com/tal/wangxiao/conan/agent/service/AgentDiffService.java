package com.tal.wangxiao.conan.agent.service;

/**
 * Agent diff service
 *
 * @author dengkunan
 * @date 2020-12-21
 */
public interface AgentDiffService {

	
	void startDiff(Integer taskExcutionId,Integer recordId,Integer replayId,Integer diffId) throws Exception;
}
