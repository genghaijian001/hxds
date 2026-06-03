package com.example.hxds.nebula.controller;

import com.example.hxds.common.exception.HxdsException;
import com.example.hxds.common.util.R;
import com.example.hxds.nebula.controller.form.InsertOrderMonitoringForm;
import com.example.hxds.nebula.service.MonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/monitoring")
@Tag(name = "MonitoringController", description = "监控服务的Web接口")
@Slf4j
public class MonitoringController {
    @Resource
    private MonitoringService monitoringService;

    @PostMapping(value = "/uploadRecordFile")
    @Operation(summary = "上传代驾录音文件")
    public R uploadRecordFile(@RequestPart("file") MultipartFile file,
                              @RequestPart("name") String name,
                              @RequestPart(value = "text", required = false) String text) {

        if (file.isEmpty()){
            throw new HxdsException("录音文件不能为空");
        }
        monitoringService.monitoring(file, name, text);
        return R.ok();
    }

    @PostMapping(value = "/insertOrderMonitoring")
    @Operation(summary = "添加订单监控摘要记录")
    public R insertOrderMonitoring(@RequestBody @Valid InsertOrderMonitoringForm form) {
        int rows = monitoringService.insertOrderMonitoring(form.getOrderId());
        return R.ok().put("rows",rows);
    }

}
