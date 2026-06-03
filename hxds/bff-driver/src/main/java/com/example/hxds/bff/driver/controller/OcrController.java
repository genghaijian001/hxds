package com.example.hxds.bff.driver.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.example.hxds.bff.driver.controller.form.OcrDriverLicenseForm;
import com.example.hxds.bff.driver.controller.form.OcrIdCardForm;
import com.example.hxds.bff.driver.feign.DrServiceApi;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/driver")
@Tag(name = "OcrController", description = "证件OCR识别接口")
public class OcrController {

    @Resource
    private DrServiceApi drServiceApi;

    @PostMapping("/ocrIdCard")
    @Operation(summary = "身份证OCR识别")
    @SaCheckLogin
    public R ocrIdCard(@RequestBody @Valid OcrIdCardForm form) {
        return drServiceApi.ocrIdCard(form);
    }

    @PostMapping("/ocrDriverLicense")
    @Operation(summary = "驾驶证OCR识别")
    @SaCheckLogin
    public R ocrDriverLicense(@RequestBody @Valid OcrDriverLicenseForm form) {
        return drServiceApi.ocrDriverLicense(form);
    }
}
