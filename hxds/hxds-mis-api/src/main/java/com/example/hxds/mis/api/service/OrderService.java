package com.example.hxds.mis.api.service;

import com.example.hxds.common.util.PageUtils;
import com.example.hxds.common.util.R;
import com.example.hxds.mis.api.controller.form.SearchOrderByPageForm;
import com.example.hxds.mis.api.controller.form.SearchOrderLastGpsForm;
import com.example.hxds.mis.api.controller.form.SearchOrderStatusForm;

import java.util.ArrayList;
import java.util.HashMap;

public interface OrderService {

    public PageUtils searchOrderByPage(SearchOrderByPageForm form);

    public HashMap searchOrderComprehensiveInfo(long orderId);

    public HashMap searchOrderLastGps(SearchOrderLastGpsForm form);

    //最近30天的代驾上车点坐标
    public ArrayList<HashMap> searchOrderStartLocationIn30Days();
}
