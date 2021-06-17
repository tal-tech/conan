package com.tal.wangxiao.conan.admin.service.impl;

import com.google.common.collect.Lists;
import com.sun.jndi.toolkit.url.UrlUtil;
import com.tal.wangxiao.conan.admin.constant.HttpMethod;
import com.tal.wangxiao.conan.admin.service.RecordService;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.entity.db.*;
import com.tal.wangxiao.conan.common.exception.BaseException;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.bo.RecordDetailInfo;
import com.tal.wangxiao.conan.common.model.bo.RecordInfo;
import com.tal.wangxiao.conan.common.redis.RedisTemplateTool;
import com.tal.wangxiao.conan.common.repository.db.*;
import com.tal.wangxiao.conan.common.service.common.ElasticCommonService;
import com.tal.wangxiao.conan.common.utils.DownloadFileUtil;
import com.tal.wangxiao.conan.utils.enumutils.EnumUtil;
import com.tal.wangxiao.conan.utils.es.EsUtils;
import com.tal.wangxiao.conan.utils.excel.ExcelHanderUtil;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @author liujinsong
 * @date 2020/1/14
 */
@Service
@Slf4j
public class RecordServiceImpl implements RecordService{
    @Resource
    private RecordRepository recordRepository;

    @Resource
    private RedisTemplateTool redisTemplateTool;

    @Resource
    private UserRepository userRepository;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private DomainRepository domainRepository;

    @Resource
    private ApiRepository apiRepository;

    @Resource
    private ElasticCommonService elasticCommonService;

    @Resource
    private RedisTemplate<String, String> template;

    @Resource
    private DepartmentRepository departmentRepository;

    @Resource
    private TaskExecutionRepository taskExecutionRepository;

    @Resource
    private RecordDetailRepository recordDetailRepository;

    @Resource
    private EsConditionSettingRepository esConditionSettingRepository;


    @Override
    public Result<String> findRecordProgress(Integer taskExecutionId) {
        Optional<Record> recordOptional = recordRepository.findFirstByTaskExecutionId(taskExecutionId);
        if (!recordOptional.isPresent()) {
            log.error("该任务执行ID没有执行记录:" + taskExecutionId);
            return new Result<>(ResponseCode.INVALID_TASK_EXECUTION_ID, "该任务执行ID没有执行记录");
        }
        try {
            return new Result<>(ResponseCode.SUCCESS, redisTemplateTool.getRecordProgress(recordOptional.get().getId()));
        } catch (Exception e) {
            log.error("taskExecutionId={}获取录制进度异常:{}", taskExecutionId, e.getMessage());
            return new Result<>(ResponseCode.REDIS_EXCEPTION, "获取录制进度异常:" + e.getMessage());

        }
    }

