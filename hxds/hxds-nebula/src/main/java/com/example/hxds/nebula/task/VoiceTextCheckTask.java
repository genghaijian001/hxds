package com.example.hxds.nebula.task;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.nebula.db.dao.OrderMonitoringDao;
import com.example.hxds.nebula.db.dao.OrderVoiceTextDao;
import com.example.hxds.nebula.db.pojo.OrderMonitoringEntity;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ciModel.auditing.AuditingJobsDetail;
import com.qcloud.cos.model.ciModel.auditing.SectionInfo;
import com.qcloud.cos.model.ciModel.auditing.TextAuditingRequest;
import com.qcloud.cos.model.ciModel.auditing.TextAuditingResponse;
import com.qcloud.cos.region.Region;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tms.v20201229.TmsClient;
import com.tencentcloudapi.tms.v20201229.models.TextModerationRequest;
import com.tencentcloudapi.tms.v20201229.models.TextModerationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class VoiceTextCheckTask {
    @Value("${tencent.cloud.appId}")
    private String appId;

    @Value("${tencent.cloud.secretId}")
    private String secretId;

    @Value("${tencent.cloud.secretKey}")
    private String secretKey;

    @Value("${tencent.cloud.bucket-public}")
    private String bucketPublic;

    @Resource
    private OrderVoiceTextDao orderVoiceTextDao;

    @Resource
    private OrderMonitoringDao orderMonitoringDao;

    @Async
    @Transactional
    public void checkText(long orderId, String content, String uuid) {
        String label = "Normal"; //审核结果
        String suggestion = "Pass"; //后续建议

        //后续建议模板
        HashMap<String, String> template = new HashMap() {{
            put("0", "Pass");
            put("1", "Block");
            put("2", "Review");
        }};

        if(StrUtil.isNotBlank(content)){
            //使用云服务的密钥和配置创建一个COS客户端 cred
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            Region region = new Region("ap-beijing");
            ClientConfig config=new ClientConfig(region);
            COSClient client = new COSClient(cred, config);

            //创建一个文本审核请求，存储桶名称，输入内容（需要进行Base64编码），检测类型
            TextAuditingRequest request = new TextAuditingRequest();
            request.setBucketName(bucketPublic);
            request.getInput().setContent(Base64.encode(content));
            request.getConf().setDetectType("all");

            //使用COS客户端发送文本审核请求
            TextAuditingResponse response=client.createAuditingTextJobs(request);
            //获取响应
            AuditingJobsDetail detail = response.getJobsDetail();
            String state = detail.getState();

            //如果审核成功，就从响应中获取审核结果，并根据模板将结果映射为具体的建议。
            //同时，也会获取可能存在的关键词，并将这些关键词添加到一个列表中。
            ArrayList keywords=new ArrayList();

            if("Success".equals(state)){
                label=detail.getLabel();  //检测结果
                String result = detail.getResult(); //后续建议
                suggestion=template.get(result);

                List<SectionInfo> list=detail.getSectionList();  //违规关键字
                for (SectionInfo info:list){
                    String keywords_1=info.getPornInfo().getKeywords();
                    String keywords_2=info.getIllegalInfo().getKeywords();
                    String keywords_3=info.getAbuseInfo().getKeywords();
                    if(keywords_1.length()>0){
                        List temp=Arrays.asList(keywords_1.split(","));
                        keywords.addAll(temp);
                    }
                    if(keywords_2.length()>0){
                        List temp=Arrays.asList(keywords_2.split(","));
                        keywords.addAll(temp);
                    }
                    if(keywords_3.length()>0){
                        List temp=Arrays.asList(keywords_3.split(","));
                        keywords.addAll(temp);
                    }
                }
            }
            Long id = orderVoiceTextDao.searchIdByUuid(uuid);
            if(id==null){
                throw new HxdsException("没有找到代驾语音文本记录");
            }
            //创建一个参数 HashMap ，并将记录的ID，审核结果，后续建议，
            // 关键词等信息放入其中，然后更新数据库中的对应记录。
            HashMap param=new HashMap();
            param.put("id", id);
            param.put("label", label);
            param.put("suggestion", suggestion);
            param.put("keywords", ArrayUtil.join(keywords.toArray(), ","));
            int rows = orderVoiceTextDao.updateCheckResult(param);
            if(rows!=1){
                throw new HxdsException("更新内容检查结果失败");
            }

            //查询该订单有多少个录音文本和需要人工审核的文本
            //使用 orderId 在数据库中查询对应的订单记录和评论，然后根据审核结果更新这些记录。
            HashMap map = orderMonitoringDao.searchOrderRecordsAndReviews(orderId);
            id = MapUtil.getLong(map, "id");
            Integer records = MapUtil.getInt(map, "records"); //录音文件数量
            Integer reviews = MapUtil.getInt(map, "reviews");  //需要人工审核的数量

            OrderMonitoringEntity entity=new OrderMonitoringEntity();
            entity.setId(id);
            entity.setOrderId(orderId);
            entity.setRecords(records+1);
            //后续建议==需要人工审核的数量
            if(suggestion.equals("Review")){
                entity.setReviews(reviews+1);
            }
            //安全等级
            if(suggestion.equals("Block")){
                entity.setSafety("danger");
            }
            rows=orderMonitoringDao.updateOrderMonitoring(entity);
            if(rows!=1){
                throw new HxdsException("更新订单监控记录失败");
            }
        }

    }
}
