# HXDS Cloud

HXDS Cloud 是一个完整的代驾平台项目，包含：

- 乘客微信小程序 `hxds-customer-wx`
- 司机微信小程序 `hxds-driver-wx`
- Vue 3 管理后台 `hxds-mis-vue`
- Java 微服务后端 `hxds/`
- 云函数与辅助部署脚本

当前仓库已经演进到新的服务结构，后端基于 **Java 21 + Spring Boot 3.3 + Spring Cloud Alibaba 2023**，并保留了面向小程序、管理后台、地图、支付、消息、规则引擎和数据分析的完整业务链路。

## 项目结构

```text
hxds-cloud-master
|-- hxds-customer-wx              # 乘客端 UniApp 小程序
|-- hxds-driver-wx                # 司机端 UniApp 小程序
|-- hxds-mis-vue                  # MIS 管理后台前端（Vue 3 + Vite）
|-- hxds/                         # Java 微服务后端
|   |-- common                    # 公共模块
|   |-- gateway                   # API 网关
|   |-- bff-customer              # 乘客端 BFF
|   |-- bff-driver                # 司机端 BFF
|   |-- hxds-cst                  # 乘客服务
|   |-- hxds-dr                   # 司机服务
|   |-- hxds-odr                  # 订单服务
|   |-- hxds-snm                  # 消息通知服务
|   |-- hxds-mps                  # 地图与位置服务
|   |-- hxds-rule                 # 规则引擎服务
|   |-- hxds-nebula               # GPS / 监控 / 大数据服务
|   `-- hxds-mis-api              # MIS 后台接口服务
|-- db/                           # 当前数据库初始化脚本与业务规则脚本
|-- docker/                       # 本地/测试环境中间件与 HBase/Phoenix 支持
|-- cloudfunctions/               # 云函数（当前包含 OCR 服务）
|-- images/                       # README 展示图片
|-- wx-miniprogram-docs/          # 小程序开发要点与官方能力摘要
`-- tasks/                        # 当前仓库内的实施方案、部署文档与运维草稿
```

## 核心能力

- 微信小程序乘客下单、代驾呼叫、支付、评价
- 微信小程序司机听单、接单、到达、开始代驾、结束订单
- BFF 聚合接口、统一鉴权、上下游服务编排
- 地图路径计算、附近司机匹配、订单位置缓存
- 微信支付、小程序消息、短信、OCR、人脸与对象存储能力接入
- 规则引擎驱动代驾费、取消费、奖励与分账
- 管理后台支持司机、订单、评论、券、权限等运营管理

## 当前技术栈

### 后端

- Java 21
- Spring Boot 3.3.13
- Spring Cloud 2023.0.3
- Spring Cloud Alibaba 2023.0.3.3
- Spring Cloud Gateway
- OpenFeign
- MyBatis
- Redis
- MongoDB
- RabbitMQ
- Nacos
- MinIO / 腾讯云 COS
- 当前最新版不依赖 TX-LCN，分布式事务中间件不属于当前激活运行链路。

### 前端

- UniApp
- uView UI
- Vue 3
- Vite

### 云能力 / 小程序生态

- 微信支付
- 腾讯地图
- 微信同声传译插件
- 微信 OCR / 证件识别相关接入
- 云函数 OCR 服务封装

## 当前服务架构

请求主链路：

```text
微信小程序 / MIS 前端
        ->
      Gateway
        ->
  bff-customer / bff-driver / hxds-mis-api
        ->
  cst / dr / odr / snm / mps / rule / nebula
        ->
MySQL / Redis / MongoDB / RabbitMQ / Nacos / MinIO
```

## 主要服务说明

| 服务 | 说明 |
|---|---|
| `gateway` | 统一 API 网关入口 |
| `bff-customer` | 面向乘客端的小程序聚合接口 |
| `bff-driver` | 面向司机端的小程序聚合接口 |
| `hxds-cst` | 乘客账号、资料、券等能力 |
| `hxds-dr` | 司机资料、实名、钱包、OCR 相关能力 |
| `hxds-odr` | 订单状态机、账单、支付状态流转 |
| `hxds-snm` | 消息通知、消息中心、队列消费 |
| `hxds-mps` | 路线计算、司机匹配、位置缓存 |
| `hxds-rule` | 规则引擎与业务规则数据 |
| `hxds-nebula` | GPS 轨迹、监控数据、分析能力 |
| `hxds-mis-api` | 管理后台接口层 |

