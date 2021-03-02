package com.tal.wangxiao.conan.agent.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.tal.wangxiao.conan.agent.cache.CodeCache;
import com.tal.wangxiao.conan.agent.model.DiffMode;
import com.tal.wangxiao.conan.agent.service.AgentDiffService;
import com.tal.wangxiao.conan.common.constant.enums.TaskStatus;
import com.tal.wangxiao.conan.common.domain.ApiInfo;
import com.tal.wangxiao.conan.common.domain.Diff;
import com.tal.wangxiao.conan.common.domain.DiffDetail;
import com.tal.wangxiao.conan.common.domain.TaskApiRelation;
import com.tal.wangxiao.conan.common.entity.db.RecordResult;
import com.tal.wangxiao.conan.common.entity.db.Replay;
import com.tal.wangxiao.conan.common.entity.db.TaskExecution;
import com.tal.wangxiao.conan.common.exception.BaseException;
import com.tal.wangxiao.conan.common.mapper.*;
import com.tal.wangxiao.conan.common.model.DiffResultInRedis;
import com.tal.wangxiao.conan.common.model.JsonKeyDiffResult;
import com.tal.wangxiao.conan.common.redis.RedisTemplateTool;
import com.tal.wangxiao.conan.common.repository.db.RecordResultRepository;
import com.tal.wangxiao.conan.common.repository.db.ReplayRepository;
import com.tal.wangxiao.conan.common.repository.db.TaskExecutionRepository;
import com.tal.wangxiao.conan.common.utils.task.TaskStatusUtil;
import com.tal.wangxiao.conan.utils.diff.TextDiffUtils;
import com.tal.wangxiao.conan.utils.diff.core.TextDiffResult;
import com.tal.wangxiao.conan.utils.json.JsonCheckUtils;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import com.tal.wangxiao.json.compare.comparator.JSONKeyCompare;
import com.tal.wangxiao.json.compare.result.JSONCompareResult;
import com.tal.wangxiao.json.compare.utils.JSONResultHandlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 流量Diff服务实现类
 */
@Slf4j
@Service
public class DiffServiceImpl implements AgentDiffService {

    @Resource
    private DiffMapper diffMapper;

    @Resource
    DiffDetailMapper diffDetailMapper;

    @Resource
    TaskMapper taskMapper;


    @Resource
    private RedisTemplate<Object, Object> template;


    @Resource
    private RecordResultRepository recordResultRepository;


    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Resource
    private RedisTemplateTool redisTemplateTool;

    @Resource
    private TaskStatusUtil taskStatusUtil;

    @Resource
    TaskApiRelationMapper taskApiRelationMapper;

    @Resource
    TaskExecutionRepository taskExecutionRepository;

    @Resource
    ReplayRepository replayRepository;


   /* @Resource
    private ApiDiffSchemeRelationRepository apiDiffSchemeRelationRepository;*/


