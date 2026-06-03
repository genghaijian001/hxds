# Nginx 生产配置模板 + 网关映射模板 + 微信小程序后台域名填写示例

## 1. 使用说明

本文件提供：

- `api.zdkjdj.cn` 的 Nginx HTTPS 反向代理模板
- 网关入口映射建议
- 微信小程序后台合法域名填写示例

适用前提：

- 域名已解析到公网
- ICP 备案已完成或正在完成
- SSL 证书已签发
- 后端网关可在服务器本机通过 `127.0.0.1:8201` 访问

## 2. 推荐目录

建议：

- 证书目录：`/etc/nginx/ssl/`
- Nginx 配置目录：`/etc/nginx/conf.d/`

## 3. `api.zdkjdj.cn` 的 Nginx 配置模板

```nginx
server {
    listen 80;
    server_name api.zdkjdj.cn;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl http2;
    server_name api.zdkjdj.cn;

    ssl_certificate     /etc/nginx/ssl/api.zdkjdj.cn.crt;
    ssl_certificate_key /etc/nginx/ssl/api.zdkjdj.cn.key;

    ssl_session_timeout 10m;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    client_max_body_size 50m;

    proxy_connect_timeout 60s;
    proxy_send_timeout 300s;
    proxy_read_timeout 300s;

    location / {
        proxy_pass http://127.0.0.1:8201;
        proxy_http_version 1.1;

        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto https;
        proxy_set_header X-Forwarded-Host $host;
    }
}
```

## 4. `admin.zdkjdj.cn` 的 Nginx 配置模板

如果后台前端和后台 API 分开：

```nginx
server {
    listen 80;
    server_name admin.zdkjdj.cn;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl http2;
    server_name admin.zdkjdj.cn;

    ssl_certificate     /etc/nginx/ssl/admin.zdkjdj.cn.crt;
    ssl_certificate_key /etc/nginx/ssl/admin.zdkjdj.cn.key;

    root /data/www/hxds-mis-vue/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /hxds-mis-api/ {
        proxy_pass http://127.0.0.1:8010/;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto https;
    }
}
```

## 5. `h5.zdkjdj.cn` 的 Nginx 配置模板

用于协议页、活动页、web-view 页：

```nginx
server {
    listen 80;
    server_name h5.zdkjdj.cn;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl http2;
    server_name h5.zdkjdj.cn;

    ssl_certificate     /etc/nginx/ssl/h5.zdkjdj.cn.crt;
    ssl_certificate_key /etc/nginx/ssl/h5.zdkjdj.cn.key;

    root /data/www/hxds-h5;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

## 6. 网关映射模板

推荐对外统一只暴露：

- `https://api.zdkjdj.cn`

网关内部路由建议保持：

- `/hxds-customer/**` -> `bff-customer`
- `/hxds-driver/**` -> `bff-driver`
- `/hxds-app/**` -> `bff-app`
- `/hxds-mis-api/**` -> `hxds-mis-api`

如果你后续新增统一 App 小程序接口，建议集中挂到：

- `/hxds-app/auth/login`
- `/hxds-app/auth/profile`
- `/hxds-app/auth/switchRole`

## 7. 微信支付回调 URL 模板

建议：

```text
https://api.zdkjdj.cn/pay/notify/wechat/miniprogram
```

注意：

- 不要带 query 参数
- 必须公网可访问
- 必须是 HTTPS
- 后端要做验签和幂等

## 8. 微信小程序后台域名填写示例

## 8.1 服务器域名

小程序后台路径：

- `开发管理 -> 开发设置 -> 服务器域名`

填写建议：

- `request 合法域名`
  - `https://api.zdkjdj.cn`
- `uploadFile 合法域名`
  - `https://api.zdkjdj.cn`
- `downloadFile 合法域名`
  - `https://api.zdkjdj.cn`
- `socket 合法域名`
  - `wss://api.zdkjdj.cn`
  - 如果当前不用 WebSocket，可以先不填

## 8.2 业务域名

如果使用 `web-view`，小程序后台路径：

- `开发管理 -> 开发设置 -> 业务域名`

填写建议：

- `https://h5.zdkjdj.cn`

## 9. Nginx 启用步骤示例

```bash
sudo nginx -t
sudo systemctl reload nginx
sudo systemctl status nginx
```

## 10. 本地自检命令示例

```bash
curl -I https://api.zdkjdj.cn
curl -I https://admin.zdkjdj.cn
curl -I https://h5.zdkjdj.cn
curl -I https://api.zdkjdj.cn/hxds-customer/customer/login
```

## 11. 常见问题提醒

1. 如果浏览器能访问，微信小程序请求仍失败：
- 优先检查小程序后台是否正确配置合法域名

2. 如果开发者工具能请求、真机不能请求：
- 优先检查是否只是开发环境跳过了域名校验

3. 如果支付成功但订单没更新：
- 优先检查支付回调 URL 是否公网可达
- 再检查回调验签和幂等逻辑

4. 如果 `api.zdkjdj.cn` 填了端口：
- 微信小程序会严格按该端口匹配
- 生产建议统一使用标准 `443`
