//package com.tal.wangxiao.conan.agent.schedule;
//
//import com.tal.wangxiao.conan.common.entity.db.Task;
//import com.tal.wangxiao.conan.common.repository.db.TaskRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.Date;
//import java.util.List;
//
///**
// * agent心跳服务
// *
// * @author huyaoguo
// * @date 2020/12/15
// **/
//@Slf4j
//@Component
//public class TestSchedule {
//
//    @Resource
//    private TaskRepository taskRepository;
//
//    @Scheduled(cron = "0 0/1 * * * ? ")
//    public void test() {
//        //每5分钟上报一次注册中心
//        log.info("test");
//        List<Task> tasks = taskRepository.findAll();
//        log.info("当前库中有{}个任务",tasks.size());
//    }
//
//
//
//    public static void main(String[] args) throws UnknownHostException {
//        System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
//        //1608117137
//        System.out.println(new Date().getTime());
//        System.out.println(System.currentTimeMillis() / 1000);
//    }
//}
