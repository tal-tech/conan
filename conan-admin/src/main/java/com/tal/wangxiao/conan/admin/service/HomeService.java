package com.tal.wangxiao.conan.admin.service;

import com.tal.wangxiao.conan.common.model.Result;

/**
 * 首页大盘服务
 *
 * @author huyaoguo
 * @date 2021/1/14
 **/
public interface HomeService {

    /**
     * 获取首页基础数据
     * @return
     */
    Result<Object> getAllCount(String cacheId);

    /**
     * 获取核心监控数据
     * @return
     */
    Result<Object> getImportantData(String cacheId);

    /**
     * 获取任务排行
     * @return
     */
    Result<Object> getTaskRank(Integer count,String cacheId);

    /**
     * 实时获取机器状态
     * @return
     */
    Result<Object> getAgentNode();

    /**
     * 各产品线监控数据
     * @return
     */
    Result<Object> getDepartmentData();

    /**
     * 平台打点
     * @return
     */
    Result<Object> addDot();
}
