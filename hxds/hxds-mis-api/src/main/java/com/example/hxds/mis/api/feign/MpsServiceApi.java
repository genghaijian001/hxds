package com.example.hxds.mis.api.feign;

import com.example.hxds.common.util.R;
import com.example.hxds.mis.api.controller.form.CalculateDriveLineForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "hxds-mps")
public interface MpsServiceApi {

    @PostMapping("/map/calculateDriveLine")
    public R calculateDriveLine(@RequestBody CalculateDriveLineForm form);

}

