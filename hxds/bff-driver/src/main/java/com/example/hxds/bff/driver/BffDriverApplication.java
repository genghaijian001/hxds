package com.example.hxds.bff.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan("com.example.*")
public class BffDriverApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffDriverApplication.class, args);
    }

}
