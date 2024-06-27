package com.deepinnet.rainsfall;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan({"com.deepinnet.rainsfall.common.dal.dao"})
@EnableDubbo(scanBasePackages = "com.deepinnet")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationStarter {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }

}
