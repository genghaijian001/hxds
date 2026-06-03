# wx.requestPayment

**Source:** https://developers.weixin.qq.com/miniprogram/dev/api/payment/wx.requestPayment.html

## 功能说明
发起微信支付。调用前需先从服务端获取支付参数（统一下单接口返回的预支付ID）。

## 参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| timeStamp | string | 是 | 时间戳（秒级） |
| nonceStr | string | 是 | 随机字符串，不长于32位 |
| package | string | 是 | 统一下单接口返回的 prepay_id，格式：`prepay_id=xxx` |
| signType | string | 是 | 签名算法，推荐 `RSA`（V3接口），旧接口用 `MD5` |
| paySign | string | 是 | 签名，由服务端生成 |
| success | function | 否 | 支付成功回调 |
| fail | function | 否 | 支付失败回调 |
| complete | function | 否 | 完成回调 |

## 错误信息

| errMsg | 说明 |
|--------|------|
| `requestpayment:fail cancel` | 用户取消支付 |
| `requestpayment:fail` | 支付失败（参数错误或签名错误） |
| `requestpayment:ok` | 支付成功 |

## 代码示例

```javascript
// 先从服务端获取支付参数
that.ajax(that.url.createWxPayment, 'POST', { orderId: that.orderId }, function(resp) {
  let payParam = resp.data.result;
  // 发起支付
  wx.requestPayment({
    timeStamp: payParam.timeStamp,
    nonceStr: payParam.nonceStr,
    package: payParam.package,     // "prepay_id=xxx"
    signType: payParam.signType,   // "RSA"
    paySign: payParam.paySign,
    success(res) {
      console.log('支付成功');
      // 更新订单状态
      that.ajax(that.url.updateOrderAboutPayment, 'POST', { orderId: that.orderId }, function() {
        uni.redirectTo({ url: '../order/order?orderId=' + that.orderId });
      });
    },
    fail(err) {
      if (err.errMsg.indexOf('cancel') > -1) {
        uni.showToast({ icon: 'none', title: '已取消支付' });
      } else {
        uni.showToast({ icon: 'none', title: '支付失败，请重试' });
      }
    }
  });
});
```

## HXDS 微信支付流程

```
顾客端发起支付
  → bff-customer /order/createWxPayment (POST)
  → hxds-odr 调用微信统一下单 V3 API
  → 返回 { timeStamp, nonceStr, package, signType, paySign }
  → 小程序调用 wx.requestPayment
  → 微信支付完成后异步回调 WECHAT_PAY_NOTIFY_URL
  → 更新 tb_order.pay_id / pay_time / tb_order_bill.real_pay
```

## 数据库字段对应

| 字段 | 说明 |
|------|------|
| `tb_order.prepay_id` | 统一下单返回的预支付ID |
| `tb_order.pay_id` | 微信支付流水号（transaction_id，来自回调） |
| `tb_order.pay_time` | 支付完成时间 |
| `tb_order_bill.real_pay` | 实际支付金额（单位：元） |

## 注意事项

- 所有支付参数必须由服务端生成（不能在前端计算签名）
- 微信支付API传金额单位为**分**，数据库存储单位为**元**
- V3接口使用 RSA 签名，V2接口使用 MD5
- 支付回调需验签，防止伪造通知

## 兼容性
基础库 1.0.0+
