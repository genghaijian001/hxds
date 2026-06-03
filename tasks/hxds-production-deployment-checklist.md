# HXDS 生产环境部署清单（按当前微服务项目逐项部署版）

本文是基于当前仓库 `D:\daijia\hxds-cloud-master\hxds-cloud-master` 的实际项目结构整理的生产部署清单，目标是把本地开发环境中的微服务、数据库和中间件，迁移成真正可供微信小程序正式版访问的公网生产环境。

相关配套文档：

- [单 AppID 正好代驾重构实施方案 v2（修正版）](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/single-appid-refactor-v2.md)
- [zdkjdj.cn 上线生产部署与域名配置实施清单](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/zdkjdj-production-deployment-checklist.md)
- [Nginx 生产配置模板 + 网关映射模板 + 微信小程序后台域名填写示例](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/nginx-gateway-wechat-domain-template.md)

---

## 1. 先说结论

如果你要把 HXDS 真正上线成“微信里可正常访问的小程序后端”，你必须准备两类东西：

1. **业务服务**
   - 也就是你仓库里的 Java 微服务和前端产物

2. **基础依赖服务**
   - 也就是现在本地 `Docker Desktop` 里跑的各种数据库、中间件和注册中心

也就是说，**买了服务器以后，不是只把域名绑上去就结束了，还必须把后端代码和这些依赖一并部署到生产环境。**

---

## 2. 生产部署目标形态

推荐第一阶段生产结构：

1. 微信小程序/后台管理系统访问 `https://api.zdkjdj.cn`
2. `api.zdkjdj.cn` 进入 Nginx 或 CLB
3. Nginx 转发到 `gateway:8201`
4. Gateway 再转发到各个 BFF 和内部微服务
5. 微服务访问 MySQL、Redis、MongoDB、RabbitMQ、Nacos、MinIO 等依赖

推荐域名分工：

- `api.zdkjdj.cn`
  - 小程序 API
  - 上传下载
  - 微信支付回调
- `admin.zdkjdj.cn`
  - MIS 管理后台
- `h5.zdkjdj.cn`
  - 协议页、隐私页、活动页、web-view

---

## 3. 当前项目的生产部署对象总表

### 3.1 核心微服务

| 服务 | 端口 | 是否建议首期必上 | 说明 |
|---|---:|---|---|
| `gateway` | 8201 | 是 | 所有小程序流量统一入口 |
| `bff-driver` | 8101 | 是 | 司机端聚合层 |
| `bff-customer` | 8102 | 是 | 乘客端聚合层 |
| `hxds-dr` | 8001 | 是 | 司机资料、认证、位置能力 |
| `hxds-cst` | 8007 | 是 | 乘客账号资料 |
| `hxds-odr` | 8002 | 是 | 订单生命周期、支付状态、超时关闭 |
| `hxds-snm` | 8003 | 是 | 消息中心、通知 |
| `hxds-mps` | 8004 | 是 | 地图、路径、附近司机计算 |
| `hxds-rule` | 8006 | 是 | 费用、取消、分账等规则 |
| `hxds-mis-api` | 8010 | 否（视后台是否一起上线） | 管理后台接口 |
| `hxds-nebula` | 8009 | 否（首期可暂缓） | 大数据分析/GPS 明细分析 |
| `hxds-tm` | 8070/8170 | 建议上 | TX-LCN 分布式事务管理器 |

### 3.2 基础依赖

| 依赖 | 默认端口 | 是否建议首期必上 | 说明 |
|---|---:|---|---|
| `MySQL` | 3306 | 是 | 各服务业务库 |
| `Redis` | 6379 | 是 | 会话、缓存、抢单并发、订单状态辅助 |
| `MongoDB` | 27017 | 是 | 消息服务 `hxds-snm` 数据存储 |
| `RabbitMQ` | 5672 | 是 | 通知/消息投递 |
| `Nacos` | 8848 | 是 | 服务注册与配置中心 |
| `MinIO` | 9000 | 建议上 | 本地对象存储能力 |
| `HBase/Phoenix` | 8765 等 | 否（首期可暂缓） | 主要给 `hxds-nebula` 用 |