    @Override
    public Result<Object> findByTaskExecutionId(Integer taskExecutionId) {
        RecordInfo recordInfo = new RecordInfo();
        List<RecordDetailInfo> recordDetailInfoList = Lists.newArrayList();
        recordInfo.setRecordDetailInfoList(recordDetailInfoList);
        recordInfo.setTaskExecutionId(taskExecutionId);

        Optional<Record> recordOptional = recordRepository.findFirstByTaskExecutionId(taskExecutionId);
        if (!recordOptional.isPresent()) {
            return new Result<>(ResponseCode.INVALID_TASK_EXECUTION_ID, "没有找到录制实体");
        }
        Record record = recordOptional.get();
        recordInfo.setRecordId(record.getId());
        recordInfo.setApiCount(record.getApiCount());
        recordInfo.setStartAt(record.getStartTime());
        recordInfo.setEndAt(record.getEndTime());
        recordInfo.setSuccessRate(record.getSuccessRate());

        Optional<User> userOptional = userRepository.findById(record.getOperateBy());
        if (!userOptional.isPresent()) {
            return new Result<>(ResponseCode.INVALID_USER_ID, "没有找到有效的录制人信息");
        }
        recordInfo.setOperateBy(userOptional.get().getUserName());
        Optional<TaskExecution> taskExecutionOptional = taskExecutionRepository.findById(taskExecutionId);
        if (!taskExecutionOptional.isPresent()) {
            return new Result<>(ResponseCode.INVALID_TASK_EXECUTION_ID, "该任务执行ID无效，找不到有关task_execution_id=" + taskExecutionId + "的执行信息");

        }
        Optional<Task> taskOptional = taskRepository.findById(taskExecutionOptional.get().getTaskId());
        if (!taskOptional.isPresent()) {
            return new Result<>(ResponseCode.INVALID_TASK_ID, "未找到任务实体，task_execution_id = " + taskExecutionId + ", task_id = " + taskExecutionOptional.get().getTaskId());
        }
        recordInfo.setTaskId(taskOptional.get().getId());
        recordInfo.setTaskName(taskOptional.get().getName());

        Optional<Department> departmentOptional = departmentRepository.findById(taskOptional.get().getDepartmentId());
        if (!departmentOptional.isPresent()) {
            return new Result<>(ResponseCode.INVALID_DEPARTMENT_ID, "未找到部门实体：task_id = " + taskOptional.get().getId() + ", department_id = " + taskOptional.get().getDepartmentId());
        }
        recordInfo.setDepartmentId(departmentOptional.get().getId());
        recordInfo.setDepartment(departmentOptional.get().getDeptName());

        List<RecordDetail> recordDetailList = recordDetailRepository.findByRecordId(record.getId());
        if (recordDetailList.isEmpty()) {
            return new Result<>(ResponseCode.INVALID_RECORD_ID, "未找到录制详情实体：record_id = " + record.getId());
        }
        for (RecordDetail recordDetail : recordDetailList) {
            RecordDetailInfo recordDetailInfo = new RecordDetailInfo();
            Optional<Api> apiOptional = apiRepository.findById(recordDetail.getApiId());
            if (!apiOptional.isPresent()) {
                return new Result<>(ResponseCode.INVALID_API_ID, "未找到接口实体：api_id = " + recordDetail.getApiId() + ", record_id = " + recordDetail.getRecordId());
            }
            recordDetailInfo.setApiId(apiOptional.get().getId());
            recordDetailInfo.setApiName(apiOptional.get().getName());
            recordDetailInfo.setDomainId(apiOptional.get().getDomainId());
            recordDetailInfo.setApiMethod(EnumUtil.getByField(HttpMethod.class, "getValue", String.valueOf(apiOptional.get().getMethod())).getLabel());
            recordDetailInfo.setRecordableCount(apiOptional.get().getRecordableCount());
            int expectCount =recordDetail.getExpectCount();
            if(recordDetail.getExpectCount() == 0 ) {
                expectCount = 5;
            }
            recordDetailInfo.setExpectCount(expectCount);
            Integer actualCount = recordDetail.getActualCount();
            if (Objects.isNull(actualCount)) {
                actualCount = 5;
            }
            recordDetailInfo.setActualCount(actualCount);
            Integer successRate = (actualCount * 100) / expectCount;
            recordDetailInfo.setSuccessRate(successRate + "%");
            Optional<Domain> domainOptional = domainRepository.findById(apiOptional.get().getDomainId());
            if (!domainOptional.isPresent()) {
                return new Result<>(ResponseCode.INVALID_API_ID, "未找到域名实体：domain_id = " + apiOptional.get().getDomainId() + ", api_id = " + apiOptional.get().getId());
            }
            recordDetailInfo.setDomainName(domainOptional.get().getName());
            recordDetailInfoList.add(recordDetailInfo);
        }

        return new Result<>(ResponseCode.SUCCESS, recordInfo);
    }

    @Override
    public Result<Object> findLogByExecutionId(Integer taskExecutionId) {
        Optional<Record> recordOptional = recordRepository.findFirstByTaskExecutionId(taskExecutionId);
        if (!recordOptional.isPresent()) {
            return new Result<>(ResponseCode.INVALID_TASK_EXECUTION_ID, "该任务执行ID没有执行记录");
        }
        String res = template.opsForValue().get("recordLog=" + recordOptional.get().getId());
        return new Result<>(ResponseCode.SUCCESS, res);
    }

