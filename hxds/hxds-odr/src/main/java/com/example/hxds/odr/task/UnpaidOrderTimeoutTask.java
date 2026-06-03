package com.example.hxds.odr.task;

import com.example.hxds.odr.db.dao.OrderDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * INC-3修复: 未支付订单超时自动关闭定时任务
 * status=6(未付款) 超过30分钟自动转为9(顾客撤单)，释放资源
 */
@Component
@Slf4j
public class UnpaidOrderTimeoutTask {

    @Resource
    private OrderDao orderDao;

    /**
     * 每5分钟扫描一次，将超过30分钟未支付的订单自动关闭
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void closeUnpaidTimeoutOrders() {
        try {
            int rows = orderDao.closeUnpaidTimeoutOrders();
            if (rows > 0) {
                log.info("未支付超时订单自动关闭: {} 条", rows);
            }
        } catch (Exception e) {
            log.error("未支付超时订单自动关闭任务异常", e);
        }
    }
}
