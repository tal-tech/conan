package com.tal.wangxiao.conan.common.service.common;

import com.tal.wangxiao.conan.common.domain.ApiInfo;
import com.tal.wangxiao.conan.common.domain.ApiRule;
import com.tal.wangxiao.conan.common.domain.DeptJsonRule;
import com.tal.wangxiao.conan.common.domain.ReplaySchemaError;
import com.tal.wangxiao.conan.common.exception.CustomException;
import com.tal.wangxiao.conan.common.mapper.ReplaySchemaErrorMapper;
import com.tal.wangxiao.conan.common.redis.RedisTemplateTool;
import com.tal.wangxiao.conan.common.service.ApiRuleService;
import com.tal.wangxiao.conan.common.service.ApiService;
import com.tal.wangxiao.conan.common.service.DeptJsonRuleService;
import com.tal.wangxiao.conan.common.service.ReplaySchemaErrorService;
import com.tal.wangxiao.conan.utils.json.JsonCheckUtils;
import com.tal.wangxiao.conan.utils.schema.JSONSchemaResult;
import com.tal.wangxiao.conan.utils.schema.api.JSONSchemaUtils;
import com.tal.wangxiao.conan.utils.schema.dept.ProductLineJsonSchemaUtils;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * @author ：dengkunnan
 * @date ：2021.01.10
 * @description： 精准规则验证相关处理,
 * 1.为了避免大量数据写入数据，对回放响应json对比成功的记录不写入数据库
 * 2.不配置api校验和产品线校验不做处理
 * @version: 1.0.0.1
 */

@Slf4j
@Service
public class RuleCheckoutService {

    @Autowired
    RedisTemplateTool redisTemplateTool;


    @Autowired
    ApiService apiService;

    @Autowired
    ReplaySchemaErrorService replaySchemaErrorService;

    @Autowired
    ApiRuleService apiRuleService;


    @Autowired
    DeptJsonRuleService deptJsonRuleService;

    @Autowired
    ReplaySchemaErrorMapper replaySchemaErrorMapper;


    /**
     * @param apiId   接口ID
     * @param replayId   回放ID
     * @param requstBody   请求Body
     * @param header      请求头信息
     * @param response   响应信息
     */
    public void checketRule(Integer apiId, Integer replayId, String header, String requstBody, String response) throws CustomException {
        JSONSchemaResult jsonSchemaResult = new JSONSchemaResult();
        //出现api response 非 JSON
        if (!JsonCheckUtils.isJSONValidByalibaba(response)) {
            log.error("aresponse 不是JSON piId = {0}， response= {1}" , apiId, response);
            ApiInfo apiInfo = apiService.selectApiById(apiId);
            //当要求的api response 本来就可以是非json， 不进行Schema验证
            if (apiInfo.getResponseIsJson() == 1) {
                jsonSchemaResult.setIsSuccess(true);
                return;
            } else {
                jsonSchemaResult.setIsSuccess(false);
                jsonSchemaResult.setDesc("发现response 不是JSON,请检查请求参数是否正确");
                insertReplayResponseErrorDao(apiId, replayId, header, requstBody, response, jsonSchemaResult);
            }
        }
        schemaHandler(apiId, replayId, header, requstBody, response, jsonSchemaResult);
    }


