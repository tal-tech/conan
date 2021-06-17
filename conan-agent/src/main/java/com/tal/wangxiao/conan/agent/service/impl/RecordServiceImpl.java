package com.tal.wangxiao.conan.agent.service.impl;


import com.google.common.collect.Lists;
import com.tal.wangxiao.conan.agent.service.RecordService;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.constant.enums.HttpMethodConstants;
import com.tal.wangxiao.conan.common.constant.enums.TaskStatus;
import com.tal.wangxiao.conan.common.entity.db.*;
import com.tal.wangxiao.conan.common.exception.BaseException;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.bo.RecordDetailInfo;
import com.tal.wangxiao.conan.common.model.bo.RecordInfo;
import com.tal.wangxiao.conan.common.model.query.RequestQuery;
import com.tal.wangxiao.conan.common.redis.RedisTemplateTool;
import com.tal.wangxiao.conan.common.repository.db.*;
import com.tal.wangxiao.conan.common.utils.task.TaskStatusUtil;
import com.tal.wangxiao.conan.common.utils.DynamicEsUtils;
import com.tal.wangxiao.conan.sys.common.exception.CustomException;
import com.tal.wangxiao.conan.utils.enumutils.EnumUtil;
import com.tal.wangxiao.conan.utils.regex.RegexUtils;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author liujinsong
 * @date 2020/1/8
 */
@Service
@Slf4j
public class RecordServiceImpl implements RecordService {

    @Resource
    private RecordRepository recordRepository;

    @Resource
    private RecordDetailRepository recordDetailRepository;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private TaskExecutionRepository taskExecutionRepository;

    @Resource
    private DepartmentRepository departmentRepository;


    @Resource
    private UserRepository userRepository;

    @Resource
    private DomainRepository domainRepository;

    @Resource
    private ApiRepository apiRepository;

    @Resource
    private TaskApiRelationRepository taskApiRelationRepository;

    @Resource
    private RecordResultRepository recordResultRepository;

    @Resource
    private TaskStatusUtil taskStatusUtil;


    @Resource
    private EsConditionSettingRepository esConditionSettingRepository;

    @Resource
    private EsSourceRepository esSourceRepository;

    @Resource
    private RedisTemplateTool redisTemplateTool;


