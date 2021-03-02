package com.tal.wangxiao.conan.admin.service.impl;

import com.tal.wangxiao.conan.admin.cache.AdminCache;
import com.tal.wangxiao.conan.admin.service.DiffService;
import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.constant.enums.TaskStatus;
import com.tal.wangxiao.conan.common.converter.ConvertUtil;
import com.tal.wangxiao.conan.common.converter.DiffDetailDetailConverter;
import com.tal.wangxiao.conan.common.domain.*;
import com.tal.wangxiao.conan.common.entity.db.Record;
import com.tal.wangxiao.conan.common.entity.db.Replay;
import com.tal.wangxiao.conan.common.entity.db.DiffDetail;
import com.tal.wangxiao.conan.common.entity.db.TaskExecution;
import com.tal.wangxiao.conan.common.exception.CustomException;
import com.tal.wangxiao.conan.common.kafaka.*;
import com.tal.wangxiao.conan.common.mapper.ApiMapper;
import com.tal.wangxiao.conan.common.mapper.DiffDetailMapper;
import com.tal.wangxiao.conan.common.mapper.DiffMapper;
import com.tal.wangxiao.conan.common.model.DiffResultInRedis;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.vo.DiffDetailVO;
import com.tal.wangxiao.conan.common.redis.RedisTemplateTool;
import com.tal.wangxiao.conan.common.repository.db.DiffDetailRepository;
import com.tal.wangxiao.conan.common.repository.db.DiffRepository;
import com.tal.wangxiao.conan.common.repository.db.RecordRepository;
import com.tal.wangxiao.conan.common.repository.db.ReplayRepository;
import com.tal.wangxiao.conan.common.service.common.AgentCommonService;
import com.tal.wangxiao.conan.common.service.common.KafkaMessageService;
import com.tal.wangxiao.conan.sys.common.utils.DateUtils;
import com.tal.wangxiao.conan.sys.framework.web.service.TokenService;
import com.tal.wangxiao.conan.utils.json.JsonChangesUtils;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 比对记录Service业务层处理
 *
 * @author dengkunan
 * @date 2021-01-08
 */
@Slf4j
@Service
public class DiffServiceImpl implements DiffService {

    @Resource
    private DiffMapper diffMapper;

    @Resource
    private TokenService tokenService;

    @Resource
    private AgentCommonService agentCommonService;

    @Resource
    private KafkaMessageService kafkaMessageService;

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private DiffRepository diffRepository;


    @Resource
    private RedisTemplateTool redisTemplateTool;


    @Resource
    private RedisTemplate<Object,Object> redisTemplateLog;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    ReplayRepository replayRepository;

    @Resource
    private RecordRepository recordRepository;

    @Resource
    DiffDetailRepository diffDetailRepository;


    /**
     * 查询比对记录
     *
     * @param id 比对记录ID
     * @return 比对记录
     */
    @Override
    public Diff selectDiffById(Integer id) {
        return diffMapper.selectDiffById(id);
    }

    /**
     * 查询比对记录列表
     *
     * @param diff 比对记录
     * @return 比对记录
     */
    @Override
    public List<Diff> selectDiffList(Diff diff) {
        return diffMapper.selectDiffList(diff);
    }

