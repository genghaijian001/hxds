# wx.chooseLocation

**Source:** https://developers.weixin.qq.com/miniprogram/dev/api/location/wx.chooseLocation.html

## 功能说明
打开地图选择位置。调用前需要 `scope.userLocation` 授权。

## 参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| latitude | number | 否 | 目标地纬度（地图中心） |
| longitude | number | 否 | 目标地经度（地图中心） |
| success | function | 否 | 成功回调 |
| fail | function | 否 | 失败回调 |
| complete | function | 否 | 完成回调 |

## 返回值（success回调参数）

| 属性 | 类型 | 说明 |
|------|------|------|
| name | string | 位置名称 |
| address | string | 详细地址 |
| latitude | number | 纬度（范围 -90 到 90） |
| longitude | number | 经度（范围 -180 到 180） |

## 代码示例

```javascript
wx.chooseLocation({
  latitude: 39.9042,
  longitude: 116.4074,
  success(res) {
    console.log('选择位置：', res.name);
    console.log('地址：', res.address);
    console.log('坐标：', res.latitude, res.longitude);
    // 保存到storage
    uni.setStorageSync('from', {
      address: res.address,
      latitude: res.latitude,
      longitude: res.longitude
    });
  },
  fail(err) {
    if (err.errMsg.indexOf('cancel') > -1) {
      console.log('用户取消');
    } else {
      console.error('选择失败：', err);
    }
  }
});
```

## 注意事项

- 需在 `requiredPrivateInfos` 声明 `"chooseLocation"`
- 需用户授权 `scope.userLocation`
- 返回的坐标为 GCJ-02（火星坐标系）
- 使用腾讯/高德地图 API 时坐标系兼容

## 兼容性
基础库 1.1.0+