    //根据任务执行ID获取任务执行信息
    @Override
    public Result findByTaskExecutionId(Integer taskExecutionId) throws Exception {
        RecordInfo recordInfo = new RecordInfo();
        List<RecordDetailInfo> recordDetailInfoList = Lists.newArrayList();
        recordInfo.setRecordDetailInfoList(recordDetailInfoList);
        recordInfo.setTaskExecutionId(taskExecutionId);

        Optional<Record> recordOptional = recordRepository.findFirstByTaskExecutionId(taskExecutionId);
        if (!recordOptional.isPresent()) {
            return new Result(ResponseCode.INVALID_RECORD_ID, ResponseCode.INVALID_RECORD_ID.getDesc());
        }
        Record record = recordOptional.get();
        recordInfo.setRecordId(record.getId());
        recordInfo.setApiCount(record.getApiCount());
        recordInfo.setStartAt(record.getStartTime());
        recordInfo.setEndAt(record.getEndTime());
        recordInfo.setSuccessRate(record.getSuccessRate());
        Optional<User> userOptional = userRepository.findById(record.getOperateBy());
        if (!userOptional.isPresent()) {
            return new Result(ResponseCode.INVALID_USER_ID, "没有找到有效的录制人信息");
        }
        recordInfo.setOperateBy(userOptional.get().getNickName());
        Optional<TaskExecution> taskExecutionOptional = taskExecutionRepository.findById(taskExecutionId);
        if (!taskExecutionOptional.isPresent()) {
            return new Result(ResponseCode.INVALID_TASK_EXECUTION_ID, "该任务执行ID无效，找不到有关task_execution_id=" + taskExecutionId + "的执行信息");

        }
        Optional<Task> taskOptional = taskRepository.findById(taskExecutionOptional.get().getTaskId());
        if (!taskOptional.isPresent()) {
            return new Result(ResponseCode.INVALID_TASK_ID, "未找到任务实体，task_execution_id = " + taskExecutionId + ", task_id = " + taskExecutionOptional.get().getTaskId());
        }
        recordInfo.setTaskId(taskOptional.get().getId());
        recordInfo.setTaskName(taskOptional.get().getName());

        Optional<Department> departmentOptional = departmentRepository.findById(taskOptional.get().getDepartmentId());
        if (!departmentOptional.isPresent()) {
            return new Result(ResponseCode.INVALID_DEPARTMENT_ID, "未找到部门实体：task_id = " + taskOptional.get().getId() + ", department_id = " + taskOptional.get().getDepartmentId());
        }
        recordInfo.setDepartmentId(departmentOptional.get().getId());
        recordInfo.setDepartment(departmentOptional.get().getDeptName());
        List<RecordDetail> recordDetailList = recordDetailRepository.findByRecordId(record.getId());
        if (recordDetailList.isEmpty()) {
            return new Result(ResponseCode.INVALID_RECORD_ID, "未找到录制详情实体：record_id = " + record.getId());
        }

        for (RecordDetail recordDetail : recordDetailList) {
            RecordDetailInfo recordDetailInfo = new RecordDetailInfo();
            Optional<Api> apiOptional = apiRepository.findById(recordDetail.getApiId());
            if (!apiOptional.isPresent()) {
                return new Result(ResponseCode.INVALID_API_ID, "未找到接口实体：api_id = " + recordDetail.getApiId() + ", record_id = " + recordDetail.getRecordId());
            }
            recordDetailInfo.setApiId(apiOptional.get().getId());
            recordDetailInfo.setApiName(apiOptional.get().getName());
            recordDetailInfo.setDomainId(apiOptional.get().getDomainId());
            recordDetailInfo.setApiMethod(EnumUtil.getByField(HttpMethodConstants.class, "getValue", String.valueOf(apiOptional.get().getMethod())).getLabel());
            recordDetailInfo.setRecordableCount(apiOptional.get().getRecordableCount());
            int expectCount = recordDetail.getExpectCount();
            if (expectCount == 0) {
                expectCount = 1;
            }
            recordDetailInfo.setExpectCount(expectCount);
            recordDetailInfo.setActualCount(expectCount);
            Integer successRate = (recordDetail.getActualCount() * 100) / expectCount;
            recordDetailInfo.setSuccessRate(String.valueOf(successRate) + "%");

            Optional<Domain> domainOptional = domainRepository.findById(apiOptional.get().getDomainId());
            if (!domainOptional.isPresent()) {
                return new Result(ResponseCode.INVALID_API_ID, "未找到域名实体：domain_id = " + apiOptional.get().getDomainId() + ", api_id = " + apiOptional.get().getId());
            }
            recordDetailInfo.setDomainName(domainOptional.get().getName());
            recordDetailInfoList.add(recordDetailInfo);
        }

        return new Result(ResponseCode.SUCCESS, recordInfo);
    }

    //admin下发录制任务 agent初始化录制任务
    @Override
    public void startRecord(Integer taskExecutionId, Integer recordId) throws Exception {
        redisTemplateTool.setRecordProgress(recordId, "1");
        redisTemplateTool.setLogByRecordId_START(recordId, "Agent收到任务消息，即将开始录制录制：record_id=" + recordId);
        try {
            taskStatusUtil.updateTaskStatus(taskExecutionId, TaskStatus.RECORD);
        } catch (CustomException e) {
            //注册等处理流程发生错误
            log.error("更新状态失败" + e);
            taskStatusUtil.updateTaskStatus(taskExecutionId, TaskStatus.RECORD_FAIL);
        }
        //获取执行人  后面修改
        //LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Integer taskId = taskExecutionRepository.findById(taskExecutionId).get().getTaskId();
        Optional<TaskExecution> taskExecutionOptional = taskExecutionRepository.findById(taskExecutionId);
        if (!taskExecutionOptional.isPresent()) {
            throw new BaseException("录制失败，找不到执行记录：taskExecutionId=" + taskExecutionId);
        }
        //创建录制对象成功，创建录制详情
        Optional<List<TaskApiRelation>> taskApiRelationList = taskApiRelationRepository.findAllByTaskId(taskId);
        if (!taskApiRelationList.isPresent()) {
            throw new BaseException("录制失败，该任务找不到接口关联关系：taskId=" + taskId);
        }
        List<Integer> apiList = Lists.newArrayList();
        for (TaskApiRelation taskApiRelation : taskApiRelationList.get()) {
            Integer apiId = taskApiRelation.getApiId();
            apiList.add(apiId);
        }
        for (TaskApiRelation taskApiRelation : taskApiRelationList.get()) {
            Integer apiId = taskApiRelation.getApiId();
            log.info("apiId=" + apiId);
            Integer recordCount = taskApiRelation.getRecordCount();
            createRecordDetail(recordId, apiId, recordCount);
        }
        //开始执行录制逻辑
        record(recordId);

    }


