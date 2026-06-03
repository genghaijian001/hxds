package com.example.hxds.dr.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "驾驶证OCR识别表单")
public class OcrDriverLicenseForm {

    @NotBlank(message = "base64Image不能为空")
    @Schema(description = "图片Base64字符串")
    private String base64Image;
}
