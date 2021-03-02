package com.tal.wangxiao.conan.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * 录制回放执行日志工具类
 * @author huyaoguo
 * @date 2020/1/7
 */
@Component
@Slf4j
public class RedisTemplateTool {

    @Resource
    private RedisTemplate<String, String> redisTemplateLog;

    private long redisCacheTime = 14;  //14天


    //录制日志
    public void setLogByRecordId_ERROR(Integer recordId, String errorDesc) {
        redisTemplateLog.opsForValue().append("recordLog=" + recordId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [ERROR]  --" + errorDesc + "\n");
    }

    public void setLogByRecordId_INFO(Integer recordId, String errorDesc) {
        redisTemplateLog.opsForValue().append("recordLog=" + recordId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]  --" + errorDesc + "\n");
    }

    public void setLogByRecordId_WARN(Integer recordId, String errorDesc) {
        redisTemplateLog.opsForValue().append("recordLog=" + recordId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [WARN]  --" + errorDesc + "\n");
    }


    public void setLogByRecordId_START(Integer recordId, String errorDesc) {
        redisTemplateLog.opsForValue().set("recordLog=" + recordId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]  --" + errorDesc + "\n", redisCacheTime, TimeUnit.DAYS);
    }

    public void setLogByRecordId_END(Integer recordId, String errorDesc) {
        redisTemplateLog.opsForValue().append("recordLog=" + recordId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]  --" + errorDesc + "(end)");
    }

    //回放日志
    public void setLogByReplayId_INFO(Integer replayId, String errorDesc) {
        redisTemplateLog.opsForValue().append("replayLog=" + replayId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]   --" + errorDesc + "\n");
    }

    public void setLogByReplayId_ERROR(Integer replayId, String errorDesc) {
        redisTemplateLog.opsForValue().append("replayLog=" + replayId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [ERROR]  --" + errorDesc + "\n");
    }

    public void setLogByReplayId_START(Integer recordId, String errorDesc) {
        redisTemplateLog.opsForValue().set("replayLog=" + recordId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]  --" + errorDesc + "\n", redisCacheTime, TimeUnit.DAYS);
    }

    public void setLogByReplayId_END(Integer replayId, String errorDesc) {
        redisTemplateLog.opsForValue().append("replayLog=" + replayId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]   --" + errorDesc + "(end)");
    }

    public void setRecordProgress(Integer recordId, String progress) {
        redisTemplateLog.opsForValue().set("recordProgress=" + recordId, progress ,redisCacheTime, TimeUnit.DAYS);

    }

    public String getRecordProgress(Integer recordId) {
        return redisTemplateLog.opsForValue().get("recordProgress=" + recordId);
    }

    public void setReplayProgress(Integer replayId, String progress) {
        redisTemplateLog.opsForValue().set("replayProgress=" + replayId, progress ,redisCacheTime, TimeUnit.DAYS);
    }

    public String getReplayProgress(Integer replayId) {
        return redisTemplateLog.opsForValue().get("replayProgress=" + replayId);
    }

    //比对日志
    public void setLogByDiffId_ERROR(Integer diffId, String errorDesc) {
        redisTemplateLog.opsForValue().append("diffLog=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [ERROR]  --" + errorDesc + "\n");
    }

    public void setLogByDiffId_START(Integer diff, String errorDesc) {
        redisTemplateLog.opsForValue().set("diffLog=" + diff, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]  --" + errorDesc + "\n",redisCacheTime);
    }

    public void setLogByDiffId_END(Integer diffId, String errorDesc) {
        redisTemplateLog.opsForValue().append("diffLog=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]   --" + errorDesc + "(end)");
    }

    public void setLogByDiffId_INFO(Integer diffId, String errorDesc) {
        redisTemplateLog.opsForValue().append("diffLog=" + diffId, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + " [INFO]   --" + errorDesc + "\n");
    }

    public void setDiffProgress(Integer diff, String progress) {
        redisTemplateLog.opsForValue().set("diffProgress=" + diff, progress ,redisCacheTime, TimeUnit.DAYS);
    }

    public String getDiffProgress(Integer diff) {
        return redisTemplateLog.opsForValue().get("diffProgress=" + diff);
    }

}
