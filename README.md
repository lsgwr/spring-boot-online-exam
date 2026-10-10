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

按当前界面逐步操作的截图见下文[图文使用说明](#图文使用说明)。

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

准备 JDK 21、Node.js 20.19+ 或 22.12+、Maven 和 MySQL 8。在仓库根目录创建并初始化数据库：

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

前端开发地址为 <http://localhost:5173>，`/api` 请求会代理到运行在 `9527` 端口的后端。后端默认连接本机 `3306` 端口的 `exam` 数据库。 接口文档在 <http://localhost:9527/swagger-ui.html>。

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

## 图文使用说明

下面的截图来自当前 Vue 3 界面。左侧是桌面导航，宽度小于 900px 时收起，改到页面底部。

### 登录和注册

打开首页即登录页。三个演示账号的密码相同，也可以点「没有账号，去注册」。

![登录页，预填演示账号](doc/images/guide/login.png)

注册只收集用户名、密码、昵称和邮箱。创建成功后直接进入考场，角色是学生。

![注册页](doc/images/guide/register.png)

### 学生：参加考试

用 `student` / `admin123` 登录。概览页标题是「我的考场」，能看到开放考试数、自己的答卷数、平均分，以及得分率分布和最近答卷。

![学生概览](doc/images/guide/dashboard-student.png)

进入「考场」，每张卡片是一场开放的考试，写着总分、时长和单选、多选、判断的题量。点「开始作答」。

![考场列表](doc/images/guide/hall.png)

作答页右上角是倒计时。单选和判断点一个选项，多选可以点多个。选中的选项会描红。时间到会自动交卷。

![作答页，已选中选项](doc/images/guide/take-exam.png)

答完点页面底部的「交卷」，在确认框里点「确定」。

![交卷确认](doc/images/guide/submit-confirm.png)

交卷后进入这份答卷。绿色是正确答案，红色是选错的选项，每题标着得分。

![答卷详情](doc/images/guide/record-detail.png)

「我的答卷」列出自己交过的所有场次，点「查看」回到上面的详情。

![我的答卷](doc/images/guide/records.png)

手机上同一套页面改成单列，底部是「概览 / 考场 / 我的答卷」。

![手机上的考场](doc/images/guide/mobile-hall.png)

![手机上的概览](doc/images/guide/mobile-dashboard.png)

### 教师：出题、组卷、看成绩

用 `teacher` / `admin123` 登录。概览标题变成「学情概览」，多出题库规模，以及每场考试的人次、均分、最高分、最低分和及格率。

![教师学情概览](doc/images/guide/dashboard-teacher.png)

「题库」可以新建、编辑和删除题目。被未删除考试引用的题目不能直接删除。题干和选项都是富文本，配图以 Base64 写入数据库。

![题库列表](doc/images/guide/questions.png)

点「新建题目」，填写题干、解析、题型、难度、学科和分值。单选、判断用单选标记正确答案；多选则勾选多个正确选项。判断题可以点「填入判断题『正确 / 错误』」。

![新建题目](doc/images/guide/question-create.png)

批量导入：先点「下载模板」，得到 `questions.csv`，填好后点「批量导入」。也支持 `.xlsx`，列顺序与 CSV 相同。

```text
type,level,category,score,stem,optionA,optionB,optionC,optionD,answer
single,易,数学,5,1+1等于几,1,2,3,4,B
multi,中,语文,6,下列哪些是元音,A,E,I,O,A|B|C|D
judge,易,生活,5,地球是圆的,,,,,正确
```

- `type`：`single` / `多选`（或 `multi`）/ `judge`（或 `判断`）
- `level`：`易`、`中`、`难`
- `category`：必须是已经存在的学科名称
- `answer`：单选用 `A`–`D`；多选用 `|` 或 `、` 分隔；判断用 `正确` 或 `错误`

「试卷」里点「新建考试」。填写名称、简介、封面、时长，以及单选、多选、判断的每题分值，再从题库里勾选题目。删除考试只会隐藏这场考试，题目仍留在题库。

![试卷列表](doc/images/guide/papers.png)

![新建考试](doc/images/guide/paper-create.png)

「学情」列出全部考生的成绩。可以用下拉框只看一场考试，也可以「导出 CSV」。点「答卷」能打开该考生的逐题详情。

![学情](doc/images/guide/grades.png)

教师自己也可以从「考场」参加考试，记录出现在「我的答卷」里。

### 管理员：学科和用户

用 `admin` / `admin123` 登录。侧栏比教师多「学科」和「用户」。

「学科」按卡片列出名称、简介和题量。仍有题目的学科不能删除。

![学科列表](doc/images/guide/subjects.png)

![新建学科](doc/images/guide/subject-create.png)

「用户」可以新建账号、修改昵称和邮箱、重置密码，以及停用账号。停用后无法登录。新建用户时选择角色：管理员、教师或学生。

![用户列表](doc/images/guide/users.png)

![新建用户](doc/images/guide/user-create.png)
