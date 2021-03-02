package com.tal.wangxiao.conan.admin.schedule;

import com.tal.wangxiao.conan.common.api.ResponseCode;
import com.tal.wangxiao.conan.common.entity.db.Department;
import com.tal.wangxiao.conan.common.model.Result;
import com.tal.wangxiao.conan.common.model.echart.BasicChartsModel;
import com.tal.wangxiao.conan.common.repository.db.ApiRepository;
import com.tal.wangxiao.conan.common.repository.db.DepartmentRepository;
import com.tal.wangxiao.conan.common.repository.db.ReplayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 定时缓存数据服务
 *
 * @author huyaoguo
 * @date 2021/1/21
 **/
@Slf4j
@Component
public class RedisSchedule {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private DepartmentRepository departmentRepository;

    @Resource
    private ApiRepository apiRepository;

    @Resource
    private ReplayRepository replayRepository;
    /**
     * 每天凌晨将首页大盘的数据缓存到redis中
     */
    @Scheduled(cron = "0 10 0 * * ? ")
    public void setHomeProductData(){
        List<Department> parentDeptList =  departmentRepository.findByParentId(0);
        if(parentDeptList.size()>1){
            log.error("数据库部分信息异常，最高级部门数据超过1个，size:"+parentDeptList.size());
        }else if(parentDeptList.size()==0){
            log.error("数据库部分信息异常，没有最高级部门数据");
        }
        //统计一级部门数据
        List<Department> deptList = departmentRepository.findByParentId(parentDeptList.get(0).getId());
        //统计部门接入接口数
        List<Integer> apis = new ArrayList<>();
        //统计部门回放次数
        List<Integer> replayCount = new ArrayList<>();
        //部门信息（横坐标）
        List<String> departments = new ArrayList<>();
        Map<String,Object> resMap = new HashMap<>();
        for(Department department:deptList){
            departments.add(department.getDeptName());
            apis.add(apiRepository.countApiCountByDeptId(department.getId()));
            replayCount.add(replayRepository.countReplayCountByDeptId(department.getId()));
        }
        BasicChartsModel apiCharts = new BasicChartsModel();
        apiCharts.setName("已接入接口数");
        apiCharts.setValue(apis);
        resMap.put("apis",apiCharts);

        BasicChartsModel replayCharts = new BasicChartsModel();
        replayCharts.setName("累计回放次数");
        replayCharts.setValue(replayCount);
        resMap.put("replays",replayCharts);

        resMap.put("xData",departments);
        redisTemplate.opsForValue().set("home_department_data",resMap);
    }
}
