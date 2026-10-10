# 在线考试系统

一个前后端分离的在线考试系统，覆盖题库维护、组卷、在线作答、自动判分、成绩分析以及用户和学科管理，并适配桌面端和移动端。

- 后端：Java 21、Spring Boot 4.1、Spring Data JPA、JWT、springdoc OpenAPI
- 前端：Vue 3、Vite、Pinia、Element Plus、WangEditor、ECharts
- 数据库：MySQL 8

## 角色与能力

| 角色 | 可用模块与能力 |
| --- | --- |
| 学生 | 查看概览，进入考场作答，查看自己的答卷 |
| 教师 | 包含学生能力；维护题库、批量导题、创建试卷、查看全部考生成绩 |
| 管理员 | 访问全部模块；包含教师能力，并可管理用户和学科 |

详细操作请查看：[学生指南](doc/user-guide.md#student-guide) · [教师指南](doc/user-guide.md#teacher-guide) · [管理员指南](doc/user-guide.md#admin-guide) · [常见问题](doc/user-guide.md#faq)

## 快速开始
需要已安装 Docker 和 Docker Compose。在仓库根目录运行：

```bash
docker compose up --build
```

服务就绪后访问 <http://localhost:9527>。首次创建数据库卷时，MySQL 会自动执行 `doc/sql/exam.sql`。

### 演示账号

| 角色 | 用户名 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `admin123` |
| 教师 | `teacher` | `admin123` |
| 学生 | `student` | `admin123` |

![管理员登录后的学情概览与全部导航菜单](doc/images/guide/admin-dashboard.png)

## 本地开发

准备 JDK 21、Node.js 20+、Maven 和 MySQL 8。在仓库根目录创建并初始化数据库：

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS exam CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -u root -p exam < doc/sql/exam.sql
```

分别启动后端和前端；将示例中的密码替换为本机 MySQL `root` 密码：

```bash
cd backend
DB_PASSWORD='<MySQL root 密码>' mvn spring-boot:run
```

```bash
cd frontend
npm ci
npm run dev
```

前端开发地址为 <http://localhost:5173>，`/api` 请求会代理到运行在 `9527` 端口的后端。后端默认连接本机 `3306` 端口的 `exam` 数据库。

## 项目结构

```text
backend/              Spring Boot API、业务逻辑和后端测试
frontend/             Vue 3 单页应用
doc/sql/exam.sql      数据库结构与演示数据
doc/images/guide/     当前界面的用户指南截图
doc/user-guide.md     按角色划分的操作指南
docker-compose.yml    应用与 MySQL 的容器编排
Dockerfile            前后端构建及应用镜像
```

## 测试与构建

```bash
cd backend
mvn test
```

```bash
cd frontend
npm ci
npm run build
```

## 参与贡献

1. Fork 本仓库并从最新代码创建功能分支。
2. 完成修改后运行相关测试和构建。
3. 提交清晰、聚焦的变更，并发起 Pull Request。
