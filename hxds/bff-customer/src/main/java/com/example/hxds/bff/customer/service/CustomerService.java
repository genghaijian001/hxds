package com.example.hxds.bff.customer.service;

import com.example.hxds.bff.customer.controller.form.LoginForm;
import com.example.hxds.bff.customer.controller.form.RegisterNewCustomerForm;
import com.example.hxds.bff.customer.controller.form.UpdateCustomerProfileForm;
import com.example.hxds.bff.customer.controller.form.UpdateCustomerTelForm;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;

public interface CustomerService {
    public long registerNewCustomer(RegisterNewCustomerForm form);
    public Long login(LoginForm form);
    public HashMap searchCustomerProfile();
    public int updateCustomerProfile(UpdateCustomerProfileForm form);
    public String updateCustomerPhoto(MultipartFile file) throws IOException;
    public void sendSmsCode(String tel);
    public int updateCustomerTel(UpdateCustomerTelForm form);
}
