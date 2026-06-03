# 微信小程序隐私保护指引

**Source:** https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/privacy.html

## 背景

微信2023年9月起强制执行隐私保护规则：
- 调用隐私相关API前必须获得用户同意
- 必须在 `manifest.json` 中配置 `__usePrivacyCheck__: true`
- 必须实现 `wx.onNeedPrivacyAuthorization` 回调

## manifest.json / app.json 配置

```json
// manifest.json (UniApp) 中的 mp-weixin 节点
{
  "mp-weixin": {
    "__usePrivacyCheck__": true,
    "requiredPrivateInfos": [
      "onLocationChange",
      "startLocationUpdate",
      "startLocationUpdateBackground",
      "getLocation",
      "chooseLocation"
    ]
  }
}
```

## wx.onNeedPrivacyAuthorization

当用户首次调用隐私相关API时自动触发，必须在 `App.onLaunch` 中注册。

### 参数

| 参数名 | 类型 | 说明 |
|--------|------|------|
| callback | function | 触发回调，接收 resolve 函数和 eventInfo |

### resolve 参数

| 参数名 | 说明 |
|--------|------|
| `{ buttonId: 'agree-btn', event: 'agreeprivacyauthorization' }` | 用户同意 |
| `{ event: 'rejectprivacyauthorization' }` | 用户拒绝 |

## 完整实现示例（App.vue）

```javascript
// App.vue onLaunch
onLaunch() {
  // 注册隐私授权回调（必须在App.onLaunch中注册）
  if (wx.onNeedPrivacyAuthorization) {
    wx.onNeedPrivacyAuthorization((resolve, eventInfo) => {
      // 弹出自定义隐私弹窗
      uni.showModal({
        title: '隐私保护提示',
        content: '在使用代驾服务前，请阅读并同意《隐私保护指引》。本应用将使用您的位置信息提供代驾服务。',
        confirmText: '同意',
        cancelText: '拒绝',
        success(res) {
          if (res.confirm) {
            // 用户同意
            resolve({
              buttonId: 'agree-btn',
              event: 'agreeprivacyauthorization'
            });
          } else {
            // 用户拒绝
            resolve({ event: 'rejectprivacyauthorization' });
          }
        }
      });
    });
  }
}
```

## 隐私弹窗组件（推荐方式）

```html
<!-- components/privacy-popup/privacy-popup.vue -->
<template>
  <view v-if="show" class="privacy-mask">
    <view class="privacy-box">
      <view class="title">隐私保护提示</view>
      <view class="content">
        在使用本服务前，请阅读并同意
        <text class="link" @tap="openPolicy">《隐私保护指引》</text>
      </view>
      <view class="btns">
        <button @tap="reject" class="btn-reject">拒绝</button>
        <button 
          id="agree-btn"
          open-type="agreePrivacyAuthorization"
          @agreeprivacyauthorization="agree"
          class="btn-agree"
        >同意</button>
      </view>
    </view>
  </view>
</template>
```

**注意**：同意按钮必须使用 `open-type="agreePrivacyAuthorization"`，不能用普通点击事件替代。

## requiredPrivateInfos 说明

| API | 需声明的privacyInfo |
|-----|---------------------|
| wx.getLocation | `getLocation` |
| wx.chooseLocation | `chooseLocation` |
| wx.startLocationUpdate | `startLocationUpdate` |
| wx.startLocationUpdateBackground | `startLocationUpdateBackground` |
| wx.onLocationChange | `onLocationChange` |
| wx.chooseAddress | `chooseAddress` |
| wx.getWeRunData | `getWeRunData` |
| wx.getUserProfile | `getUserProfile`（已废弃） |

## wx.requirePrivacyAuthorize（主动触发）

```javascript
// 主动检查/触发隐私授权
wx.requirePrivacyAuthorize({
  success() {
    console.log('用户已同意隐私授权，可以调用相关API');
    wx.getLocation({ type: 'gcj02', success: res => {} });
  },
  fail(err) {
    console.log('用户拒绝或未处理');
  }
});
```

## 注意事项

- `__usePrivacyCheck__` 只在 `mp-weixin` 节点有效（UniApp manifest.json）
- `onNeedPrivacyAuthorization` 回调中的 resolve **必须调用**，否则阻塞API
- 若用户拒绝，相关隐私API会返回错误（errno: 104或105）
- 建议在小程序隐私协议页面展示具体的数据使用说明