    @Override
    public Result<Object> startDiff(Integer replayId,Integer operateBy) throws CustomException {
        Optional<Replay> replayOptional = replayRepository.findById(replayId);
        if(!replayOptional.isPresent()){
            return new Result<>(ResponseCode.INVALID_REPLAY_ID, "没有该回放记录replayId："+replayId);
        }
        Replay replay = replayOptional.get();
        Integer taskExecutionId = replay.getTaskExecutionId();
        Integer recordId = replay.getRecordId();
        TaskExecution taskExecution = diffMapper.getApiResponseById(taskExecutionId);
        if (StringHandlerUtils.isNull(taskExecution)) {
            return new Result<>(ResponseCode.INVALID_TASK_EXECUTION_ID, "无效的任务执行Id："+taskExecutionId);
        }
        if (taskExecution.getStatus() < TaskStatus.RECORD_SUCCESS.getValue() || TaskStatus.REPLAY_FAIL.getValue().equals(taskExecution.getStatus())) {
            return new Result<>(ResponseCode.INVALID_DIFF_ID, taskExecution.getStatus().toString());
        }
        //必须有基准消息才能进行比对taskExecutionId
        Integer baseCount = replayRepository.countByTaskExecutionIdAndIsBaseline(taskExecutionId,true);
        if (baseCount < 1) {
            return new Result<>(ResponseCode.INVALID_TASK_EXECUTION_ID, "taskExecutionId(" + taskExecutionId + ")没有基准比对信息");
        }
        Diff diff = new Diff();
        diff.setTaskExecutionId(taskExecutionId);
        diff.setRecordId(recordId);
        diff.setCreateBy(operateBy);
        diff.setCreateTime(DateUtils.getNowDate());
        diff.setReplayId(replayId);
        diffMapper.insertDiff(diff);
        return sendAgent(taskExecutionId, recordId, replayId, diff);

    }


    private Result<Object> sendAgent(Integer taskExecutionId, Integer recordId, Integer replayId, Diff diff) {

        KafkaTaskData kafkaTaskData = new KafkaTaskData();
        kafkaTaskData.setTaskExecutionId(taskExecutionId);
        kafkaTaskData.setDiffId(diff.getDiffId());
        kafkaTaskData.setRecordId(recordId);
        kafkaTaskData.setReplayId(replayId);
        String agentId = agentCommonService.getAgentId(AdminCache.getEnv());
        kafkaTaskData.setAgentId(agentId);
        log.info("下发回放任务，agentId = {}, task_execution_id = {}, record_id = {}, replay_id = {}, diffID={}", agentId, taskExecutionId, recordId, replayId, diff.getDiffId());
        KafkaData<KafkaTaskData> kafkaData = new KafkaData<KafkaTaskData>();
        kafkaData.setType(KafkaType.DIFF);
        kafkaData.setRunEnv(AdminCache.getEnv());
        kafkaData.setData(kafkaTaskData);
        TaskMessage<KafkaTaskData> taskMessage = new TaskMessage<>();
        taskMessage.setTimestamp(System.currentTimeMillis());
        taskMessage.setData(kafkaData);
        log.info("消息写入,执行环境为: " + AdminCache.getEnv());
        kafkaMessageService.sendKafkaMessage(taskMessage, KafkaTopic.CONAN_TASK_DIST);
        return new Result<>(ResponseCode.SUCCESS, taskMessage);
    }


    /**
     * 获取 diff 结果列表
     * @param replayId
     */
    @Override
    public Result<Object> getDiffResultList(Integer replayId) {
        Optional<Replay> replayOptional = replayRepository.findById(replayId);
        if(!replayOptional.isPresent()){
            return new Result<>(ResponseCode.INVALID_REPLAY_ID, "没有该回放记录replayId："+replayId);
        }
        Replay replay = replayOptional.get();
        Integer taskExecutionId = replay.getTaskExecutionId();
        Optional<com.tal.wangxiao.conan.common.entity.db.Diff> diffOptional = diffRepository.findFirstByTaskExecutionIdAndReplayIdOrderByCreateTimeDesc(taskExecutionId,replayId);
        if(!diffOptional.isPresent()){
            return new Result<>(ResponseCode.INVALID_DIFF_ID, "该回放没有比对记录replayId："+replayId);
        }
        List<DiffDetail> diffDetailList = diffDetailRepository.findByDiffId(diffOptional.get().getId());
        if (diffDetailList.size()==0) {
            return new Result<>(ResponseCode.INVALID_DIFF_ID, "找不到对应的diff详情信息diffId: " + diffOptional.get().getId());
        }
        List<DiffDetailVO> diffDetailVOS = ConvertUtil.convert2List(diffDetailList, DiffDetailVO.class, new DiffDetailDetailConverter());
        return new Result<>(ResponseCode.SUCCESS,diffDetailVOS);
    }


