# 微信小程序开发文档索引

本目录存放 HXDS 代驾项目开发所需的微信小程序官方文档摘要，供所有开发 Agent 快速参考。

## 位置 API

| 文件 | API | 说明 |
|------|-----|------|
| [api-getLocation.md](api-getLocation.md) | wx.getLocation | 单次获取位置 |
| [api-startLocationUpdate.md](api-startLocationUpdate.md) | wx.startLocationUpdate | 开启前台位置更新 |
| [api-onLocationChange.md](api-onLocationChange.md) | wx.onLocationChange | 监听位置变化 |
| [api-startLocationUpdateBackground.md](api-startLocationUpdateBackground.md) | wx.startLocationUpdateBackground | 开启后台位置更新 |
| [api-chooseLocation.md](api-chooseLocation.md) | wx.chooseLocation | 地图选点 |
| [api-chooseAddress.md](api-chooseAddress.md) | wx.chooseAddress | 选择收货地址 |

## 支付 API

| 文件 | API | 说明 |
|------|-----|------|
| [api-requestPayment.md](api-requestPayment.md) | wx.requestPayment | 发起微信支付 |

## 组件

| 文件 | 组件 | 说明 |
|------|------|------|
| [component-map.md](component-map.md) | map | 地图组件（标记、折线、坐标系） |

## 隐私保护

| 文件 | 说明 |
|------|------|
| [privacy-protection.md](privacy-protection.md) | 隐私协议配置、__usePrivacyCheck__、onNeedPrivacyAuthorization |

## 框架

| 文件 | 说明 |
|------|------|
| [page-lifecycle.md](page-lifecycle.md) | 页面生命周期（onLoad/onShow/onHide/onUnload） |

## 关键规范

- **坐标系**: 地图组件和国内定位API均使用 **GCJ-02（火星坐标系）**，wx.getLocation 需指定 `type: 'gcj02'`
- **隐私合规**: 2023年9月起强制要求 `__usePrivacyCheck__` + `onNeedPrivacyAuthorization`
- **requiredPrivateInfos**: 使用位置API必须声明（见 privacy-protection.md）
- **支付签名**: 所有支付参数由服务端生成，V3接口用RSA签名
- **金额单位**: 微信支付传**分**，数据库存储**元**
