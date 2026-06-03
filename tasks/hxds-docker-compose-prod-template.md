# HXDS 线上 docker-compose 初版模板说明

模板文件：

- [hxds-docker-compose-prod-template.yml](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/hxds-docker-compose-prod-template.yml)

这份模板是给你 **首期生产环境** 用的，不是最终高可用版。它只覆盖当前 HXDS 最小闭环必须的中间件：

- `MySQL`
- `Redis`
- `MongoDB`
- `RabbitMQ`
- `Nacos`
- `MinIO`

它**不包含**下面这些内容：

- Java 微服务本身
- `Nginx`
- `HBase/Phoenix`
- `hxds-nebula`
- 云数据库托管版配置

---

## 1. 为什么这样拆

你的当前项目最适合第一阶段：

1. **Docker 跑中间件**
2. **JAR 跑 Java 服务**
3. **Nginx 跑域名和 HTTPS**

原因是：

- 和你本地开发环境最接近
- 排错最容易
- 不用一开始就把全部微服务镜像化

---

## 2. 使用前必须改的地方

模板里这些值你必须先改：

- `CHANGE_ME_MYSQL_ROOT_PASSWORD`
- `CHANGE_ME_REDIS_PASSWORD`
- `CHANGE_ME_MONGO_PASSWORD`
- `CHANGE_ME_RABBITMQ_PASSWORD`
- `CHANGE_ME_NACOS_AUTH_TOKEN`
- `CHANGE_ME_NACOS_IDENTITY_KEY`
- `CHANGE_ME_NACOS_IDENTITY_VALUE`
- `CHANGE_ME_MINIO_PASSWORD`

否则不要上线。

---

## 3. 目录准备

Linux 服务器先创建这些目录：

```bash
sudo mkdir -p /opt/hxds/data/mysql
sudo mkdir -p /opt/hxds/data/redis
sudo mkdir -p /opt/hxds/data/mongo
sudo mkdir -p /opt/hxds/data/rabbitmq
sudo mkdir -p /opt/hxds/data/nacos/logs
sudo mkdir -p /opt/hxds/data/nacos/data
sudo mkdir -p /opt/hxds/data/minio
sudo mkdir -p /opt/hxds/init/mysql
sudo mkdir -p /opt/hxds/init/nacos
```

---

## 4. Nacos 特别说明

模板里 `Nacos` 用的是 **MySQL 外部库模式**，不是纯内置 Derby。

你要提前：

1. 在 MySQL 里创建 `nacos_config`
2. 导入对应版本的 Nacos MySQL 初始化 SQL
3. 再启动 Nacos

这也是为什么模板里挂了：

- `/opt/hxds/init/nacos/mysql-schema.sql`

如果你不用这个文件挂载，也至少要自己手动导库。

---

## 5. 如何启动

把模板复制到服务器，比如：

```bash
/opt/hxds/docker/docker-compose.yml
```

启动命令：

```bash
cd /opt/hxds/docker
docker compose up -d
```

查看状态：

```bash
docker compose ps
```

查看日志：

```bash
docker compose logs -f mysql
docker compose logs -f nacos
docker compose logs -f rabbitmq
```

---

## 6. 启动后你要验证什么

### MySQL

- `3306` 正常监听
- 可以登录
- 业务库可创建

### Redis

- 能用密码连接

### MongoDB

- `admin` 账号可登录

### RabbitMQ

- `15672` 控制台能打开
- 自定义账号可登录

### Nacos

- `8848` 控制台可打开
- 可以创建/导入配置
- Java 服务能注册成功

### MinIO

- `9001` 控制台可打开
- root 账号能登录

---

## 7. 这份模板之后怎么接到整套生产环境

顺序是：

1. 先起这份 compose
2. 导库
3. 导入 Nacos 配置
4. 再启动 Java 微服务
5. 最后起 Nginx 和域名

配套文件：

- [HXDS 生产环境部署清单](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/hxds-production-deployment-checklist.md)
- [HXDS 生产部署操作手册](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/hxds-production-operations-manual-tencent-cloud.md)
- [Nginx 生产配置模板](D:/daijia/hxds-cloud-master/hxds-cloud-master/tasks/nginx-gateway-wechat-domain-template.md)

---

## 8. 最后一句话

这份模板的定位是：

- **先把你本地 Docker Desktop 里的关键中间件，迁成 Linux 生产版**

不是：

- 一步到位高可用
- 一步到位全容器化

先用它把生产基础盘搭起来，后面再继续补：

- Java 微服务 `systemd`
- Nginx
- HTTPS
- `api.zdkjdj.cn`
- 微信小程序合法域名