    /**
     * 获取相同api不同requestId的diff详情
     * @param apiId          apiId
     * @param diffId         diffId
     */
    @Override
    public ApiResponse getDiffDetail( Integer apiId, Integer diffId) {
        Diff diff = diffMapper.selectDiffById(diffId);
        Integer taskExcutionId = diff.getTaskExecutionId();
        Integer replayId = diff.getReplayId();
        String diffResultkey = "";
        String compareData = "";
        String baseData = "";
        DiffResultInRedis diffResultInRedis = null;
        String requestBody = "";
        int baseDataReplayId = 1;
        List<String> apiRequestIdSet = diffMapper.getRecordRequestIdApiByTaskexcuteId(taskExcutionId, apiId);
        ApiInfo apiInfo = apiMapper.selectApiById(apiId);
        Optional<Record> recordOptional = recordRepository.findFirstByTaskExecutionId(taskExcutionId);
        if(!recordOptional.isPresent()){
            return new ApiResponse(new Result(ResponseCode.INVALID_TASK_EXECUTION_ID, "找不到有效的任务执行ID,taskExecutionId:"+taskExcutionId));
        }
        Integer recordId = recordOptional.get().getId();
        ApiDiffDetailInfo apiDiffDetailInfo = new ApiDiffDetailInfo();
        apiDiffDetailInfo.setDiffApiLogInfoList(new ArrayList<DiffApiLogInfo>());
        apiDiffDetailInfo.setDomainName(apiInfo.getDomainName());
        apiDiffDetailInfo.setApiName(apiInfo.getName());

        for (String requestId : apiRequestIdSet) {
            boolean flag = false;
            DiffApiLogInfo apiLogInfo = new DiffApiLogInfo();
            try {
                compareData = stringRedisTemplate.opsForValue().get(requestId + "-" + recordId + "-" + replayId + "-" + apiId)+"";
                baseData = stringRedisTemplate.opsForValue().get(requestId + "-" + recordId + "-" + baseDataReplayId + "-" + apiId)+"";
            } catch (Exception e) {
                return new ApiResponse(new Result(ResponseCode.INVALID_REDIS_KEY, requestId + "-" + recordId + "-" + baseDataReplayId + "-" + apiId));
            }
            if (null == compareData) {
                continue;
            }
            //diffResultkey = recordId + "-" + replayId + "-" + apiId + "-" + diffId + "-" + requestId;

            diffResultkey = recordId + "-" + replayId + "-" + apiId + "-" + diffId + "-" + requestId;
            try {
                Object obj = redisTemplateLog.opsForValue().get(diffResultkey);
                if (StringHandlerUtils.isNull(obj)) {
                    log.error("找不到数据iffResultkey=" + diffResultkey);
                    continue;
                }
                diffResultInRedis = (DiffResultInRedis) obj;
            } catch (Exception e) {
                log.error("redis key=" + diffResultkey + "无对应数据");
                log.error("e=" + e);
                continue;

            }
            //获取body信息
            try {
                requestBody = stringRedisTemplate.opsForValue().get(requestId + "-" + recordId + "-" + replayId + "-" + apiId + "-body")+"";
            } catch (Exception e) {
                log.error("获取body信息失败，无效的redis key");
            }
            if (null == diffResultInRedis) {
                continue;
            }
            apiLogInfo.setRequestId(requestId);
            // apiLogInfo.setCompareData(compareData);
            try {
                flag = JsonChangesUtils.isJson(compareData);
            } catch (Exception e) {
                flag = false;
            }
            if (flag) {
                apiLogInfo.setCompareData(compareData);
                apiLogInfo.setBaseData(baseData);
                apiLogInfo.setRequestBody(requestBody);
            } else {
                try {
                    apiLogInfo.setCompareData(compareData.trim().split("style")[0]);
                    apiLogInfo.setBaseData(baseData.trim().split("style")[0]);
                    apiLogInfo.setRequestBody(requestBody);
                } catch (Exception e) {
                    apiLogInfo.setCompareData("非Json暂不支持比对");
                    apiLogInfo.setBaseData("非Json暂不支持比对");
                    apiLogInfo.setRequestBody(requestBody);
                }
            }
            //String body = StringHandlerUtils.replaceSpeStr(requestBody);
            apiDiffDetailInfo.getDiffApiLogInfoList().add(apiLogInfo);

        }
        return ApiResponse.success("获取diff 详细信息成功", apiDiffDetailInfo);//.success("", resultMap);
    }

