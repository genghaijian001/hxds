# 小程序页面生命周期

**Source:** https://developers.weixin.qq.com/miniprogram/dev/framework/app-service/page.html

## 生命周期方法

| 方法 | 触发时机 | 参数 | 说明 |
|------|---------|------|------|
| onLoad | 页面创建时 | options（query参数） | 只触发一次 |
| onShow | 页面显示时（含从后台切回） | 无 | 每次显示都触发 |
| onReady | 初次渲染完成 | 无 | 只触发一次 |
| onHide | 页面从前台变后台 | 无 | 切换Tab或navigateTo时触发 |
| onUnload | 页面销毁时 | 无 | redirectTo/navigateBack时触发 |

## 生命周期顺序

```
onLoad → onShow → onReady
（再次进入）→ onShow
（离开）→ onHide
（销毁）→ onUnload
```

## 附加事件处理

| 方法 | 触发时机 |
|------|---------|
| onPullDownRefresh | 下拉刷新 |
| onReachBottom | 页面滚动到底部 |
| onPageScroll | 页面滚动 |
| onResize | 页面尺寸变化 |
| onTabItemTap | Tab被点击 |
| onShareAppMessage | 点击转发 |

## UniApp（Vue）生命周期对应

```javascript
export default {
  data() {
    return { /* 数据 */ };
  },
  onLoad(options) {
    // 等同于 Page.onLoad
    let orderId = options.orderId;
  },
  onShow() {
    // 等同于 Page.onShow
  },
  onReady() {
    // 等同于 Page.onReady
  },
  onHide() {
    // 清理定时器等
    clearInterval(this.timer);
  },
  onUnload() {
    // 页面销毁时的清理
  },
  methods: { /* 方法 */ }
};
```

## 注意事项

- UniApp 的 `onLoad` 等生命周期需写在 export default 根层，不在 `methods` 里
- 定时器（setInterval）需在 `onHide` 或 `onUnload` 中清除，防止内存泄漏
- `onShow` 每次页面显示都触发，适合做数据刷新
- 页面间传参通过 URL query 字符串，在 `onLoad(options)` 中获取
