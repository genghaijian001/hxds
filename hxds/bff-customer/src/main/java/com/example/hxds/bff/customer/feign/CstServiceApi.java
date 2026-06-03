package com.example.hxds.bff.customer.feign;

import com.example.hxds.bff.customer.controller.form.*;
import com.example.hxds.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "hxds-cst")
public interface CstServiceApi {

    @PostMapping("/customer/registerNewCustomer")
    public R registerNewCustomer(@RequestBody RegisterNewCustomerForm form);

    @PostMapping("/customer/login")
    public R login(@RequestBody LoginForm form);

    @PostMapping("/customer/car/insertCustomerCar")
    public R insertCustomerCar(@RequestBody InsertCustomerCarForm form);

    @PostMapping("/customer/car/searchCustomerCarList")
    public R searchCustomerCarList(@RequestBody SearchCustomerCarListForm form);

    @PostMapping("/customer/car/deleteCustomerCarById")
    public R deleteCustomerCarById(@RequestBody DeleteCustomerCarByIdForm form);

    @PostMapping("/customer/searchCustomerOpenId")
    public R searchCustomerOpenId(@RequestBody SearchCustomerOpenIdForm form);

    @PostMapping("/customer/searchCustomerProfile")
    public R searchCustomerProfile(@RequestBody SearchCustomerOpenIdForm form);

    @PostMapping("/voucher/searchUnUseVoucherCount")
    public R searchUnUseVoucherCount(@RequestBody SearchUnUseVoucherCountForm form);

    @PostMapping("/voucher/searchUnUseVoucherByPage")
    public R searchUnUseVoucherByPage(@RequestBody SearchUnUseVoucherByPageForm form);

    @PostMapping("/voucher/searchUsedVoucherByPage")
    public R searchUsedVoucherByPage(@RequestBody SearchUsedVoucherByPageForm form);

    @PostMapping("/voucher/searchUnTakeVoucherByPage")
    public R searchUnTakeVoucherByPage(@RequestBody SearchUnTakeVoucherByPageForm form);

    @PostMapping("/voucher/takeVoucher")
    public R takeVoucher(@RequestBody TakeVoucherForm form);

    @PostMapping("/customer/updateCustomerProfile")
    public R updateCustomerProfile(@RequestBody UpdateCustomerProfileForm form);

    @PostMapping("/customer/updateCustomerPhoto")
    public R updateCustomerPhoto(@RequestBody UpdateCustomerPhotoForm form);

    @PostMapping("/customer/updateCustomerTel")
    public R updateCustomerTel(@RequestBody UpdateCustomerTelForm form);
}
