package com.tal.wangxiao.conan.admin.service.impl;

import com.tal.wangxiao.conan.admin.service.HomeService;
import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.constant.ReplayConstants;
import com.tal.wangxiao.conan.common.constant.enums.AgentStatus;
import com.tal.wangxiao.conan.common.entity.db.AgentNode;
import com.tal.wangxiao.conan.common.entity.db.Version;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.home.HomeBasicData;
import com.tal.wangxiao.conan.common.repository.db.*;
import com.tal.wangxiao.conan.utils.date.DateHandleUtils;
import com.tal.wangxiao.conan.utils.number.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 首页大盘服务实现类
 *
 * @author huyaoguo
 * @date 2021/1/14
 **/
@Service
@Slf4j
public class HomeServiceImpl implements HomeService {

    @Resource
    private ApiRepository apiRepository;

    @Resource
    private ReplayRepository replayRepository;

    @Resource
    private ReplayDetailRepository replayDetailRepository;

    @Resource
    private DiffRepository diffRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private VersionRepository versionRepository;

    @Resource
    private TaskExecutionRepository taskExecutionRepository;

    @Resource
    private AgentNodeRepository agentNodeRepository;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = "all_count", key = "#cacheId")
    @Override
    public Result<Object> getAllCount(String cacheId) {
        HomeBasicData homeBasicData = new HomeBasicData();
        homeBasicData.setApiCount(NumberUtils.numberFormat(apiRepository.findAll().size()));
        homeBasicData.setReplayCount(NumberUtils.numberFormat(replayRepository.findAll().size()));
        homeBasicData.setDiffCount(NumberUtils.numberFormat(diffRepository.findAll().size()));
        //回放总流量
        int replayFlows = 0;
        if (!Objects.isNull(replayDetailRepository.getAllFlows())) {
            replayFlows = replayDetailRepository.getAllFlows();
        }
        homeBasicData.setReplayFlows(NumberUtils.numberFormat(replayFlows));
        homeBasicData.setUserCount(NumberUtils.numberFormat(userRepository.findAll().size()));
        homeBasicData.setPvCount(NumberUtils.numberFormat(versionRepository.findFirstByOrderByIdDesc().get().getPvCount()));
        return new Result<>(ResponseCode.SUCCESS, homeBasicData);
    }

    @Cacheable(value = "important_data", key = "#cacheId")
    @Override
    public Result<Object> getImportantData(String cacheId) {
        List<Map<String, Object>> resList = new ArrayList<>();
        //默认获取7天数据
        int duration = 7;
        for (int i = duration - 1; i >= 0; i--) {
            Map<String, Object> resMap = new HashMap<>();
            LocalDate basicTime = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = LocalDateTime.of(basicTime, LocalTime.MIN);//开始时间
            LocalDateTime dayEnd = LocalDateTime.of(basicTime, LocalTime.MAX);//结束时间
            resMap.put("time", DateHandleUtils.dateToStrWithMMdd(basicTime));
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("定时巡检次数", replayRepository.countByStartAtBetweenAndReplayType(dayStart, dayEnd, ReplayConstants.REPLAY_SCHEDULE));
            valueMap.put("外部发布次数", replayRepository.countByStartAtBetweenAndReplayType(dayStart, dayEnd, ReplayConstants.REPLAY_DEPLOY));
            resMap.put("value", valueMap);
            resList.add(resMap);
        }
        return new Result<>(ResponseCode.SUCCESS, resList);
    }

    @Cacheable(value = "task_rank", key = "#cacheId+'-'+#count")
    @Override
    public Result<Object> getTaskRank(Integer count, String cacheId) {
        //通过当前时间获取缓存key,每小时缓存一次
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime dataFrom = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        String beginTime = dataFrom.format(df);
        List<Map<String, Object>> resList = taskExecutionRepository.getTaskRankByTimeAndStatus(beginTime, 4, count);
        if (Objects.isNull(resList)) {
            return new Result<>(ResponseCode.INVALID_TASK_ID, "获取数据异常");
        }
        return new Result<>(ResponseCode.SUCCESS, resList);
    }

    @Override
    public Result<Object> getAgentNode() {
        Map<String, Object> resMap = new HashMap<>();
        List<AgentNode> agentNodeList1 = agentNodeRepository.findByStatus(AgentStatus.READY.getValue());
        resMap.put(AgentStatus.READY.getLabel(), agentNodeList1);
        List<AgentNode> agentNodeList2 = agentNodeRepository.findByStatus(AgentStatus.FREE.getValue());
        resMap.put(AgentStatus.FREE.getLabel(), agentNodeList2);
        List<AgentNode> agentNodeList3 = agentNodeRepository.findByStatus(AgentStatus.BUSY.getValue());
        resMap.put(AgentStatus.BUSY.getLabel(), agentNodeList3);

        return new Result<>(ResponseCode.SUCCESS, resMap);
    }

    @Override
    public Result<Object> getDepartmentData() {
        return new Result<>(ResponseCode.SUCCESS, redisTemplate.opsForValue().get("home_department_data"));
    }

    @Override
    public Result<Object> addDot() {
        Optional<Version> versionOptional = versionRepository.findFirstByOrderByIdDesc();
        if (!versionOptional.isPresent()) {
            return new Result<>(ResponseCode.NO_VERSION_INFO, "找不到版本信息");
        }
        Version version = versionOptional.get();
        version.setPvCount(version.getPvCount() + 1);
        versionRepository.save(version);
        return new Result<>(ResponseCode.SUCCESS, "ok");
    }


    public static void main(String[] args) {
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);//当天零点
        System.out.println(today_start);
    }
}
