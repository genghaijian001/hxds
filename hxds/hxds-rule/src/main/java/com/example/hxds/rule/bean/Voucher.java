package com.example.hxds.rule.bean;

import lombok.Data;

@Data
public class Voucher {
    private Integer takeCount = Integer.valueOf(1);
    private Integer usedCount = Integer.valueOf(0);
    private Integer limitQuota = Integer.valueOf(1);
    private Integer totalQuota = Integer.valueOf(1);
    private String remark = "订单取消系统补偿的代金券";
    private String withAmount = "0";
    private String tag = "补偿券";
    private Integer type = Integer.valueOf(1);
    private Integer status = Integer.valueOf(1);
    private String endTime;
    private Integer timeType;
    private Integer days;
    private String discount;
    private String name;
    private String startTime;

    public Voucher(String name, String discount) {
        this.remark = "订单取消系统补偿的代金券";
        this.tag = "补偿券";
        this.totalQuota = 1;
        this.takeCount = 1;
        this.usedCount = 0;
        this.withAmount = "0";
        this.type = 1;
        this.limitQuota = 1;
        this.status = 1;
        this.name = name;
        this.discount = discount;
    }


    public static String ALLATORIxDEMO(String a) {
        int n = a.length();
        int n2 = n - 1;
        char[] cArray = new char[n];
        int n3 = (3 ^ 5) << 4 ^ (3 << 2 ^ 3);
        int cfr_ignored_0 = (2 ^ 5) << 4 ^ (2 << 2 ^ 1);
        int n4 = n2;
        int n5 = 5 << 4 ^ (3 ^ 5) << 1;
        while (n4 >= 0) {
            int n6 = n2--;
            cArray[n6] = (char) (a.charAt(n6) ^ n5);
            if (n2 < 0) break;
            int n7 = n2--;
            cArray[n7] = (char) (a.charAt(n7) ^ n3);
            n4 = n2;
        }
        return new String(cArray);
    }

}

