package com.tal.wangxiao.conan.agent.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tal.wangxiao.conan.agent.cache.CodeCache;
import com.tal.wangxiao.conan.agent.service.ReplayService;
import com.tal.wangxiao.conan.common.constant.enums.HttpMethodConstants;
import com.tal.wangxiao.conan.common.constant.enums.TaskStatus;
import com.tal.wangxiao.conan.common.entity.db.*;
import com.tal.wangxiao.conan.common.exception.CustomException;
import com.tal.wangxiao.conan.common.exception.api.ApiException;
import com.tal.wangxiao.conan.common.exception.execution.TaskExecutionException;
import com.tal.wangxiao.conan.common.exception.record.RecordException;
import com.tal.wangxiao.conan.common.exception.replay.ReplayException;
import com.tal.wangxiao.conan.common.redis.RedisTemplateTool;
import com.tal.wangxiao.conan.common.repository.db.*;
import com.tal.wangxiao.conan.common.service.common.RuleCheckoutService;
import com.tal.wangxiao.conan.common.utils.HttpUtil;
import com.tal.wangxiao.conan.utils.enumutils.EnumUtil;
import com.tal.wangxiao.conan.utils.http.CurlUtils;
import com.tal.wangxiao.conan.utils.json.JsonChangesUtils;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 流量回放服务实现类
 *
 * @author huyaoguo
 * @date 2021/1/6
 **/
@Service
@Slf4j
public class ReplayServiceImpl implements ReplayService {

    @Resource
    private RedisTemplateTool redisTemplateTool;

    @Resource
    private TaskExecutionRepository taskExecutionRepository;

    @Resource
    private RecordResultRepository recordResultRepository;

    @Resource
    private ReplayRepository replayRepository;

    @Resource
    private ReplayDetailRepository replayDetailRepository;

    @Resource
    private ApiRepository apiRepository;

    @Resource
    private DomainRepository domainRepository;

    @Resource
    private DomainAuthRepository domainAuthRepository;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Value("${system.replay.thread-count}")
    private int threadCount;

    @Value("${system.replay.timeout}")
    private int replayTimeout;
    @Resource
    RuleCheckoutService ruleCheckoutService;

