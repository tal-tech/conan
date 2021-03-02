package com.tal.wangxiao.conan.agent;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 启动类
 *
 * @author dengkunnan
 * @date 2020/12/24
 */

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.tal.wangxiao.conan.*"})
@EntityScan("com.tal.wangxiao.conan.common.entity")
@EnableJpaRepositories("com.tal.wangxiao.conan.common.repository")
@EnableScheduling
public class ConanAgentApplication {
    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(ConanAgentApplication.class, args);
    }

}
