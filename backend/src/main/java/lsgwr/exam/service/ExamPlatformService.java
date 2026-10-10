package lsgwr.exam.service;

import lsgwr.exam.dto.Payloads.*;
import lsgwr.exam.entity.*;
import lsgwr.exam.repo.*;
import lsgwr.exam.security.CurrentUser;
import lsgwr.exam.security.JwtService;
import lsgwr.exam.security.PasswordCodec;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamPlatformService {
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of("Asia/Shanghai"));
    private static final List<String> BUCKETS = List.of("0-59", "60-69", "70-79", "80-89", "90-100");

    private final UserRepository users;
    private final RoleRepository roles;
    private final QuestionRepository questions;
    private final QuestionOptionRepository options;
    private final ExamRepository exams;
    private final ExamRecordRepository records;
    private final QuestionCategoryRepository categories;
    private final QuestionTypeRepository types;
    private final QuestionLevelRepository levels;
    private final PasswordCodec passwords;
    private final JwtService jwt;

    public ExamPlatformService(UserRepository users, RoleRepository roles, QuestionRepository questions,
                               QuestionOptionRepository options, ExamRepository exams, ExamRecordRepository records,
                               QuestionCategoryRepository categories, QuestionTypeRepository types,
                               QuestionLevelRepository levels, PasswordCodec passwords, JwtService jwt) {
        this.users = users;
        this.roles = roles;
        this.questions = questions;
        this.options = options;
        this.exams = exams;
        this.records = records;
        this.categories = categories;
        this.types = types;
        this.levels = levels;
        this.passwords = passwords;
        this.jwt = jwt;
    }

    @Transactional
    public AuthView login(LoginRequest request) {
        User user = users.findByUserUsername(require(request.username(), "请输入用户名"))
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));
        if (user.getEnabled() != null && user.getEnabled() == 0) {
            throw new IllegalArgumentException("账号已停用");
        }
        if (!passwords.matches(require(request.password(), "请输入密码"), user.getUserPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        return auth(user);
    }

    @Transactional
    public AuthView register(RegisterRequest request) {
        String username = require(request.username(), "请输入用户名");
        if (users.existsByUserUsername(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        if (request.email() != null && !request.email().isBlank() && users.existsByUserEmail(request.email())) {
            throw new IllegalArgumentException("邮箱已存在");
        }
        User user = newUser(username, require(request.password(), "请输入密码"),
                blank(request.nickname()) ? username : request.nickname(),
                request.email(), request.phone(), 3, request.nickname());
        return auth(users.save(user));
    }

    public UserView me(CurrentUser current) {
        return view(loadUser(current.id()));
    }

    public List<UserView> listUsers(CurrentUser current) {
        requireAdmin(current);
        return users.findAll().stream().map(this::view).toList();
    }

    @Transactional
    public UserView createUser(CurrentUser current, UserWrite body) {
        requireAdmin(current);
        String username = require(body.username(), "请输入用户名");
        if (users.existsByUserUsername(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        int roleId = body.roleId() == null ? 3 : body.roleId();
        requireRole(roleId);
        User user = newUser(username, require(body.password(), "请输入初始密码"),
                blank(body.nickname()) ? username : body.nickname(),
                body.email(), body.phone(), roleId, body.description());
        if (body.enabled() != null) {
            user.setEnabled(body.enabled());
        }
        return view(users.save(user));
    }

    @Transactional
    public UserView updateUser(CurrentUser current, String id, UserWrite body) {
        requireAdmin(current);
        User user = loadUser(id);
        if (!blank(body.nickname())) {
            user.setUserNickname(body.nickname());
        }
        if (body.email() != null) {
            user.setUserEmail(body.email());
        }
        if (body.phone() != null) {
            user.setUserPhone(body.phone());
        }
        if (body.description() != null) {
            user.setUserDescription(body.description());
        }
        if (body.roleId() != null) {
            requireRole(body.roleId());
            user.setUserRoleId(body.roleId());
        }
        if (body.enabled() != null) {
            if (current.id().equals(id) && body.enabled() == 0) {
                throw new IllegalArgumentException("不能停用自己");
            }
            user.setEnabled(body.enabled());
        }
        if (!blank(body.password())) {
            user.setUserPassword(passwords.encode(body.password()));
        }
        return view(users.save(user));
    }

    public Meta meta() {
        return new Meta(
                types.findAll().stream().map(t -> new Named(t.getQuestionTypeId(), t.getQuestionTypeDescription(), t.getQuestionTypeName())).toList(),
                levels.findAll().stream().map(t -> new Named(t.getQuestionLevelId(), t.getQuestionLevelDescription(), t.getQuestionLevelName())).toList(),
                categories.findAll().stream().map(this::subject).toList()
        );
    }

    public List<SubjectView> subjects() {
        return categories.findAll().stream().map(this::subject).toList();
    }

    @Transactional
    public SubjectView createSubject(CurrentUser current, SubjectWrite body) {
        requireAdmin(current);
        QuestionCategory category = new QuestionCategory();
        category.setQuestionCategoryName(require(body.name(), "请输入学科名称"));
        category.setQuestionCategoryDescription(body.description());
        return subject(categories.save(category));
    }

    @Transactional
    public SubjectView updateSubject(CurrentUser current, Integer id, SubjectWrite body) {
        requireAdmin(current);
        QuestionCategory category = categories.findById(id).orElseThrow(() -> new IllegalArgumentException("学科不存在"));
        category.setQuestionCategoryName(require(body.name(), "请输入学科名称"));
        category.setQuestionCategoryDescription(body.description());
        return subject(categories.save(category));
    }

    @Transactional
    public void deleteSubject(CurrentUser current, Integer id) {
        requireAdmin(current);
        long used = questions.findAll().stream()
                .filter(q -> active(q.getVisible()) && Objects.equals(q.getQuestionCategoryId(), id))
                .count();
        if (used > 0) {
            throw new IllegalStateException("仍有 " + used + " 道题目属于该学科，无法删除");
        }
        categories.deleteById(id);
    }

    public List<QuestionView> questions(CurrentUser current, boolean hidden) {
        requireStaff(current);
        return questions.findAll().stream()
                .filter(q -> hidden || active(q.getVisible()))
                .sorted(Comparator.comparing(Question::getUpdateTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(q -> questionView(q, true))
                .toList();
    }

    @Transactional
    public QuestionView createQuestion(CurrentUser current, QuestionWrite body) {
        requireStaff(current);
        Question question = new Question();
        question.setQuestionId(id());
        question.setQuestionCreatorId(current.id());
        question.setCreateTime(new Date());
        question.setVisible(1);
        fillQuestion(question, body);
        return questionView(questions.save(question), true);
    }

    @Transactional
    public QuestionView updateQuestion(CurrentUser current, String id, QuestionWrite body) {
        requireStaff(current);
        Question question = questions.findById(id).orElseThrow(() -> new IllegalArgumentException("题目不存在"));
        fillQuestion(question, body);
        question.setVisible(1);
        return questionView(questions.save(question), true);
    }

    @Transactional
    public void deleteQuestion(CurrentUser current, String id) {
        requireStaff(current);
        Question question = questions.findById(id).orElseThrow(() -> new IllegalArgumentException("题目不存在"));
        List<String> holders = exams.findAll().stream()
                .filter(e -> active(e.getVisible()) && examUses(e, id))
                .map(Exam::getExamName)
                .toList();
        if (!holders.isEmpty()) {
            throw new IllegalStateException("题目仍被考试引用，请先删除考试：" + String.join("、", holders));
        }
        question.setVisible(0);
        questions.save(question);
    }

    @Transactional
    public ImportResult importQuestions(CurrentUser current, String filename, byte[] bytes) {
        requireStaff(current);
        List<String[]> rows = filename != null && filename.toLowerCase(Locale.ROOT).endsWith(".xlsx")
                ? readXlsx(bytes) : readCsv(bytes);
        int created = 0;
        List<String> errors = new ArrayList<>();
        int line = 1;
        for (String[] row : rows) {
            line++;
            try {
                if (row.length == 0 || Arrays.stream(row).allMatch(this::blank)) {
                    continue;
                }
                createQuestion(current, fromRow(row));
                created++;
            } catch (RuntimeException ex) {
                errors.add("第 " + line + " 行：" + ex.getMessage());
            }
        }
        return new ImportResult(created, errors.size(), errors.size() > 8 ? errors.subList(0, 8) : errors);
    }

    public List<ExamCard> examCards() {
        Map<String, String> names = users.findAll().stream().collect(Collectors.toMap(User::getUserId, User::getUserNickname, (a, b) -> a));
        return exams.findAll().stream()
                .filter(e -> active(e.getVisible()))
                .map(e -> new ExamCard(e.getExamId(), e.getExamName(), e.getExamDescription(), e.getExamAvatar(),
                        e.getExamScore(), e.getExamTimeLimit(),
                        split(e.getExamQuestionIdsRadio()).size(),
                        split(e.getExamQuestionIdsCheck()).size(),
                        split(e.getExamQuestionIdsJudge()).size(),
                        names.getOrDefault(e.getExamCreatorId(), "未知")))
                .toList();
    }

    public Paper paper(String examId) {
        Exam exam = visibleExam(examId);
        List<PaperQuestion> list = new ArrayList<>();
        list.addAll(paperQuestions(split(exam.getExamQuestionIdsRadio()), nvl(exam.getExamScoreRadio())));
        list.addAll(paperQuestions(split(exam.getExamQuestionIdsCheck()), nvl(exam.getExamScoreCheck())));
        list.addAll(paperQuestions(split(exam.getExamQuestionIdsJudge()), nvl(exam.getExamScoreJudge())));
        return new Paper(exam.getExamId(), exam.getExamName(), exam.getExamDescription(), exam.getExamAvatar(),
                exam.getExamTimeLimit(), exam.getExamScore(), list);
    }

    public List<ExamEditor> editableExams(CurrentUser current) {
        requireStaff(current);
        return exams.findAll().stream().filter(e -> active(e.getVisible())).map(this::editor).toList();
    }

    public ExamEditor examEditor(CurrentUser current, String id) {
        requireStaff(current);
        return editor(visibleExam(id));
    }

    @Transactional
    public ExamEditor createExam(CurrentUser current, ExamWrite body) {
        requireStaff(current);
        Exam exam = new Exam();
        exam.setExamId(id());
        exam.setExamCreatorId(current.id());
        exam.setCreateTime(new Date());
        exam.setVisible(1);
        exam.setExamStartDate(new Date());
        exam.setExamEndDate(new Date());
        fillExam(exam, body);
        return editor(exams.save(exam));
    }

    @Transactional
    public ExamEditor updateExam(CurrentUser current, String id, ExamWrite body) {
        requireStaff(current);
        Exam exam = exams.findById(id).orElseThrow(() -> new IllegalArgumentException("考试不存在"));
        fillExam(exam, body);
        exam.setVisible(1);
        return editor(exams.save(exam));
    }

    @Transactional
    public void deleteExam(CurrentUser current, String id) {
        requireStaff(current);
        Exam exam = exams.findById(id).orElseThrow(() -> new IllegalArgumentException("考试不存在"));
        exam.setVisible(0);
        exams.save(exam);
    }

    @Transactional
    public RecordDetail submit(CurrentUser current, String examId, SubmitRequest body) {
        Exam exam = visibleExam(examId);
        Map<String, List<String>> answers = body.answers() == null ? Map.of() : body.answers();
        Map<String, Integer> perQuestion = new LinkedHashMap<>();
        perQuestion.putAll(scoreMap(split(exam.getExamQuestionIdsRadio()), nvl(exam.getExamScoreRadio())));
        perQuestion.putAll(scoreMap(split(exam.getExamQuestionIdsCheck()), nvl(exam.getExamScoreCheck())));
        perQuestion.putAll(scoreMap(split(exam.getExamQuestionIdsJudge()), nvl(exam.getExamScoreJudge())));
        StringBuilder stored = new StringBuilder();
        int total = 0;
        for (String questionId : perQuestion.keySet()) {
            Question question = questions.findById(questionId).orElse(null);
            if (question == null) {
                continue;
            }
            List<String> expected = split(question.getQuestionAnswerOptionIds());
            List<String> chosen = new ArrayList<>(answers.getOrDefault(questionId, List.of()));
            boolean correct = sameSet(expected, chosen);
            int awarded = correct ? perQuestion.get(questionId) : 0;
            total += awarded;
            stored.append(questionId).append(correct ? "@True_" : "@False_").append(String.join("-", chosen)).append('$');
        }
        ExamRecord record = new ExamRecord();
        record.setExamRecordId(id());
        record.setExamId(examId);
        record.setExamJoinerId(current.id());
        record.setExamJoinDate(new Date());
        record.setExamJoinScore(total);
        record.setExamTimeCost(body.timeCost() == null ? 0 : body.timeCost());
        record.setExamResultLevel(levelOf(total, nvl(exam.getExamScore())));
        String text = stored.toString();
        if (text.length() > 4096) {
            text = text.substring(0, 4096);
        }
        record.setAnswerOptionIds(text.endsWith("$") ? text.substring(0, text.length() - 1) : text);
        records.save(record);
        return recordDetail(record, current);
    }

    public List<RecordRow> myRecords(CurrentUser current) {
        return records.findByExamJoinerIdOrderByExamJoinDateDesc(current.id()).stream()
                .map(r -> row(r, current.id()))
                .toList();
    }

    public RecordDetail recordDetail(CurrentUser current, String recordId) {
        ExamRecord record = records.findById(recordId).orElseThrow(() -> new IllegalArgumentException("记录不存在"));
        if (!current.staff() && !current.id().equals(record.getExamJoinerId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "只能查看自己的答卷");
        }
        return recordDetail(record, current);
    }

    public List<RecordRow> grades(CurrentUser current, String examId) {
        requireStaff(current);
        List<ExamRecord> list = blank(examId) ? records.findAllByOrderByExamJoinDateDesc() : records.findByExamIdOrderByExamJoinDateDesc(examId);
        return list.stream().map(r -> row(r, r.getExamJoinerId())).toList();
    }

    public String gradesCsv(CurrentUser current, String examId) {
        StringBuilder csv = new StringBuilder("考试,考生,得分,总分,用时秒,时间\n");
        for (RecordRow row : grades(current, examId)) {
            csv.append(csvCell(row.examName())).append(',')
                    .append(csvCell(row.studentName())).append(',')
                    .append(row.score()).append(',')
                    .append(row.total()).append(',')
                    .append(row.timeCost()).append(',')
                    .append(csvCell(row.joinDate())).append('\n');
        }
        return csv.toString();
    }

    public Analytics analytics(CurrentUser current) {
        List<ExamRecord> source = current.staff()
                ? records.findAllByOrderByExamJoinDateDesc()
                : records.findByExamJoinerIdOrderByExamJoinDateDesc(current.id());
        long attempts = source.size();
        double average = source.stream().mapToInt(r -> nvl(r.getExamJoinScore())).average().orElse(0);
        Map<String, Long> buckets = new LinkedHashMap<>();
        BUCKETS.forEach(b -> buckets.put(b, 0L));
        Map<String, Exam> examMap = exams.findAll().stream().collect(Collectors.toMap(Exam::getExamId, e -> e, (a, b) -> a));
        for (ExamRecord record : source) {
            Exam exam = examMap.get(record.getExamId());
            int total = exam == null ? 0 : nvl(exam.getExamScore());
            int pct = total <= 0 ? 0 : nvl(record.getExamJoinScore()) * 100 / total;
            buckets.merge(bucket(pct), 1L, Long::sum);
        }
        List<ExamStat> stats = new ArrayList<>();
        List<CategoryStat> categoryStats = List.of();
        Overview overview;
        if (current.staff()) {
            Map<String, List<ExamRecord>> grouped = source.stream().collect(Collectors.groupingBy(ExamRecord::getExamId));
            for (Map.Entry<String, List<ExamRecord>> entry : grouped.entrySet()) {
                Exam exam = examMap.get(entry.getKey());
                if (exam == null || !active(exam.getVisible())) {
                    continue;
                }
                IntSummaryStatistics summary = entry.getValue().stream().mapToInt(r -> nvl(r.getExamJoinScore())).summaryStatistics();
                long passed = entry.getValue().stream().filter(r -> passed(nvl(r.getExamJoinScore()), nvl(exam.getExamScore()))).count();
                stats.add(new ExamStat(exam.getExamId(), exam.getExamName(), summary.getCount(), round(summary.getAverage()),
                        summary.getMax(), summary.getMin(), summary.getCount() == 0 ? 0 : round(passed * 100.0 / summary.getCount())));
            }
            stats.sort(Comparator.comparingLong(ExamStat::attempts).reversed());
            categoryStats = categories.findAll().stream()
                    .map(c -> new CategoryStat(c.getQuestionCategoryName(), questions.findAll().stream()
                            .filter(q -> active(q.getVisible()) && Objects.equals(q.getQuestionCategoryId(), c.getQuestionCategoryId())).count()))
                    .toList();
            overview = new Overview(users.count(), exams.findAll().stream().filter(e -> active(e.getVisible())).count(),
                    questions.findAll().stream().filter(q -> active(q.getVisible())).count(), attempts, round(average));
        } else {
            overview = new Overview(1, examCards().size(), 0, attempts, round(average));
        }
        List<RecordRow> recent = source.stream().limit(8).map(r -> row(r, r.getExamJoinerId())).toList();
        return new Analytics(current.staff() ? (current.admin() ? "admin" : "teacher") : "student", overview,
                buckets.entrySet().stream().map(e -> new Bucket(e.getKey(), e.getValue())).toList(),
                stats, categoryStats, recent);
    }

    public String imageDataUrl(String contentType, byte[] bytes) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("请上传图片");
        }
        if (bytes.length > 2_000_000) {
            throw new IllegalArgumentException("图片需小于 2MB，将以 Base64 存入数据库");
        }
        return "data:" + contentType + ";base64," + Base64.getEncoder().encodeToString(bytes);
    }

    private void fillQuestion(Question question, QuestionWrite body) {
        int typeId = body.typeId() == null ? 1 : body.typeId();
        if (types.findById(typeId).isEmpty()) {
            throw new IllegalArgumentException("题型不存在");
        }
        List<OptionWrite> incoming = body.options() == null ? List.of() : body.options();
        if (incoming.size() < 2) {
            throw new IllegalArgumentException("至少需要两个选项");
        }
        long correct = incoming.stream().filter(o -> Boolean.TRUE.equals(o.correct())).count();
        if (typeId == 2 && correct < 1) {
            throw new IllegalArgumentException("多选题至少选择一个正确答案");
        }
        if (typeId != 2 && correct != 1) {
            throw new IllegalArgumentException("单选题和判断题只能有一个正确答案");
        }
        List<String> optionIds = new ArrayList<>();
        List<String> answerIds = new ArrayList<>();
        for (OptionWrite option : incoming) {
            QuestionOption entity = new QuestionOption();
            entity.setQuestionOptionId(id());
            entity.setQuestionOptionContent(require(option.content(), "选项内容不能为空"));
            options.save(entity);
            optionIds.add(entity.getQuestionOptionId());
            if (Boolean.TRUE.equals(option.correct())) {
                answerIds.add(entity.getQuestionOptionId());
            }
        }
        question.setQuestionName(require(body.stem(), "请输入题干"));
        question.setQuestionDescription(body.analysis());
        question.setQuestionScore(body.score() == null ? 5 : body.score());
        question.setQuestionTypeId(typeId);
        question.setQuestionLevelId(body.levelId() == null ? 3 : body.levelId());
        question.setQuestionCategoryId(body.categoryId() == null ? 1 : body.categoryId());
        question.setQuestionImage(body.image());
        question.setQuestionOptionIds(String.join("-", optionIds));
        question.setQuestionAnswerOptionIds(String.join("-", answerIds));
        question.setUpdateTime(new Date());
    }

    private void fillExam(Exam exam, ExamWrite body) {
        List<String> radio = clean(body.radioIds());
        List<String> checks = clean(body.checkIds());
        List<String> judges = clean(body.judgeIds());
        if (radio.isEmpty() && checks.isEmpty() && judges.isEmpty()) {
            throw new IllegalArgumentException("请至少选择一道题目");
        }
        int radioScore = nvl(body.radioScore());
        int checkScore = nvl(body.checkScore());
        int judgeScore = nvl(body.judgeScore());
        exam.setExamName(require(body.name(), "请输入考试名称"));
        exam.setExamDescription(body.description());
        exam.setExamAvatar(body.avatar());
        exam.setExamTimeLimit(body.timeLimit() == null ? 60 : body.timeLimit());
        exam.setExamQuestionIdsRadio(String.join("-", radio));
        exam.setExamQuestionIdsCheck(String.join("-", checks));
        exam.setExamQuestionIdsJudge(String.join("-", judges));
        List<String> all = new ArrayList<>();
        all.addAll(radio);
        all.addAll(checks);
        all.addAll(judges);
        exam.setExamQuestionIds(String.join("-", all));
        exam.setExamScoreRadio(radioScore);
        exam.setExamScoreCheck(checkScore);
        exam.setExamScoreJudge(judgeScore);
        exam.setExamScore(radio.size() * radioScore + checks.size() * checkScore + judges.size() * judgeScore);
        exam.setUpdateTime(new Date());
    }

    private QuestionWrite fromRow(String[] row) {
        String typeName = cell(row, 0);
        int typeId = switch (typeName) {
            case "多选", "multi", "2" -> 2;
            case "判断", "judge", "3" -> 3;
            default -> 1;
        };
        int levelId = switch (cell(row, 1)) {
            case "难", "high", "1" -> 1;
            case "中", "middle", "2" -> 2;
            default -> 3;
        };
        String categoryName = cell(row, 2);
        int categoryId = categories.findAll().stream()
                .filter(c -> c.getQuestionCategoryName().equals(categoryName))
                .map(QuestionCategory::getQuestionCategoryId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("学科不存在：" + categoryName));
        int score = parseInt(cell(row, 3), 5);
        String stem = require(cell(row, 4), "题干为空");
        List<OptionWrite> optionList = new ArrayList<>();
        if (typeId == 3 && blank(cell(row, 5))) {
            String answer = cell(row, 9);
            optionList.add(new OptionWrite("正确", answer.equals("正确") || answer.equalsIgnoreCase("true") || answer.equals("A")));
            optionList.add(new OptionWrite("错误", answer.equals("错误") || answer.equalsIgnoreCase("false") || answer.equals("B")));
        } else {
            String answer = cell(row, 9).toUpperCase(Locale.ROOT);
            Set<String> keys = Arrays.stream(answer.split("[|,、]")).map(String::trim).collect(Collectors.toSet());
            for (int i = 0; i < 4; i++) {
                String content = cell(row, 5 + i);
                if (blank(content)) {
                    continue;
                }
                String key = String.valueOf((char) ('A' + i));
                optionList.add(new OptionWrite(content, keys.contains(key) || keys.contains(content)));
            }
        }
        return new QuestionWrite(stem, "", score, typeId, levelId, categoryId, null, optionList);
    }

    private List<String[]> readCsv(byte[] bytes) {
        try (CSVParser parser = CSVParser.parse(new InputStreamReader(new ByteArrayInputStream(stripBom(bytes)), StandardCharsets.UTF_8),
                CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setIgnoreEmptyLines(true).setTrim(true).build())) {
            List<String[]> rows = new ArrayList<>();
            for (CSVRecord record : parser) {
                rows.add(new String[]{record.get(0), record.get(1), record.get(2), record.get(3), record.get(4),
                        record.size() > 5 ? record.get(5) : "", record.size() > 6 ? record.get(6) : "",
                        record.size() > 7 ? record.get(7) : "", record.size() > 8 ? record.get(8) : "",
                        record.size() > 9 ? record.get(9) : ""});
            }
            return rows;
        } catch (Exception ex) {
            throw new IllegalArgumentException("无法解析 CSV，请使用模板表头");
        }
    }

    private List<String[]> readXlsx(byte[] bytes) {
        try (XSSFWorkbook book = new XSSFWorkbook(new ByteArrayInputStream(bytes))) {
            Sheet sheet = book.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            List<String[]> rows = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                String[] cells = new String[10];
                for (int c = 0; c < 10; c++) {
                    Cell cell = row.getCell(c);
                    cells[c] = cell == null ? "" : formatter.formatCellValue(cell).trim();
                }
                rows.add(cells);
            }
            return rows;
        } catch (Exception ex) {
            throw new IllegalArgumentException("无法解析 Excel");
        }
    }

    private RecordDetail recordDetail(ExamRecord record, CurrentUser current) {
        Exam exam = exams.findById(record.getExamId()).orElse(null);
        List<AnswerLine> lines = new ArrayList<>();
        for (String piece : splitDollar(record.getAnswerOptionIds())) {
            int at = piece.indexOf('@');
            int us = piece.indexOf('_');
            if (at < 0 || us < 0) {
                continue;
            }
            String questionId = piece.substring(0, at);
            boolean correct = piece.substring(at + 1, us).equals("True");
            List<String> chosen = split(piece.substring(us + 1));
            Question question = questions.findById(questionId).orElse(null);
            lines.add(new AnswerLine(questionId, question == null ? "题目已移除" : question.getQuestionName(),
                    question == null ? null : question.getQuestionImage(), correct,
                    correct && exam != null ? scoreOf(exam, questionId) : 0,
                    question == null ? List.of() : optionViews(question, true), chosen));
        }
        return new RecordDetail(row(record, record.getExamJoinerId()), lines);
    }

    private int scoreOf(Exam exam, String questionId) {
        if (split(exam.getExamQuestionIdsRadio()).contains(questionId)) {
            return nvl(exam.getExamScoreRadio());
        }
        if (split(exam.getExamQuestionIdsCheck()).contains(questionId)) {
            return nvl(exam.getExamScoreCheck());
        }
        if (split(exam.getExamQuestionIdsJudge()).contains(questionId)) {
            return nvl(exam.getExamScoreJudge());
        }
        return 0;
    }

    private List<PaperQuestion> paperQuestions(List<String> ids, int score) {
        List<PaperQuestion> list = new ArrayList<>();
        for (String id : ids) {
            Question question = questions.findById(id).orElse(null);
            if (question == null || !active(question.getVisible())) {
                continue;
            }
            QuestionType type = types.findById(question.getQuestionTypeId()).orElse(null);
            list.add(new PaperQuestion(question.getQuestionId(), question.getQuestionName(), question.getQuestionImage(),
                    question.getQuestionTypeId(), type == null ? "" : type.getQuestionTypeDescription(), score,
                    optionViews(question, false)));
        }
        return list;
    }

    private QuestionView questionView(Question question, boolean withAnswer) {
        QuestionType type = types.findById(nvl(question.getQuestionTypeId())).orElse(null);
        QuestionLevel level = levels.findById(nvl(question.getQuestionLevelId())).orElse(null);
        QuestionCategory category = question.getQuestionCategoryId() == null ? null : categories.findById(question.getQuestionCategoryId()).orElse(null);
        return new QuestionView(question.getQuestionId(), question.getQuestionName(), question.getQuestionDescription(),
                question.getQuestionScore(), question.getQuestionTypeId(), type == null ? "" : type.getQuestionTypeDescription(),
                question.getQuestionLevelId(), level == null ? "" : level.getQuestionLevelDescription(),
                question.getQuestionCategoryId(), category == null ? "" : category.getQuestionCategoryName(),
                question.getQuestionImage(), question.getVisible(), optionViews(question, withAnswer));
    }

    private List<OptionView> optionViews(Question question, boolean withAnswer) {
        Set<String> answers = new HashSet<>(split(question.getQuestionAnswerOptionIds()));
        List<OptionView> list = new ArrayList<>();
        for (String optionId : split(question.getQuestionOptionIds())) {
            QuestionOption option = options.findById(optionId).orElse(null);
            if (option == null) {
                continue;
            }
            list.add(new OptionView(optionId, option.getQuestionOptionContent(), withAnswer && answers.contains(optionId)));
        }
        return list;
    }

    private ExamEditor editor(Exam exam) {
        return new ExamEditor(exam.getExamId(), exam.getExamName(), exam.getExamDescription(), exam.getExamAvatar(),
                exam.getExamTimeLimit(), exam.getExamScoreRadio(), exam.getExamScoreCheck(), exam.getExamScoreJudge(),
                exam.getExamScore(), split(exam.getExamQuestionIdsRadio()), split(exam.getExamQuestionIdsCheck()),
                split(exam.getExamQuestionIdsJudge()));
    }

    private RecordRow row(ExamRecord record, String studentId) {
        Exam exam = exams.findById(record.getExamId()).orElse(null);
        User student = users.findById(studentId).orElse(null);
        return new RecordRow(record.getExamRecordId(), record.getExamId(), exam == null ? "已删除考试" : exam.getExamName(),
                studentId, student == null ? "未知" : student.getUserNickname(), nvl(record.getExamJoinScore()),
                exam == null ? 0 : nvl(exam.getExamScore()), nvl(record.getExamTimeCost()),
                record.getExamJoinDate() == null ? "" : TIME.format(record.getExamJoinDate().toInstant()));
    }

    private SubjectView subject(QuestionCategory category) {
        long count = questions.findAll().stream()
                .filter(q -> active(q.getVisible()) && Objects.equals(q.getQuestionCategoryId(), category.getQuestionCategoryId()))
                .count();
        return new SubjectView(category.getQuestionCategoryId(), category.getQuestionCategoryName(), category.getQuestionCategoryDescription(), count);
    }

    private AuthView auth(User user) {
        CurrentUser current = new CurrentUser(user.getUserId(), user.getUserUsername(), nvl(user.getUserRoleId()));
        return new AuthView(jwt.issue(current), view(user));
    }

    private UserView view(User user) {
        Role role = user.getUserRoleId() == null ? null : roles.findById(user.getUserRoleId()).orElse(null);
        return new UserView(user.getUserId(), user.getUserUsername(), user.getUserNickname(), user.getUserEmail(),
                user.getUserPhone(), user.getUserRoleId(), role == null ? "" : role.getRoleDescription(),
                user.getUserAvatar(), user.getUserDescription(), user.getEnabled() == null ? 1 : user.getEnabled());
    }

    private User newUser(String username, String password, String nickname, String email, String phone, int roleId, String description) {
        User user = new User();
        user.setUserId(id());
        user.setUserUsername(username);
        user.setUserPassword(passwords.encode(password));
        user.setUserNickname(nickname);
        user.setUserEmail(blank(email) ? null : email);
        user.setUserPhone(blank(phone) ? null : phone);
        user.setUserRoleId(roleId);
        user.setUserDescription(description);
        user.setEnabled(1);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return user;
    }

    private User loadUser(String id) {
        return users.findById(id).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    private Exam visibleExam(String id) {
        Exam exam = exams.findById(id).orElseThrow(() -> new IllegalArgumentException("考试不存在"));
        if (!active(exam.getVisible())) {
            throw new IllegalArgumentException("考试已删除");
        }
        return exam;
    }

    private boolean examUses(Exam exam, String questionId) {
        return split(exam.getExamQuestionIds()).contains(questionId)
                || split(exam.getExamQuestionIdsRadio()).contains(questionId)
                || split(exam.getExamQuestionIdsCheck()).contains(questionId)
                || split(exam.getExamQuestionIdsJudge()).contains(questionId);
    }

    private Map<String, Integer> scoreMap(List<String> ids, int score) {
        Map<String, Integer> map = new LinkedHashMap<>();
        ids.forEach(id -> map.put(id, score));
        return map;
    }

    private static boolean sameSet(List<String> expected, List<String> chosen) {
        return new HashSet<>(expected).equals(new HashSet<>(chosen.stream().filter(s -> !s.isBlank()).toList()));
    }

    private static List<String> split(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split("-")).filter(s -> !s.isBlank()).toList();
    }

    private static List<String> splitDollar(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split("\\$")).filter(s -> !s.isBlank()).toList();
    }

    private static List<String> clean(List<String> ids) {
        if (ids == null) {
            return List.of();
        }
        return ids.stream().filter(s -> s != null && !s.isBlank()).distinct().toList();
    }

    private static boolean active(Integer visible) {
        return visible == null || visible == 1;
    }

    private static boolean passed(int score, int total) {
        return total <= 0 ? score > 0 : score * 100 >= total * 60;
    }

    private static int levelOf(int score, int total) {
        int pct = total <= 0 ? 0 : score * 100 / total;
        if (pct >= 90) return 1;
        if (pct >= 75) return 2;
        if (pct >= 60) return 3;
        return 4;
    }

    private static String bucket(int pct) {
        if (pct >= 90) return "90-100";
        if (pct >= 80) return "80-89";
        if (pct >= 70) return "70-79";
        if (pct >= 60) return "60-69";
        return "0-59";
    }

    private static double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private static int nvl(Integer value) {
        return value == null ? 0 : value;
    }

    private static int parseInt(String raw, int fallback) {
        try {
            return Integer.parseInt(raw);
        } catch (Exception ex) {
            return fallback;
        }
    }

    private String cell(String[] row, int index) {
        return index < row.length && row[index] != null ? row[index].trim() : "";
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }

    private static String require(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private static void requireStaff(CurrentUser current) {
        if (current == null || !current.staff()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "需要教师或管理员权限");
        }
    }

    private static void requireAdmin(CurrentUser current) {
        if (current == null || !current.admin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "需要管理员权限");
        }
    }

    private void requireRole(int roleId) {
        if (roles.findById(roleId).isEmpty()) {
            throw new IllegalArgumentException("角色不存在");
        }
    }

    private static String id() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static byte[] stripBom(byte[] bytes) {
        if (bytes.length >= 3 && (bytes[0] & 0xFF) == 0xEF && (bytes[1] & 0xFF) == 0xBB && (bytes[2] & 0xFF) == 0xBF) {
            return Arrays.copyOfRange(bytes, 3, bytes.length);
        }
        return bytes;
    }

    private static String csvCell(String value) {
        String safe = value == null ? "" : value.replace("\"", "\"\"");
        return "\"" + safe + "\"";
    }
}
