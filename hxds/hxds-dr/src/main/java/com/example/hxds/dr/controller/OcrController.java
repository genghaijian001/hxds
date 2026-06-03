package com.example.hxds.dr.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.hxds.common.util.R;
import com.example.hxds.dr.controller.form.OcrDriverLicenseForm;
import com.example.hxds.dr.controller.form.OcrIdCardForm;
import com.example.hxds.dr.service.OcrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/driver")
@Tag(name = "OcrController", description = "证件OCR识别接口")
public class OcrController {

    @Resource
    private OcrService ocrService;

    @PostMapping("/ocrIdCard")
    @Operation(summary = "身份证OCR识别")
    public R ocrIdCard(@RequestBody @Valid OcrIdCardForm form) {
        Map<String, String> result = ocrService.ocrIdCard(form.getBase64Image(), form.getCardSide());
        return R.ok().put("result", result);
    }

    @PostMapping("/ocrDriverLicense")
    @Operation(summary = "驾驶证OCR识别")
    public R ocrDriverLicense(@RequestBody @Valid OcrDriverLicenseForm form) {
        Map<String, String> result = ocrService.ocrDriverLicense(form.getBase64Image());
        return R.ok().put("result", result);
    }
}
