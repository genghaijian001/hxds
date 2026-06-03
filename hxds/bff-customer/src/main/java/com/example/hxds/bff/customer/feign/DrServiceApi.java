package com.example.hxds.bff.customer.feign;

import com.example.hxds.bff.customer.controller.form.SearchDriverBriefInfoForm;
import com.example.hxds.bff.customer.controller.form.SearchDriverOpenIdForm;
import com.example.hxds.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@FeignClient(value = "hxds-dr")
public interface DrServiceApi {

    @PostMapping("/driver/searchDriverBriefInfo")
    public R searchDriverBriefInfo(@RequestBody SearchDriverBriefInfoForm form);

    @PostMapping("/driver/searchDriverOpenId")
    public R searchDriverOpenId(@RequestBody SearchDriverOpenIdForm form);
}
