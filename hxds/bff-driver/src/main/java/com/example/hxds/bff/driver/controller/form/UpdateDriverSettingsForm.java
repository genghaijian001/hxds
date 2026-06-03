package com.example.hxds.bff.driver.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新司机设置的表单")
public class UpdateDriverSettingsForm {
    @Schema(description = "司机ID(由Token自动注入，无需前端传入)")
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