---

## 4. 按业务闭环拆分：哪些是“必须”的

如果你的目标是先实现：

- 乘客登录
- 发起代驾订单
- 司机接单
- 行程进行
- 支付
- 消息通知

那么首期最小生产闭环必须有：

### 必须上线的微服务

- `gateway`
- `bff-customer`
- `bff-driver`
- `hxds-cst`
- `hxds-dr`
- `hxds-odr`
- `hxds-mps`
- `hxds-snm`
- `hxds-rule`
- `hxds-tm`

### 必须上线的基础依赖

- `MySQL`
- `Redis`
- `MongoDB`
- `RabbitMQ`
- `Nacos`
- `MinIO`（如果你头像、证件、图片、对象存储链路会用到）

### 首期可暂缓项

- `hxds-nebula`
- `HBase/Phoenix`
- `hxds-mis-api`（如果你先不急着开放后台）

说明：

- `hxds-nebula` 更偏 GPS 轨迹分析和大数据能力，不是乘客下单闭环的最小必需项
- 但如果你当前生产流程里确实有功能强依赖它，就不能省略

---

## 5. 按服务逐项部署说明

## 5.1 Gateway

### 服务
- `gateway`

### 作用
- 对外唯一公网 API 入口
- 按路径把请求转给 `bff-customer` / `bff-driver` / `hxds-mis-api`

### 是否必须
- 是

### 依赖
- `Nacos`
- `bff-customer`
- `bff-driver`
- `hxds-mis-api`（如启用后台）

### 建议部署方式
- Java JAR 或 Docker 容器均可
- 建议由 Nginx 转发到 `8201`

---

## 5.2 bff-customer

### 服务
- `bff-customer`

### 作用
- 乘客端 API 聚合层
- 登录、资料、消息、下单、支付前端接口入口

### 是否必须
- 是

### 依赖
- `Redis`
- `hxds-cst`
- `hxds-odr`
- `hxds-mps`
- `hxds-snm`
- `hxds-rule`
- `Nacos`

### 建议部署方式
- 与 `gateway` 同机部署也可以
- 生产配置中 Redis/Feign/Nacos 地址必须改成线上地址

---

## 5.3 bff-driver

### 服务
- `bff-driver`

### 作用
- 司机端 API 聚合层
- 司机登录、接单、订单、消息、钱包、认证接口入口

### 是否必须
- 是

### 依赖
- `Redis`
- `hxds-dr`
- `hxds-odr`
- `hxds-snm`
- `hxds-mps`
- `hxds-rule`
- `Nacos`

### 建议部署方式
- 与 `bff-customer` 同组部署即可

---

## 5.4 hxds-cst

### 服务
- `hxds-cst`

### 作用
- 乘客账号资料
- 注册、登录映射、手机号、头像、个人信息

### 是否必须
- 是

### 依赖
- `MySQL(hxds_cst)`
- `Nacos`

### 建议部署方式
- 生产上要确保库结构初始化完成

---

## 5.5 hxds-dr

### 服务
- `hxds-dr`

### 作用
- 司机资料、身份认证、车辆信息、位置等

### 是否必须
- 是

### 依赖
- `MySQL(hxds_dr)`
- `Redis`
- `Nacos`
- 可能还涉及 OCR/人脸相关云配置

### 建议部署方式
- 与 `hxds-cst` 类似
- 要检查腾讯云 OCR、人脸识别等生产密钥配置

---

## 5.6 hxds-odr

### 服务
- `hxds-odr`

### 作用
- 订单状态机核心
- 费用账单
- 取消、结束、待支付、已支付
- 超时任务

### 是否必须
- 是

### 依赖
- `MySQL(hxds_odr)`
- `Redis`
- `Nacos`
- `hxds-rule`
- `hxds-tm`

### 建议部署方式
- 必须重点验证订单状态流转和微信支付回调
- 这里是生产最核心服务之一

---

## 5.7 hxds-snm

### 服务
- `hxds-snm`

### 作用
- 通知、消息中心、消息投递、Mongo 消息记录

### 是否必须
- 是

