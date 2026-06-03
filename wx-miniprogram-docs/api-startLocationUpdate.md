# wx.startLocationUpdate

**Source:** https://developers.weixin.qq.com/miniprogram/dev/api/location/wx.startLocationUpdate.html

## 功能说明
开启小程序进入前台时接收位置消息（地理围栏）。支持前台定位。

## 参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | string | 否 | 坐标系类型。`wgs84`（默认）或 `gcj02` |
| success | function | 否 | 接口调用成功的回调函数 |
| fail | function | 否 | 接口调用失败的回调函数 |
| complete | function | 否 | 接口调用结束的回调函数 |

## 关联事件

**wx.onLocationChange(callback)** - 位置变更时触发，回调参数：
- `latitude` - 纬度
- `longitude` - 经度
- `accuracy` - 精确度（m）
- `altitude` - 高度（m）
- `speed` - 速度（m/s）
- `heading` - 方向（度）

**wx.onLocationChangeError(callback)** - 位置获取失败时触发

## 相关API

- `wx.stopLocationUpdate()` - 关闭位置更新
- `wx.startLocationUpdateBackground()` - 后台位置更新
- `wx.offLocationChange()` - 取消监听
- `wx.getLocation()` - 单次获取位置

## 代码示例

```javascript
// 开启前台位置更新
wx.startLocationUpdate({
  type: 'gcj02',
  success() {
    console.log('位置更新已开启');
  },
  fail(err) {
    console.error('开启失败：', err);
  }
});

// 监听位置变化
wx.onLocationChange(function(res) {
  console.log('位置更新：', res.latitude, res.longitude);
});

// 页面卸载时关闭
wx.stopLocationUpdate();
```

## 注意事项

- 需声明 `requiredPrivateInfos: ["startLocationUpdate"]`
- 需用户授权 `scope.userLocation`
- 前台更新：小程序在前台时才接收更新
- 后台更新需调用 `startLocationUpdateBackground`

## 兼容性
基础库 2.8.0+