    @Override
    public Result<String> findDiffLog( Integer diffId) {
        String res = stringRedisTemplate.opsForValue().get("diffLog="+diffId);
        return new Result<>(ResponseCode.SUCCESS, res);
    }


/*    @Override
    //获取相同api不同requestId的diff详情
    public Result getDiffDetail(Integer taskExcutionId, Integer replayId, Integer apiId, Integer diffId)  {
        String diffResultkey = "";
        String compareData = "";
        String baseData = "";
        DiffResultInRedis diffResultInRedis = null;
        String requestBody = "";
        int baseDataReplayId = 1;
        boolean flag = false;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> detailList = new ArrayList<>();
        Optional<Replay> findRecordId = replayRepository.findById(replayId);
        if (!findRecordId.isPresent()) {
            return new Result(ResponseCode.INVALID_RECORD_ID, "replayId为" + replayId + "找不到对应replayId");
        }
        int recordId = findRecordId.get().getRecordId();
        Optional<Api> findById = apiRepository.findById(apiId);
        if (!findById.isPresent()) {
            return new Result(ResponseCode.INVALID_API_ID, "apiId " + apiId + "找不到对应api");
        }
        int domainID = findById.get().getDomainId();
        Optional<Domain> domain = domainRepository.findById(domainID);
        if (!domain.isPresent()) {
            return new Result(ResponseCode.INVALID_DOMAIN_ID, "domainId " + domainID + "找不到对应domain");
        }
        Optional<DiffDetail> diffDetail = diffDetailRepository.findByApiIdAndDiffId(apiId, diffId);
        if (Objects.isNull(diffDetail)) {
            return new Result(ResponseCode.INVALID_DIFF_ID, "diffId为" + diffId + "找不到对应api apiId为" + apiId);
        }
        resultMap.put("domain", domain.get().getName());
        resultMap.put("apiName", findById.get().getName());
        diffDetail.ifPresent(detail -> resultMap.put("successRate", MathUtils.getRate(detail.getActualCount(), detail.getExpectCount())));
        Optional<Replay> replayOptional = replayRepository.findByTaskExecutionIdAndIsBaseline(taskExcutionId, 1);
        if (!replayOptional.isPresent()) {
            return new Result(ResponseCode.INVALID_TASK_EXECUTION_ID, "replay表中taskExcutionId=" + taskExcutionId + "无baseline replayId");
        }
        baseDataReplayId = replayOptional.get().getId();
        //获取同一 record_id 相同api的request_ID set去重
        List<RecordResult> findByRecordIdAndApiId = recordResultRepository.findByRecordIdAndApiId(recordId, apiId);
        Set<String> apiRequestSet = new HashSet<String>();
        for (RecordResult requestResult : findByRecordIdAndApiId) {
            apiRequestSet.add(requestResult.getRequestId());
        }
        for (String requestId : apiRequestSet) {
            try {
                compareData = stringRedisTemplate.opsForValue().get(requestId + "-" + recordId + "-" + replayId + "-" + apiId);
                baseData = stringRedisTemplate.opsForValue().get(requestId + "-" + recordId + "-" + baseDataReplayId + "-" + apiId);
            } catch (Exception e) {
                return new Result(ResponseCode.INVALID_REDIS_KEY, requestId + "-" + recordId + "-" + baseDataReplayId + "-" + apiId);
            }
            if (null == compareData) {
                continue;
            }
            Map<String, Object> detailListMap = new HashMap<String, Object>();
            diffResultkey = recordId + "-" + replayId + "-" + apiId + "-" + diffId + "-" + requestId;
            try {
                //logger.error("template=" +redisTemplate);
                if(redisTemplate == null) {
                    logger.error("template= null" );
                }
                Object obj = redisTemplate.opsForValue().get(diffResultkey);
                if (StringHandlerUtils.isNull(obj)) {
                    logger.error("找不到数据iffResultkey=" +diffResultkey);
                    continue;
                }
                //logger.error("obj=" +obj);
                diffResultInRedis = (DiffResultInRedis) obj;
            } catch (Exception e) {
                logger.error("redis key=" + diffResultkey + "无对应数据");
                logger.error("e=" + e);

            }
            //获取body信息
            try {
                requestBody = stringRedisTemplate.opsForValue().get(requestId + "-" + recordId + "-" + replayId + "-" + apiId + "-body");
            } catch (Exception e) {
                logger.error("获取body信息失败，无效的redis key");
            }
            if (null != diffResultInRedis) {
                detailListMap.put("request_id", requestId);
                try {
                    flag = JsonUtils.isJson(compareData);
                } catch (Exception e) {
                    flag = false;
                }
                if (flag == false) {
                    try {
                        detailListMap.put("compareData", compareData.trim().split("style")[0]);
                        detailListMap.put("baseData", baseData.trim().split("style")[0]);
                        detailListMap.put("requestBody", requestBody);
                    } catch (Exception e) {
                        detailListMap.put("compareData", "非Json暂不支持比对");
                        detailListMap.put("baseData", "非Json暂不支持比对");
                        detailListMap.put("requestBody", requestBody);
                    }
                } else {
                    detailListMap.put("compareData", compareData);
                    detailListMap.put("baseData", baseData);
                    detailListMap.put("requestBody", requestBody);
                }
                String body = EsUtils.replaceSpeStr(requestBody);
                body = StringUtil.replaceBlank(requestBody);
                if (null == diffResultInRedis.getDiffMsg() || "" == diffResultInRedis.getDiffMsg() || "[ ]" == diffResultInRedis.getDiffMsg()) {
                    detailListMap.put("diffMsg", "无");
                } else {
                    detailListMap.put("diffMsg", diffResultInRedis.getDiffMsg());
                }
                detailList.add(detailListMap);
            }
        }
        resultMap.put("detailList", detailList);
        result.setDesc(resultMap);
        return result;
    }*/

