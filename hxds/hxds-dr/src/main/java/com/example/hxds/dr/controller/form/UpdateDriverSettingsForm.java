package com.example.hxds.dr.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@Schema(description = "更新司机设置的表单")
public class UpdateDriverSettingsForm {
    @NotNull(message = "driverId不能为空")
    @Min(value = 1, message = "driverId不能小于1")
    @Schema(description = "司机ID")
    private Long driverId;

    @Schema(description = "接单范围(公里)")
    private Integer rangeDistance;

    @Schema(description = "订单里程范围(公里)")
    private Integer orderDistance;

    @Schema(description = "定向接单坐标(纬度,经度 或空字符串)")
    private String orientation;

    @Schema(description = "是否开启监听服务(0=否,1=是)")
    private Integer listenService;

    @Schema(description = "是否自动接单(0=否,1=是)")
    private Integer autoAccept;
}