    //生成任务录制详情信息信息
    private void createRecordDetail(Integer recordId, Integer apiId, Integer recordCount) {
        RecordDetail recordDetail = new RecordDetail();
        recordDetail.setRecordId(recordId);
        recordDetail.setApiId(apiId);
        recordDetail.setExpectCount(recordCount);
        recordDetailRepository.save(recordDetail);
    }

    //流量采集业务逻辑
    public void record(Integer recordId) throws Exception {
        redisTemplateTool.setRecordProgress(recordId, "5");
        log.info("正在开始录制，录制ID：record_id=" + recordId);
        redisTemplateTool.setLogByRecordId_INFO(recordId, "正在开始录制，录制ID：record_id=" + recordId);
        Optional<Record> recordOptional = recordRepository.findById(recordId);
        if (!recordOptional.isPresent()) {
            log.error("无效的录制ID：record_id = " + recordId);
            throw new BaseException("无效的录制ID：record_id = ");
        }
        List<RecordDetail> recordDetailList = recordDetailRepository.findByRecordId(recordId);
        if (Objects.isNull(recordDetailList) || recordDetailList.isEmpty()) {
            log.error("无找到录制详情实体：record_id = " + recordId);
            throw new BaseException("无找到录制详情实体：record_id = " + recordId);
        }
        Integer totalExpectCount = 0;
        Integer totalActualCount = 0;
        for (RecordDetail recordDetail : recordDetailList) {
            Optional<Api> apiOptional = apiRepository.findById(recordDetail.getApiId());
            if (!apiOptional.isPresent()) {
                //把对应的回放ID的日志写入Redis, key='logByrecordId='+recordId
                log.error("未找到该接口api_id = " + recordDetail.getApiId());
                throw new BaseException("未找到该接口api_id = " + recordDetail.getApiId());
            }
            Api api = apiOptional.get();
            Optional<Domain> domainOptional = domainRepository.findById(api.getDomainId());
            if (!domainOptional.isPresent()) {
                log.error("未找到该域名domain_id = " + api.getDomainId());
                throw new BaseException("未找到该域名domain_id = " + api.getDomainId());
            }
            String domain = domainOptional.get().getName();
            RequestQuery requestQuery = RequestQuery.builder()
                    .domain(domain)
                    .api(api.getName())
                    .method(api.getMethod())
                    .count(recordDetail.getExpectCount()).build();
            totalExpectCount += recordDetail.getExpectCount();
            log.info("开始录制流量 " + domainOptional.get().getName() + api.getName());
            redisTemplateTool.setLogByRecordId_INFO(recordId, "开始录制流量 " + domainOptional.get().getName() + api.getName());
            //查询并存储流量
            List<String> flowByEsClient = getFlowByEsClientAndSaveFLow(requestQuery, recordId, api.getDomainId());
            log.info(api.getName() + "录制完成: 期望录制总数=" + recordDetail.getExpectCount() + "," + "实际录制总数=" + flowByEsClient.size());
            redisTemplateTool.setLogByRecordId_INFO(recordId, api.getName() + "录制完成: 期望录制总数=" + recordDetail.getExpectCount() + "," + "实际录制总数=" + flowByEsClient.size());
            totalActualCount += flowByEsClient.size();
            recordDetail.setActualCount(flowByEsClient.size());
            recordDetailRepository.save(recordDetail);
        }
        redisTemplateTool.setRecordProgress(recordId, "99");
        double successRate = 0;//录制成功率
        if (totalExpectCount <= 0) {
            successRate = 0;
        } else {
            successRate = (totalActualCount * 100.0) / totalExpectCount;
        }
        recordOptional.get().setSuccessRate(successRate);
        recordOptional.get().setEndTime(LocalDateTime.now());
        recordRepository.save(recordOptional.get());
        log.info("录制任务完成 任务ID={} 成功率={}", recordId, successRate);
        redisTemplateTool.setLogByRecordId_END(recordId, "录制任务执行完成，录制完成率:" + successRate + "%");
        taskStatusUtil.updateTaskStatus(recordOptional.get().getTaskExecutionId(), TaskStatus.RECORD_SUCCESS);
    }

