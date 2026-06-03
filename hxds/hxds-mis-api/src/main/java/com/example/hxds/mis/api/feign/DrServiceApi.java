package com.example.hxds.mis.api.feign;

import com.example.hxds.common.util.R;
import com.example.hxds.mis.api.controller.form.SearchDriverBriefInfoForm;
import com.example.hxds.mis.api.controller.form.SearchDriverByPageForm;
import com.example.hxds.mis.api.controller.form.SearchDriverRealSummaryForm;
import com.example.hxds.mis.api.controller.form.UpdateDriverRealAuthForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "hxds-dr")
public interface DrServiceApi {

    /**
     * 查询司机分页记录
     * @param form
     * @return
     */
    @PostMapping("/driver/searchDriverByPage")
    public R searchDriverByPage(@RequestBody SearchDriverByPageForm form);

    /**
     * 司机微服务中查询司机实名认证申请
     * @param form
     * @return
     */
    @PostMapping("/driver/searchDriverRealSummary")
    public R searchDriverRealSummary(@RequestBody SearchDriverRealSummaryForm form);

    /**
     * 更新司机实名认证状态
     * @param form
     * @return
     */
    @PostMapping("/driver/updateDriverRealAuth")
    public R updateDriverRealAuth(@RequestBody UpdateDriverRealAuthForm form);

    @PostMapping("/driver/searchDriverBriefInfo")
    public R searchDriverBriefInfo(@RequestBody SearchDriverBriefInfoForm form);

}
