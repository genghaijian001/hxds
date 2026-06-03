package com.example.hxds.dr.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "身份证OCR识别表单")
public class OcrIdCardForm {

    @NotBlank(message = "base64Image不能为空")
    @Schema(description = "图片Base64字符串")
    private String base64Image;

    @NotBlank(message = "cardSide不能为空")
    @Pattern(regexp = "^(FRONT|BACK)$", message = "cardSide必须是FRONT或BACK")
    @Schema(description = "身份证面: FRONT=人像面, BACK=国徽面")
    private String cardSide;
}
