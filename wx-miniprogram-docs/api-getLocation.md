# wx.getLocation

**Source:** https://developers.weixin.qq.com/miniprogram/dev/api/location/wx.getLocation.html

## 功能说明
获取当前的地理位置、速度。当用户离开小程序后，此接口无法调用。

## 参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | string | 否 | 坐标系类型。`wgs84`（默认，GPS坐标）或 `gcj02`（火星坐标系，适用于国内地图） |
| altitude | boolean | 否 | 是否返回高度信息，默认 `false` |
| highAccuracyAndroid | boolean | 否 | Android 高精度定位，默认 `false` |
| isHighAccuracy | boolean | 否 | 开启高精度定位，默认 `false` |
| success | function | 否 | 接口调用成功的回调函数 |
| fail | function | 否 | 接口调用失败的回调函数 |
| complete | function | 否 | 接口调用结束的回调函数（调用成功、失败都会执行） |

## 返回值（success回调参数）

| 属性 | 类型 | 说明 |
|------|------|------|
| latitude | number | 纬度，范围 -90 到 90 |
| longitude | number | 经度，范围 -180 到 180 |
| speed | number | 速度，单位 m/s |
| accuracy | number | 位置的精确度，单位 m |
| altitude | number | 高度，单位 m（altitude=true时返回） |
| verticalAccuracy | number | 垂直精度，单位 m |
| horizontalAccuracy | number | 水平精度，单位 m |

## 错误码

| 错误码 | 说明 |
|--------|------|
| 1 | 用户拒绝定位权限 |
| 2 | 定位服务已关闭 |
| 3 | 定位请求超时 |
| 4 | API调用失败 |

## 代码示例

```javascript
// 基本用法（国内地图用gcj02）
wx.getLocation({
  type: 'gcj02',
  success(res) {
    const { latitude, longitude, speed, accuracy } = res;
    console.log('纬度：', latitude);
    console.log('经度：', longitude);
  },
  fail(err) {
    console.error('定位失败：', err);
  }
});

// 高精度定位
wx.getLocation({
  type: 'gcj02',
  isHighAccuracy: true,
  altitude: true,
  success(res) {
    console.log('高度：', res.altitude);
  }
});
```

## 注意事项

- 需在 `app.json` 中配置 `permission.scope.userLocation`
- 需在 `requiredPrivateInfos` 中声明 `"getLocation"`
- 国内地图（腾讯/高德）使用 `gcj02` 坐标系
- GPS定位需要时间，建议显示加载状态
- 高精度定位耗电较多

## 兼容性
基础库 1.0.0+