### 依赖
- `MongoDB`
- `RabbitMQ`
- `Nacos`

### 建议部署方式
- 要特别验证消息列表、消息详情、未读状态

---

## 5.8 hxds-mps

### 服务
- `hxds-mps`

### 作用
- 地图路径规划、附近司机搜索、距离/时长计算

### 是否必须
- 是

### 依赖
- `Redis`
- `Nacos`
- 腾讯地图能力配置

### 建议部署方式
- 必须验证乘客起终点搜索、预估里程、司机匹配

---

## 5.9 hxds-rule

### 服务
- `hxds-rule`

### 作用
- 费用规则、取消规则、奖励、分账、工作流

### 是否必须
- 是

### 依赖
- `MySQL(hxds_rule)`
- `Nacos`

### 建议部署方式
- 规则表初始化后再启动业务服务联调

---

## 5.10 hxds-tm

### 服务
- `hxds-tm`

### 作用
- TX-LCN 分布式事务协调器

### 是否必须
- 建议上

### 依赖
- `MySQL(tx-manager)`
- `Redis`

### 建议部署方式
- 如果你线上仍然保留 TX-LCN 事务链路，就必须上线
- 不要默认省略

---

## 5.11 hxds-mis-api

### 服务
- `hxds-mis-api`

### 作用
- 管理后台 API

### 是否必须
- 如果你暂时不开放管理后台，可稍后部署

### 依赖
- `MySQL(hxds_mis)`
- `Redis`
- `Nacos`

---

## 5.12 hxds-nebula

### 服务
- `hxds-nebula`

### 作用
- GPS 明细、大数据分析、轨迹分析

### 是否必须
- 首期通常可暂缓

### 依赖
- `HBase/Phoenix`
- `Nacos`

### 建议部署方式
- 如果你暂时只先跑小程序核心订单闭环，可延后
- 但如果某个生产功能已经强依赖它，就必须补上

---

## 6. 基础依赖逐项部署说明

## 6.1 MySQL

### 是否必须
- 是

### 用途
- `hxds_dr`
- `hxds_cst`
- `hxds_odr`
- `hxds_rule`
- `hxds_mis`
- `tx-manager`

### 建议
- 首期可单机部署
- 但必须持久化、备份
- 所有 SQL 初始化脚本要按库逐个执行

---

## 6.2 Redis

### 是否必须
- 是

### 用途
- Sa-Token
- 抢单并发控制
- 订单辅助状态
- GEO 地理位置缓存
- TX-LCN

### 建议
- 生产必须设密码
- Redis DB 分库编号需和服务配置一致

---

## 6.3 MongoDB

### 是否必须
- 是（只要你保留消息中心）

### 用途
- `hxds-snm` 消息记录、消息引用

---

## 6.4 RabbitMQ

### 是否必须
- 是（只要你保留消息/通知）

### 用途
- 新订单/通知消息投递

---

## 6.5 Nacos

### 是否必须
- 是

### 用途
- 服务注册发现
- 配置中心

### 建议
- 生产要导入 `hxds` 命名空间和对应配置

---

## 6.6 MinIO

### 是否建议部署
- 建议

### 用途
- 对象存储、图片/文件

### 说明
- 你项目里同时也接了腾讯云 COS
- 生产最终到底主要用 MinIO 还是 COS，要按你当前实际代码链路决定

---

## 6.7 HBase / Phoenix

### 是否必须
- 首期通常不是

### 用途
- `hxds-nebula`

### 建议
- 如果你第一阶段不启用 `hxds-nebula`，可以先不部署

---

## 7. 首期最推荐的部署策略

### 方案 A：单机生产试运行版

适合：

- 先快速上线
- 访问量还不大
- 你要控制成本

结构：

1. 一台 Linux 服务器
2. 上面部署：
   - Nginx
   - Gateway
   - BFF
   - 核心微服务
   - MySQL
   - Redis
   - MongoDB
   - RabbitMQ
   - Nacos
   - MinIO
3. 对外只暴露：
   - `443`
   - 必要时 `80`

优点：

- 成本最低
- 和你当前本地 Docker Desktop 开发形态最接近

