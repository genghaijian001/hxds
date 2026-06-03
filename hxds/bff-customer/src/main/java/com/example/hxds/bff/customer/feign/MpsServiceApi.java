package com.example.hxds.bff.customer.feign;

import com.example.hxds.bff.customer.controller.form.EstimateOrderMileageAndMinuteForm;
import com.example.hxds.bff.customer.controller.form.SearchBefittingDriverAboutOrderForm;
import com.example.hxds.bff.customer.controller.form.SearchOrderLocationCacheForm;
import com.example.hxds.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "hxds-mps")
public interface MpsServiceApi {
    @PostMapping("/map/estimateOrderMileageAndMinute")
    public R estimateOrderMileageAndMinute(@RequestBody EstimateOrderMileageAndMinuteForm form);

    @PostMapping("/driver/location/searchBefittingDriverAboutOrder")
    public R searchBefittingDriverAboutOrder(@RequestBody SearchBefittingDriverAboutOrderForm form);

    @PostMapping("/driver/location/searchOrderLocationCache")
    public R searchOrderLocationCache(@RequestBody SearchOrderLocationCacheForm form);

}
