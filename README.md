# SY Torrent Feed

## 功能介绍

将指定目录下的种子文件，转换为种子RSS订阅链接供订阅器(例如 qBittorrent)使用。

## 原理

1. 扫描指定目录下的种子文件，根据种子的元数据生成 RSS Feed Item。生成之后将种子文件重命名为 `${hash}.torrent`。对应的 api 路径为 `/api/torrent/feed`。
2. RSS 客户端通过订阅链接获取 RSS Feed Item，然后根据 RSS Feed Item 中的下载地址下载种子文件。对应的 api 路径为 `/api/torrent/download`。

## 用法

### 部署

通过 docker compose 部署

```yaml
version: "3.8"
services:
  sy-torrent-feed:
    image: ghcr.io/sunriseydy/sy-torrent-feed:latest
    restart: unless-stopped
    ports:
      - 9009:9009
    volumes:
      - /tmp/torrent:/tmp/torrent
    environment:
      SY_LOG_LEVEL: debug
```

### 使用

RSS 订阅链接: `http://localhost:9009/api/torrent/feed`

### 配置

| 环境变量                 | 说明     | 默认值          |
|:---------------------|:-------|:-------------|
| SY_PORT              | 端口     | 9009         |
| SY_TORRENT_FEED_PATH | 种子文件目录 | /tmp/torrent |
| SY_LOG_LEVEL         | 日志级别   | INFO         |