    @Override
    public void replay(Integer taskExecutionId, Integer recordId, Integer replayId) throws Exception {
        redisTemplateTool.setReplayProgress(replayId, "1");
        log.info("开始执行回放, 回放ID：replay_id={},task_execution_id={},record_id={}", replayId, taskExecutionId, recordId);
        redisTemplateTool.setLogByReplayId_START(replayId, "开始执行回放, 回放ID：replay_id=" + replayId + ",task_execution_id=" + taskExecutionId + ",record_id=" + recordId);
        Optional<TaskExecution> taskExecutionOptional = taskExecutionRepository.findById(taskExecutionId);
        if (!taskExecutionOptional.isPresent()) {
            log.error("无效的taskExecutionId {},无法进行回放操作", taskExecutionId);
            throw new TaskExecutionException("无效的taskExecutionId " + taskExecutionId + ",无法进行回放操作", TaskExecutionException.Code.NO_EXECUTION_EXISTS);
        }
        TaskExecution taskExecution = taskExecutionOptional.get();
        List<RecordResult> recordResultList = recordResultRepository.findByRecordId(recordId);
        Map<Integer, Integer> totalActualCountMap = Maps.newHashMap();
        if (Objects.isNull(recordResultList) || recordResultList.isEmpty()) {
            log.error("未找到录制结果实体：record_id = {}", recordId);
            throw new RecordException("未找到录制结果实体：record_id = " + recordId, RecordException.Code.NO_RECORD_DETAIL_EXISTS);
        }
        List<Future<Map<Integer, Integer>>> futureList = Lists.newArrayList();
        Integer subListLength = recordResultList.size() / threadCount;
        ReplayServiceImpl replayServiceAgent = (ReplayServiceImpl) AopContext.currentProxy();
        redisTemplateTool.setReplayProgress(replayId, "9");
        if (subListLength > 0) {
            for (int i = 0; i < threadCount; i++) {
                Future<Map<Integer, Integer>> mapFuture = replayServiceAgent.doReplay(i, recordId, replayId, recordResultList.subList(i * subListLength, (i + 1) * subListLength));
                futureList.add(mapFuture);
                log.info("线程#" + i + "已创建");
            }
        }
        redisTemplateTool.setReplayProgress(replayId, "15");
        if (threadCount * subListLength < recordResultList.size()) {
            //请求流量小于线程最大条数，不需异步回放
            Future<Map<Integer, Integer>> mapFuture = replayServiceAgent.doReplay(1, recordId, replayId, recordResultList.subList(threadCount * subListLength, recordResultList.size()));
            futureList.add(mapFuture);
        }
        redisTemplateTool.setReplayProgress(replayId, "35");
        //汇总各线程实际回放流量条数
        for (Future<Map<Integer, Integer>> future : futureList) {
            int tick = 0;
            while (!future.isDone() && !future.isCancelled()) {
                Thread.sleep(1000);
                if (++tick >= replayTimeout) {
                    //把对应的回放ID的日志写入Redis, key='logByreplayId='+replayId
                    log.error("流量回放执行超时");
                    throw new ReplayException("流量回放执行超时", ReplayException.Code.REPLAY_TIMEOUT);
                }
            }
            redisTemplateTool.setReplayProgress(replayId, "50");
            try {
                Map<Integer, Integer> actualCountMap = future.get();
                for (Integer key : actualCountMap.keySet()) {
                    if (actualCountMap.get(key) >= 0) {
                        if (totalActualCountMap.containsKey(key)) {
                            totalActualCountMap.put(key, totalActualCountMap.get(key) + actualCountMap.get(key));
                        } else {
                            totalActualCountMap.put(key, actualCountMap.get(key));
                        }
                    }
                }
            } catch (Exception e) {
                log.error("replayServiceImpl future error,msg=" + e.getMessage());
                throw new ReplayException("流量回放线程异常", ReplayException.Code.REPLAY_THREAD_ERROR);
            }
        }
        redisTemplateTool.setReplayProgress(replayId, "80");
        //更新replay_detail表
        Integer totalExpectCount = 0, totalActualCount = 0;
        //统计多个回放线程的结果
        for (Integer apiId : totalActualCountMap.keySet()) {
            Optional<Api> apiOptional = apiRepository.findById(apiId);
            if (!apiOptional.isPresent()) {
                log.error("未找到接口实体，api_id = " + apiId + ",该接口不进行回放");
                throw new ApiException("未找到接口实体, apiId = " + apiId, ApiException.Code.NO_API_EXISTS);
            }
            Optional<ReplayDetail> replayDetailOptional = replayDetailRepository.findByReplayIdAndApiId(replayId, apiId);
            if (!replayDetailOptional.isPresent()) {
                log.error("未找到回放详情实体，replay_id = " + replayId + ", apiId = " + apiId);
                throw new ReplayException("未找到回放详情实体，replay_id = " + replayId + ", apiId = " + apiId, ReplayException.Code.NOT_FOUND_REPLAY_DETAIL);
            }
            ReplayDetail replayDetail = replayDetailOptional.get();
            //每个接口的期望回放数和实际回放数
            Integer oneExpectCount = replayDetail.getExpectCount();
            Integer oneActualCount = totalActualCountMap.get(apiId);
            if (Objects.isNull(oneActualCount)) {
                oneActualCount = 0;
            }
            replayDetail.setActualCount(oneActualCount);
            totalExpectCount += replayDetail.getExpectCount();
            totalActualCount += replayDetail.getActualCount();
            replayDetailRepository.save(replayDetail);
            redisTemplateTool.setLogByReplayId_INFO(replayId, "url = " + apiOptional.get().getName() + ", 期望流量回放总数: " + oneExpectCount + ", 实际流量总数: " + oneActualCount + " ,成功率: " + oneActualCount * 100.0 / oneExpectCount);
        }
        redisTemplateTool.setReplayProgress(replayId, "95");
        Optional<Replay> replayOptional = replayRepository.findById(replayId);
        if (!replayOptional.isPresent()) {
            throw new ReplayException("未找到回放实体，replay_id = " + replayId, ReplayException.Code.NO_REPLAY_EXISTS);
        }
        Replay replay = replayOptional.get();
        replay.setEndAt(LocalDateTime.now());
        //回放成功率
        double successRate = 0;
        if (totalExpectCount <= 0) {
            successRate = 0;
        } else {
            successRate = (totalActualCount * 100.0) / totalExpectCount;
        }
        replay.setSuccessRate(successRate);
        replayRepository.save(replay);

        redisTemplateTool.setLogByReplayId_INFO(replayId, "本次任务回放接口数" + totalActualCountMap.size() + ", 期望流量回放总数: " + totalExpectCount + ", 实际流量总数: " + totalActualCount + " ,成功率: " + successRate);
        taskExecution.setStatus(TaskStatus.REPLAY_SUCCESS.getValue());
        taskExecutionRepository.save(taskExecution);
        redisTemplateTool.setLogByReplayId_END(replayId, "回放replay_id = " + replayId + "的过程已完成 成功率" + successRate);
        log.info("本次任务回放接口数" + totalActualCountMap.size() + ", 期望流量回放总数: " + totalExpectCount + ", 实际流量总数: " + totalActualCount + " ,成功率: " + successRate);
        log.info("回放结束");
    }

