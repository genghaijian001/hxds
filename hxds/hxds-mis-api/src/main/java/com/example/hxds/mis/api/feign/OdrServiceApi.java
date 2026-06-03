package com.example.hxds.mis.api.feign;

import com.example.hxds.common.util.R;
import com.example.hxds.mis.api.controller.form.SearchCommentByPageForm;
import com.example.hxds.mis.api.controller.form.SearchOrderByPageForm;
import com.example.hxds.mis.api.controller.form.SearchOrderContentForm;
import com.example.hxds.mis.api.controller.form.SearchOrderStatusForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "hxds-odr")
public interface OdrServiceApi {

    @PostMapping("/order/searchOrderByPage")
    public R searchOrderByPage(@RequestBody SearchOrderByPageForm form);

    @PostMapping("/order/searchOrderContent")
    public R searchOrderContent(@RequestBody SearchOrderContentForm form);

    @PostMapping("/order/searchOrderStatus")
    public R searchOrderStatus(@RequestBody SearchOrderStatusForm form);

    @PostMapping("/order/searchOrderStartLocationIn30Days")
    public R searchOrderStartLocationIn30Days();

}
