package com.example.hxds.bff.driver.feign;

import com.example.hxds.bff.driver.controller.form.*;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@FeignClient(value = "hxds-dr")
public interface DrServiceApi {

    /**
     * 新司机注册Feign接口
     * @param form
     * @return
     */
    @PostMapping("/driver/registerNewDriver")
    public R registerNewDriver(@RequestBody RegisterNewDriverForm form);

    /**
     * 更新实名认证信息
     * @param form
     * @return
     */
    @PostMapping("/driver/updateDriverAuth")
    public R updateDriverAuth(@RequestBody UpdateDriverAuthForm form);

    /**
     * 创建司机人脸模型归档
     * @param form
     * @return
     */
    @PostMapping("/driver/createDriverFaceModel")
    public R createDriverFaceModel(@RequestBody CreateDriverFaceModelForm form);

    /**
     * 登录系统
     * @param form
     * @return
     */
    @PostMapping("/driver/login")
    public R login(@RequestBody LoginForm form);

    /**
     * 查询司机基本信息
     * @param form
     * @return
     */
    @PostMapping("/driver/searchDriverBaseInfo")
    public R searchDriverBaseInfo(@RequestBody SearchDriverBaseInfoForm form);

    /**
     * 查询司机设置
     * @param form
     * @return
     */
    @PostMapping("/settings/searchDriverSettings")
    public R searchDriverSettings(@RequestBody SearchDriverSettingsForm form);

    @PostMapping("/settings/updateDriverSettings")
    public R updateDriverSettings(@RequestBody UpdateDriverSettingsForm form);

    /**
     * 查询司机实名认证信息
     * @param form
     * @return
     */
    @PostMapping("/driver/searchDriverAuth")
    public R searchDriverAuth(@RequestBody SearchDriverAuthForm form);

    @PostMapping("/driver/ocrIdCard")
    public R ocrIdCard(@RequestBody OcrIdCardForm form);

    @PostMapping("/driver/ocrDriverLicense")
    public R ocrDriverLicense(@RequestBody OcrDriverLicenseForm form);

    @PostMapping("/wallet/searchDriverWallet")
    R searchDriverWallet(@RequestBody SearchDriverWalletForm form);

}