    @Async
    public Future<Map<Integer, Integer>> doReplay(int i, Integer recordId, Integer replayId, List<RecordResult> recordResultList) {
        log.info("线程{}执行回放", i);
        Map<Integer, Integer> actualCountMap = Maps.newHashMap();
        for (RecordResult recordResult : recordResultList) {
            //回放接口response
            String response = "";
            //日志消息
            String errMsg = "";
            //线上日志中id为内部字段 使用query查询
            Integer apiId = recordResult.getApiId();
            Optional<Api> apiOptional = apiRepository.findById(apiId);
            if (!apiOptional.isPresent()) {
                log.error("找不到该接口，apiId = {}", apiId);
                redisTemplateTool.setLogByReplayId_ERROR(replayId, "找不到该接口，apiId = " + apiId + " ,回放失败");
                continue;
            }
            Api api = apiOptional.get();
            Optional<Domain> domainOptional = domainRepository.findById(api.getDomainId());
            if (!domainOptional.isPresent()) {
                log.error("找不到该域名, domainId = {} ,apiId = {}", api.getDomainId(), api.getId());
                redisTemplateTool.setLogByReplayId_ERROR(replayId, "找不到该域名，domainId = " + api.getDomainId() + ", apiId = " + api.getId() + " ,回放失败");
                continue;
            }
            String domainName = domainOptional.get().getName();
            String method = EnumUtil.getByField(HttpMethodConstants.class, "getValue", String.valueOf(api.getMethod())).getLabel();
            String urlStr = "http://" + domainName + api.getName();
            String body = StringHandlerUtils.replaceSpeStr(recordResult.getBody());
            String oldBody = body;
            Map<String, Object> headerMap = new HashMap<>();
            //针对域名的鉴权逻辑
            Optional<DomainAuth> domainAuthOptional = domainAuthRepository.findByDomainId(api.getDomainId());
            if (domainAuthOptional.isPresent()) {
                DomainAuth domainAuth = domainAuthOptional.get();
                String cookieResponse = "";
                try {
                    //1-body中jsonPath获取，2-header头获取
                    if (domainAuth.getKeyType() == 1) {
                        cookieResponse = CurlUtils.getBodyByCurl(domainAuth.getCurlUrl());
                        cookieResponse = JsonChangesUtils.getValueByJsonPath(cookieResponse, domainAuth.getResponseGetCookieKey());
                    } else if (domainAuth.getKeyType() == 2) {
                        cookieResponse = CurlUtils.getCookieByCurl(domainAuth.getCurlUrl(), domainAuth.getResponseGetCookieKey());
                    }
                    headerMap.put("Cookie",cookieResponse);
                    domainAuth.setCookie(cookieResponse);
                    domainAuth.setUpdateTime(LocalDateTime.now());
                    domainAuthRepository.save(domainAuth);
                } catch (Exception e) {
                    log.error("该域名{}获取cookie信息异常", domainName);
                }
            }
            try {
                response = replayByHttp(urlStr, method, body, headerMap);
                log.info("回放url={},method={},body={},response={}", urlStr, method, body, response);
                redisTemplateTool.setLogByReplayId_INFO(replayId, "回放url=" + urlStr + ",body=" + body + ",response=" + response);
            } catch (ReplayException rest) {
                response = rest.getMessage();
                errMsg = "url=" + urlStr + ", body=" + body + ",回放异常：" + rest.getMessage();
                log.error(errMsg);
                redisTemplateTool.setLogByReplayId_ERROR(replayId, errMsg);
            }

            if (!Objects.isNull(response)) {
                try {
                    ruleCheckoutService.checketRule(apiId, replayId, headerMap.toString() + "",body, response);
                } catch (CustomException e) {
                    log.error("精准比对发现异常 CustomException" + e);
                } catch (Exception e) {
                    log.error("精准比对发现异常" + e);
                }
            }
            //回放校验开发
            if (actualCountMap.containsKey(apiId)) {
                actualCountMap.put(apiId, actualCountMap.get(apiId) + 1);
            } else {
                actualCountMap.put(apiId, 1);
            }
            String param = "";//用于存储get请求参数
            if ("GET".equals(method) && urlStr.contains("?")) {
                param = urlStr.substring(urlStr.indexOf("?") + 1);
                insertDataWithRedis(recordResult.getRequestId(), recordId, response, replayId, recordResult.getApiId(), urlStr, param, param);
            } else {
                //回放结果写入Redis，key = 'requestId-recordId-replayId+apiid'
                insertDataWithRedis(recordResult.getRequestId(), recordId, response, replayId, recordResult.getApiId(), urlStr, oldBody, body);
            }

        }
        return new AsyncResult<>(actualCountMap);
    }