    //执行diff对比
    //回放结果写入Redis，key = 'requestId-recordId-replayId-apiId'
    @Override
    public void startDiff(Integer taskExecutionId, Integer recordId, Integer replayId, Integer diffId) throws BaseException {
        DiffMode diffMode = initDiffMode(taskExecutionId, recordId, replayId, diffId);
        List<Integer> apiList = diffMapper.getRecordIdApiById(recordId);
        //根据record_id 在record_result表中获取 apiId list
        JsonKeyDiffResult jsonKeyDiffResult = new JsonKeyDiffResult();
        HashMap<Integer, Object> resultMap = new HashMap<Integer, Object>(apiList.size());
        log.info("api list id {}", apiList.toString());
        //初始化进度条
        int progress = 10;
        redisTemplateTool.setDiffProgress(diffId, "10");
        int growSize = 80 / apiList.size();//每次递增进度
        //遍历apiID diff后写入redis
        for (Integer apiId : apiList) {
            //获取同一 record_id 相同api的request_ID set去重
            List<RecordResult> findByRecordIdAndApiId = recordResultRepository.findByRecordIdAndApiId(recordId, apiId);
            Set<String> apiRequestSet = new HashSet<String>();
            for (RecordResult requestResult : findByRecordIdAndApiId) {
                apiRequestSet.add(requestResult.getRequestId());
            }
            List<HashMap> jsonCompareResultsList = new ArrayList<HashMap>(apiRequestSet.size());
            resultMap.put(apiId, jsonCompareResultsList);
            int count = 0;
            for (String requestId : apiRequestSet) {
                count++;
                //回放的json redis key
                String compreDataKey = requestId + "-" + recordId + "-" + replayId + "-" + apiId;
                log.info("compreDataKey= {}", compreDataKey);
                //基准json redis key
                //baseJson 在replay表中有字段标记 应该task_excution_id 对应唯一的一个replay_id作为基准json
                String BaseDataKey = requestId + "-" + recordId + "-" + diffMode.getBaseReplayId() + "-" + apiId;
                log.info("BaseDataKey=" + BaseDataKey);
                //获取diff规则
                String compareJson = strReplceHandler(stringRedisTemplate.opsForValue().get(compreDataKey));
                String baseJson = strReplceHandler(stringRedisTemplate.opsForValue().get(BaseDataKey));
                //response is JSON, compareJson 和 BaseDataKey 不是JSON
                if (diffMode.getTaskApiRespnseIsJsonMap().get(apiId) == 0) {
                    if (!JsonCheckUtils.isJSONValidByalibaba(compareJson)) {
                        //不进行比对，直接记录比对失败
                        jsonKeyDiffResult.setEqual(false);
                        jsonKeyDiffResult.setTotalMsgCount(1);
                        jsonKeyDiffResult.setDiffMsgCount(1);
                        resultWriteRedis(jsonKeyDiffResult, diffMode, apiId, requestId);
                        continue;
                    }
                }

                if ((null == compareJson || "".equals(compareJson)) || (null == baseJson || "".equals(baseJson))) {
                    log.info("请检查对比json的redis key=" + compreDataKey + "与baseJson的redis key=" + BaseDataKey + "是否在redis中有Value");
                    redisTemplateTool.setLogByDiffId_ERROR(diffId, "请检查对比json的redis key=" + compreDataKey + "与baseJson的redis key=" + BaseDataKey + "是否在redis中有Value");
                    continue;
                }

                switch (diffMode.getTaskApiRelationMap().get(apiId).getDiffType()) {
                    case 0:
                        isLostArrayAndAddAnddiffJsonSchema(jsonKeyDiffResult, baseJson, compareJson, count, jsonCompareResultsList);
                        break;
                    case 1:
                        textDiffJsonSchema(jsonKeyDiffResult, baseJson, compareJson, jsonCompareResultsList);
                        break;
                    default:
                        isLostArrayAndAddAnddiffJsonSchema(jsonKeyDiffResult, baseJson, compareJson, count, jsonCompareResultsList);

                }

                resultWriteRedis(jsonKeyDiffResult, diffMode, apiId, requestId);
            }
            progress = progress + growSize;
            redisTemplateTool.setDiffProgress(diffId, String.valueOf(progress));
            editDiffTAndDetailTable(diffId, recordId, replayId, apiId, diffMode);
        }

        try {
            jsonCompareKeyHandler(resultMap, diffMode);
            redisTemplateTool.setDiffProgress(diffId, "95");
        } catch (Exception e) {
            log.error("diff 更新汇总数据异常 e=" + e);
        }
        editDiffTable(diffId, taskExecutionId);


    }

    private String strReplceHandler(String str) {
        if (StringHandlerUtils.isNull(str)) {
            return "";
        }
        if (str.trim().substring(0, 1).equals("\"")) {
            str = str.substring(1, str.length());
        }
        if (str.trim().substring(str.length() - 1, str.length()).equals("\"")) {
            str = str.substring(0, str.length() - 1);
        }
        str = str.replace("\\", "");
        return str;
    }