    @Override
    public void getGetFlowsByDomain(String domainName, HttpServletResponse response) throws Exception{
        Map<String,Integer> apiMap = new HashMap<>();
        //根据域名获取es mapping设置
        String domainKeyword = "";
        String apiKeyWord = "";
        String methodKeyword = "";
        Optional<EsConditionSetting> esConditionSetting = esConditionSettingRepository.findByDomainId(domainRepository.findByName(domainName).get().getId());
        if (esConditionSetting.isPresent()) {
            domainKeyword = esConditionSetting.get().getDomain();
            apiKeyWord = esConditionSetting.get().getApi();
            methodKeyword = esConditionSetting.get().getMethod();
        } else {
            log.info("域名" + domainName + "无es mapping信息配置");
            throw new BaseException("域名" + domainName + "无es mapping信息配置");
        }
        QueryBuilder shouldQuery = QueryBuilders.boolQuery()
                .should(QueryBuilders.termQuery(domainKeyword+".keyword", domainName));
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(shouldQuery);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        log.info(queryBuilder.toString());
        sourceBuilder.query(queryBuilder);
        sourceBuilder.sort("@timestamp", SortOrder.DESC);
        sourceBuilder.size(500);
        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(10L));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder).scroll(scroll).indices();
        //根据域名获取对应es配置信息
        RestHighLevelClient restHighLevelClient = elasticCommonService.getRestHighLevelClient(domainName);
        SearchResponse searchResponse = null;
        long totalElements = 0;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("日志查询失败" + e);
        }
        String scrollId = searchResponse.getScrollId();
        totalElements = searchResponse.getHits().totalHits;
        int total = (int) totalElements;
        log.info("查询到总流量" + totalElements + "条");
        int length = searchResponse.getHits().getHits().length;
        log.info("查询到总流量 length" + length + "");
        int page = total / 10000;//计算总页数,每次搜索数量为分片数*设置的size大小
        page = page + 1;
        int scanElements = 0;
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
            scanElements+=length;
            if(scanElements>=1000000){
                log.info("扫描流量超过100W，即将退出扫描");
                break;
            }
            // 业务逻辑
            for (SearchHit at : searchResponse.getHits()) {
                String method = at.getSourceAsMap().get(methodKeyword).toString();
                String url = EsUtils.getUrlByRequest(at.getSourceAsMap().get(apiKeyWord).toString());
                //根据方法名判断
                if ("OPTIONS".equals(method)||"HEAD".equals(method)) {
                    //再次过滤OPTIONS HEAD请求
                    continue;
                }
                //根据url判断
                if (EsUtils.shouldIgnore(url)||url==null||url.contains("null")||url.contains(".")) {
                    //再次过滤静态资源请求
                    continue;
                }
                url = EsUtils.replacePathParam(url).replaceAll("[&?!].*","");
                url = method + " "+ url;
                //url= url.replaceAll("null","{pp}");
                if(!apiMap.containsKey(url)){
                    apiMap.put(url,0);
                }else{
                    //控制单接口录制条数
                    if (apiMap.get(url) > 100) {
                        continue;
                    }
                    apiMap.put(url,apiMap.get(url)+1);
                }
            }
        }
        try {
            saveExcelApiList(apiMap,domainName,response);
        }catch (Exception e){
            log.error("存储Excel异常");
            throw new BaseException("存储Excel异常");
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
    }

    private void saveExcelApiList(Map<String,Integer> apiMap,String domain, HttpServletResponse response) throws Exception {
        deleteExcel(domain);
        String path = ResourceUtils.getURL("classpath:").getPath() + "static/";//+domain+"域名接口.xlsx";
        String name = domain+".xlsx";
     /*  File file =new File(path);
       List<TestExcel> testExcelList =  readToList(2,3,file, TestExcel.class);
       System.out.println(testExcelList.toString());*/
        List shu = new ArrayList<Object>();
        List<String> title1 = new ArrayList<String>();
        title1.add("domainName");
        title1.add("apiName");
        title1.add("method");
        title1.add("isRead");
        title1.add("recordableCount");
        title1.add("department");
        title1.add("productLine");
        shu.add(title1);
        List<String> title = new ArrayList<String>();
        title.add("域名");
        title.add("接口名");
        title.add("请求方式（大写GET/POST）");
        title.add("读/写");
        title.add("可录制条数");
        title.add("部门");
        title.add("产品线");
        shu.add(title);
        for (String s:apiMap.keySet()) {
            String str[] = s.split(" ");
            String method = str[0];
            String apiName = str[1];
            List heng2 = new ArrayList<String>();
            heng2.add(domain);
            heng2.add(apiName);
            heng2.add(method);
            heng2.add("");
            heng2.add(apiMap.get(s));
            heng2.add("");
            heng2.add("");
            shu.add(heng2);
        }
        File file = ExcelHanderUtil.excelWriterReturnFile(0, 1, path + name, shu, "Conan 流量回放平台接入接口模板");
        if(response == null) {
            return;
        }
        DownloadFileUtil. downloadFile(name,file,response);
        log.info("写入excel完成");
    }

    public void deleteExcel(String domain) throws FileNotFoundException {
        log.info("deleteFileByDomain");
        try {
            String path = ResourceUtils.getURL("classpath:").getPath() + "static/";//+domain+"域名接口.xlsx";
            String name = domain+"域名接口.xlsx";
            File file = new File(path + name);
            file.delete();
        } catch (Exception e) {
            log.error("删除文件" + e);
        }
    }


}
