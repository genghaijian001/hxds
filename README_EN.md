# HXDS Cloud

HXDS Cloud is a full-stack designated-driver platform that includes:

- a customer WeChat mini-program `hxds-customer-wx`
- a driver WeChat mini-program `hxds-driver-wx`
- a Vue 3 admin portal `hxds-mis-vue`
- a Java microservice backend under `hxds/`
- deployment helpers, cloud functions, and operational documents

The current repository reflects the latest refactored structure based on **Java 21 + Spring Boot 3.3 + Spring Cloud Alibaba 2023**, with complete customer, driver, order, map, payment, notification, rule-engine, and admin workflows.

## Repository Layout

```text
hxds-cloud-master
|-- hxds-customer-wx              # Customer mini-program (UniApp)
|-- hxds-driver-wx                # Driver mini-program (UniApp)
|-- hxds-mis-vue                  # Admin frontend (Vue 3 + Vite)
|-- hxds/                         # Java backend microservices
|   |-- common
|   |-- gateway
|   |-- bff-customer
|   |-- bff-driver
|   |-- hxds-cst
|   |-- hxds-dr
|   |-- hxds-odr
|   |-- hxds-snm
|   |-- hxds-mps
|   |-- hxds-rule
|   |-- hxds-nebula
|   `-- hxds-mis-api
|-- db/                           # Database initialization and business rule SQL files
|-- docker/                       # Local/test middleware stack and HBase/Phoenix support
|-- cloudfunctions/               # Cloud functions (currently OCR service)
|-- images/                       # README screenshots
|-- wx-miniprogram-docs/          # Mini-program capability notes and official API summaries
`-- tasks/                        # Refactor, deployment, and operational planning docs
```

## Core Capabilities

- customer order creation, ride-hailing, payment, and review
- driver dispatch, order acceptance, arrival confirmation, trip execution, and completion
- BFF aggregation, authentication, and downstream orchestration
- route planning, nearby-driver matching, and location caching
- WeChat Pay, SMS, OCR, facial verification, object storage, and messaging integrations
- rule-engine driven pricing, cancellation fees, rewards, and settlement logic
- admin management for drivers, orders, comments, vouchers, and permissions

## Tech Stack

### Backend

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
- MinIO / Tencent COS
- The current latest version does not rely on TX-LCN, and distributed transaction middleware is not part of the active runtime chain.

### Frontend

- UniApp
- uView UI
- Vue 3
- Vite

### Cloud / WeChat integrations

- WeChat Pay
- Tencent Map
- WeChat speech translation plugin
- OCR and ID-document recognition integrations
- OCR cloud function wrapper

## Service Architecture

```text
Mini-programs / MIS frontend
          ->
        Gateway
          ->
 bff-customer / bff-driver / hxds-mis-api
          ->
 cst / dr / odr / snm / mps / rule / nebula
          ->
MySQL / Redis / MongoDB / RabbitMQ / Nacos / MinIO
```

## Service Overview

| Service | Responsibility |
|---|---|
| `gateway` | unified API entry |
| `bff-customer` | customer-facing mini-program aggregation |
| `bff-driver` | driver-facing mini-program aggregation |
| `hxds-cst` | customer accounts, profile, vouchers |
| `hxds-dr` | driver profile, verification, wallet, OCR-related flows |
| `hxds-odr` | order state machine, billing, payment status |
| `hxds-snm` | notifications, messaging center, queue consumption |
| `hxds-mps` | route calculation, driver matching, location cache |
| `hxds-rule` | business rule engine |
| `hxds-nebula` | GPS, monitoring, and analytics |
| `hxds-mis-api` | admin backend APIs |

## Runtime Dependencies

The current project expects the following infrastructure:

- MySQL
- Redis
- MongoDB
- RabbitMQ
- Nacos
- MinIO
- HBase / Phoenix (only required for `hxds-nebula`)

See also:

- [docker/docker-compose.yml](docker/docker-compose.yml)
- [Production deployment checklist](tasks/hxds-production-deployment-checklist.md)
- [Windows-to-Tencent-Cloud Linux operations manual](tasks/hxds-production-operations-manual-tencent-cloud.md)

## Sensitive Configuration

The GitHub version keeps all cloud, payment, SMS, and storage secrets as environment-variable placeholders only.
To run locally, provide your real values through local environment variables or an untracked local override strategy instead of committing them into the repository.

## Local Development

### Backend

```bash
cd hxds
mvn clean package -DskipTests
```

Run a single service:

```bash
cd hxds/hxds-odr
mvn clean package -DskipTests
java -jar target/hxds-odr-0.0.1-SNAPSHOT.jar
```

### Admin Portal

```bash
cd hxds-mis-vue
npm install
npm run dev
```

### Mini Programs

```bash
cd hxds-customer-wx
npm install

cd ../hxds-driver-wx
npm install
```

Then compile the `unpackage/dist/dev/mp-weixin/` output with HBuilderX / WeChat DevTools.

## Database Scripts

The repository now uses per-database SQL files:

- `db/hxds_cst.sql`
- `db/hxds_dr.sql`
- `db/hxds_odr.sql`
- `db/hxds_rule.sql`
- `db/hxds_mis.sql`
- `db/hxds_vhr.sql`

Business rule SQL files:

- `db/代驾费规则.sql`
- `db/分账规则.sql`
- `db/取消规则.sql`
- `db/奖励规则.sql`
- `db/工作流.sql`

## Operational Documents

The repository now includes updated Chinese deployment and refactor documents:

- [Single-AppID refactor plan v2](tasks/single-appid-refactor-v2.md)
- [Production deployment checklist](tasks/hxds-production-deployment-checklist.md)
- [Windows-to-Tencent-Cloud Linux operations manual](tasks/hxds-production-operations-manual-tencent-cloud.md)
- [Domain and go-live checklist](tasks/zdkjdj-production-deployment-checklist.md)
- [Nginx / gateway / mini-program domain templates](tasks/nginx-gateway-wechat-domain-template.md)
- [Production docker-compose template](tasks/hxds-docker-compose-prod-template.md)

## Screenshots

### Login and registration
![Login and registration](images/19b42ea4ce97ae85bed914efd42c06cd.png)

### Workbench
![Workbench](images/a3f5e7c370d7e4fbe78241e0bace415d.png)

### Driver verification
![Driver verification](images/c69fbd4bb2a27c9d007ea11a7ab4bbc9.png)

### Customer order creation
![Customer order creation](images/88662dec6d60331055efc705ff834e19.png)

### Driver order pickup
![Driver order pickup](images/b64cd58bef9c5eea8dd5bd17f59b7674.png)

### Order dispatch and state transitions
![Order dispatch](images/6b7e7f9edf79a90541578950e06cc570.png)
![Order state transitions](images/c90024c1735b2ff6d76e5669a353df3a.png)

### Arrival, live trip view, completion, and payment
![Arrival confirmation](images/97ff29bd44341881942d68cf496f6ece.png)
![Live trip view](images/0ba6552a69155f0ad8b2378dcfde7b10.png)
![Trip completion](images/6f32414dd31722411474284439bb8e3d.png)
![Bill push](images/dc67a6a7bb2f54030c896b98ea083448.png)
![Payment and review](images/8b522364a38af29202b9158a3b62ddc0.png)

## License

This repository keeps the original license file:

- [LICENSE](LICENSE)
