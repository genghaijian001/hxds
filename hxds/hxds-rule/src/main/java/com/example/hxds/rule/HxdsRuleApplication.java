/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.codingapi.txlcn.tc.config.EnableDistributedTransaction
 *  com.example.hxds.rule.HxdsRuleApplication
 *  org.mybatis.spring.annotation.MapperScan
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.boot.web.servlet.ServletComponentScan
 *  org.springframework.cloud.client.discovery.EnableDiscoveryClient
 *  org.springframework.cloud.openfeign.EnableFeignClients
 *  org.springframework.context.annotation.ComponentScan
 */
package com.example.hxds.rule;

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
@MapperScan(value = {"com.example.hxds.rule.db.dao"})
@ComponentScan(value = {"com.example.*"})
public class HxdsRuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(HxdsRuleApplication.class, args);

    }
}