    @Override
    public Result<Object> findDiffProgress(Integer diffId) {
        try {
            return new Result<>(ResponseCode.SUCCESS,redisTemplateTool.getDiffProgress(diffId));
        }catch (Exception e){
            return new Result<>(ResponseCode.REDIS_EXCEPTION,"获取比对进度异常："+e.getMessage());
        }
    }

    /**
     * 新增比对记录
     *
     * @param diff 比对记录
     * @return 结果
     */
    @Override
    public int insertDiff(Diff diff) {
        diff.setCreateTime(DateUtils.getNowDate());
        return diffMapper.insertDiff(diff);
    }

    /**
     * 修改比对记录
     *
     * @param diff 比对记录
     * @return 结果
     */
    @Override
    public int updateDiff(Diff diff) {
        diff.setUpdateTime(DateUtils.getNowDate());
        return diffMapper.updateDiff(diff);
    }

    /**
     * 批量删除比对记录
     *
     * @param ids 需要删除的比对记录ID
     * @return 结果
     */
    @Override
    public int deleteDiffByIds(Integer[] ids) {
        return diffMapper.deleteDiffByIds(ids);
    }

    /**
     * 删除比对记录信息
     *
     * @param id 比对记录ID
     * @return 结果
     */
    @Override
    public int deleteDiffById(Integer id) {
        return diffMapper.deleteDiffById(id);
    }
}