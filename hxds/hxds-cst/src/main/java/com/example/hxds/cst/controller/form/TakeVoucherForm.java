package com.example.hxds.cst.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "领取代金券的表单")
public class TakeVoucherForm {

    @NotNull(message = "customerId不能为空")
    @Min(value = 1, message = "customerId不能小于1")
    @Schema(description = "客户ID")
    private Long customerId;

    @NotNull(message = "id不能为空")
    @Min(value = 1, message = "id不能小于1")
    @Schema(description = "代金券ID")
    private Long id;

    @NotBlank(message = "uuid不能为空")
    @Schema(description = "代金券uuid")
    private String uuid;
}
