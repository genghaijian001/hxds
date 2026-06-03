package com.example.hxds.common.wxpay;

import com.wechat.pay.java.core.RSAPublicKeyConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WxPayV3Config {

    @Value("${wx.v3.mch-id}")
    private String mchId;

    @Value("${wx.v3.serial-no}")
    private String serialNo;

    @Value("${wx.v3.private-key-path}")
    private String privateKeyPath;

    @Value("${wx.v3.api-v3-key}")
    private String apiV3Key;

    @Value("${wx.v3.pub-key-id}")
    private String pubKeyId;

    @Value("${wx.v3.pub-key-path}")
    private String pubKeyPath;

    public String getMchId() {
        return mchId;
    }

    @Bean
    public RSAPublicKeyConfig rsaPublicKeyConfig() {
        return new RSAPublicKeyConfig.Builder()
                .merchantId(mchId)
                .privateKeyFromPath(privateKeyPath)
                .merchantSerialNumber(serialNo)
                .publicKeyFromPath(pubKeyPath)
                .publicKeyId(pubKeyId)
                .apiV3Key(apiV3Key)
                .build();
    }
}
