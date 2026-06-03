# wx.chooseAddress

**Source:** https://developers.weixin.qq.com/miniprogram/dev/api/open-api/address/wx.chooseAddress.html

## 功能说明
获取用户收货地址。调起用户编辑收货地址原生界面，并在编辑完成后返回用户选择的地址。

## 参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| success | function | 否 | 成功回调 |
| fail | function | 否 | 失败回调 |
| complete | function | 否 | 完成回调 |

## 返回值（success回调参数）

| 属性 | 类型 | 说明 |
|------|------|------|
| userName | string | 收货人姓名 |
| postalCode | string | 邮编 |
| provinceName | string | 国标收货地址第一级地址（省） |
| cityName | string | 国标收货地址第二级地址（市） |
| countyName | string | 国标收货地址第三级地址（区） |
| detailInfo | string | 详细收货地址信息 |
| nationalCode | string | 收货地址国家码 |
| telNumber | string | 收货人手机号码 |

## 代码示例

```javascript
wx.chooseAddress({
  success(res) {
    const address = [
      res.provinceName,
      res.cityName,
      res.countyName,
      res.detailInfo
    ].join('');
    console.log('完整地址：', address);
    console.log('收货人：', res.userName);
    console.log('电话：', res.telNumber);
  },
  fail(err) {
    console.error('获取地址失败：', err);
  }
});
```

## 注意事项

- 需用户授权（弹出系统权限弹窗）
- 返回微信地址簿中的地址，非GPS坐标
- 该接口主要用于电商场景，代驾项目一般不使用

## 兼容性
基础库 1.1.0+