    /**
     * 获取ES流量
     *
     * @param requestQuery
     * @return ES 数据源配置，域名需要绑定ES数据源
     */
    public List<String> getFlowByEsClientAndSaveFLow(RequestQuery requestQuery, Integer recordId, Integer domainId) {
        String domainKeyword = "";
        String url = "";
        String method = EnumUtil.getByField(HttpMethodConstants.class, "getValue", String.valueOf(requestQuery.getMethod())).getLabel();
        SearchResponse searchResponse = null;
        List<String> requestIdLists = new ArrayList<String>();
        long totalElements = 0;
        int scanElements = requestQuery.getCount();
        //根据域名获取es mapping设置
        Optional<EsConditionSetting> esConditionSetting = esConditionSettingRepository.findByDomainId(domainId);
        if (esConditionSetting.isPresent()) {
            domainKeyword = esConditionSetting.get().getDomain();
            url = esConditionSetting.get().getApi();
        } else {
            log.info("域名" + requestQuery.getDomain() + "无es mapping信息配置");
            return null;
        }

        QueryBuilder shouldQuery = QueryBuilders.boolQuery()
                .should(QueryBuilders.termQuery(domainKeyword + ".keyword", requestQuery.getDomain()));
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.wildcardQuery(url + ".keyword", method + " " + requestQuery.getApi() + "*"))
                .must(shouldQuery);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        log.info(queryBuilder.toString());
        sourceBuilder.query(queryBuilder);
        sourceBuilder.sort("@timestamp", SortOrder.ASC);
        sourceBuilder.size(500);
        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10L));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder).scroll(scroll).indices();
        //根据域名获取对应es配置信息
        RestHighLevelClient restHighLevelClient = getRestHighLevelClient(domainId);
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            redisTemplateTool.setLogByRecordId_WARN(recordId, "日志ES查询失败");
            log.error("日志查询失败" + e);
        }
        String scrollId = searchResponse.getScrollId();
        log.info("scroll_id=" + scrollId);
        totalElements = searchResponse.getHits().totalHits;
        int total = (int) totalElements;
        log.info("查询到总流量" + totalElements + "条");
        int length = searchResponse.getHits().getHits().length;
        log.info("查询到总流量 length" + length + "");
        int page = total / 10000;//计算总页数,每次搜索数量为分片数*设置的size大小
        page = page + 1;
        //控制单个接口录制条数
        for (int i = 0; i < page; i++) {
            if (i != 0) {
                SearchScrollRequest scrollRequest = new SearchScrollRequest(searchResponse.getScrollId());
                scroll = new Scroll(TimeValue.timeValueMinutes(10L));
                scrollRequest.scroll(scroll);
                try {
                    searchResponse = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
                } catch (Exception e) {
                    log.error("日志查询失败");
                }
            }
            //控制录制总流量数
            scanElements += length;
            if (scanElements >= 800000) {
                redisTemplateTool.setLogByRecordId_WARN(recordId, "扫描的流量数已达到100W条，将结束录制");
                break;
            }
        }
        // 业务逻辑
        for (SearchHit at : searchResponse.getHits().getHits()) {
            String requestId = at.getId();
            log.info(requestId);
            if (!"".equals(requestId)) {
                requestIdLists.add(requestId);
                //遍历流量并且存储
                saveFlowInDb(recordId, at, esConditionSetting.get());
                if (requestIdLists.size() == requestQuery.getCount()) {
                    break;
                }
            }
        }
        //清除scroll
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);//也可以选择setScrollIds()将多个scrollId一起使用
        ClearScrollResponse clearScrollResponse = null;
        try {
            clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean succeeded = clearScrollResponse.isSucceeded();
        log.info("ES succeeded = " + succeeded);
        return requestIdLists;
    }

    /**
     * 根据domain获取restHighLevelClient
     *
     * @param domainId
     * @return restHighLevelClient
     */
    private RestHighLevelClient getRestHighLevelClient(Integer domainId) {
        Optional<Domain> domainOptional = domainRepository.findById(domainId);
        if (domainOptional.isPresent()) {
            Optional<EsConditionSetting> esConditionSetting = esConditionSettingRepository.findByDomainId(domainOptional.get().getId());
            if (esConditionSetting.isPresent()) {
                Optional<EsSource> esSource = esSourceRepository.findById(domainOptional.get().getEsSourceId());
                if (esSource.isPresent()) {
                    String esIp = esSource.get().getEsIp();
                    Integer esPort = esSource.get().getEsPort();
                    String beanName = esSource.get().getEsBeanName();
                    return DynamicEsUtils.getRestHighLevelClient(beanName, esIp, esPort);
                }
            }
        }
        return null;
    }


    /**
     * 根据ES内流量数据存储至DB
     *
     * @param recordId,hit,esConditionSetting
     */
    private void saveFlowInDb(int recordId, SearchHit hit, EsConditionSetting esConditionSetting) {
        RecordResult recordResult = new RecordResult();
        Map esFlowMap = hit.getSourceAsMap();
        String apiKey = esConditionSetting.getApi();
        //当URL中GET 请求参数是单独的字段存储的时候，采用接口key?参数key
        if (apiKey.contains("?")) {
            String[] urlPathKeys = apiKey.split("\\?");
            apiKey = urlPathKeys[0];
        }
        String domianKey = esConditionSetting.getDomain();
        String methodKey = esConditionSetting.getMethod();
        String requestBodyKey = esConditionSetting.getRequestBody();
        String headerKey = esConditionSetting.getHeader();
        // 在获取不到key时避免抛出 NullPointerException 空指针异常
        String apiName = RegexUtils.getMsgByRegex(esFlowMap.get(apiKey) + "", esConditionSetting.getApiRegex());
        String method = RegexUtils.getMsgByRegex(esFlowMap.get(methodKey) + "", esConditionSetting.getMethodRegex());
        Api api = getApiInfo(recordId,method,apiName);
        if (api !=null) {
            int apiId = api.getId();
            String body = RegexUtils.getMsgByRegex(esFlowMap.get(requestBodyKey) + "", esConditionSetting.getRequestBodyRegex());
            String header = RegexUtils.getMsgByRegex(esFlowMap.get(headerKey) + "", esConditionSetting.getHeaderRegex());
            String requestId = hit.getId();
            recordResult.setApiId(apiId);
            recordResult.setBody(body);
            recordResult.setHeader(header);
            recordResult.setRequestId(requestId);
            recordResult.setRecordId(recordId);
            recordResultRepository.save(recordResult);
        } else {
            log.error("未查询到api数据 API={}", apiName);
        }

    }


    private Api getApiInfo(Integer recordId, String method,String apiName) {
        List<Api> apiList = apiRepository.findByNameAndMethod(apiName, HttpMethodConstants.valueOf(method).getValue());
        if(apiList == null) {
            return null;
        }
        if(apiList.size()<1) {
            return null;
        }
        List<RecordDetail> recordDetailList = recordDetailRepository.findByRecordId(recordId);
        for (Api api : apiList) {

            if (recordDetailList.isEmpty()) {
                continue;
            }
            for (RecordDetail recordDetail : recordDetailList) {
                if (recordDetail.getApiId().intValue() == api.getId().intValue()) {
                    return api;
                }
            }
        }
        return apiList.get(0);
    }

    public static void main(String[] arge) {
        String apiKey = "cs_ui_stem?uri_pslist";
        if (apiKey.contains("?")) {
            String[] urlPathKeys = apiKey.split("\\?");
        }
    }


}
