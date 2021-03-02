package com.tal.wangxiao.conan.common.service.impl;

import java.util.HashMap;
import java.util.List;

import com.tal.wangxiao.conan.common.api.ApiResponse;
import com.tal.wangxiao.conan.common.domain.ReplayErrorInterfaceListInfo;
import com.tal.wangxiao.conan.utils.str.StringHandlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tal.wangxiao.conan.common.mapper.ReplaySchemaErrorMapper;
import com.tal.wangxiao.conan.common.domain.ReplaySchemaError;
import com.tal.wangxiao.conan.common.service.ReplaySchemaErrorService;

/**
 * 回放schema错误记录Service业务层处理
 *
 * @author dengkunan
 * @date 2021-01-07
 */
@Service
@Slf4j
public class ReplaySchemaErrorServiceImpl implements ReplaySchemaErrorService {
    @Autowired
    private ReplaySchemaErrorMapper replaySchemaErrorMapper;

    /**
     * 查询回放schema错误记录
     *
     * @param schemaErrorId 回放schema错误记录ID
     * @return 回放schema错误记录
     */
    @Override
    public ReplaySchemaError selectReplaySchemaErrorById(Integer schemaErrorId) {
        return replaySchemaErrorMapper.selectReplaySchemaErrorById(schemaErrorId);
    }

    /**
     * 查询回放schema错误记录列表
     *
     * @param replaySchemaError 回放schema错误记录
     * @return 回放schema错误记录
     */
    @Override
    public List<ReplaySchemaError> selectReplaySchemaErrorList(ReplaySchemaError replaySchemaError) {
        return replaySchemaErrorMapper.selectReplaySchemaErrorList(replaySchemaError);
    }

    @Override
    public ApiResponse getResponseInterfaceNumber(Integer rePlayid) {
        ReplaySchemaError replayResponseErrorInfo = new ReplaySchemaError();
        replayResponseErrorInfo.setReplayId(rePlayid);
        List<ReplaySchemaError> apiRuleInfoList = replaySchemaErrorMapper.selectReplaySchemaErrorList(replayResponseErrorInfo);
        if (StringHandlerUtils.isNull(apiRuleInfoList)) {
            log.info("replayId = " + apiRuleInfoList + "回放ID未发现精准接口结构错误信息");
            ApiResponse.success("回放ID未发现精准接口结构错误信息");

        }
        return ApiResponse.success("", getReplayErrorInterfaceListInfo(apiRuleInfoList));
    }


    public ReplayErrorInterfaceListInfo getReplayErrorInterfaceListInfo(List<ReplaySchemaError> replayResponseErrorInfoList) {
        HashMap countHash = new HashMap<String, Integer>();
        ReplayErrorInterfaceListInfo replayErrorInterfaceListInfo = new ReplayErrorInterfaceListInfo();
        for (ReplaySchemaError info : replayResponseErrorInfoList) {
            if (StringHandlerUtils.isNull(countHash.get(info.getApiName()))) {
                countHash.put(info.getApiName(), 1);
            } else {
                Integer countKey = (Integer) countHash.get(info.getApiName());
                countHash.put(info.getApiName(), countKey +1);
            }
        }

        for (Object key : countHash.keySet()) {
            if (StringHandlerUtils.isNull(key)) {
                continue;
            }
            replayErrorInterfaceListInfo.getApiNameMap().put(key.toString(), (Integer) countHash.get(key));
        }

        replayErrorInterfaceListInfo.setCount(replayResponseErrorInfoList.size());
        replayErrorInterfaceListInfo.setApiCount(countHash.size());

        return replayErrorInterfaceListInfo;
    }

    /**
     * 新增回放schema错误记录
     *
     * @param replaySchemaError 回放schema错误记录
     * @return 结果
     */
    @Override
    public int insertReplaySchemaError(ReplaySchemaError replaySchemaError) {
        return replaySchemaErrorMapper.insertReplaySchemaError(replaySchemaError);
    }

    /**
     * 修改回放schema错误记录
     *
     * @param replaySchemaError 回放schema错误记录
     * @return 结果
     */
    @Override
    public int updateReplaySchemaError(ReplaySchemaError replaySchemaError) {
        return replaySchemaErrorMapper.updateReplaySchemaError(replaySchemaError);
    }

    /**
     * 批量删除回放schema错误记录
     *
     * @param schemaErrorIds 需要删除的回放schema错误记录ID
     * @return 结果
     */
    @Override
    public int deleteReplaySchemaErrorByIds(Integer[] schemaErrorIds) {
        return replaySchemaErrorMapper.deleteReplaySchemaErrorByIds(schemaErrorIds);
    }

    /**
     * 删除回放schema错误记录信息
     *
     * @param schemaErrorId 回放schema错误记录ID
     * @return 结果
     */
    @Override
    public int deleteReplaySchemaErrorById(Integer schemaErrorId) {
        return replaySchemaErrorMapper.deleteReplaySchemaErrorById(schemaErrorId);
    }
}