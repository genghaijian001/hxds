package com.example.hxds.nebula.service;

import org.springframework.web.multipart.MultipartFile;

public interface MonitoringService {

    //上传文本录音文件
    public void monitoring(MultipartFile file,String name,String text);

    //利用AI对司乘对话内容安全评级  添加订单监控摘要记录
    public int insertOrderMonitoring(long orderId);
}