    private void resultWriteRedis(JsonKeyDiffResult jsonKeyDiffResult, DiffMode diffMode, Integer apiId, String requestId) {
        //向redis中写入结果
        if (Objects.isNull(jsonKeyDiffResult)) {
            return;
        }

        DiffResultInRedis diffResultInRedis = new DiffResultInRedis();
        diffResultInRedis.setAllKeysNu(jsonKeyDiffResult.getTotalMsgCount());
        diffResultInRedis.setSameKeyNu(jsonKeyDiffResult.getTotalMsgCount() - jsonKeyDiffResult.getDiffMsgCount());
        diffResultInRedis.setDiffResult(jsonKeyDiffResult.isEqual());
        //diffResultkey = recordId + "-" + replayId + "-" + apiId + "-" + diffId + "-" + requestId;
        String diffResultKey = diffMode.getRecordId() + "-" + diffMode.getReplayId() + "-" + apiId + "-" + diffMode.getDiffId() + "-" + requestId;
        log.info("diff结果写入redis redis key=" + diffResultKey);
        try {
            template.opsForValue().set(diffResultKey, diffResultInRedis, CodeCache.getRedisCacheTime(), TimeUnit.DAYS);
        } catch (Exception e) {
            //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
            redisTemplateTool.setLogByDiffId_ERROR(diffMode.getDiffId(), "diff结果写入redis失败可能没有比对日志 redis key=" + diffResultKey + ",errMsg = " + e.getMessage());
            log.error("diff结果写入redis失败 redis key=" + diffResultKey + ",errMsg = " + e.getMessage());
        }
    }


    /**
     * 文本比对
     */
    private void textDiffJsonSchema(JsonKeyDiffResult jsonKeyDiffResult, String baseJson, String compareJson, List<HashMap> jsonCompareResultsList) {
        TextDiffResult textDiffResult = TextDiffUtils.stringDiff(baseJson, compareJson);
        jsonKeyDiffResult.setEqual(textDiffResult.isEqual());
        jsonKeyDiffResult.setTotalMsgCount(textDiffResult.getTotalMsgCount());
        jsonKeyDiffResult.setDiffMsgCount(textDiffResult.getDiffMsgCount());
    }

    /**
     * 允许丢失和新加数组，默认diff json 结构
     */
    private void isLostArrayAndAddAnddiffJsonSchema(JsonKeyDiffResult jsonKeyDiffResult, String baseJson, String compareJson, Integer count, List<HashMap> jsonCompareResultsList) {

        try {
            JSONCompareResult jsonCompareResult = new JSONCompareResult();
            jsonCompareResult = JSONKeyCompare.compareJSON_FFTTF(baseJson, compareJson);
            HashMap<Integer, JSONCompareResult> comparJSONMap = new HashMap<Integer, JSONCompareResult>();
            comparJSONMap.put(count, jsonCompareResult);
            jsonCompareResultsList.add(comparJSONMap);
            log.error("jsonCompareResult = " + jsonCompareResult);
            if (jsonCompareResult.isSuccess()) {
                jsonKeyDiffResult.setEqual(jsonCompareResult.isSuccess());
                jsonKeyDiffResult.setTotalMsgCount(1);
                jsonKeyDiffResult.setDiffMsgCount(0);
            } else {
                jsonKeyDiffResult.setEqual(false);
                jsonKeyDiffResult.setTotalMsgCount(1);
                jsonKeyDiffResult.setDiffMsgCount(1);
            }
        } catch (Exception e) {
            jsonKeyDiffResult.setEqual(false);
            jsonKeyDiffResult.setTotalMsgCount(1);
            jsonKeyDiffResult.setDiffMsgCount(1);
            log.error("e =" + e + "比对错误，请检查JSON，base=" + baseJson + "compareJson=" + compareJson);
        }
    }


