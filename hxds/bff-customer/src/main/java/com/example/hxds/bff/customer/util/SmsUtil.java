package com.example.hxds.bff.customer.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.example.hxds.common.exception.HxdsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsUtil {

    @Value("${sms.accessKeyId}")
    private String accessKeyId;

    @Value("${sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${sms.signName}")
    private String signName;

    @Value("${sms.templateCode}")
    private String templateCode;

    public void sendSmsCode(String tel, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(tel);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            if (!"OK".equals(response.getCode())) {
                throw new HxdsException("短信发送失败：" + response.getMessage());
            }
        } catch (HxdsException e) {
            throw e;
        } catch (Exception e) {
            throw new HxdsException("短信发送异常：" + e.getMessage());
        }
    }
}
