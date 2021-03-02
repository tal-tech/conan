/*
package com.tal.wangxiao.conan.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.kvnxiao.jsonequals.JsonCompareResult;
import com.tal.wangxiao.conan.agent.constant.ErrorCode;
import com.tal.wangxiao.conan.agent.constant.TaskStatus;
import com.tal.wangxiao.conan.agent.entity.dao.DiffInfo;
import com.tal.wangxiao.conan.agent.entity.db.*;
import com.tal.wangxiao.conan.agent.entity.es.AccessLogOnline;
import com.tal.wangxiao.conan.agent.model.JsonKeyDiffResult;
import com.tal.wangxiao.conan.agent.model.Result;
import com.tal.wangxiao.conan.agent.model.TextDiffResult;
import com.tal.wangxiao.conan.agent.repository.dao.DiffDao;
import com.tal.wangxiao.conan.agent.repository.db.*;
import com.tal.wangxiao.conan.agent.repository.es.AccessLogRepository;
import com.tal.wangxiao.conan.agent.utility.conan.TaskStatusUtil;
import com.tal.wangxiao.conan.agent.utility.dao.DaoUtils;
import com.tal.wangxiao.conan.agent.utility.diff.JsonUtils;
import com.tal.wangxiao.conan.agent.utility.diff.TextUtils;
import com.tal.wangxiao.conan.agent.utils.JsonCheckUtils;
import com.tal.wangxiao.conan.agent.utils.tools.StringHandlerUtils;
import com.tal.wangxiao.conan.common.model.DiffResultInRedis;
import com.tal.wangxiao.conan.common.redis.RedisTemplateTool;
import com.tal.wangxiao.json.compare.comparator.JSONKeyCompare;
import com.tal.wangxiao.json.compare.mode.JSONDiffKeyMode;
import com.tal.wangxiao.json.compare.result.JSONCompareResult;
import com.tal.wangxiao.json.compare.utils.JSONDiffObject;
import com.tal.wangxiao.json.compare.utils.JSONResultHandlerUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

*/
/**
 * 流量Diff服务实现类
 *
 * @author liujinsong
 * @date 2019/6/19
 *//*

@Service
public class DiffServiceNewImpl {

    @Resource
    private JsonUtils jsonUtils;

    @Resource
    private DiffSchemeRepository diffSchemeRepository;

    @Resource
    private TaskStatusUtil taskStatusUtil;

    @Resource
    private DiffRepository diffRepository;

    @Resource
    private ApiRepository apiRepository;

    @Resource
    DiffDao diffDao;

    @Resource
    private RedisTemplate<Object, Object> template;

    @Resource
    private DiffDetailRepository diffDetailRepository;

    @Resource
    private RecordResultRepository recordResultRepository;

    @Resource
    private ReplayRepository replayRepository;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private AccessLogRepository accessLogRepository;

    @Resource
    private DaoUtils daoUtils;


    @Value("${system.redis.cacheTime}")
    private long redisCacheTime;

    @Resource
    private RedisTemplateTool redisTemplateTool;


    @Resource
    private ApiDiffSchemeRelationRepository apiDiffSchemeRelationRepository;


    private Result result = new Result(ErrorCode.SUCCESS, null);

    private static final Logger logger = LoggerFactory.getLogger(DiffServiceNewImpl.class);


    //执行diff对比
    //回放结果写入Redis，key = 'requestId-recordId-replayId-apiId'
    @Async
    public void startDiff(Integer taskExcutionId, Integer recordId, Integer replayId, Integer diffId) throws Exception {
        // TODO Auto-generated method stub
        logger.info("开始执行比对, diff_id = {}", diffId);
        List<ApiDiffSchemeRelation> findByApiId = null;
        redisTemplateTool.setLogByDiffId_START(diffId,"开始执行比对, diff_id = " + diffId);
        Optional<Replay> replayOptional = replayRepository.findByTaskExecutionIdAndIsBaseline(taskExcutionId, 1);
        if (!replayOptional.isPresent()) {
            redisTemplateTool.setLogByDiffId_ERROR(diffId,"找不到基准比对信息, replay_id=" + replayId + " ,diff_id=" + diffId + "record_id = " + recordId);
            redisTemplateTool.setLogByDiff_END(diffId,"对比失败");
            throw new Exception("找不到基准比对信息, replay_id=" + replayId + " ,diff_id=" + diffId + "record_id = " + recordId);
        }
        Replay replay = replayOptional.get();
        redisTemplateTool.setLogByDiffId_INFO(diffId,"此次对比信息 replay_id = " + replayId + "基准回放,base replay_id=" + replay.getId());
        //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
        int baseDataReplayId = 1;
        TextUtils textUtils = new TextUtils();
        TextDiffResult textDiffResult = new TextDiffResult();
        DiffScheme diffScheme = null;
        Set<String> ignoreFields = null;
        JsonCompareResult compareResult = null;
        JsonKeyDiffResult jsonKeyDiffResult = new JsonKeyDiffResult();
        boolean isJson = true;
        //recourd_result中获取apiList
        List<RecordResult> findByRecordId = recordResultRepository.findByRecordId(recordId);
        if (null == findByRecordId || findByRecordId.isEmpty()) {
            //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
            redisTemplateTool.setLogByDiffId_ERROR(diffId,"recordId" + recordId + "无对应requestId" );
            redisTemplateTool.setLogByDiff_END(diffId,"比对失败");
            throw new Exception("recordId" + recordId + "无对应requestId");
        }
        //根据record_id 在record_result表中获取 apiId list
        Set<Integer> apiSet = new HashSet<Integer>();
        for (RecordResult recoudResult1 : findByRecordId) {
            apiSet.add(recoudResult1.getApiId());
        }
        HashMap<Integer, Object> resultMap = new HashMap<Integer, Object>(apiSet.size());
        logger.info(apiSet.toString());
        //遍历apiID diff后写入redis
        for (Integer apiId : apiSet) {
            //获取同一 record_id 相同api的request_ID set去重
            List<RecordResult> findByRecordIdAndApiId = recordResultRepository.findByRecordIdAndApiId(recordId, apiId);
            Set<String> apiRequestSet = new HashSet<String>();
            for (RecordResult requestResult : findByRecordIdAndApiId) {
                apiRequestSet.add(requestResult.getRequestId());
                //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
                //stringRedisTemplate.opsForValue().append("logBydiffId="+diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+" [INFO]   --"+apiId+"对应的request"+requestResult.getRequestId()+"\n");
            }
            List<HashMap> jsonCompareResultsList = new ArrayList<HashMap>(apiRequestSet.size());
            resultMap.put(apiId, jsonCompareResultsList);
            int count = 0;
            for (String requestId : apiRequestSet) {
                count++;
                //回放的json redis key
                String compreDataKey = requestId + "-" + recordId + "-" + replayId + "-" + apiId;
                logger.info("compreDataKey=" + compreDataKey);
                //基准json redis key
                //baseJson 在replay表中有字段标记 应该task_excution_id 对应唯一的一个replay_id作为基准json
                baseDataReplayId = replay.getId();
                String BaseDataKey = requestId + "-" + recordId + "-" + baseDataReplayId + "-" + apiId;
                logger.info("BaseDataKey=" + BaseDataKey);
                //stringRedisTemplate.opsForValue().append("logBydiffId="+diffId, "BaseDataKey"+BaseDataKey+"\n");
                //获取diff规则
                try {
                    findByApiId = apiDiffSchemeRelationRepository.findByApiId(apiId);
                } catch (Exception e) {

                }
                if (findByApiId.size() > 1) {
                    logger.error("此api在" + apiId + " api_scheme_relation表对应多个diffschemeId");
                }
                Optional<DiffScheme> diffR;
                if (1 == daoUtils.getReplayTypeByTaskExecId(taskExcutionId)) {
                    //判断回放任务类型 如果为基于场次ID回放 比对时默认使用json比对
                    diffR = diffSchemeRepository.findById(1);
                } else {
                    if (StringHandlerUtils.isNull(findByApiId.get(0))) {
                        diffR = diffSchemeRepository.findById(1);
                    } else if (StringHandlerUtils.isNull(findByApiId.get(0))) {
                        diffR = diffSchemeRepository.findById(1);
                    } else {
                        diffR = diffSchemeRepository.findById(findByApiId.get(0).getDiffSchemeId());
                    }
                }
                String compareJson = stringRedisTemplate.opsForValue().get(compreDataKey);
                String baseJson = stringRedisTemplate.opsForValue().get(BaseDataKey);
                try {
                    if (JsonCheckUtils.isJSONValidByalibaba(compareJson)) {
                        isJson = true;
                    } else {
                        isJson = false;
                    }
                } catch (Exception e) {
                    isJson = false;
                }
                logger.error("diffR = " + diffR);
                if (diffR.isPresent()) {
                    diffScheme = diffR.get();
                    logger.error("diffScheme = " + diffScheme);
                    //根据response类型 0为json  1为文本类型
                    if (diffScheme.getType() == 0 && isJson == true) {
                        logger.info("比对模式为Json比对");
                        try {
                            ignoreFields = new HashSet((Arrays.asList(diffScheme.getDescription().split(","))));
                        } catch (Exception e) {
                            ignoreFields = null;
                        }
                        if ((null == compareJson || "".equals(compareJson)) || (null == baseJson || "".equals(baseJson))) {
                            logger.info("请检查对比json的redis key=" + compreDataKey + "与baseJson的redis key=" + BaseDataKey + "是否在redis中有Value");
                            //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
                            redisTemplateTool.setLogByDiffId_ERROR(diffId,"请检查对比json的redis key=" + compreDataKey + "与baseJson的redis key=" + BaseDataKey + "是否在redis中有Value");
                            continue;
                        }
                        if (!Objects.isNull(ignoreFields) && !ignoreFields.isEmpty()) {
                            if (apiId == 158279 || apiId == 158283) {
                                Map<String, Object> baseBodyMap = JSONObject.parseObject(baseJson, Map.class);
                                baseBodyMap.put("data", "");
                                baseJson = JSONObject.toJSON(baseBodyMap).toString();
                                Map<String, Object> compareBodyMap = JSONObject.parseObject(compareJson, Map.class);
                                compareBodyMap.put("data", "");
                                compareJson = JSONObject.toJSON(compareBodyMap).toString();
                            }
                            jsonKeyDiffResult = jsonUtils.compareJsonKey(baseJson, compareJson);
                            if (!jsonKeyDiffResult.isEqual()) {
                                //新JSON 比较

                                String apiName = "";
                                try {
                                    //忽律数组接口处理
                                    apiName = diffDao.getApiNAme(apiId);
                                    if (StringHandlerUtils.isNull(apiName)) {
                                        continue;
                                    }
                                } catch (Exception e) {
                                    logger.info("数据库未发现apiId = " + apiId);
                                    continue;
                                }

                                try {
                                    JSONCompareResult jsonCompareResult = new JSONCompareResult();
                                    //忽律数组接口处理
                                    if ("/v1/teacher/barrage/statistic".equals(apiName)) {
                                        JSONDiffObject jsonDiffObject = new JSONDiffObject(baseJson, compareJson, JSONDiffKeyMode.CURRENCY_FFTTF);
                                        jsonDiffObject.setDiffValue(false);
                                        jsonDiffObject.setIgnoreArray(true);
                                        jsonCompareResult = jsonDiffObject.startCompare();
                                    } else {
                                        jsonCompareResult = JSONKeyCompare.compareJSON_FFTTF(baseJson, compareJson);
                                    }

                                    HashMap<Integer, JSONCompareResult> comparJSONMap = new HashMap<Integer, JSONCompareResult>();
                                    comparJSONMap.put(count, jsonCompareResult);
                                    jsonCompareResultsList.add(comparJSONMap);
                                    logger.error("jsonCompareResult = " + jsonCompareResult);
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
                                    logger.error("e =" + e + "比对错误，请检查JSON，base=" + baseJson + "compareJson=" + compareJson);
                                }
                            }

                            logger.error("jsonKeyDiffResult = " + jsonKeyDiffResult);

                            //compareResult = jsonUtils.compareJson(baseJson, compareJson);
                        }
                        //向redis中写入结果
                        if (!Objects.isNull(jsonKeyDiffResult)) {
                            DiffResultInRedis diffResultInRedis = new DiffResultInRedis();
                            diffResultInRedis.setAllKeysNu(jsonKeyDiffResult.getTotalMsgCount());
                            diffResultInRedis.setSameKeyNu(jsonKeyDiffResult.getTotalMsgCount() - jsonKeyDiffResult.getDiffMsgCount());
                            diffResultInRedis.setDiffResult(jsonKeyDiffResult.isEqual());
                            String diffResultkey = recordId + "-" + replayId + "-" + apiId + "-" + diffId + "-" + requestId;
                            logger.info("diff结果写入redis redis key=" + diffResultkey);
                            //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
                            //stringRedisTemplate.opsForValue().append("logBydiffId="+diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+" [INFO]   --"+"diff结果写入redis redis key="+diffResultkey+"\n");
                            try {
                                template.opsForValue().set(diffResultkey, diffResultInRedis, redisCacheTime, TimeUnit.DAYS);
                            } catch (Exception e) {
                                //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
                                redisTemplateTool.setLogByDiffId_ERROR(diffId,"diff结果写入redis失败可能没有比对日志 redis key=" + diffResultkey+",errMsg = "+e.getMessage());
                                logger.error("diff结果写入redis失败 redis key=" + diffResultkey+",errMsg = "+e.getMessage());
                            }
                        }
                    }
                    //对比模式文本对比
                    if (diffScheme.getType() == 1 || isJson == false) {
                        logger.info("对比模式为文本对比");
                        try {
                            ignoreFields = new HashSet((Arrays.asList(diffScheme.getDescription().split(","))));
                        } catch (Exception e) {
                            ignoreFields = null;
                        }
                        try {
                            String compareString = stringRedisTemplate.opsForValue().get(compreDataKey);
                            String baseString = stringRedisTemplate.opsForValue().get(BaseDataKey);
                            if ((null == compareString || "".equals(compareString)) || ((null == baseString || "".equals(baseString)))) {
                                //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
                                redisTemplateTool.setLogByDiffId_ERROR(diffId,"请检查对比json的redis key=" + compreDataKey + "与baseJson的redis key=" + BaseDataKey + "是否在redis中有Value");
                                logger.info("请检查对比json的redis key=" + compreDataKey + "与baseJson的redis key=" + BaseDataKey + "是否在redis中有Value");
                                continue;
                            }
                            if (ignoreFields != null && !ignoreFields.isEmpty()) {
                                textDiffResult = textUtils.stringDiff(baseString, compareString, ignoreFields);
                            } else {
                                textDiffResult = textUtils.stringDiff(baseString, compareString);
                            }
                        } catch (Exception e) {
                            //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
                            redisTemplateTool.setLogByDiffId_ERROR(diffId,"获取redis对比信息失败, cause:" + e.getMessage());
                            logger.info("请检查对比compareString的redis key=" + compreDataKey + "与baseString的redis key=" + BaseDataKey + "是否在redis中有Value");
                        }
                        //向redis写入对比结果数据
                        if (textDiffResult != null) {
                            DiffResultInRedis diffResultInRedis = new DiffResultInRedis();
                            diffResultInRedis.setAllKeysNu(textDiffResult.getTotalMsgCount());
                            //diffResultInRedis.setDiffMsg(textDiffResult.getDiffMsgList().toString());
                            diffResultInRedis.setSameKeyNu(textDiffResult.getTotalMsgCount() - textDiffResult.getDiffMsgCount());
                            diffResultInRedis.setDiffResult(textDiffResult.isEqual());
                            String diffResultkey = recordId + "-" + replayId + "-" + apiId + "-" + diffId + "-" + requestId;
                            logger.info("diff结果写入redis redis key=" + diffResultkey);
                            try {
                                template.opsForValue().set(diffResultkey, diffResultInRedis, redisCacheTime, TimeUnit.DAYS);
                            } catch (Exception e) {
                                //把对应的比对ID的日志写入Redis,  key='logBydiffId='+diffId
                                stringRedisTemplate.opsForValue().append("logBydiffId=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [ERROR]  --" + "diff结果写入redis失败 redis key=" + diffResultkey + ",cause=" + e.getMessage() + "\n");
                                redisTemplateTool.setLogByDiffId_ERROR(diffId,"diff结果写入redis失败 redis key=" + diffResultkey + ",cause=" + e.getMessage());
                            }
                            //diffDetailRepository
                        }

                    }

                }
            }
            //针对api_id
            editDiffTAndDetailTable(taskExcutionId, diffId, recordId, replayId, apiId);
        }
        try {
            jsonCompareKeyHandler(resultMap, diffId);
        } catch (Exception e) {
            logger.error("diff 更新汇总数据异常 e=" + e);
        }

        editDiffTable(diffId, taskExcutionId);
    }

    //在redis中获取相同apiID 不同request的diff结果 计算后在diff_detail表中插入相关数据
    public void editDiffTAndDetailTable(Integer taskExcutionId, Integer diffId, Integer recordId, Integer replayId, Integer apiId) throws Exception {
        final String prefix = recordId + "-" + replayId + "-" + apiId + "-" + diffId + "-*";
        DiffDetail diffDetail = new DiffDetail();
        Set<String> keys = stringRedisTemplate.keys(prefix);
        //diff_detail 中的actual_count diff结果相同接口个数
        int actual_count = 0;
        //diff_detail 中的expect_count 接口总数
        float successRate = 0;
        int expect_count = 0;
        for (String key : keys) {
            //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
            //stringRedisTemplate.opsForValue().append("logBydiffId="+diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+" [INFO]   --"+"result Key in Redis"+key+"\n");
            logger.error("key = " + key);
            Object obj = template.opsForValue().get(key);
            if (StringHandlerUtils.isNull(obj)) {
                logger.error("redis 查询null");
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
                diffDetailRepository.saveAndFlush(diffDetail);
            } catch (Exception e) {
                //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
                stringRedisTemplate.opsForValue().append("logBydiffId=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]   --" + "apiId为" + apiId + "diffId为" + diffId + "的数据存储失败，请检查" + "\n");
                logger.info("apiId为" + apiId + "diffId为" + diffId + "的数据存储失败，请检查");
            }
            successRate = (actual_count * 100.0f) / expect_count;
        } else {
            //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
            successRate = 0;
            stringRedisTemplate.opsForValue().append("logBydiffId=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]   --" + "apiId为" + apiId + "diffId为" + diffId + "的对应流量为0，请检查redis中是否有diff结果" + "\n");
            logger.info("apiId为" + apiId + "diffId为" + diffId + "的对应流量为0，请检查redis中是否有diff结果");
        }
        Optional<Api> apiOptional = apiRepository.findById(apiId);
        if (!apiOptional.isPresent()) {
            stringRedisTemplate.opsForValue().append("logBydiffId=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [ERROR]  --" + "比对完成，该api_id=" + apiId + "不存在" + "\n");
        } else {
            stringRedisTemplate.opsForValue().append("logBydiffId=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]   --" + "api=" + apiOptional.get().getName() + "比对完成 ,成功率 : " + successRate + "%" + "\n");
        }
    }

    private void jsonCompareKeyHandler(HashMap<Integer, Object> resultMap, Integer diffId) {

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

    }


    //暂时舍弃，采用其他方式进行合并
   */
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

    }*//*



    //更新diff表中数据
    public void editDiffTable(int diffId, Integer taskExcutionId) throws Exception {
        //获取diffId对应的所有api对比结果  所有actual/所有expect
        List<DiffDetail> findByDiffId = diffDetailRepository.findByDiffId(diffId);
        int totalActual = 0;
        int totalExpect = 0;
        for (DiffDetail diffDetail : findByDiffId) {
            totalActual += diffDetail.getActualCount();
            totalExpect += diffDetail.getExpectCount();
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String success_rate = "0";
        if (totalExpect != 0) {
            success_rate = numberFormat.format((float) totalActual / (float) totalExpect * 100.0f);
        }
        logger.info("总比对条数为" + totalExpect + ",比对成功条数为" + totalActual);
        Optional<Diff> findById = diffRepository.findById(diffId);
        //更新 end_at success_rate 字段
        if (!findById.isPresent()) {
            //把对应的比对ID的日志写入Redis, key='logBydiffId='+diffId
            stringRedisTemplate.opsForValue().append("logBydiffId=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]   --" + "请查看在diff表中是否有相关数据" + diffId + "\n");
            stringRedisTemplate.opsForValue().append("logBydiffId=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]   --" + "对比结束(end)");
            throw new Exception("请查看在diff表中是否有相关数据" + diffId);
        } else {
            Diff diff = findById.get();
            diff.setEndAt(LocalDateTime.now());
            diff.setSuccessRate(success_rate + "%");
            diffRepository.saveAndFlush(diff);
            stringRedisTemplate.opsForValue().append("logBydiffId=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]   --" + "当前比对任务diffId=" + diffId + "的比对任务成功执行，成功率为" + success_rate + "%" + "\n");
            stringRedisTemplate.opsForValue().append("logBydiffId=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]   --" + "对比结束(end)");
        }
        taskStatusUtil.updateTaskStatus(taskExcutionId, TaskStatus.DIFF_SUCCESS);
    }


    public void getLogByES(String Id) {
        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.DESC, "@timestamp");
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("_id", Id));
        //测试环境ES存储字段缺少 url 字段 暂时匹配查询
        //System.out.println(queryBuilder);
        Page<AccessLogOnline> search = accessLogRepository.search(queryBuilder, pageable);
        // System.out.println("result" + search.getContent().get(0));
    }

    public static void main(String[] args) {
        String baseJson = "{\n" +
                "  \"code\": 0,\n" +
                "  \"stat\": 1,\n" +
                "  \"msg\": \"ok\",\n" +
                "  \"data\": {\n" +
                "    \"plugins\": [\n" +
                "      {\n" +
                "        \"pluginId\": 119,\n" +
                "        \"pluginName\": \"大班-削峰策略\",\n" +
                "        \"moduleId\": 20,\n" +
                "        \"properties\": {\n" +
                "          \"eliminationList\": \"102,105,107,108,110,112,113,115,116,117,119,125,133,137\",\n" +
                "          \"maxDelayMS\": \"3000\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 103,\n" +
                "        \"pluginName\": \"追播-直播\",\n" +
                "        \"moduleId\": 80,\n" +
                "        \"properties\": {\n" +
                "          \"duration\": \"40000\",\n" +
                "          \"isNewPlayerKernel\": \"1\",\n" +
                "          \"maxWaterMark\": \"4000\",\n" +
                "          \"minWaterMark\": \"1500\",\n" +
                "          \"waterMark\": \"3500\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 72,\n" +
                "        \"pluginName\": \"直播课签到\",\n" +
                "        \"moduleId\": 1,\n" +
                "        \"properties\": {\n" +
                "          \"getSignStatusURL\": \"https://studentlive.xueersi.com/v1/student/signtask/status/get\",\n" +
                "          \"signExecuteURL\": \"https://studentlive.xueersi.com/v1/student/signtask/execute\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 56,\n" +
                "        \"pluginName\": \"正确率排名-直播\",\n" +
                "        \"moduleId\": 56,\n" +
                "        \"properties\": {\n" +
                "          \"getClassRank\": \"https://studentlive.xueersi.com/v1/student/honour/classRank/get\",\n" +
                "          \"isPlaybackShow\": \"0\",\n" +
                "          \"liveSkin\": \"2\",\n" +
                "          \"playBackSkin\": \"2\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 77,\n" +
                "        \"pluginName\": \"live_gift\",\n" +
                "        \"moduleId\": 6,\n" +
                "        \"properties\": {\n" +
                "          \"getGiftList\": \"https://studentlive.xueersi.com/v1/student/gift/getGiftList\",\n" +
                "          \"sendGift\": \"https://studentlive.xueersi.com/v1/student/gift/send\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 76,\n" +
                "        \"pluginName\": \"live_vote\",\n" +
                "        \"moduleId\": 5,\n" +
                "        \"properties\": {\n" +
                "          \"commitVote\": \"https://studentlive.xueersi.com/v1/student/vote/commit\",\n" +
                "          \"getVoteStatistics\": \"https://studentlive.xueersi.com/v1/student/vote/getStatistics\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 106,\n" +
                "        \"pluginName\": \"家长旁听-直播\",\n" +
                "        \"moduleId\": 70,\n" +
                "        \"properties\": {\n" +
                "          \"gslbServerUrl\": \"https://gslb.xueersi.com/xueersi_gslb/live\",\n" +
                "          \"logServerUrl\": \"https://gslb.xueersi.com/xueersi/live/log\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 74,\n" +
                "        \"pluginName\": \"live_自传互动题\",\n" +
                "        \"moduleId\": 3,\n" +
                "        \"properties\": {\n" +
                "          \"maxDelayMS\": \"2000\",\n" +
                "          \"suQuestionGetTestInfoURL\": \"https://studentlive.xueersi.com/v1/student/suquestion/test/get\",\n" +
                "          \"suQuestionSubmitURL\": \"https://studentlive.xueersi.com/v1/student/suquestion/submit\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 147,\n" +
                "        \"pluginName\": \"大班直播开关配置\",\n" +
                "        \"moduleId\": 98,\n" +
                "        \"properties\": {\n" +
                "          \"preGetCourseware\": \"1\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 148,\n" +
                "        \"pluginName\": \"大班新版预加载\",\n" +
                "        \"moduleId\": 99,\n" +
                "        \"properties\": {\n" +
                "          \"GetCourseware\": \"https://studentlive.xueersi.com/v1/student/plan/courseware/get\",\n" +
                "          \"PreloadCourseware\": \"https://studentlive.xueersi.com/v1/student/courseware/preload\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 55,\n" +
                "        \"pluginName\": \"本场成就-直播\",\n" +
                "        \"moduleId\": 55,\n" +
                "        \"properties\": {\n" +
                "          \"honourGet\": \"https://studentlive.xueersi.com/v1/student/honour/get\",\n" +
                "          \"isPlaybackShow\": \"1\",\n" +
                "          \"liveSkin\": \"2\",\n" +
                "          \"playBackSkin\": \"2\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 92,\n" +
                "        \"pluginName\": \"大题互动-直播\",\n" +
                "        \"moduleId\": 62,\n" +
                "        \"properties\": {\n" +
                "          \"getCourseWare\": \"https://studentlive.xueersi.com/v1/student/courseWarePage/get\",\n" +
                "          \"maxDelayMS\": \"2000\",\n" +
                "          \"submitCourse\": \"https://studentlive.xueersi.com/v1/student/courseware/submit\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 79,\n" +
                "        \"pluginName\": \"liveduration\",\n" +
                "        \"moduleId\": 11,\n" +
                "        \"properties\": {\n" +
                "          \"durationPushUrl\": \"https://studentlive.xueersi.com/v1/student/duration/push\",\n" +
                "          \"interval\": \"60\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 94,\n" +
                "        \"pluginName\": \"轻互动-聊天面板-直播\",\n" +
                "        \"moduleId\": 68,\n" +
                "        \"properties\": {\n" +
                "          \"upload\": \"https://studentlive.xueersi.com/v1/student/barrage/upload\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 93,\n" +
                "        \"pluginName\": \"NB物理实验-直播\",\n" +
                "        \"moduleId\": 69,\n" +
                "        \"properties\": {\n" +
                "          \"getNBStatisticResult\": \"https://studentlive.xueersi.com/v1/student/nobook/getSubmitResult\",\n" +
                "          \"getNBTestInfo\": \"https://studentlive.xueersi.com/v1/student/courseWarePage/get\",\n" +
                "          \"maxDelayMS\": \"3000\",\n" +
                "          \"nbLogin\": \"https://studentlive.xueersi.com/v1/student/nobook/login\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 78,\n" +
                "        \"pluginName\": \"live_barrage\",\n" +
                "        \"moduleId\": 7,\n" +
                "        \"properties\": {\n" +
                "          \"barrageHistory\": \"https://studentlive.xueersi.com/v1/student/barrage/history\",\n" +
                "          \"barrageUpload\": \"https://studentlive.xueersi.com/v1/student/barrage/upload\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 73,\n" +
                "        \"pluginName\": \"live_lucky\",\n" +
                "        \"moduleId\": 2,\n" +
                "        \"properties\": {\n" +
                "          \"luckyReceive\": \"https://studentlive.xueersi.com/v1/student/lucky/receive\",\n" +
                "          \"maxDelayMS\": \"2000\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 95,\n" +
                "        \"pluginName\": \"灰度场次控制-三期\",\n" +
                "        \"moduleId\": 8,\n" +
                "        \"properties\": {\n" +
                "          \"planVersion\": \"3\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 143,\n" +
                "        \"pluginName\": \"live_collectiveSpeak\",\n" +
                "        \"moduleId\": 153,\n" +
                "        \"properties\": {\n" +
                "          \"getRTCToken\": \"https://studentlive.xueersi.com/v1/student/barrage/getRTCToken\",\n" +
                "          \"upload\": \"https://studentlive.xueersi.com/v1/student/barrage/upload\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 75,\n" +
                "        \"pluginName\": \"live_linkmic\",\n" +
                "        \"moduleId\": 4,\n" +
                "        \"properties\": {\n" +
                "          \"applyLinkMic\": \"https://studentlive.xueersi.com/v1/student/linkMic/apply\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 84,\n" +
                "        \"pluginName\": \"视频标记-直播\",\n" +
                "        \"moduleId\": 57,\n" +
                "        \"properties\": {\n" +
                "          \"delUrl\": \"https://studentlive.xueersi.com/v1/student/video/metadata/del\",\n" +
                "          \"getUrl\": \"https://studentlive.xueersi.com/v1/student/video/metadata/get\",\n" +
                "          \"setUrl\": \"https://studentlive.xueersi.com/v1/student/video/metadata/set\",\n" +
                "          \"uploadServerUrl\": \"https://upload.xueersi.com/serverlist\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 86,\n" +
                "        \"pluginName\": \"教学互动-直播\",\n" +
                "        \"moduleId\": 59,\n" +
                "        \"properties\": {\n" +
                "          \"coursewareTimeoutMS\": \"15000\",\n" +
                "          \"getCourseWare\": \"https://studentlive.xueersi.com/v1/student/courseWarePage/get\",\n" +
                "          \"isEnglish\": \"0\",\n" +
                "          \"maxDelayMS\": \"2000\",\n" +
                "          \"preloadUrl\": \"https://student.xueersi.com/science/LiveCourses/preLoadNewCourseWare\",\n" +
                "          \"submitCourse\": \"https://studentlive.xueersi.com/v1/student/courseware/submit\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 116,\n" +
                "        \"pluginName\": \"evaluation\",\n" +
                "        \"moduleId\": 151,\n" +
                "        \"properties\": {\n" +
                "          \"getRule\": \"http://npsopenapi.xueersi.com/App/nps/isRuleTrigger\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 85,\n" +
                "        \"pluginName\": \"rank-直播\",\n" +
                "        \"moduleId\": 58,\n" +
                "        \"properties\": {\n" +
                "          \"rankGetClassStuRanking\": \"https://studentlive.xueersi.com/v1/student/rank/getClassStuRanking\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        String compareJson = "{\n" +
                "  \"code\": 0,\n" +
                "  \"stat\": 1,\n" +
                "  \"msg\": \"ok\",\n" +
                "  \"data\": {\n" +
                "    \"plugins\": [\n" +
                "      {\n" +
                "        \"pluginId\": 56,\n" +
                "        \"pluginName\": \"正确率排名-直播\",\n" +
                "        \"moduleId\": 56,\n" +
                "        \"properties\": {\n" +
                "          \"getClassRank\": \"https://studentlive.xueersi.com/v1/student/honour/classRank/get\",\n" +
                "          \"isPlaybackShow\": \"0\",\n" +
                "          \"liveSkin\": \"2\",\n" +
                "          \"playBackSkin\": \"2\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 78,\n" +
                "        \"pluginName\": \"live_barrage\",\n" +
                "        \"moduleId\": 7,\n" +
                "        \"properties\": {\n" +
                "          \"barrageHistory\": \"https://studentlive.xueersi.com/v1/student/barrage/history\",\n" +
                "          \"barrageUpload\": \"https://studentlive.xueersi.com/v1/student/barrage/upload\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 147,\n" +
                "        \"pluginName\": \"大班直播开关配置\",\n" +
                "        \"moduleId\": 98,\n" +
                "        \"properties\": {\n" +
                "          \"preGetCourseware\": \"1\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 95,\n" +
                "        \"pluginName\": \"灰度场次控制-三期\",\n" +
                "        \"moduleId\": 8,\n" +
                "        \"properties\": {\n" +
                "          \"planVersion\": \"3\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 103,\n" +
                "        \"pluginName\": \"追播-直播\",\n" +
                "        \"moduleId\": 80,\n" +
                "        \"properties\": {\n" +
                "          \"duration\": \"40000\",\n" +
                "          \"isNewPlayerKernel\": \"1\",\n" +
                "          \"maxWaterMark\": \"4000\",\n" +
                "          \"minWaterMark\": \"1500\",\n" +
                "          \"waterMark\": \"3500\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 119,\n" +
                "        \"pluginName\": \"大班-削峰策略\",\n" +
                "        \"moduleId\": 20,\n" +
                "        \"properties\": {\n" +
                "          \"eliminationList\": \"102,105,107,108,110,112,113,115,116,117,119,125,133,137\",\n" +
                "          \"maxDelayMS\": \"3000\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 76,\n" +
                "        \"pluginName\": \"live_vote\",\n" +
                "        \"moduleId\": 5,\n" +
                "        \"properties\": {\n" +
                "          \"commitVote\": \"https://studentlive.xueersi.com/v1/student/vote/commit\",\n" +
                "          \"getVoteStatistics\": \"https://studentlive.xueersi.com/v1/student/vote/getStatistics\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 116,\n" +
                "        \"pluginName\": \"evaluation\",\n" +
                "        \"moduleId\": 151,\n" +
                "        \"properties\": {\n" +
                "          \"getRule\": \"http://npsopenapi.xueersi.com/App/nps/isRuleTrigger\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 86,\n" +
                "        \"pluginName\": \"教学互动-直播\",\n" +
                "        \"moduleId\": 59,\n" +
                "        \"properties\": {\n" +
                "          \"coursewareTimeoutMS\": \"15000\",\n" +
                "          \"getCourseWare\": \"https://studentlive.xueersi.com/v1/student/courseWarePage/get\",\n" +
                "          \"isEnglish\": \"0\",\n" +
                "          \"maxDelayMS\": \"2000\",\n" +
                "          \"preloadUrl\": \"https://student.xueersi.com/science/LiveCourses/preLoadNewCourseWare\",\n" +
                "          \"submitCourse\": \"https://studentlive.xueersi.com/v1/student/courseware/submit\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 73,\n" +
                "        \"pluginName\": \"live_lucky\",\n" +
                "        \"moduleId\": 2,\n" +
                "        \"properties\": {\n" +
                "          \"luckyReceive\": \"https://studentlive.xueersi.com/v1/student/lucky/receive\",\n" +
                "          \"maxDelayMS\": \"2000\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 94,\n" +
                "        \"pluginName\": \"轻互动-聊天面板-直播\",\n" +
                "        \"moduleId\": 68,\n" +
                "        \"properties\": {\n" +
                "          \"upload\": \"https://studentlive.xueersi.com/v1/student/barrage/upload\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 148,\n" +
                "        \"pluginName\": \"大班新版预加载\",\n" +
                "        \"moduleId\": 99,\n" +
                "        \"properties\": {\n" +
                "          \"GetCourseware\": \"https://studentlive.xueersi.com/v1/student/plan/courseware/get\",\n" +
                "          \"PreloadCourseware\": \"https://studentlive.xueersi.com/v1/student/courseware/preload\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 79,\n" +
                "        \"pluginName\": \"liveduration\",\n" +
                "        \"moduleId\": 11,\n" +
                "        \"properties\": {\n" +
                "          \"durationPushUrl\": \"https://studentlive.xueersi.com/v1/student/duration/push\",\n" +
                "          \"interval\": \"60\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 74,\n" +
                "        \"pluginName\": \"live_自传互动题\",\n" +
                "        \"moduleId\": 3,\n" +
                "        \"properties\": {\n" +
                "          \"maxDelayMS\": \"2000\",\n" +
                "          \"suQuestionGetTestInfoURL\": \"https://studentlive.xueersi.com/v1/student/suquestion/test/get\",\n" +
                "          \"suQuestionSubmitURL\": \"https://studentlive.xueersi.com/v1/student/suquestion/submit\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 55,\n" +
                "        \"pluginName\": \"本场成就-直播\",\n" +
                "        \"moduleId\": 55,\n" +
                "        \"properties\": {\n" +
                "          \"honourGet\": \"https://studentlive.xueersi.com/v1/student/honour/get\",\n" +
                "          \"isPlaybackShow\": \"1\",\n" +
                "          \"liveSkin\": \"2\",\n" +
                "          \"playBackSkin\": \"2\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 85,\n" +
                "        \"pluginName\": \"rank-直播\",\n" +
                "        \"moduleId\": 58,\n" +
                "        \"properties\": {\n" +
                "          \"rankGetClassStuRanking\": \"https://studentlive.xueersi.com/v1/student/rank/getClassStuRanking\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 72,\n" +
                "        \"pluginName\": \"直播课签到\",\n" +
                "        \"moduleId\": 1,\n" +
                "        \"properties\": {\n" +
                "          \"getSignStatusURL\": \"https://studentlive.xueersi.com/v1/student/signtask/status/get\",\n" +
                "          \"signExecuteURL\": \"https://studentlive.xueersi.com/v1/student/signtask/execute\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 92,\n" +
                "        \"pluginName\": \"大题互动-直播\",\n" +
                "        \"moduleId\": 62,\n" +
                "        \"properties\": {\n" +
                "          \"getCourseWare\": \"https://studentlive.xueersi.com/v1/student/courseWarePage/get\",\n" +
                "          \"maxDelayMS\": \"2000\",\n" +
                "          \"submitCourse\": \"https://studentlive.xueersi.com/v1/student/courseware/submit\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 93,\n" +
                "        \"pluginName\": \"NB物理实验-直播\",\n" +
                "        \"moduleId\": 69,\n" +
                "        \"properties\": {\n" +
                "          \"getNBStatisticResult\": \"https://studentlive.xueersi.com/v1/student/nobook/getSubmitResult\",\n" +
                "          \"getNBTestInfo\": \"https://studentlive.xueersi.com/v1/student/courseWarePage/get\",\n" +
                "          \"maxDelayMS\": \"3000\",\n" +
                "          \"nbLogin\": \"https://studentlive.xueersi.com/v1/student/nobook/login\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 75,\n" +
                "        \"pluginName\": \"live_linkmic\",\n" +
                "        \"moduleId\": 4,\n" +
                "        \"properties\": {\n" +
                "          \"applyLinkMic\": \"https://studentlive.xueersi.com/v1/student/linkMic/apply\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 143,\n" +
                "        \"pluginName\": \"live_collectiveSpeak\",\n" +
                "        \"moduleId\": 153,\n" +
                "        \"properties\": {\n" +
                "          \"getRTCToken\": \"https://studentlive.xueersi.com/v1/student/barrage/getRTCToken\",\n" +
                "          \"upload\": \"https://studentlive.xueersi.com/v1/student/barrage/upload\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 106,\n" +
                "        \"pluginName\": \"家长旁听-直播\",\n" +
                "        \"moduleId\": 70,\n" +
                "        \"properties\": {\n" +
                "          \"gslbServerUrl\": \"https://gslb.xueersi.com/xueersi_gslb/live\",\n" +
                "          \"logServerUrl\": \"https://gslb.xueersi.com/xueersi/live/log\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 84,\n" +
                "        \"pluginName\": \"视频标记-直播\",\n" +
                "        \"moduleId\": 57,\n" +
                "        \"properties\": {\n" +
                "          \"delUrl\": \"https://studentlive.xueersi.com/v1/student/video/metadata/del\",\n" +
                "          \"getUrl\": \"https://studentlive.xueersi.com/v1/student/video/metadata/get\",\n" +
                "          \"setUrl\": \"https://studentlive.xueersi.com/v1/student/video/metadata/set\",\n" +
                "          \"uploadServerUrl\": \"https://upload.xueersi.com/serverlist\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"pluginId\": 77,\n" +
                "        \"pluginName\": \"live_gift\",\n" +
                "        \"moduleId\": 6,\n" +
                "        \"properties\": {\n" +
                "          \"getGiftList\": \"https://studentlive.xueersi.com/v1/student/gift/getGiftList\",\n" +
                "          \"sendGift\": \"https://studentlive.xueersi.com/v1/student/gift/send\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        Map<String, Object> baseBodyMap = JSONObject.parseObject(baseJson, Map.class);
        baseBodyMap.put("data", "");
        baseJson = JSONObject.toJSON(baseBodyMap).toString();
        Map<String, Object> compareBodyMap = JSONObject.parseObject(compareJson, Map.class);
        compareBodyMap.put("data", "");
        compareJson = JSONObject.toJSON(compareBodyMap).toString();

        JsonUtils jsu = new JsonUtils();
        JsonKeyDiffResult jsonKeyDiffResult = jsu.compareJsonKey(baseJson, compareJson);
        logger.debug(jsonKeyDiffResult.toString());

    }

}
*/