    private void jsonCompareKeyHandler(HashMap<Integer, Object> resultMap, DiffMode diffMode) {

        ArrayList<Object> resultSetMeg = new ArrayList<>();
        for (Integer apiId : resultMap.keySet()) {
            List<HashMap> jsonCompareResultList = (List<HashMap>) resultMap.get(apiId);
            JSONObject jsonObject = JSONResultHandlerUtils.getSimpleCompareResultJSONObject(jsonCompareResultList);
            if (jsonObject.size() < 1) {
                continue;
            }
            log.info("jsonCompareResult.getMessage() = {}", jsonObject);
            HashMap map = new HashMap<String, Object>();
            map.put(diffMode.getTaskApiNameMap().get(apiId), jsonObject);
            resultSetMeg.add(map);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", resultSetMeg);
        try {
            Diff diffInfo = diffMapper.selectDiffById(diffMode.getDiffId());
            diffInfo.setDiffErrorMsg(jsonObject.toJSONString());
            log.info("jsonObject= {}", jsonObject.toJSONString());
            diffMapper.updateDiff(diffInfo);
        } catch (Exception e) {
            log.info("数据库更新异常 = ");
        }

    }


    //在redis中获取相同apiID 不同request的diff结果 计算后在diff_detail表中插入相关数据
    public void editDiffTAndDetailTable(Integer diffId, Integer recordId, Integer replayId, Integer apiId, DiffMode diffMode) throws BaseException {
        final String prefix = recordId + "-" + replayId + "-" + apiId + "-" + diffId + "-*";
        DiffDetail diffDetail = new DiffDetail();
        Set<String> keys = stringRedisTemplate.keys(prefix);
        //diff_detail 中的actual_count diff结果相同接口个数
        int actual_count = 0;
        //diff_detail 中的expect_count 接口总数
        float successRate = 0;
        int expect_count = 0;
        for (String key : keys) {
            log.error("key = " + key);
            Object obj = template.opsForValue().get(key);
            if (StringHandlerUtils.isNull(obj)) {
                log.error("redis 查询null");
                continue;
            }
            DiffResultInRedis diffResultInRedis = (DiffResultInRedis) obj;
            //diff_detail 中的actual_count2
            expect_count++;
            if (diffResultInRedis.isDiffResult() == true) {
                actual_count++;
            }
        }
        if (expect_count != 0) {
            diffDetail.setActualCount(actual_count);
            diffDetail.setApiId(apiId);
            diffDetail.setExpectCount(expect_count);
            diffDetail.setDiffId(diffId);
            try {
                diffDetailMapper.insertDiffDetail(diffDetail);
            } catch (Exception e) {
                redisTemplateTool.setLogByDiffId_INFO(diffId, "apiId为" + apiId + "diffId为" + diffId + "的数据存储失败，请检查");
                log.info("apiId为" + apiId + "diffId为" + diffId + "的数据存储失败，请检查");
            }
            successRate = (actual_count * 100.0f) / expect_count;
        } else {
            successRate = 0;
            redisTemplateTool.setLogByDiffId_INFO(diffId, "apiId为" + apiId + "diffId为" + diffId + "的对应流量为0，请检查redis中是否有diff结果");
            log.info("apiId为" + apiId + "diffId为" + diffId + "的对应流量为0，请检查redis中是否有diff结果");
        }
        String apiName = diffMode.getTaskApiNameMap().get(apiId);
        if (StringHandlerUtils.isNull(apiName)) {
            redisTemplateTool.setLogByDiffId_INFO(diffId, "比对完成，该api_id=" + apiId + "不存在");
        } else {
            redisTemplateTool.setLogByDiffId_INFO(diffId, "api=" + apiName + "比对完成 ,成功率 : " + successRate + "%");
        }
    }


    private DiffMode initDiffMode(Integer taskExecutionId, Integer recordId, Integer replayId, Integer diffId) throws BaseException {
        log.info("开始执行比对, diff_id = {}", diffId);
        redisTemplateTool.setDiffProgress(diffId, "1");
        DiffMode diffMode = new DiffMode();
        diffMode.setTaskExcutionId(taskExecutionId);
        diffMode.setRecordId(recordId);
        diffMode.setReplayId(replayId);
        diffMode.setDiffId(diffId);

        Optional<TaskExecution> taskExecutionOptional = taskExecutionRepository.findById(taskExecutionId);
        if (!taskExecutionOptional.isPresent()) {
            log.error("找不到执行记录请检查, task_execution_id =" + taskExecutionId);
            throw new BaseException("找不到执行记录请检查, task_execution_id =" + taskExecutionId);
        }
        Integer taskId = taskExecutionOptional.get().getTaskId();
        redisTemplateTool.setLogByDiffId_START(diffId, "开始执行比对, diff_id = " + diffId);
        //必须有基准消息才能进行比对taskExecutionId
        List<Replay> replayList = replayRepository.findAllByTaskExecutionIdAndIsBaseline(taskExecutionId, true);
        if (replayList.size() != 1) {
            log.error("基准比对信息异常, 基准的size = " + replayList.size() + ",taskExecutionId = " + taskExecutionId);
            throw new BaseException("基准比对信息异常, 基准的size = " + replayList.size() + ",taskExecutionId = " + taskExecutionId);
        }
        Replay baseReplay = replayList.get(0);
        Integer baseReplayId = baseReplay.getId();
        redisTemplateTool.setLogByDiffId_INFO(diffId, "此次对比信息 replay_id = " + replayId + "基准回放,base replay_id=" + baseReplayId);
        redisTemplateTool.setDiffProgress(diffId, "2");
        List<RecordResult> recordResultList = recordResultRepository.findByRecordId(recordId);
        if (null == recordResultList || recordResultList.isEmpty()) {
            log.error("recordId" + recordId + "无对应requestId");
            throw new BaseException("recordId" + recordId + "无对应requestId");
        }
        diffMode.setTaskId(taskId);
        diffMode.setBaseReplayId(baseReplayId);
        TaskApiRelation taskApiRelation = new TaskApiRelation();
        taskApiRelation.setTaskId(taskId);
        List<TaskApiRelation> taskApiRelationList = taskApiRelationMapper.selectTaskApiRelationList(taskApiRelation);
        HashMap<Integer, TaskApiRelation> taskApiRelationMap = new HashMap<Integer, TaskApiRelation>();
        for (TaskApiRelation taskApiRelationDb : taskApiRelationList) {
            taskApiRelationMap.put(taskApiRelationDb.getApiId(), taskApiRelationDb);
        }
        diffMode.setTaskApiRelationMap(taskApiRelationMap);

        HashMap<Integer, Integer> taskApiResponseIsJsonMap = new HashMap<>();
        HashMap<Integer, String> taskApiNameMap = new HashMap<>();
        List<ApiInfo> requestIdApiList = diffMapper.getApiInfoById(recordId);
        for (ApiInfo apiInfo : requestIdApiList) {
            taskApiResponseIsJsonMap.put(apiInfo.getApiId(), apiInfo.getResponseIsJson());
            taskApiNameMap.put(apiInfo.getApiId(), apiInfo.getName());
        }
        diffMode.setTaskApiRespnseIsJsonMap(taskApiResponseIsJsonMap);
        diffMode.setTaskApiNameMap(taskApiNameMap);
        return diffMode;
    }


    //更新diff表中数据
    public void editDiffTable(int diffId, Integer taskExecutionId) throws BaseException {
        Diff diff = diffMapper.selectDiffById(diffId);
        if (diff == null) {
            //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
            redisTemplateTool.setLogByDiffId_INFO(diffId, "请查看在diff表中是否有相关数据" + diffId);
            redisTemplateTool.setLogByDiffId_ERROR(diffId, "对比结束(end)");
            throw new BaseException("请查看在diff表中是否有相关数据" + diffId);
        }
        //获取diffId对应的所有api对比结果  所有actual/所有expect
        DiffDetail diffDetailCondition = new DiffDetail();
        diffDetailCondition.setDiffId(diffId);
        List<DiffDetail> diffDetailList = diffDetailMapper.selectDiffDetailList(diffDetailCondition);
        int totalActual = 0;
        int totalExpect = 0;
        for (DiffDetail diffDetail : diffDetailList) {
            totalActual += diffDetail.getActualCount();
            totalExpect += diffDetail.getExpectCount();
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        double success_rate = 0;
        if (totalExpect != 0) {
            success_rate = (totalActual * 100.0) / totalExpect;
        }
        log.info("总比对条数为" + totalExpect + ",比对成功条数为" + totalActual);
        diff.setUpdateTime(new Date());
        diff.setSuccessRate(success_rate);
        diffMapper.updateDiff(diff);
        redisTemplateTool.setLogByDiffId_INFO(diffId, "当前比对任务diffId=" + diffId + "的比对任务成功执行，成功率为" + success_rate + "%");
        redisTemplateTool.setLogByDiffId_ERROR(diffId, "对比结束(end)");
        taskStatusUtil.updateTaskStatus(taskExecutionId, TaskStatus.DIFF_SUCCESS);

    }



    /*    private void jsonCompareKeyHandler(HashMap<Integer, Object> resultMap, Integer diffId) {

        ArrayList<Object> resultSetMeg = new ArrayList<>();
        for (Integer apiId : resultMap.keySet()) {
            List<HashMap> jsonCompareResultList = (List<HashMap>) resultMap.get(apiId);
            JSONObject jsonObject = JSONResultHandlerUtils.getSimpleCompareResultJSONObject(jsonCompareResultList);
            if (jsonObject.size() < 1) {
                continue;
            }
            logger.info("jsonCompareResult.getMessage() = " + jsonObject);
            try {
                String apiName = diffDao.getApiNAme(apiId);
                if (StringHandlerUtils.isNull(apiName)) {
                    continue;
                }
                HashMap map = new HashMap<String, Object>();
                map.put(apiName, jsonObject);
                resultSetMeg.add(map);

            } catch (Exception e) {
                logger.info("数据库未发现apiId = " + apiId);
                continue;
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", resultSetMeg);
        try {
            DiffInfo diffInfo = diffDao.getDiffInfoById(diffId);
            diffInfo.setDiffErrorMsg(jsonObject.toJSONString());
            logger.info("jsonObject= " + jsonObject.toJSONString());
            diffDao.updateById(diffInfo);
        } catch (Exception e) {
            logger.info("数据库更新异常 = ");
        }

    }*/


    //暂时舍弃，采用其他方式进行合并
   /* private void jsonCompareKeyHandler(HashMap<Integer, Object> resultMap, Integer diffId) {

        HashSet<String> resultSetMeg = new HashSet<>();
        for (Integer o : resultMap.keySet()) {
            List<JSONCompareResult> jsonCompareResultList = (List<JSONCompareResult>) resultMap.get(o);
            HashSet<String> resultSet = new HashSet<>();

            for (JSONCompareResult jsonCompareResult : jsonCompareResultList) {
                if (jsonCompareResult.isSuccess()) {
                    continue;
                }
                if (jsonCompareResult.getMessage() == null) {
                    continue;
                }
                String[] strNumber = jsonCompareResult.getMessage().toString().split(",");
                logger.info("jsonCompareResult.getMessage() = " + jsonCompareResult.getMessage());
                for (String errormeg : strNumber) {
                    logger.info("errormeg = " + errormeg);
                    if (errormeg.equals("Object Key丢失")) {
                        resultSet.add("key丢失");
                    } else if (errormeg.equals("Object Key字段新增")) {
                        resultSet.add("key新增");
                    } else {

                        resultSet.add(errormeg);
                    }
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            String meg = "";
            for (String str : resultSet) {
                stringBuilder.append(str).append(",");
            }
            if (resultSet.size() > 0) {
                meg = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
                try {
                    String apiName=  diffDao.getApiNAme(o);
                    if(StringHandlerUtils.isNull(apiName)) {
                        continue;
                    }
                    meg = apiName + ":" + meg;
                } catch (Exception e) {
                    logger.info("数据库未发现apiId = " + o);
                    continue;
                }
            }
            if(!StringHandlerUtils.isNull(meg)) {
                resultSetMeg.add(meg);
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data" ,resultSetMeg);
        DiffInfo diffInfo = diffDao.getDiffInfoById(diffId);
        diffInfo.setDiffErrorMsg(jsonObject.toJSONString());
        logger.info("jsonObject= " + jsonObject.toJSONString());
        diffDao.updateById(diffInfo);

    }*/


}