    private void schemaHandler(Integer apiId, Integer replayId, String header, String requstBody, String response, JSONSchemaResult jsonSchemaResult) throws CustomException {
        ApiRule apiRule = new ApiRule();
        apiRule.setApiId(apiId);
        List<ApiRule> apiRuleList = apiRuleService.selectApiRuleList(apiRule);
        if (StringHandlerUtils.isNull(apiRuleList)) {
            redisTemplateTool.setLogByRecordId_ERROR(replayId, "该ApiId数据库没有发现，请检查数据库信息apiId =" + apiId);
            throw new CustomException("该ApiId数据库没有发现，请检查数据库信息apiId =" + apiId);
        }
        //api没有配置验证JSON,走产品线验证流程
        if (StringHandlerUtils.isNull(apiRuleList)) {
            jsonSchemaResult = productLineRuleChecke(apiId, replayId, response);
            if (jsonSchemaResult.getIsSuccess()) {
                //验证成功不入库
                return;
            }
            insertReplayResponseErrorDao(apiId, replayId, header, requstBody, response, jsonSchemaResult);
            return;
        }

        //针对接口级别处理
        //当有一个Schema 符合就跳出，认为这次校验是正常
        StringBuffer stringBuffer = new StringBuffer();
        for (ApiRule apiRuleDb : apiRuleList) {
            jsonSchemaResult = JSONSchemaUtils.verifyJsonSchemaByString(apiRuleDb.getRuleJson(), response);
            if (jsonSchemaResult.getIsSuccess()) {
                //验证成功不入库
                return;
            } else {
                //合并记录错误信息
                stringBuffer = stringBuffer.append(jsonSchemaResult.getDesc()).append("; ");
            }
        }
        jsonSchemaResult.setDesc(stringBuffer);
        insertReplayResponseErrorDao(apiId, replayId, header, requstBody, response, jsonSchemaResult);
    }

    /**
     * 产品线规则校验
     */
    private JSONSchemaResult productLineRuleChecke(Integer apiId, Integer replayId, String response) throws CustomException {
        List<DeptJsonRule> deptJsonRuleList = replaySchemaErrorMapper.getRuleByDeptId(replayId);
        JSONSchemaResult jsonSchemaResult = new JSONSchemaResult();
        jsonSchemaResult.setIsSuccess(true);
        if (StringHandlerUtils.isNull(deptJsonRuleList)) {
            log.debug("该产品线没有配置产品线规则校验");
            throw new CustomException("该产品线没有配置产品线规则校验");
        }

        //当有一个Schema 符合就跳出，认为这次校验是正常
        StringBuffer stringBuffer = new StringBuffer();
        for (DeptJsonRule deptJsonRule : deptJsonRuleList) {
            if (StringHandlerUtils.isNull(deptJsonRule.getRuleJson())) {
                log.debug("该产品线没有配置产品线规则校验");
                throw new CustomException("该产品线没有配置产品线规则校验");
            }
            jsonSchemaResult = ProductLineJsonSchemaUtils.checkProductLineJson(deptJsonRule.getRuleJson(), response);
            if (jsonSchemaResult.getIsSuccess()) {
                return jsonSchemaResult;
            } else {
                //合并记录错误信息
                stringBuffer = stringBuffer.append(jsonSchemaResult.getDesc()).append("; ");
            }
        }
        jsonSchemaResult.setDesc(stringBuffer.toString());
        return jsonSchemaResult;
    }


    /**
     * @param apiId
     * @param replayId
     * @param requst
     * @param response
     * @param jsonSchemaResult 验证响应信息
     * @descrption 回放错误信息记录表插入
     */
    private void insertReplayResponseErrorDao(Integer apiId, Integer replayId, String header, String requst, String response, JSONSchemaResult jsonSchemaResult) {
        ReplaySchemaError replaySchemaError = new ReplaySchemaError();
        replaySchemaError.setApiId(apiId);
        replaySchemaError.setReplayId(replayId);
        replaySchemaError.setRequst(requst);
        replaySchemaError.setHeader(header);
        replaySchemaError.setResponse(response);
        if (!StringHandlerUtils.isNull(jsonSchemaResult)) {
            replaySchemaError.setErrorDesc(jsonSchemaResult.toString());
        } else {
            log.error("error :jsonSchemaResult is null，发现未知错误请检查接口信息及规则配置信息");
            replaySchemaError.setErrorDesc("发现未知错误请检查接口信息及规则配置信息");
        }
        replaySchemaErrorService.insertReplaySchemaError(replaySchemaError);
        log.info("replayId= {0},  返回信息和预期值不一样  , apiId= {1}" , replayId, apiId);
        redisTemplateTool.setLogByRecordId_ERROR(replayId, "返回信息和预期值不一样 =" + apiId);

    }

}
