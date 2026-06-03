package com.example.hxds.dr.service.impl;

import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.dr.service.OcrService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.DriverLicenseOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.DriverLicenseOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OcrServiceImpl implements OcrService {

    @Value("${tencent.cloud.secretId}")
    private String secretId;

    @Value("${tencent.cloud.secretKey}")
    private String secretKey;

    @Override
    public Map<String, String> ocrIdCard(String base64Image, String cardSide) {
        try {
            Credential cred = new Credential(secretId, secretKey);
            OcrClient client = new OcrClient(cred, "ap-beijing");

            IDCardOCRRequest req = new IDCardOCRRequest();
            req.setImageBase64(base64Image);
            req.setCardSide(cardSide);

            IDCardOCRResponse resp = client.IDCardOCR(req);

            Map<String, String> result = new HashMap<>();
            if ("FRONT".equals(cardSide)) {
                result.put("name", resp.getName() != null ? resp.getName() : "");
                result.put("sex", resp.getSex() != null ? resp.getSex() : "");
                result.put("nationality", resp.getNation() != null ? resp.getNation() : "");
                result.put("birth", resp.getBirth() != null ? resp.getBirth() : "");
                result.put("address", resp.getAddress() != null ? resp.getAddress() : "");
                result.put("idNum", resp.getIdNum() != null ? resp.getIdNum() : "");
            } else {
                // BACK face
                result.put("authority", resp.getAuthority() != null ? resp.getAuthority() : "");
                result.put("validDate", resp.getValidDate() != null ? resp.getValidDate() : "");
            }
            return result;
        } catch (TencentCloudSDKException e) {
            log.error("身份证OCR识别失败", e);
            throw new HxdsException("身份证识别失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> ocrDriverLicense(String base64Image) {
        try {
            Credential cred = new Credential(secretId, secretKey);
            OcrClient client = new OcrClient(cred, "ap-beijing");

            DriverLicenseOCRRequest req = new DriverLicenseOCRRequest();
            req.setImageBase64(base64Image);

            DriverLicenseOCRResponse resp = client.DriverLicenseOCR(req);

            Map<String, String> result = new HashMap<>();
            result.put("name", resp.getName() != null ? resp.getName() : "");
            result.put("licenseNum", resp.getCardCode() != null ? resp.getCardCode() : "");
            result.put("validInDate", resp.getStartDate() != null ? resp.getStartDate() : "");
            result.put("validPeriod", resp.getEndDate() != null ? resp.getEndDate() : "");
            result.put("clazz", resp.getClass_() != null ? resp.getClass_() : "");
            return result;
        } catch (TencentCloudSDKException e) {
            log.error("驾驶证OCR识别失败", e);
            throw new HxdsException("驾驶证识别失败: " + e.getMessage());
        }
    }
}
