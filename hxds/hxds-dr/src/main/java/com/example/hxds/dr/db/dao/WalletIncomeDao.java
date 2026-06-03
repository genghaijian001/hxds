package com.example.hxds.dr.db.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface WalletIncomeDao {

    // Fix-10: 补充完整的钱包收入 DAO 方法（原接口为空）

    // 查询司机收入流水（分页）
    ArrayList<HashMap> searchIncomeByDriver(Map param);

    // 查询司机收入流水总数
    long searchIncomeCount(Map param);

    // 插入收入记录
    int insertIncome(Map param);

    // 更新收入记录状态
    int updateIncomeStatus(Map param);

    // 按月查询收入记录
    ArrayList<HashMap> searchIncomeByMonth(Map param);

    // 统计某月收入合计
    BigDecimal sumIncomeInMonth(Map param);

    // 统计某日收入合计
    BigDecimal sumIncomeInDay(Map param);

}