## 运行环境

本项目当前默认依赖以下基础设施：

- MySQL
- Redis
- MongoDB
- RabbitMQ
- Nacos
- MinIO
- HBase / Phoenix（仅 `hxds-nebula` 场景需要）

开发环境可参考：

- [docker/docker-compose.yml](docker/docker-compose.yml)
- [tasks/hxds-production-deployment-checklist.md](tasks/hxds-production-deployment-checklist.md)
- [tasks/hxds-production-operations-manual-tencent-cloud.md](tasks/hxds-production-operations-manual-tencent-cloud.md)

## 敏感配置说明

GitHub 仓库版本仅保留云服务、支付、短信、对象存储等配置项的环境变量占位符，不提交真实密钥。
本地开发如需启动，请通过本机环境变量或不纳入 Git 管理的本地覆盖配置注入真实值。

## 本地开发

### 1. 后端

```bash
cd hxds
mvn clean package -DskipTests
```

逐个服务运行示例：

```bash
cd hxds/hxds-odr
mvn clean package -DskipTests
java -jar target/hxds-odr-0.0.1-SNAPSHOT.jar
```

### 2. 管理后台

```bash
cd hxds-mis-vue
npm install
npm run dev
```

### 3. 小程序

```bash
cd hxds-customer-wx
npm install

cd ../hxds-driver-wx
npm install
```

然后使用 HBuilderX / 微信开发者工具编译 `unpackage/dist/dev/mp-weixin/` 目录。

## 数据库脚本

当前仓库使用按业务库拆分后的 SQL 文件：

- `db/hxds_cst.sql`
- `db/hxds_dr.sql`
- `db/hxds_odr.sql`
- `db/hxds_rule.sql`
- `db/hxds_mis.sql`
- `db/hxds_vhr.sql`

业务规则脚本：

- `db/代驾费规则.sql`
- `db/分账规则.sql`
- `db/取消规则.sql`
- `db/奖励规则.sql`
- `db/工作流.sql`

## 部署与运维文档

当前仓库已经补充了一批针对现版本的中文方案文档：

- [单 AppID 正好代驾重构实施方案 v2](tasks/single-appid-refactor-v2.md)
- [生产部署清单](tasks/hxds-production-deployment-checklist.md)
- [Windows 本地迁移到腾讯云 Linux 操作手册](tasks/hxds-production-operations-manual-tencent-cloud.md)
- [域名与上线配置清单](tasks/zdkjdj-production-deployment-checklist.md)
- [Nginx / 网关 / 小程序域名配置模板](tasks/nginx-gateway-wechat-domain-template.md)
- [生产 docker-compose 初版模板](tasks/hxds-docker-compose-prod-template.md)

## 截图

### 登录与注册
![登录与注册](images/19b42ea4ce97ae85bed914efd42c06cd.png)

### 工作台
![工作台](images/a3f5e7c370d7e4fbe78241e0bace415d.png)

### 司机实名认证
![司机实名认证](images/c69fbd4bb2a27c9d007ea11a7ab4bbc9.png)

### 乘客下单
![乘客下单](images/88662dec6d60331055efc705ff834e19.png)

### 司机接单
![司机接单](images/b64cd58bef9c5eea8dd5bd17f59b7674.png)

### 订单抢单 / 状态流转
![订单抢单](images/6b7e7f9edf79a90541578950e06cc570.png)
![订单状态流转](images/c90024c1735b2ff6d76e5669a353df3a.png)

### 到达 / 同显 / 结束 / 支付
![到达确认](images/97ff29bd44341881942d68cf496f6ece.png)
![同显页面](images/0ba6552a69155f0ad8b2378dcfde7b10.png)
![结束代驾](images/6f32414dd31722411474284439bb8e3d.png)
![账单推送](images/dc67a6a7bb2f54030c896b98ea083448.png)
![支付与评价](images/8b522364a38af29202b9158a3b62ddc0.png)

## License

本仓库保留原项目授权文件：

- [LICENSE](LICENSE)
