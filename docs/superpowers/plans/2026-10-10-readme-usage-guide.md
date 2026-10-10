# README Illustrated Usage Guide Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the stale root documentation with a concise project entry point and add a current, illustrated Chinese guide for students, teachers, and administrators.

**Architecture:** `README.md` remains the short project overview and links into `doc/user-guide.md`, which owns role-based procedures and troubleshooting. Current-version screenshots live under `doc/images/guide/` and are referenced through repository-relative paths.

**Tech Stack:** Markdown, Vue 3/Vite frontend, Spring Boot 4.1 backend, Docker Compose, browser screenshots

## Global Constraints

- Do not modify application behavior or UI.
- Capture every guide image from the current running version.
- Store guide images in `doc/images/guide/` with lowercase, hyphenated English names.
- Do not expose personal information, tokens, or secrets in screenshots.
- Prefer a small set of screenshots that demonstrates key states rather than one image per click.

---

### Task 1: Capture current application workflows

**Files:**
- Create: `doc/images/guide/login.png`
- Create: `doc/images/guide/student-exam-hall.png`
- Create: `doc/images/guide/student-records.png`
- Create: `doc/images/guide/teacher-questions.png`
- Create: `doc/images/guide/teacher-papers.png`
- Create: `doc/images/guide/teacher-grades.png`
- Create: `doc/images/guide/admin-dashboard.png`
- Create: `doc/images/guide/admin-users.png`
- Create: `doc/images/guide/admin-subjects.png`

**Interfaces:**
- Consumes: Demo users `student`, `teacher`, and `admin`, all with password `admin123`; application URL `http://localhost:9527`.
- Produces: Nine current-version PNG images used by `README.md` and `doc/user-guide.md`.

- [ ] **Step 1: Build and start the current application**

Run:

```bash
docker compose up --build
```

Expected: MySQL becomes healthy and the `app` service listens on port `9527`.

- [ ] **Step 2: Verify the login page**

Open `http://localhost:9527/login` in a desktop browser and confirm that the login form lists the three demo accounts.

Expected: The page shows username and password fields, an “进入考场” button, and the demo-account hint.

- [ ] **Step 3: Capture the login and student workflow**

Save the clean login page as `doc/images/guide/login.png`. Log in as `student`, then capture “考场” and “我的答卷” as `student-exam-hall.png` and `student-records.png`.

Expected: Student navigation contains 概览、考场、我的答卷 and does not expose staff-only menus.

- [ ] **Step 4: Capture the teacher workflow**

Log out, log in as `teacher`, and capture 题库、试卷、学情 as `teacher-questions.png`, `teacher-papers.png`, and `teacher-grades.png`.

Expected: Teacher navigation contains the student entries plus 题库、试卷、学情, without 学科 or 用户.

- [ ] **Step 5: Capture the administrator workflow**

Log out, log in as `admin`, and capture 概览、用户、学科 as `admin-dashboard.png`, `admin-users.png`, and `admin-subjects.png`.

Expected: Administrator navigation exposes every menu entry.

- [ ] **Step 6: Check image dimensions and names**

Run:

```bash
file doc/images/guide/*.png
```

Expected: Nine valid PNG images are reported and all filenames use lowercase English words separated by hyphens.

- [ ] **Step 7: Commit**

```bash
git add doc/images/guide
git commit -m "docs: capture current application workflows"
```

### Task 2: Write the concise README and role-based guide

**Files:**
- Modify: `README.md`
- Create: `doc/user-guide.md`

**Interfaces:**
- Consumes: Screenshot files from Task 1 and verified UI menu names.
- Produces: A concise project landing page and one complete role-based operation guide.

- [ ] **Step 1: Rewrite the root README**

Replace stale paths, commands, ports, and the legacy screenshot list. Include:

- project summary and current stack;
- role capability table;
- `docker compose up --build` quick start;
- JDK 21, Node.js 20+, MySQL 8 local-development commands;
- demo account table;
- one current screenshot;
- links to the student, teacher, administrator, and FAQ anchors in `doc/user-guide.md`;
- project layout, tests, and contribution instructions.

Expected: A new reader can start the application and reach the correct detailed section without reading an obsolete command.

- [ ] **Step 2: Write the student guide**

In `doc/user-guide.md`, document login, entering 考场, opening an exam, submitting it, and reviewing 我的答卷. Embed `login.png`, `student-exam-hall.png`, and `student-records.png` with descriptive alt text.

Expected: Every procedure names the visible menu or button and states the resulting page.

- [ ] **Step 3: Write the teacher guide**

Document question creation/import, paper creation, and class-performance review. Embed `teacher-questions.png`, `teacher-papers.png`, and `teacher-grades.png`.

Expected: The guide explains that teachers inherit student permissions but cannot manage users or subjects.

- [ ] **Step 4: Write the administrator guide and FAQ**

Document administrator access to all modules, user management, and subject management. Embed `admin-dashboard.png`, `admin-users.png`, and `admin-subjects.png`. Add answers for hidden menus, initialization, ports, and persistent Docker data.

Expected: The permission explanation matches `frontend/src/router.js` and Docker advice matches `docker-compose.yml`.

- [ ] **Step 5: Inspect the documentation diff**

Run:

```bash
git diff --check
git diff -- README.md doc/user-guide.md
```

Expected: `git diff --check` exits with status 0 and the diff contains no stale `frontend/exam`, `backend/exam`, `npm run serve`, or port `8000` instructions.

- [ ] **Step 6: Commit**

```bash
git add README.md doc/user-guide.md
git commit -m "docs: add illustrated role-based usage guide"
```

### Task 3: Validate links, content, and build instructions

**Files:**
- Modify if validation finds defects: `README.md`
- Modify if validation finds defects: `doc/user-guide.md`

**Interfaces:**
- Consumes: Documentation and images from Tasks 1–2.
- Produces: A verified documentation set with resolvable local links and commands aligned to the repository.

- [ ] **Step 1: Validate repository-relative Markdown targets**

Run a script that extracts local Markdown links and image targets from both files, ignores URL and anchor-only targets, URL-decodes paths, and fails if any referenced file is absent.

Expected: The script reports every repository-relative target as present and exits with status 0.

- [ ] **Step 2: Validate current commands and configuration**

Run:

```bash
docker compose config --quiet
npm --prefix frontend run build
(cd backend && mvn test)
```

Expected: Compose configuration is valid, the frontend build exits with status 0, and Maven reports all tests passing.

- [ ] **Step 3: Re-read permissions against routing code**

Compare the role table and guide menu names with `frontend/src/router.js` and `frontend/src/layouts/Shell.vue`.

Expected: Student, staff, and administrator entries match the actual route guards and visible navigation.

- [ ] **Step 4: Perform final cleanliness checks**

Run:

```bash
git diff --check
git status --short
```

Expected: No whitespace errors are reported and only intentional documentation changes remain.

- [ ] **Step 5: Commit validation fixes if needed**

```bash
git add README.md doc/user-guide.md doc/images/guide
git commit -m "docs: fix usage guide validation issues"
```

Skip this commit when validation required no changes.
