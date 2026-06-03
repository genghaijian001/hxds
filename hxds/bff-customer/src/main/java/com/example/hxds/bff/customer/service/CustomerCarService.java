package com.example.hxds.bff.customer.service;

import com.example.hxds.bff.customer.controller.form.DeleteCustomerCarByIdForm;
import com.example.hxds.bff.customer.controller.form.InsertCustomerCarForm;
import com.example.hxds.bff.customer.controller.form.SearchCustomerCarListForm;

import java.util.ArrayList;
import java.util.HashMap;

public interface CustomerCarService {

    /**
     * 插入客户车辆记录
     *
     * @param form 车辆插入表单实体对象
     */
    public void insertCustomerCar(InsertCustomerCarForm form);

    /**
     * 查询并检索指定参数配置的客户车辆列表信息
     * 通过声明具体的内部键值对泛型实现方法安全级别的严格阻滞与防范
     *
     * @param form 车辆搜索属性表单参数
     * @return 包含具体的String键与Object值的严谨嵌套哈希表列表
     */
    public ArrayList<HashMap<String, Object>> searchCustomerCarList(SearchCustomerCarListForm form);

    /**
     * 根据主键数据精确销毁设定的用户车辆关联记录
     *
     * @param form 囊括目标车辆ID的删除表单参数
     * @return 数据库反馈的受变动影响行数指标
     */
    public int deleteCustomerCarById(DeleteCustomerCarByIdForm form);

}
