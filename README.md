# spring-boot-online-exam

在线考试系统。题库、组卷、作答和学情在同一套界面里完成，窄屏时改用底部导航。

在线 Demo：http://129.211.88.191 。演示账号是 `admin`、`teacher`、`student`，密码都是 `admin123`。代码讲解：https://www.bilibili.com/video/BV1FP4y1L7xt/

Python 实现：https://github.com/xingxingzaixian/django-drf-online-exam

当前技术栈：Java 21、Spring Boot 4.1、Vue 3、Vite、Element Plus。

## 1. 快速开始

### 1.1 演示账号

| 角色 | 用户名 | 密码 | 登录后能看到的菜单 |
| --- | --- | --- | --- |
| 管理员 | `admin` | `admin123` | 概览、考场、我的答卷、题库、试卷、学情、学科、用户 |
| 教师 | `teacher` | `admin123` | 概览、考场、我的答卷、题库、试卷、学情 |
| 学生 | `student` | `admin123` | 概览、考场、我的答卷 |

自己注册的账号默认是学生。教师和管理员由管理员在「用户」里分配。

### 1.2 容器一键启动

```shell
docker compose up --build
```

浏览器打开 http://localhost:9527 。MySQL 会用 `doc/sql/exam.sql` 初始化。

### 1.3 本地开发

准备 JDK 21、Node 20+ 和 MySQL 8。新建 `exam` 库，账号密码与 `backend/src/main/resources/application.yml` 一致（默认 root / `aA111111`），然后导入 `doc/sql/exam.sql`。

```shell
cd backend && mvn spring-boot:run
cd frontend && npm install && npm run dev
```

前端开发地址 http://localhost:5173 ，接口代理到 9527。接口文档在 http://localhost:9527/swagger-ui.html 。

## 2. 使用说明

下面的截图来自当前 Vue 3 界面。左侧是桌面导航，宽度小于 900px 时收起，改到页面底部。

### 2.1 登录和注册

打开首页即登录页。三个演示账号的密码相同，也可以点「没有账号，去注册」。

![登录页，预填演示账号](doc/images/guide/login.png)

注册只收集用户名、密码、昵称和邮箱。创建成功后直接进入考场，角色是学生。

![注册页](doc/images/guide/register.png)

### 2.2 学生：参加考试

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

### 2.3 教师：出题、组卷、看成绩

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

### 2.4 管理员：学科和用户

用 `admin` / `admin123` 登录。侧栏比教师多「学科」和「用户」。

「学科」按卡片列出名称、简介和题量。仍有题目的学科不能删除。

![学科列表](doc/images/guide/subjects.png)

![新建学科](doc/images/guide/subject-create.png)

「用户」可以新建账号、修改昵称和邮箱、重置密码，以及停用账号。停用后无法登录。新建用户时选择角色：管理员、教师或学生。

![用户列表](doc/images/guide/users.png)

![新建用户](doc/images/guide/user-create.png)

## 3. 功能范围

- 题型：单选题、多选题、判断题
- 学生：参加考试，查看我的答卷
- 教师：学生的全部能力，再加上题库、组卷和学情
- 管理员：教师的全部能力，再加上学科和用户
- 后端：Spring Boot 4.1、Spring Data JPA、springdoc OpenAPI、JWT
- 前端：Vue 3、Vite、Pinia、Element Plus、WangEditor

## 4. 参与贡献

1. Fork 本仓库
2. 新建 `exam_xxx` 分支
3. 提交代码
4. 新建 Pull Request

## 5. 进度

+ `√` 0. 修复题目创建失败
+ `√` 1. 考试详情编辑
+ `√` 2. 题目和考试软删除：`visible=0`。题目仍被未删除考试引用时拒绝删除；删除考试不删除题目
+ `√` 3. 图片以 Base64 存入数据库（封面、配图、富文本内图片）
+ `√` 4. 题干和选项支持富文本
+ `√` 5. 按 CSV / Excel 模板批量导入题目
+ `√` 6. 用户管理、学科管理
+ `√` 7. 教师查看全部考生成绩和每场考试统计
+ `√` 8. 概览页得分率、学科题量和考试统计
+ `√` 9. Dockerfile 与 docker compose 一键部署
+ `√` 10. 移动端：同一套 Vue 3 界面在窄屏使用底部导航，不另维护 uni-app 工程
