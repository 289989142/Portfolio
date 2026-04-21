# Portfolio

一个基于 **Spring Boot 3.x + MyBatis-Plus + MySQL** 的作品集后端，配套 **Vue 3 + Vite + Element Plus + Tailwind CSS** 前端。

## 目录结构

- `src/main/java`：Spring Boot 后端
- `src/main/resources`：后端配置和建表脚本
- `frontend/`：Vue 3 前端工程

## 环境要求

- JDK 17
- Maven 3.8+
- Node.js 18+
- MySQL 8+

## 1. 初始化数据库

先创建数据库：

```sql
CREATE DATABASE portfolio DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

然后确认后端配置文件中的数据库连接正确：

- [`src/main/resources/application.yml`](/Users/mac/dev/my/portfolio/Portfolio/src/main/resources/application.yml)

默认数据库用户名密码通过环境变量读取：

```bash
export DB_USERNAME=portfolio
export DB_PASSWORD=Portfolio@123456
```

表结构脚本在：

- [`src/main/resources/schema.sql`](/Users/mac/dev/my/portfolio/Portfolio/src/main/resources/schema.sql)

后端启动时会自动执行 `schema.sql`，前提是数据库账号有建表权限。

## 2. 启动后端

先切换到 JDK 17：

```bash
export JAVA_HOME=/Users/mac/Library/Java/JavaVirtualMachines/ms-17.0.17/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
```

然后启动：

```bash
mvn spring-boot:run
```

后端默认地址：

- `http://localhost:8080`

后端主入口：

- [`PortfolioApplication.java`](/Users/mac/dev/my/portfolio/Portfolio/src/main/java/com/example/portfolio/PortfolioApplication.java)

## 3. 启动前端

进入前端目录：

```bash
cd frontend
npm install
npm run dev
```

前端默认地址：

- `http://localhost:5173`

前端配置文件：

- [`frontend/vite.config.js`](/Users/mac/dev/my/portfolio/Portfolio/frontend/vite.config.js)

## 4. 用户展示页

访问：

- `/`

功能包括：

- 左侧个人简介、电话、微信二维码、简历按钮
- 右侧作品集分类卡片
- 全屏背景图
- 悬浮音乐播放器
- 分类详情页查看作品缩略图
- 点击图片弹出放大详情

## 5. 管理后台

### 登录页

访问：

- `/admin/login`

输入内容：

- `site_settings.admin_password`

### 管理主页

访问：

- `/admin`

包含三个标签页：

1. 基础设置
2. 作品集管理
3. 作品管理

### 上传接口

后台上传统一走：

- `POST /api/admin/upload`

前端上传组件：

- [`frontend/src/components/admin/FileUploadField.vue`](/Users/mac/dev/my/portfolio/Portfolio/frontend/src/components/admin/FileUploadField.vue)

上传成功后返回可直接访问的相对路径，例如：

- `/uploads/2026/04/20/xxxxxxxx.png`

默认上传目录是项目根目录下的 `./uploads/`，会在运行时自动创建。

## 6. 静态资源访问

本地文件会映射到：

- `/uploads/**`

对应的本地目录来自：

- `app.upload-path`

配置位置：

- [`src/main/resources/application.yml`](/Users/mac/dev/my/portfolio/Portfolio/src/main/resources/application.yml)

## 7. 常见测试步骤

1. 启动 MySQL
2. 创建 `portfolio` 数据库
3. 启动后端 `mvn spring-boot:run`
4. 启动前端 `npm run dev`
5. 打开 `/` 检查用户展示页
6. 打开 `/admin/login` 测试后台登录
7. 登录后测试设置、分类、作品和上传

## 8. 常见问题

### 1. 项目编译失败，提示 Java 版本不对

确认当前终端使用的是 JDK 17，而不是 1.8。

### 2. 上传后图片无法访问

检查：

- `app.upload-path` 是否存在且可写。默认是项目根目录下的 `./uploads/`
- `frontend` 或浏览器访问的地址是否和后端 `/uploads/**` 映射一致

### 3. 前端接口请求失败

确认后端已经启动，并且 Vite 代理配置正常：

- [`frontend/vite.config.js`](/Users/mac/dev/my/portfolio/Portfolio/frontend/vite.config.js)

## 9. Docker 部署

这个项目已经补好了 Docker 部署文件：

- [`Dockerfile`](/Users/mac/dev/my/portfolio/Portfolio/Dockerfile)
- [`frontend/Dockerfile`](/Users/mac/dev/my/portfolio/Portfolio/frontend/Dockerfile)
- [`frontend/nginx/default.conf`](/Users/mac/dev/my/portfolio/Portfolio/frontend/nginx/default.conf)
- [`docker-compose.yml`](/Users/mac/dev/my/portfolio/Portfolio/docker-compose.yml)

### 本地一键启动

前提是你的机器已经安装 Docker 和 Docker Compose。

在项目根目录执行：

```bash
docker compose up -d --build
```

启动后访问：

- 用户端：`http://localhost/`
- 管理后台：`http://localhost/admin/login`

默认会启动三个容器：

- `mysql`：MySQL 8.4
- `backend`：Spring Boot 后端
- `frontend`：Nginx 静态站点

### 云服务器部署

1. 把代码上传到云服务器
2. 确认服务器已安装 Docker 和 Docker Compose
3. 在项目根目录执行：

```bash
docker compose up -d --build
```

4. 放通云服务器安全组的 `80` 端口
5. 用浏览器访问 `http://你的服务器IP/`

### 数据与上传目录

Compose 里已经准备了两个持久化卷：

- `mysql-data`：MySQL 数据
- `uploads-data`：上传文件

如果你想把上传文件映射到宿主机目录，也可以把 compose 里的 volume 改成宿主机挂载。

### 线上环境变量

后端支持环境变量覆盖数据库和上传配置：

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`
- `APP_UPLOAD_PATH`
- `APP_UPLOAD_URL_PREFIX`
- `APP_UPLOAD_DATE_SUBDIR`
- `SERVER_PORT`

### 如果你要接外部 MySQL

把 `docker-compose.yml` 里的 `mysql` 服务删掉，然后修改 `backend` 服务里的：

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`

为你的云数据库地址和账号即可。

### 最短部署清单

如果你只想快速上线，按这 5 步做：

1. 把项目上传到云服务器，例如放到 `/opt/portfolio`
2. 进入项目目录，先把 `docker-compose.yml` 里的数据库密码改成你自己的
3. 执行：

```bash
docker compose up -d --build
```

4. 在云服务器安全组或防火墙放行 `80` 端口
5. 浏览器打开：

```text
http://你的服务器IP/
```

如果你启用了内置 MySQL，后台登录和上传都已经一起可用。

## 10. 默认接口

### Public

- `GET /api/public/settings`
- `GET /api/public/categories`
- `GET /api/public/categories/{id}/items`

### Admin

- `POST /api/admin/login`
- `GET /api/admin/settings`
- `PUT /api/admin/settings`
- `GET /api/admin/categories`
- `POST /api/admin/categories`
- `PUT /api/admin/categories`
- `DELETE /api/admin/categories/{id}`
- `GET /api/admin/items?categoryId=1`
- `POST /api/admin/items`
- `PUT /api/admin/items`
- `DELETE /api/admin/items/{id}`
- `POST /api/admin/upload`
