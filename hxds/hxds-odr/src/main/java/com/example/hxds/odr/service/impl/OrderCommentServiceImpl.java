package com.example.hxds.odr.service.impl;

import cn.hutool.core.codec.Base64;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.odr.db.dao.OrderCommentDao;
import com.example.hxds.odr.db.dao.OrderDao;
import com.example.hxds.odr.db.pojo.OrderCommentEntity;
import com.example.hxds.odr.service.OrderCommentService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ciModel.auditing.AuditingJobsDetail;
import com.qcloud.cos.model.ciModel.auditing.TextAuditingRequest;
import com.qcloud.cos.model.ciModel.auditing.TextAuditingResponse;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderCommentServiceImpl implements OrderCommentService {
    @Value("${tencent.cloud.appId}")
    private String appId;

    @Value("${tencent.cloud.secretId}")
    private String secretId;

    @Value("${tencent.cloud.secretKey}")
    private String secretKey;

    @Value("${tencent.cloud.bucket-public}")
    private String bucketPublic;
    @Resource
    private OrderCommentDao orderCommentDao;
    @Resource
    private OrderDao orderDao;


    @Override
    public int insert(OrderCommentEntity entity) {
        //查询司机与乘客跟订单是否有关联关系
        HashMap param = new HashMap(){{
           put("orderId", entity.getOrderId());
            put("driverId", entity.getDriverId());
            put("customerId", entity.getCustomerId());
        }};
        long count = orderDao.validDriverAndCustomerOwnOrder(param);
        if (count != 1) {
            throw new HxdsException("订单与司机乘客无关联");
        }

        //审核评价内容
        COSCredentials cred = new BasicCOSCredentials(secretId,secretKey);
        Region region = new Region("ap-beijing");
        ClientConfig config = new ClientConfig(region);
        COSClient client = new COSClient(cred,config);//封装秘钥、地点
        TextAuditingRequest request = new TextAuditingRequest();
        request.setBucketName(bucketPublic);//存储桶
        request.getInput().setContent(Base64.encode(entity.getRemark()));//获取评价内容
        request.getConf().setDetectType("all");

        TextAuditingResponse response = client.createAuditingTextJobs(request);
        AuditingJobsDetail detail = response.getJobsDetail();
        String state = detail.getState();
        if ("Success".equals(state)){
            String result = detail.getResult();
            //内容审查不同过就设置评价内容为null
            if (!"0".equals(result)){
                entity.setRemark("*****");
            }
        }
        //保存评价内容
        int rows = orderCommentDao.insert(entity);
        return rows;
    }

    @Override
    public HashMap searchCommentByOrderId(Map param) {
        HashMap map = orderCommentDao.searchCommentByOrderId(param);
        return map;
    }
}
