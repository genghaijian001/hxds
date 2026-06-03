# HXDS 生产部署操作手册（按 Windows 本地开发 -> 腾讯云 Linux 服务器迁移步骤版）

本文面向当前项目 `D:\daijia\hxds-cloud-master\hxds-cloud-master`，目标是把你现在在 **Windows 本地开发机 + Docker Desktop** 上运行的 HXDS 系统，迁移到 **腾讯云 Linux 服务器**，形成真正可供微信小程序正式版访问的生产环境。

配套文档：

- [HXDS 生产环境部署清单（按当前微服务项目逐项部署版）](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/hxds-production-deployment-checklist.md)
- [zdkjdj.cn 上线生产部署与域名配置实施清单](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/zdkjdj-production-deployment-checklist.md)
- [Nginx 生产配置模板 + 网关映射模板 + 微信小程序后台域名填写示例](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/nginx-gateway-wechat-domain-template.md)
- [单 AppID 正好代驾重构实施方案 v2（修正版）](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/single-appid-refactor-v2.md)

官方文档：

- 腾讯云轻量应用服务器 Linux 快速创建：[官方文档](https://cloud.tencent.com/document/product/1207/44548)
- 腾讯云轻量应用服务器操作指南：[官方文档](https://cloud.tencent.com/document/product/1207/44359)
- 腾讯云云解析 DNS：[官方文档](https://cloud.tencent.com/document/product/302)
- 腾讯云免费 SSL 证书申请：[官方文档](https://cloud.tencent.com/document/product/400/6814)
- 微信小程序服务器域名要求：[官方文档](https://developers.weixin.qq.com/miniprogram/dev/framework/ability/network.html)
- 微信小程序业务域名要求：[官方文档](https://developers.weixin.qq.com/miniprogram/dev/framework/ability/domain.html)
- 微信支付成功回调通知：[官方文档](https://pay.wechatpay.cn/doc/v3/merchant/4012791902)

---

## 1. 迁移目标

把当前本地运行形态：

- Windows 开发机
- Docker Desktop
- 本地 MySQL / Redis / MongoDB / RabbitMQ / Nacos / MinIO / HBase
- 本地 Java 21 JAR
- 本地 `127.0.0.1:8201` 网关

迁移成生产形态：

- 腾讯云 Linux 服务器
- Linux Docker Engine 或 Linux 原生服务
- 生产版数据库和中间件
- 生产版 Java 微服务
- `https://api.zdkjdj.cn`
- 微信小程序正式合法域名

---

## 2. 迁移前先定部署策略

对你这个项目，最现实的第一阶段策略是：

### 中间件
- 用 Linux Docker 部署

### Java 微服务
- 第一阶段先直接跑 JAR

### Nginx
- 直接部署在 Linux 上

这样做的好处：

1. 和你当前本地开发结构最接近
2. 比把所有 Java 服务都先改成容器更快
3. 出问题时更容易定位

也就是说，推荐第一阶段：

- Docker 跑：`MySQL / Redis / MongoDB / RabbitMQ / Nacos / MinIO`
- JAR 跑：`gateway / bff / hxds-*`

如果以后稳定了，再考虑把 Java 服务也全部容器化。

---

## 3. 当前本地环境和线上环境的关系

必须先理解这一点：

- 你本地 `Docker Desktop` 里的容器只是开发环境
- 线上腾讯云 Linux 服务器是全新的运行环境

这意味着：

1. 本地容器不会自动同步到线上
2. 本地数据库数据不会自动同步到线上
3. 本地 Nacos 配置不会自动同步到线上
4. 本地 `localhost`、`127.0.0.1` 配置上线后都要重新改

所以迁移不是“复制一个域名”这么简单，而是：

- 新建线上环境
- 部署依赖
- 部署服务
- 导入配置
- 导入数据
- 绑域名
- 联调

---

## 4. 第 0 步：确认你这次要迁哪些东西

建议先按 **首期最小生产闭环** 来迁，不要一开始把所有边角系统都搬上去。

### 建议首期必须迁移

#### Java 服务

- `gateway`
- `bff-customer`
- `bff-driver`
- `hxds-cst`
- `hxds-dr`
- `hxds-odr`
- `hxds-snm`
- `hxds-mps`
- `hxds-rule`
- `hxds-tm`

#### 中间件

- `MySQL`
- `Redis`
- `MongoDB`
- `RabbitMQ`
- `Nacos`
- `MinIO`

### 首期可暂缓

- `hxds-nebula`
- `HBase/Phoenix`
- `hxds-mis-api`
- MIS 前端

前提是假设你当前小程序闭环不强依赖这几项。

---

## 5. 第 1 步：在腾讯云购买 Linux 服务器

### 推荐

- 中国内地地域
- Ubuntu 22.04 LTS 或 CentOS Stream / Rocky Linux
- 至少 `4核8G`
- 更推荐 `4核16G`

### 控制台操作

1. 登录腾讯云控制台
2. 进入轻量应用服务器
3. 购买 Linux 实例
4. 设置登录方式：
   - 推荐 `SSH 密钥`
   - 或强密码
5. 记录：
   - 公网 IP
   - 登录账号
   - 密钥或密码

官方文档：

- [快速创建 Linux 实例](https://cloud.tencent.com/document/product/1207/44548)

---

## 6. 第 2 步：登录服务器并做基础初始化

### Windows 本地操作

推荐用：

- PowerShell + `ssh`
- 或 Xshell / MobaXterm

### 首次登录后要做的事

1. 更新系统软件包
2. 安装基础工具
3. 设置时区
4. 创建部署目录

### 参考命令（Ubuntu）

```bash
sudo apt update && sudo apt upgrade -y
sudo apt install -y curl wget git unzip zip vim net-tools lsof nginx
sudo timedatectl set-timezone Asia/Shanghai
sudo mkdir -p /data/{deploy,logs,backup}
sudo mkdir -p /opt/hxds
```

如果你最后用新加坡时区或别的地区，也可以改时区，但中国业务一般建议用 `Asia/Shanghai`。

---

## 7. 第 3 步：安装 Docker Engine 和 Docker Compose

因为你本地很多依赖已经在 Docker Desktop 里跑，所以线上最省事的是继续用 Docker 跑这些中间件。

### 官方建议

腾讯云不要求你必须用 Docker，但对你这个项目这是最现实的迁移方式。

### 你要做的

1. 安装 Docker Engine
2. 安装 Docker Compose 插件
3. 开机启动 Docker

### 迁移原则

本地这些服务，线上建议继续容器化：

- `MySQL`
- `Redis`
- `MongoDB`
- `RabbitMQ`
- `Nacos`
- `MinIO`

### 注意

不要把 `Docker Desktop` 配置文件原封不动拿到 Linux。

因为：

- Docker Desktop 是 Windows 桌面开发环境
- 线上 Linux 需要自己的 `docker-compose.yml`

---

## 8. 第 4 步：在 Linux 上准备中间件目录

建议：

```bash
sudo mkdir -p /opt/hxds/docker
sudo mkdir -p /opt/hxds/data/mysql
sudo mkdir -p /opt/hxds/data/redis
sudo mkdir -p /opt/hxds/data/mongo
sudo mkdir -p /opt/hxds/data/rabbitmq
sudo mkdir -p /opt/hxds/data/nacos
sudo mkdir -p /opt/hxds/data/minio
sudo mkdir -p /opt/hxds/config
```

这样后续容器重启不会丢数据。

---

## 9. 第 5 步：部署中间件

你需要在 Linux 服务器上新建一个生产版 `docker-compose.yml`。

### 第一阶段建议部署的容器

1. `mysql`
2. `redis`
3. `mongodb`
4. `rabbitmq`
5. `nacos`
6. `minio`

### 为什么先不上 HBase

因为首期可以先不启 `hxds-nebula`，这样部署难度会低很多。

### 关键点

1. 密码全部改成生产密码
2. 数据目录全部挂载到宿主机
3. 端口只开放必要项
4. 内部优先走 Docker bridge 或宿主机内网

---

## 10. 第 6 步：初始化数据库

线上数据库不是自动从你本地长出来的，你要自己初始化。

### 你要做的

1. 登录线上 MySQL
2. 创建这些库：
   - `hxds_dr`
   - `hxds_cst`
   - `hxds_odr`
   - `hxds_rule`
   - `hxds_mis`
   - `tx-manager`
3. 按实际需要执行 SQL 初始化脚本

### 数据来源

优先顺序建议：

1. 如果你已有正式测试数据，要导出本地数据再导入线上
2. 如果没有，就先导入结构和基础规则数据

### 注意

`hxds-rule` 的规则表、工作流配置、价格规则不能漏，不然订单链路会不完整。

---

## 11. 第 7 步：导出并迁移 Nacos 配置

线上 Nacos 不能只装服务不导配置。

### 你要迁移的东西

1. 命名空间
2. Data ID
3. Group
4. 所有服务对应的 `application.yml` / `application-*.yml`

### 关键点

把本地开发配置中的：

- `localhost`
- `127.0.0.1`
- 本地密码
- 本地端口

改成生产环境地址。

比如：

- MySQL 改成线上 MySQL 地址
- Redis 改成线上 Redis 地址
- MongoDB 改成线上 Mongo 地址
- RabbitMQ 改成线上 RabbitMQ 地址
- MinIO/COS 改成线上对象存储配置
- Nacos 地址改成线上 Nacos

---

## 12. 第 8 步：在 Windows 本地打包 Java 服务

你的项目当前是 Maven 多模块。

### 本地打包建议

在 Windows 本地打包，然后把产物上传到 Linux。

### 命令

```bash
cd D:\daijia\hxds-cloud-master\hxds-cloud-master\hxds
mvn clean package -DskipTests
```

### 重点

项目要求 Java 21。

如果本地系统默认 Java 不是 21，要显式用项目要求的 Java 环境。

---

## 13. 第 9 步：把构建产物上传到 Linux

### 你需要上传的内容

1. 各服务 JAR
2. 前端编译产物（如果要部署后台前端/H5）
3. Nginx 配置文件
4. Docker Compose 文件
5. SQL 或初始化脚本

### Windows 到 Linux 传输方式

可选：

- `scp`
- WinSCP
- MobaXterm SFTP

### 建议目录

```bash
/opt/hxds/apps/
/opt/hxds/config/
/opt/hxds/nginx/
```

例如：

```bash
/opt/hxds/apps/gateway/
/opt/hxds/apps/bff-customer/
/opt/hxds/apps/bff-driver/
...
```

---

## 14. 第 10 步：在 Linux 上部署 Java 微服务

### 第一阶段推荐方式

直接 `java -jar`

### 原因

1. 最贴近你当前项目
2. 容错高
3. 调试方便

### 运行前确认

1. Linux 上安装 Java 21
2. 配置好环境变量或直接写绝对路径

### 建议目录

```bash
/opt/hxds/apps/gateway/gateway.jar
/opt/hxds/apps/bff-customer/bff-customer.jar
/opt/hxds/apps/bff-driver/bff-driver.jar
...
```

### 建议日志目录

```bash
/data/logs/hxds/gateway/
/data/logs/hxds/bff-customer/
/data/logs/hxds/bff-driver/
...
```

### 运行建议

每个服务后续都用 `systemd` 托管，不要长期手工开着窗口跑。

---

## 15. 第 11 步：把服务做成 systemd

生产上不要用手敲 `java -jar` 长期开服务。

### 建议

为每个服务建立一个 `systemd` unit，例如：

- `hxds-gateway.service`
- `hxds-bff-customer.service`
- `hxds-bff-driver.service`
- `hxds-cst.service`

### 这样做的好处

1. 开机自启
2. 崩了能自动重启
3. 日志更稳定
4. 更适合生产运维

---

## 16. 第 12 步：按顺序启动服务

推荐顺序：

### 先启动依赖

1. MySQL
2. Redis
3. MongoDB
4. RabbitMQ
5. Nacos
6. MinIO
7. TX-LCN Manager

### 再启动核心服务

1. `hxds-dr`
2. `hxds-cst`
3. `hxds-rule`
4. `hxds-mps`
5. `hxds-snm`

### 再启动订单服务

1. `hxds-odr`

### 再启动 BFF

1. `bff-driver`
2. `bff-customer`

### 最后启动网关

1. `gateway`

---

## 17. 第 13 步：检查服务是否成功注册到 Nacos

这一步非常关键。

### 检查点

登录线上 Nacos 控制台，确认这些服务都注册成功：

- `gateway`
- `bff-customer`
- `bff-driver`
- `hxds-cst`
- `hxds-dr`
- `hxds-odr`
- `hxds-snm`
- `hxds-mps`
- `hxds-rule`
- `hxds-tm`

### 如果没注册上

优先排查：

1. Nacos 地址配置
2. 端口是否被占用
3. 服务配置是否读取成功
4. Java 版本是否正确

---

## 18. 第 14 步：部署 Nginx

Nginx 负责：

1. 接域名
2. 提供 HTTPS
3. 把外部请求转到 `gateway:8201`

### 生产访问路径建议

- `https://api.zdkjdj.cn/hxds-customer/**`
- `https://api.zdkjdj.cn/hxds-driver/**`

### 配置模板

直接参考：

- [nginx-gateway-wechat-domain-template.md](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/nginx-gateway-wechat-domain-template.md)

---

## 19. 第 15 步：配置域名和证书

### 你要做的

1. 在 DNSPod / 腾讯云解析里新增：
   - `api.zdkjdj.cn`
   - `admin.zdkjdj.cn`
   - `h5.zdkjdj.cn`
2. 给 `api.zdkjdj.cn` 申请 SSL 证书
3. 把证书部署到 Nginx

### 注意

小程序正式版不能请求：

- `http`
- `IP`
- `localhost`
- `127.0.0.1`

必须走：

- `https://api.zdkjdj.cn`

官方文档：

- [服务器域名要求](https://developers.weixin.qq.com/miniprogram/dev/framework/ability/network.html)

---

## 20. 第 16 步：配置微信小程序后台合法域名

进入微信小程序后台，配置：

### 服务器域名

- `request 合法域名`：`https://api.zdkjdj.cn`
- `uploadFile 合法域名`：`https://api.zdkjdj.cn`
- `downloadFile 合法域名`：`https://api.zdkjdj.cn`
- 如使用 WebSocket：`wss://api.zdkjdj.cn`

### 业务域名

如果要用 `web-view` 打开协议页/H5：

- `https://h5.zdkjdj.cn`

---

## 21. 第 17 步：配置微信支付回调

### 推荐回调地址

```text
https://api.zdkjdj.cn/pay/notify/wechat/miniprogram
```

### 你要做的

1. 确保这个公网地址能被微信访问
2. 后端能正确验签
3. 订单状态和账单状态能更新

官方文档：

- [支付成功回调通知](https://pay.wechatpay.cn/doc/v3/merchant/4012791902)

---

## 22. 第 18 步：把小程序前端里的本地地址切换成生产地址

你现在本地开发时，很多地方可能是：

- `http://127.0.0.1:8201`

上线前必须切到：

- `https://api.zdkjdj.cn`

### 重点检查

1. 乘客端请求基地址
2. 司机端请求基地址
3. 上传接口地址
4. 支付接口地址

否则你就会出现：

- 真机请求不了
- 开发版能跑，正式版不能跑

---

## 23. 第 19 步：公网联调

### 先做接口联调

1. `https://api.zdkjdj.cn/swagger-ui.html` 或服务文档页面是否能访问
2. 网关是否能正确转发
3. BFF 是否能调下游服务

### 再做小程序联调

#### 乘客端

1. 登录
2. 个人资料
3. 地图起终点
4. 创建订单
5. 查询当前订单
6. 支付

#### 司机端

1. 登录
2. 听单/工作台
3. 接单
4. 到达
5. 开始代驾
6. 结束订单

#### 平台功能

1. 头像上传
2. 消息中心
3. 微信支付回调

---

## 24. 第 20 步：上线前必须做的数据和安全动作

### 数据层

1. 备份数据库
2. 备份 Nacos 配置
3. 备份 MinIO 数据

### 安全层

1. 修改所有默认密码
2. 限制数据库和中间件公网暴露
3. 防火墙只放必要端口
4. 尽量只公开 `80/443`

### 运维层

1. 日志落盘
2. 自动重启
3. 磁盘监控
4. 进程监控

---

## 25. 你本地 Docker Desktop 里的东西，到线上怎么对应

### 本地开发

- Docker Desktop
- Windows 文件路径
- 本地端口映射
- 本地数据库

### 线上生产

- Linux Docker Engine
- Linux 数据目录
- 线上私有网络/本机回环
- 线上数据库和中间件

对应关系是：

| 本地 | 线上 |
|---|---|
| Docker Desktop | Linux Docker Engine |
| `localhost:3306` | 线上 MySQL 地址 |
| `localhost:6379` | 线上 Redis 地址 |
| `localhost:27017` | 线上 MongoDB 地址 |
| `localhost:5672` | 线上 RabbitMQ 地址 |
| `127.0.0.1:8848` | 线上 Nacos 地址 |
| `127.0.0.1:8201` | `https://api.zdkjdj.cn` |

所以答案非常明确：

**是的，你现在 Docker Desktop 里的那些依赖，线上都要有对应部署。**

---

## 26. 第一阶段最推荐的落地打法

### 推荐形态

#### Linux Docker 跑中间件

- MySQL
- Redis
- MongoDB
- RabbitMQ
- Nacos
- MinIO

#### JAR 跑微服务

- Gateway
- BFF
- `hxds-*`

#### Nginx 跑公网入口

- `api.zdkjdj.cn`

### 为什么推荐这样

1. 最接近你当前项目
2. 修改最少
3. 成本最低
4. 出问题最好修

---

## 27. 最后一段话

把 HXDS 从你现在的 Windows 本地环境迁到腾讯云 Linux，正确步骤不是：

- 买服务器
- 绑域名

而是：

1. 买服务器
2. 装 Linux 运行环境
3. 部署 Docker 中间件
4. 初始化数据库和配置中心
5. 打包并上传 Java 服务
6. 启动并注册到 Nacos
7. 配 Nginx + HTTPS + 域名
8. 配微信小程序合法域名
9. 配微信支付回调
10. 做整链路联调

只有这样，你的小程序后端才算真的“上线”。
