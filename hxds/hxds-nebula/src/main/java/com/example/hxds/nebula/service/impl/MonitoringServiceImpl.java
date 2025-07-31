package com.example.hxds.nebula.service.impl;

import cn.hutool.core.util.IdUtil;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.nebula.db.dao.OrderMonitoringDao;
import com.example.hxds.nebula.db.dao.OrderVoiceTextDao;
import com.example.hxds.nebula.db.pojo.OrderVoiceTextEntity;
import com.example.hxds.nebula.service.MonitoringService;
import com.example.hxds.nebula.task.VoiceTextCheckTask;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
@Slf4j
public class MonitoringServiceImpl implements MonitoringService {
    @Resource
    private OrderVoiceTextDao orderVoiceTextDao;
    @Resource
    private OrderMonitoringDao orderMonitoringDao;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;
    @Resource
    private VoiceTextCheckTask voiceTextCheckTask;

    /**
     * 检测   上传文本录音文件
     * @param file
     * @param name
     * @param text
     */
    @Override
    @Transactional
    public void monitoring(MultipartFile file, String name, String text) {
        //把录音文件.上传到Minio
        try{
            MinioClient client=new MinioClient.Builder().endpoint(endpoint)
                    .credentials(accessKey,secretKey).build();
            //使用Minio客户端将录音文件上传到指定的Minio存储桶（bucket）中。
            client.putObject(PutObjectArgs.builder().bucket("hxds-record")
                    //使用 file 参数中的输入流来进行上传。第一个参数是输入流，第二个参数 -1 表示不限制上传的大小，
                    // 第三个参数 20971520 表示上传的最大字节数为20MB。
                    .object(name).stream(file.getInputStream(),-1,20971520)
                    .contentType("audio/x-mpeg").build());
        }catch(Exception e){
            log.error("上传代驾录音文件失败", e);
            throw new HxdsException("上传代驾录音文件失败");
        }

        OrderVoiceTextEntity entity=new OrderVoiceTextEntity();

        //文件名格式例如:2156356656617-1.mp3，我们要解析出订单号
        String[] temp=name.substring(0, name.indexOf(".mp3")).split("-");
        Long orderId=Long.parseLong(temp[0]);

        String uuid = IdUtil.simpleUUID();
        entity.setOrderId(orderId);
        entity.setUuid(uuid);
        entity.setRecordFile(name);
        entity.setText(text);
        //把文稿保存到hbase
        int rows = orderVoiceTextDao.insert(entity);
        if(rows != 1){
            throw new HxdsException("保存录音文稿失败");
        }

        //执行文稿内容审查
        voiceTextCheckTask.checkText(orderId,text,uuid);
    }

    /**
     * 利用AI对司乘对话内容安全评级  添加订单监控摘要记录
     * @param orderId
     * @return
     */
    @Override
    @Transactional
    public int insertOrderMonitoring(long orderId) {
        int rows = orderMonitoringDao.insert(orderId);
        if(rows != 1){
            throw new HxdsException("添加订单监控摘要记录失败");
        }
        return rows;
    }
}
