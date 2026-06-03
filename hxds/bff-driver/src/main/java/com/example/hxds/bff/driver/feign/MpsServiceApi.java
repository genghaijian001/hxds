package com.example.hxds.bff.driver.feign;

import com.example.hxds.bff.driver.controller.form.RemoveLocationCacheForm;
import com.example.hxds.bff.driver.controller.form.UpdateLocationCacheForm;
import com.example.hxds.bff.driver.controller.form.UpdateOrderLocationCacheForm;
import com.example.hxds.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "hxds-mps")
public interface MpsServiceApi {

    @PostMapping("/driver/location/updateLocationCache")
    public R updateLocationCache(@RequestBody UpdateLocationCacheForm form);

    @PostMapping("/driver/location/removeLocationCache")
    public R removeLocationCache(@RequestBody RemoveLocationCacheForm form);

    @PostMapping("/driver/location/updateOrderLocationCache")
    public R updateOrderLocationCache(@RequestBody UpdateOrderLocationCacheForm form);
}
