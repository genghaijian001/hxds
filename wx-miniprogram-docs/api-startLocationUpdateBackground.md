# wx.startLocationUpdateBackground

**Source:** https://developers.weixin.qq.com/miniprogram/dev/api/location/wx.startLocationUpdateBackground.html

## 功能说明
开启小程序进入前台/后台时均接收位置消息（地理围栏），需用户授权 `scope.userLocationBackground`。

## 参数

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | string | 否 | 坐标系类型。`wgs84`（默认）或 `gcj02` |
| success | function | 否 | 成功回调 |
| fail | function | 否 | 失败回调 |
| complete | function | 否 | 完成回调 |

## 与 startLocationUpdate 的区别

| 特性 | startLocationUpdate | startLocationUpdateBackground |
|------|--------------------|-----------------------------|
| 前台定位 | ✅ | ✅ |
| 后台定位 | ❌ | ✅ |
| 权限要求 | scope.userLocation | scope.userLocationBackground |
| app.json配置 | — | requiredBackgroundModes: ["location"] |

## app.json 配置

```json
{
  "requiredBackgroundModes": ["location"],
  "permission": {
    "scope.userLocation": {
      "desc": "后台定位用于接单时实时更新位置"
    },
    "scope.userLocationBackground": {
      "desc": "后台定位用于接单时实时更新位置"
    }
  },
  "requiredPrivateInfos": [
    "onLocationChange",
    "startLocationUpdate",
    "startLocationUpdateBackground"
  ]
}
```

## 代码示例

```javascript
// 申请后台定位权限
wx.authorize({
  scope: 'scope.userLocationBackground',
  success() {
    wx.startLocationUpdateBackground({
      type: 'gcj02',
      success() {
        console.log('后台定位已开启');
        wx.onLocationChange(function(res) {
          // 上报位置
        });
      }
    });
  },
  fail() {
    // 降级到前台定位
    wx.startLocationUpdate({ type: 'gcj02' });
  }
});
```

## 注意事项

- 需在 `app.json` 中配置 `requiredBackgroundModes: ["location"]`
- 需声明 `requiredPrivateInfos: ["startLocationUpdateBackground"]`
- 后台定位需用户明确授权 `scope.userLocationBackground`
- iOS 需在系统设置中允许"始终"使用位置权限

## 兼容性
基础库 2.8.0+
