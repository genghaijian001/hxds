package com.example.hxds.bff.customer.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新客户头像的内部Feign调用表单")
public class UpdateCustomerPhotoForm {

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "COS头像URL")
    private String photo;
}
