package com.tal.wangxiao.conan.agent.controller;

import com.tal.wangxiao.conan.agent.service.impl.RecordServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * 任务执行管理接口类
 *
 * @author liujinsong
 * @date 2020/1/22
 */
@Api(value = "Record API", tags = "Agent录制接口")
@RestController
@RequestMapping("api/1.0/agent_record")
@Slf4j
public class RecordController {

    @Resource
    private RecordServiceImpl recordService;


    @ApiOperation(value = "agent录制接口", notes = "agent录制接口")
    @PostMapping(value = "/start")
    public void startRecord(@RequestParam(value = "record_id") Integer recordId) throws Exception {
        log.info("agentRecordController#startRecord, record_id = {}", recordId);
        recordService.record(recordId);
    }


}
