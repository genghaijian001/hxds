package com.example.hxds.dr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ServletComponentScan
@MapperScan("com.example.hxds.dr.db.dao")
@ComponentScan("com.example.*")
public class HxdsDrApplication {

    public static void main(String[] args) {
        SpringApplication.run(HxdsDrApplication.class, args);
    }

}