缺点：

- 容灾能力弱
- 单机风险高

---

### 方案 B：单机应用 + 云托管数据库版

适合：

- 你想先降低数据库运维风险

结构：

1. 应用和网关在服务器上
2. MySQL/Redis/Mongo 改云产品或至少部分托管

优点：

- 稳定性更高

缺点：

- 成本更高
- 配置更复杂

---

## 8. 推荐上线顺序

### 第一层：基础依赖

1. MySQL
2. Redis
3. MongoDB
4. RabbitMQ
5. Nacos
6. MinIO
7. TX-LCN Manager

### 第二层：核心服务

1. `hxds-dr`
2. `hxds-cst`
3. `hxds-rule`
4. `hxds-mps`
5. `hxds-snm`

### 第三层：订单与后台

1. `hxds-odr`
2. `hxds-mis-api`

### 第四层：BFF

1. `bff-driver`
2. `bff-customer`

### 第五层：统一入口

1. `gateway`
2. `Nginx`

---

## 9. 生产部署前必须改掉的本地开发配置

你现在很多服务默认还是本地开发地址，比如：

- `127.0.0.1`
- `localhost`
- 本地 Redis
- 本地 MySQL
- 本地 MinIO

上线前必须逐项改成生产地址：

1. MySQL 地址
2. Redis 地址
3. MongoDB 地址
4. RabbitMQ 地址
5. Nacos 地址
6. MinIO/COS 配置
7. 微信支付生产参数
8. 小程序正式域名
9. 微信开发版里写死的 `127.0.0.1:8201`

---

## 10. Docker Desktop 和生产的关系

必须明确：

- 你本地 `Docker Desktop` 是开发环境
- 生产服务器上的 Docker 是另一个独立环境

也就是说：

- 本地容器不会自动变成线上容器
- 本地数据库不会自动变成线上数据库
- 本地 RabbitMQ/MongoDB/HBase 不会自动提供公网服务

如果你要线上继续沿用容器化思路，正确做法是：

1. 在线上 Linux 服务器安装 Docker Engine
2. 准备 `docker-compose.yml` 或镜像
3. 把 MySQL/Redis/Mongo/RabbitMQ/Nacos/MinIO 等在线上启动
4. 把 Java 服务也在线上启动

---

## 11. 第一阶段最现实的建议

如果你现在要的是：

- 先把小程序真正跑上线
- 域名接通
- 微信能正常请求
- 乘客能下单
- 司机能接单

那么我建议：

### 必做

1. 买腾讯云中国内地服务器
2. 完成备案
3. 先部署：
   - MySQL
   - Redis
   - MongoDB
   - RabbitMQ
   - Nacos
   - MinIO
   - Gateway
   - BFF
   - `hxds-cst`
   - `hxds-dr`
   - `hxds-odr`
   - `hxds-mps`
   - `hxds-snm`
   - `hxds-rule`
   - `hxds-tm`
4. 接 `api.zdkjdj.cn`
5. 配微信小程序合法域名
6. 配微信支付回调地址

### 可暂缓

1. `hxds-nebula`
2. `HBase/Phoenix`
3. `hxds-mis-api`
4. 管理后台前端

---

## 12. 部署完成后的验收顺序

### 小程序乘客闭环

1. 登录
2. 地图定位
3. 选择起终点
4. 创建订单
5. 查询当前订单
6. 支付
7. 订单完成

### 小程序司机闭环

1. 登录
2. 工作台
3. 接单
4. 到达
5. 开始代驾
6. 结束订单

### 平台支撑闭环

1. 消息中心
2. 头像/文件上传
3. 微信支付回调
4. 网关转发
5. Nacos 注册
6. Redis 会话

---

## 13. 最后一句话

你当前这套 HXDS 项目如果要上线，正确理解是：

- **不是“买台服务器”就上线**
- 而是“买台服务器后，把现在本地 Docker Desktop 里的开发依赖和所有必要微服务，迁成真正的生产运行环境”

只有这样，`api.zdkjdj.cn` 才能真正作为微信小程序正式版后端入口使用。