    //不知道流量的回放请求方式，采用多次重试请求
    public String replayByHttp(String url, String method, String body, Map<String, Object> headerMap) throws ReplayException {
        //响应结果
        String response = "";
        try {
            response = HttpUtil.request(HttpMethod.valueOf(method), url, body, headerMap, String.class, HttpUtil.Type.JSON);
        } catch (RestClientException e1) {
            log.error("该接口请求可能是非json请求 ，将采用form请求重试,err_msg = " + e1.getMessage());
            try {
                response = HttpUtil.request(HttpMethod.valueOf(method), url, body, headerMap, String.class, HttpUtil.Type.FORM);
            } catch (RestClientException e2) {
                log.error("该接口请求可能是非json请求|form请求 ，将采用默认请求重试,err_msg = " + e2.getMessage());
                try {
                    response = HttpUtil.request(HttpMethod.valueOf(method), url, body, headerMap, String.class, HttpUtil.Type.PARAM);
                } catch (RestClientException e3) {
                    log.error("该接口请求重试异常 ，请求失败,err_msg = " + e3.getMessage());
                    throw new ReplayException(e1.getMessage(), ReplayException.Code.REPLAY_REQUEST_FAIL);
                }
            }
        }
        /*// 响应response 非JSON 或null,异常重试Form表单请求
        if (StringUtils.isEmpty(response)) {
            log.info("该接口请求可能是非json请求 ，将采用form请求重试");
            response = HttpUtil.request(HttpMethod.valueOf(method), url, body, headerMap, String.class, HttpUtil.Type.FORM);
            //异常重试 Param请求
            if (StringUtils.isEmpty(response)) {
                log.info("该接口请求可能是非json请求|form请求 ，将采用默认请求重试");
                response = HttpUtil.request(HttpMethod.valueOf(method), url, body, headerMap, String.class, HttpUtil.Type.PARAM);
            }
        }*/
        return response;
    }

    //结果写入Redis
    public void insertDataWithRedis(String requestId, Integer recordId, String response, Integer replayId, Integer apiId, String urlStr, String oldBody, String body) {
        //字符unicode处理
        response = StringHandlerUtils.unicodeToString(response);
        String key = requestId + "-" + recordId + "-"
                + replayId + "-" + apiId;
        if (!Objects.isNull(response)) {
            try {
                redisTemplate.opsForValue().set(key, response, CodeCache.getRedisCacheTime(), TimeUnit.DAYS);
                redisTemplate.opsForValue().set(key + "-body", "requestId=" + requestId + "\n" + "原body=" + oldBody + "\n" + "新body=" + body);
                log.info("返回结果写入redis: key = " + key);
            } catch (Exception e) {
                //把对应的回放ID的日志写入Redis, key='logByreplayId='+replayId
                redisTemplateTool.setLogByReplayId_ERROR(replayId, "回放接口写入失败 , 接口地址： " + urlStr + ", 原因：redis存储异常");
                log.error("返回结果写入redis: key = " + key + ", 接口地址： " + urlStr + " redis存储异常");
            }
        } else {
            //把对应的回放ID的日志写入Redis, key='logByreplayId='+replayId
            redisTemplateTool.setLogByReplayId_ERROR(replayId, "回放接口写入失败 接口地址： " + urlStr + ", 原因 response 为null");
            log.error("返回结果写入redis: key = " + " 写入失败 接口地址： " + urlStr + ", 原因 response 为null");
        }
    }
}
