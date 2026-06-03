package com.example.hxds.dr.controller;

import com.example.hxds.common.util.R;
import com.example.hxds.dr.controller.form.SearchDriverSettingsForm;
import com.example.hxds.dr.controller.form.UpdateDriverSettingsForm;
import com.example.hxds.dr.service.DriverSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/settings")
@Tag(name = "SettingsController", description = "司机设置模块Web接口")
public class DriverSettingsController {

    @Resource
    private DriverSettingsService driverSettingsService;

    @PostMapping("/searchDriverSettings")
    @Operation(summary = "查询司机的设置")
    public R searchDriverSettings(@RequestBody @Valid SearchDriverSettingsForm form){
        HashMap map = driverSettingsService.searchDriverSettings(form.getDriverId());
        return R.ok().put("result",map);
    }

    @PostMapping("/updateDriverSettings")
    @Operation(summary = "更新司机的设置")
    public R updateDriverSettings(@RequestBody @Valid UpdateDriverSettingsForm form){
        Map<String, Object> param = new HashMap<>();
        if (form.getRangeDistance() != null) param.put("rangeDistance", form.getRangeDistance());
        if (form.getOrderDistance() != null) param.put("orderDistance", form.getOrderDistance());
        if (form.getOrientation() != null) param.put("orientation", form.getOrientation());
        if (form.getListenService() != null) param.put("listenService", form.getListenService());
        if (form.getAutoAccept() != null) param.put("autoAccept", form.getAutoAccept());
        int rows = driverSettingsService.updateDriverSettings(form.getDriverId(), param);
        return R.ok().put("rows", rows);
    }

}
