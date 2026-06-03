# HXDS Cloud

HXDS Cloud 是一个完整的代驾平台项目，当前仓库包含：

- 乘客端微信小程序：`hxds-customer-wx`
- 司机端微信小程序：`hxds-driver-wx`
- MIS 管理后台前端：`hxds-mis-vue`
- Java 微服务后端：`hxds/`
- 云函数、数据库脚本、Docker 中间件编排

当前仓库内容已经基于你现在这版代码完成升级整理，README 以下内容以**当前项目源码**为准。

## 当前版本概览

### 后端主版本

- Java：`21`
- Maven 编译插件：`3.15.0`
- Spring Boot：`3.3.13`
- Spring Cloud：`2023.0.3`
- Spring Cloud Alibaba：`2023.0.3.3`
- Lombok：`1.18.30`
- Hutool：`5.6.3`

### 管理后台前端版本

根据 `hxds-mis-vue/package.json`：

- Vue：`3.0.3`
- Vite：`2.1.5`
- Vue Router：`4.0.5`
- Element Plus：`1.0.2-beta.42`
- ECharts：`5.1.1`
- Less：`4.1.1`
- Sass：`1.32.8`

### 小程序前端依赖

根据 `hxds-customer-wx/package.json` 与 `hxds-driver-wx/package.json`：

- UniApp 项目结构
- uView：`1.0.0`
- dayjs：`^1.10.7`
- vue-i18n：`^8.20.0`

### 分布式事务

当前项目按代码配置使用 **Seata**。

从当前代码配置可见，以下服务已存在 Seata 配置：

- `bff-customer`
- `bff-driver`
- `hxds-cst`
- `hxds-dr`
- `hxds-odr`
- `hxds-rule`
- `hxds-mis-api`

## 当前后端微服务

根据 `hxds/pom.xml` 当前模块定义，后端实际模块如下：

- `common`
- `gateway`
- `bff-customer`
- `bff-driver`
- `hxds-cst`
- `hxds-dr`
- `hxds-odr`
- `hxds-snm`
- `hxds-mps`
- `hxds-rule`
- `hxds-nebula`
- `hxds-mis-api`

## 项目结构

```text
hxds-cloud-master
|-- hxds-customer-wx              # 乘客端微信小程序（UniApp）
|-- hxds-driver-wx                # 司机端微信小程序（UniApp）
|-- hxds-mis-vue                  # MIS 管理后台前端（Vue 3 + Vite）
|-- hxds/                         # Java 微服务后端
|   |-- common                    # 公共模块与通用配置
|   |-- gateway                   # API 网关
|   |-- bff-customer              # 乘客端 BFF
|   |-- bff-driver                # 司机端 BFF
|   |-- hxds-cst                  # 乘客服务
|   |-- hxds-dr                   # 司机服务
|   |-- hxds-odr                  # 订单服务
|   |-- hxds-snm                  # 消息通知服务
|   |-- hxds-mps                  # 地图与位置服务
|   |-- hxds-rule                 # 规则引擎服务
|   |-- hxds-nebula               # GPS / 监控 / 分析服务
|   `-- hxds-mis-api              # MIS 后台接口服务
|-- db/                           # 当前数据库初始化脚本与规则脚本
|-- docker/                       # 本地/测试环境中间件与容器编排
|-- cloudfunctions/               # 云函数（当前含 OCR 服务）
`-- images/                       # README 截图
```

## 当前业务能力

- 乘客端下单、叫代驾、支付、评价
- 司机端听单、接单、到达、开始代驾、结束订单
- BFF 聚合接口、统一鉴权、上下游编排
- 地图路线计算、附近司机匹配、位置缓存
- 微信支付、短信、OCR、对象存储
- 规则引擎驱动计费、取消费、奖励、分账
- MIS 管理后台支持司机、订单、评论、券、权限等运营管理

## 当前服务链路

```text
微信小程序 / MIS 前端
        ->
      Gateway
        ->
  bff-customer / bff-driver / hxds-mis-api
        ->
  cst / dr / odr / snm / mps / rule / nebula
        ->
MySQL / Redis / MongoDB / RabbitMQ / Nacos / MinIO / Seata
```

## 服务说明

| 服务 | 说明 |
|---|---|
| `gateway` | 统一 API 网关入口 |
| `bff-customer` | 乘客端小程序聚合接口 |
| `bff-driver` | 司机端小程序聚合接口 |
| `hxds-cst` | 乘客账号、资料、券等能力 |
| `hxds-dr` | 司机资料、实名、钱包、OCR 等能力 |
| `hxds-odr` | 订单状态机、账单、支付状态流转 |
| `hxds-snm` | 消息通知、消息中心、MQ 消费 |
| `hxds-mps` | 路线计算、司机匹配、位置缓存 |
| `hxds-rule` | 规则引擎与业务规则数据 |
| `hxds-nebula` | GPS 轨迹、监控数据、分析能力 |
| `hxds-mis-api` | 管理后台接口层 |

## 基础依赖

当前项目默认依赖以下基础设施：

- MySQL
- Redis
- MongoDB
- RabbitMQ
- Nacos
- MinIO
- Seata
- HBase / Phoenix（仅 `hxds-nebula` 相关场景需要）

参考：

- [docker/docker-compose.yml](docker/docker-compose.yml)

## 本地开发

### 后端

```bash
cd hxds
mvn clean package -DskipTests
```

单服务运行示例：

```bash
cd hxds/hxds-odr
mvn clean package -DskipTests
java -jar target/hxds-odr-0.0.1-SNAPSHOT.jar
```

### MIS 管理后台

```bash
cd hxds-mis-vue
npm install
npm run dev
```

### 小程序

```bash
cd hxds-customer-wx
npm install

cd ../hxds-driver-wx
npm install
```

然后使用 HBuilderX 或微信开发者工具编译 `unpackage/dist/dev/mp-weixin/`。

## 数据库脚本

当前仓库使用拆分后的业务库 SQL：

- `db/hxds_cst.sql`
- `db/hxds_dr.sql`
- `db/hxds_odr.sql`
- `db/hxds_rule.sql`
- `db/hxds_mis.sql`
- `db/hxds_vhr.sql`

规则脚本：

- `db/代驾费规则.sql`
- `db/分账规则.sql`
- `db/取消规则.sql`
- `db/奖励规则.sql`
- `db/工作流.sql`

## 敏感配置说明

GitHub 仓库版本应仅保留云服务、支付、短信、对象存储等配置项的环境变量占位符，不提交真实密钥。

你的**本地可启动版本**应继续保留本机真实配置，或通过本机环境变量、未纳入 Git 管理的本地覆盖配置注入真实值。

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

### 订单状态流转
![订单抢单](images/6b7e7f9edf79a90541578950e06cc570.png)
![订单状态流转](images/c90024c1735b2ff6d76e5669a353df3a.png)

### 到达、同显、结束、支付
![到达确认](images/97ff29bd44341881942d68cf496f6ece.png)
![同显页面](images/0ba6552a69155f0ad8b2378dcfde7b10.png)
![结束代驾](images/6f32414dd31722411474284439bb8e3d.png)
![账单推送](images/dc67a6a7bb2f54030c896b98ea083448.png)
![支付与评价](images/8b522364a38af29202b9158a3b62ddc0.png)

## License

- [LICENSE](LICENSE)
