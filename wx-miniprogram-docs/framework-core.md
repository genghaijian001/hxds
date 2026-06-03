# 微信小程序框架核心概念

## 双线程架构

- **逻辑层**：执行 JavaScript，处理业务逻辑
- **渲染层**：管理 UI 组件和视图更新
- 两层异步通信，不共享同一线程（与传统 web 不同）

## 应用生命周期

| 钩子 | 触发时机 |
|------|---------|
| onLaunch | 小程序初始化（全局只触发一次） |
| onShow | 小程序从后台进入前台 |
| onHide | 小程序从前台进入后台 |

## 页面生命周期

| 钩子 | 触发时机 |
|------|---------|
| onLoad | 页面加载，接收路由参数 |
| onShow | 页面显示 |
| onReady | 首次渲染完成 |
| onHide | 页面隐藏 |
| onUnload | 页面销毁 |

## 页面路由

```javascript
wx.navigateTo({ url: '/pages/xxx/xxx' })   // 保留当前页，跳转（最多10层）
wx.redirectTo({ url: '/pages/xxx/xxx' })   // 关闭当前页，跳转
wx.switchTab({ url: '/pages/xxx/xxx' })    // 跳转 tabBar 页面
wx.navigateBack({ delta: 1 })              // 返回上一页
wx.reLaunch({ url: '/pages/xxx/xxx' })    // 关闭所有页面，打开指定页
```

## 事件系统

- 事件通过冒泡机制传播
- 主要类型：tap、longpress、touchstart/move/end
- 子→父通信：`this.triggerEvent('eventName', data)`
- 跨页面通信：`uni.$emit` / `uni.$on`（UniApp）

## 自定义组件

```javascript
Component({
  properties: { propA: { type: String, value: '' } },
  data: { innerData: '' },
  methods: {
    myMethod() { this.triggerEvent('myevent', { value: 1 }) }
  },
  lifetimes: { created() {}, attached() {}, detached() {} }
})
```

## 数据绑定与更新

```javascript
// 必须用 setData，直接赋值不触发渲染
this.setData({ key: value })
this.setData({ 'array[0].name': 'newName' })  // 局部更新数组
```

## 注意事项

- 单次 setData 数据量不超过 1MB，避免频繁调用
- UniApp 页面钩子与原生小程序一致
- 全局状态通过 `getApp().globalData` 访问
