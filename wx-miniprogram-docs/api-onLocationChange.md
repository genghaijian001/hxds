# wx.onLocationChange

**Source:** https://developers.weixin.qq.com/miniprogram/dev/api/location/wx.onLocationChange.html

## 功能说明
监听实时地理位置变化事件。需先调用 `wx.startLocationUpdate` 或 `wx.startLocationUpdateBackground` 才能触发此事件。

## 参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| listener | function | 是 | 位置变化时的回调函数 |

## 回调参数

| 属性 | 类型 | 说明 |
|------|------|------|
| latitude | number | 纬度 |
| longitude | number | 经度 |
| accuracy | number | 位置精确度（m） |
| altitude | number | 高度（m） |
| verticalAccuracy | number | 垂直精度（m） |
| horizontalAccuracy | number | 水平精度（m） |
| speed | number | 速度（m/s） |
| heading | number | 方向（度） |

## 相关API

- `wx.offLocationChange()` - 取消监听
- `wx.onLocationChangeError()` - 监听错误
- `wx.startLocationUpdate()` - 开启前台位置更新
- `wx.startLocationUpdateBackground()` - 开启后台位置更新
- `wx.stopLocationUpdate()` - 停止位置更新

## 代码示例

```javascript
// 注册监听（需在startLocationUpdate之前）
wx.onLocationChange(function(res) {
  console.log('lat:', res.latitude, 'lng:', res.longitude);
  // 上报到服务器
  wx.request({
    url: 'https://api.example.com/location',
    method: 'POST',
    data: { lat: res.latitude, lng: res.longitude }
  });
});

// 监听错误
wx.onLocationChangeError(function(err) {
  console.error('位置获取失败：', err);
});

// 开启更新
wx.startLocationUpdate({ type: 'gcj02' });

// 不再需要时取消监听
wx.offLocationChange();
wx.stopLocationUpdate();
```

## HXDS项目使用方式（App.vue）

```javascript
// hxds-driver-wx/App.vue 中的位置上报逻辑
wx.startLocationUpdateBackground({
  type: 'gcj02',
  success() { console.log('后台定位开启'); }
});

wx.onLocationChange(function({ latitude, longitude }) {
  let workStatus = uni.getStorageSync('workStatus');
  if (workStatus == '开始接单') {
    // 上报位置到后端 updateLocationCache
    uni.request({
      url: `${baseUrl}/driver/location/updateLocationCache`,
      method: 'POST',
      data: { latitude, longitude, /* ...其他设置 */ }
    });
  }
});
```

## 注意事项

- 需在 `requiredPrivateInfos` 声明 `"onLocationChange"`
- 频繁位置更新会增加电量消耗
- 监听回调在 `startLocationUpdate` 之前注册

## 兼容性
基础库 2.8.0+
