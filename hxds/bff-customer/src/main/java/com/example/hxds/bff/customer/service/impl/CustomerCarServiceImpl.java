package com.example.hxds.bff.customer.service.impl;

import cn.hutool.core.map.MapUtil;
import io.seata.spring.annotation.GlobalTransactional;
import com.example.hxds.bff.customer.controller.form.DeleteCustomerCarByIdForm;
import com.example.hxds.bff.customer.controller.form.InsertCustomerCarForm;
import com.example.hxds.bff.customer.controller.form.SearchCustomerCarListForm;
import com.example.hxds.bff.customer.feign.CstServiceApi;
import com.example.hxds.bff.customer.service.CustomerCarService;
import com.example.hxds.common.util.R;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class CustomerCarServiceImpl implements CustomerCarService {

    @Resource
    private CstServiceApi cstServiceApi;

    @Override
    @GlobalTransactional
    public void insertCustomerCar(InsertCustomerCarForm form) {
        cstServiceApi.insertCustomerCar(form);
    }

    /**
     * 客户车辆信息的实体检索行为具体实现逻辑
     * 明确接管并人工显式忽略已知受控环境下的泛型擦除警告
     *
     * @param form 定义目标搜索边界参数的实体业务对象
     * @return 解析底层服务响应后返回的具体业务层嵌套映射列表
     */
    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<HashMap<String, Object>> searchCustomerCarList(SearchCustomerCarListForm form) {
        R r = cstServiceApi.searchCustomerCarList(form);
        // 执行验证过的绝对匹配类型强制转换，完全屏蔽泛型擦除造成的过度警告抛出机制
        ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) r.get("result");
        return list;
    }

    @Override
    public int deleteCustomerCarById(DeleteCustomerCarByIdForm form) {
        R r = cstServiceApi.deleteCustomerCarById(form);
        int rows = MapUtil.getInt(r, "rows");
        return rows;
    }
}
