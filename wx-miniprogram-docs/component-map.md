# map 地图组件

**Source:** https://developers.weixin.qq.com/miniprogram/dev/component/map.html

## 功能说明
地图组件，支持显示标记、折线、多边形、圆形等覆盖物，支持个性化地图样式。坐标系使用 **GCJ-02（火星坐标系）**。

## 核心属性

| 属性 | 类型 | 默认值 | 必填 | 说明 | 最低版本 |
|------|------|--------|------|------|---------|
| longitude | number | — | 是 | 中心经度（-180 to 180） | 1.0.0 |
| latitude | number | — | 是 | 中心纬度（-90 to 90） | 1.0.0 |
| scale | number | 16 | 否 | 缩放级别（3-20） | 1.0.0 |
| min-scale | number | 3 | 否 | 最小缩放级别 | 2.13.0 |
| max-scale | number | 20 | 否 | 最大缩放级别 | 2.13.0 |
| markers | Array | — | 否 | 标记点 | 1.0.0 |
| polyline | Array | — | 否 | 路线 | 1.0.0 |
| circles | Array | — | 否 | 圆形覆盖物 | 1.0.0 |
| polygons | Array | — | 否 | 多边形覆盖物 | 2.3.0 |
| show-location | boolean | false | 否 | 显示当前位置（带方向） | 1.0.0 |
| enable-3D | boolean | false | 否 | 展示3D楼块 | 2.3.0 |
| show-compass | boolean | false | 否 | 显示指南针 | 2.3.0 |
| show-scale | boolean | false | 否 | 显示比例尺 | 2.8.0 |
| enable-zoom | boolean | true | 否 | 是否支持缩放 | 2.3.0 |
| enable-scroll | boolean | true | 否 | 是否支持拖动 | 2.3.0 |
| enable-rotate | boolean | false | 否 | 是否支持旋转 | 2.3.0 |
| enable-satellite | boolean | false | 否 | 是否开启卫星图 | 2.7.0 |
| enable-traffic | boolean | false | 否 | 是否开启实时路况 | 2.7.0 |
| subkey | string | — | 否 | 个性化地图key（需购买） | 2.3.0 |

## markers 标记点对象

| 属性 | 类型 | 必填 | 说明 |
|------|------|------|------|
| id | number | 是 | 标记点 id（点击事件时返回） |
| latitude | number | 是 | 纬度 |
| longitude | number | 是 | 经度 |
| title | string | 否 | 标注名（tap时显示） |
| iconPath | string | 是 | 图标路径（网络/本地/包内路径） |
| width | number/string | 否 | 图标宽度 |
| height | number/string | 否 | 图标高度 |
| rotate | number | 否 | 旋转角度（0-360°） |
| alpha | number | 否 | 透明度（0-1，默认1） |
| callout | object | 否 | 气泡窗口 |
| label | object | 否 | 标签 |
| zIndex | number | 否 | 显示层级（2.3.0+） |
| anchor | object | 否 | 锚点 {x: 0-1, y: 0-1} |

### callout 气泡属性

| 属性 | 类型 | 说明 |
|------|------|------|
| content | string | 文本 |
| color | string | 文字颜色（#RRGGBB） |
| fontSize | number | 文字大小 |
| bgColor | string | 背景色 |
| borderColor | string | 边框颜色 |
| borderWidth | number | 边框宽度 |
| borderRadius | number | 边框圆角 |
| display | string | `'BYCLICK'`（点击显示）或 `'ALWAYS'`（始终显示） |

## polyline 折线对象

| 属性 | 类型 | 必填 | 说明 |
|------|------|------|------|
| points | array | 是 | 坐标点数组 [{latitude, longitude}] |
| color | string | 否 | 线的颜色（#RRGGBBAA） |
| width | number | 否 | 线的宽度 |
| dottedLine | boolean | 否 | 是否虚线（默认false） |
| arrowLine | boolean | 否 | 是否带箭头（默认false） |
| borderColor | string | 否 | 线的边框颜色 |
| borderWidth | number | 否 | 线的边框宽度 |

## 事件

| 事件 | 说明 | 回调数据 |
|------|------|---------|
| bindtap | 点击地图 | 返回坐标 |
| bindmarkertap | 点击标记点 | `{markerId}` |
| bindcallouttap | 点击气泡 | `{markerId}` |
| bindupdated | 地图渲染完成 | — |
| bindregionchange | 视野变化 | type, causedBy, detail |
| bindpoitap | 点击POI | `{name, longitude, latitude}` |

## MapContext（地图上下文）

通过 `wx.createMapContext(mapId)` 获取：

```javascript
const mapCtx = wx.createMapContext('myMap');

// 移动到当前位置
mapCtx.moveToLocation();

// 获取当前视野
mapCtx.getRegion({ success: res => console.log(res) });

// 设置缩放级别
mapCtx.setZoom({ zoom: 14 });

// 沿路线移动标记
mapCtx.moveAlong({
  markerId: 1,
  path: [{ latitude: 39.9, longitude: 116.4 }],
  speed: 60
});
```

## 代码示例

```html
<map
  id="myMap"
  style="width:100%;height:400rpx"
  :latitude="latitude"
  :longitude="longitude"
  :markers="markers"
  :polyline="polyline"
  show-location
  enable-traffic
  @markertap="onMarkerTap"
/>
```

```javascript
data() {
  return {
    latitude: 39.9042,
    longitude: 116.4074,
    markers: [{
      id: 1,
      latitude: 39.9042,
      longitude: 116.4074,
      iconPath: '/static/marker.png',
      width: 40,
      height: 40,
      callout: {
        content: '起点',
        display: 'ALWAYS',
        bgColor: '#2074FD',
        color: '#FFFFFF',
        borderRadius: 5,
        padding: 5
      }
    }],
    polyline: [{
      points: [
        { latitude: 39.9042, longitude: 116.4074 },
        { latitude: 39.92, longitude: 116.44 }
      ],
      color: '#2074FD',
      width: 6,
      arrowLine: true
    }]
  };
},
methods: {
  onMarkerTap(e) {
    console.log('点击标记：', e.markerId);
  }
}
```

## 注意事项

- 颜色值格式：`#RRGGBBAA`（后两位为透明度，可选）
- 坐标系为 **GCJ-02**，`wx.getLocation` 需指定 `type: 'gcj02'`
- 个性化地图（subkey）需在腾讯位置服务购买
- 2023年6月29日起个性化地图样式需付费

## 兼容性
基础库 1.0.0+
