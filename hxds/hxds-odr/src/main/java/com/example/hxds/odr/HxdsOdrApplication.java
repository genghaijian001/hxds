package com.example.hxds.odr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ServletComponentScan
@MapperScan({"com.example.hxds.odr.db.dao"})
@ComponentScan({"com.example.*"})
@EnableScheduling  // INC-3: 启用定时任务，处理未支付订单超时
public class HxdsOdrApplication {

    public static void main(String[] args) {
        SpringApplication.run(HxdsOdrApplication.class, args);

    }

}
