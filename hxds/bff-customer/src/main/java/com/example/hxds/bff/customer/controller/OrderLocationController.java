package com.example.hxds.bff.customer.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.example.hxds.bff.customer.controller.form.SearchOrderLocationCacheForm;
import com.example.hxds.bff.customer.service.OrderLocationService;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/order/location")
@Tag(name = "OrderLocationController", description = "订单定位服务Web接口")
public class OrderLocationController {

    @Resource
    private OrderLocationService orderLocationService;

    //888730271510671360
    //token: bb87118c-5377-4f11-9cee-3d3d48792102
    @PostMapping("/searchOrderLocationCache")
    @Operation(summary = "查询订单定位缓存")
    @SaCheckLogin
    public R searchOrderLocationCache(@RequestBody @Valid SearchOrderLocationCacheForm form){
        HashMap map = orderLocationService.searchOrderLocationCache(form);
        return R.ok().put("result",map);
    }
}
