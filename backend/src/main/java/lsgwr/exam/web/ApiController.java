package lsgwr.exam.web;

import jakarta.servlet.http.HttpServletRequest;
import lsgwr.exam.api.ApiResponse;
import lsgwr.exam.config.AuthInterceptor;
import lsgwr.exam.dto.Payloads.*;
import lsgwr.exam.security.CurrentUser;
import lsgwr.exam.service.ExamPlatformService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final ExamPlatformService service;

    public ApiController(ExamPlatformService service) {
        this.service = service;
    }

    @PostMapping("/auth/login")
    public ApiResponse<AuthView> login(@RequestBody LoginRequest request) {
        return ApiResponse.ok("登录成功", service.login(request));
    }

    @PostMapping("/auth/register")
    public ApiResponse<AuthView> register(@RequestBody RegisterRequest request) {
        return ApiResponse.ok("注册成功", service.register(request));
    }

    @GetMapping("/auth/me")
    public ApiResponse<UserView> me(HttpServletRequest request) {
        return ApiResponse.ok(service.me(current(request)));
    }

    @GetMapping("/meta")
    public ApiResponse<Meta> meta() {
        return ApiResponse.ok(service.meta());
    }

    @GetMapping("/subjects")
    public ApiResponse<List<SubjectView>> subjects() {
        return ApiResponse.ok(service.subjects());
    }

    @PostMapping("/subjects")
    public ApiResponse<SubjectView> createSubject(HttpServletRequest request, @RequestBody SubjectWrite body) {
        return ApiResponse.ok(service.createSubject(current(request), body));
    }

    @PutMapping("/subjects/{id}")
    public ApiResponse<SubjectView> updateSubject(HttpServletRequest request, @PathVariable Integer id, @RequestBody SubjectWrite body) {
        return ApiResponse.ok(service.updateSubject(current(request), id, body));
    }

    @DeleteMapping("/subjects/{id}")
    public ApiResponse<Void> deleteSubject(HttpServletRequest request, @PathVariable Integer id) {
        service.deleteSubject(current(request), id);
        return ApiResponse.ok("已删除学科", null);
    }

    @GetMapping("/users")
    public ApiResponse<List<UserView>> users(HttpServletRequest request) {
        return ApiResponse.ok(service.listUsers(current(request)));
    }

    @PostMapping("/users")
    public ApiResponse<UserView> createUser(HttpServletRequest request, @RequestBody UserWrite body) {
        return ApiResponse.ok(service.createUser(current(request), body));
    }

    @PutMapping("/users/{id}")
    public ApiResponse<UserView> updateUser(HttpServletRequest request, @PathVariable String id, @RequestBody UserWrite body) {
        return ApiResponse.ok(service.updateUser(current(request), id, body));
    }

    @GetMapping("/questions")
    public ApiResponse<List<QuestionView>> questions(HttpServletRequest request, @RequestParam(defaultValue = "false") boolean hidden) {
        return ApiResponse.ok(service.questions(current(request), hidden));
    }

    @PostMapping("/questions")
    public ApiResponse<QuestionView> createQuestion(HttpServletRequest request, @RequestBody QuestionWrite body) {
        return ApiResponse.ok("题目已创建", service.createQuestion(current(request), body));
    }

    @PutMapping("/questions/{id}")
    public ApiResponse<QuestionView> updateQuestion(HttpServletRequest request, @PathVariable String id, @RequestBody QuestionWrite body) {
        return ApiResponse.ok("题目已更新", service.updateQuestion(current(request), id, body));
    }

    @DeleteMapping("/questions/{id}")
    public ApiResponse<Void> deleteQuestion(HttpServletRequest request, @PathVariable String id) {
        service.deleteQuestion(current(request), id);
        return ApiResponse.ok("题目已隐藏，关联数据仍保留", null);
    }

    @PostMapping("/questions/import")
    public ApiResponse<ImportResult> importQuestions(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception {
        return ApiResponse.ok(service.importQuestions(current(request), file.getOriginalFilename(), file.getBytes()));
    }

    @PostMapping("/media")
    public ApiResponse<String> media(@RequestParam("file") MultipartFile file) throws Exception {
        return ApiResponse.ok(service.imageDataUrl(file.getContentType(), file.getBytes()));
    }

    @GetMapping("/exams")
    public ApiResponse<List<ExamCard>> exams() {
        return ApiResponse.ok(service.examCards());
    }

    @GetMapping("/exams/editing")
    public ApiResponse<List<ExamEditor>> editing(HttpServletRequest request) {
        return ApiResponse.ok(service.editableExams(current(request)));
    }

    @GetMapping("/exams/{id}/paper")
    public ApiResponse<Paper> paper(@PathVariable String id) {
        return ApiResponse.ok(service.paper(id));
    }

    @GetMapping("/exams/{id}")
    public ApiResponse<ExamEditor> exam(HttpServletRequest request, @PathVariable String id) {
        return ApiResponse.ok(service.examEditor(current(request), id));
    }

    @PostMapping("/exams")
    public ApiResponse<ExamEditor> createExam(HttpServletRequest request, @RequestBody ExamWrite body) {
        return ApiResponse.ok("考试已创建", service.createExam(current(request), body));
    }

    @PutMapping("/exams/{id}")
    public ApiResponse<ExamEditor> updateExam(HttpServletRequest request, @PathVariable String id, @RequestBody ExamWrite body) {
        return ApiResponse.ok("考试已更新", service.updateExam(current(request), id, body));
    }

    @DeleteMapping("/exams/{id}")
    public ApiResponse<Void> deleteExam(HttpServletRequest request, @PathVariable String id) {
        service.deleteExam(current(request), id);
        return ApiResponse.ok("考试已隐藏，题目仍保留", null);
    }

    @PostMapping("/exams/{id}/submit")
    public ApiResponse<RecordDetail> submit(HttpServletRequest request, @PathVariable String id, @RequestBody SubmitRequest body) {
        return ApiResponse.ok("交卷成功", service.submit(current(request), id, body));
    }

    @GetMapping("/records")
    public ApiResponse<List<RecordRow>> records(HttpServletRequest request) {
        return ApiResponse.ok(service.myRecords(current(request)));
    }

    @GetMapping("/records/{id}")
    public ApiResponse<RecordDetail> record(HttpServletRequest request, @PathVariable String id) {
        return ApiResponse.ok(service.recordDetail(current(request), id));
    }

    @GetMapping("/grades")
    public ApiResponse<List<RecordRow>> grades(HttpServletRequest request, @RequestParam(required = false) String examId) {
        return ApiResponse.ok(service.grades(current(request), examId));
    }

    @GetMapping("/grades/export")
    public ResponseEntity<byte[]> export(HttpServletRequest request, @RequestParam(required = false) String examId) {
        byte[] body = service.gradesCsv(current(request), examId).getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=grades.csv")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(body);
    }

    @GetMapping("/analytics")
    public ApiResponse<Analytics> analytics(HttpServletRequest request) {
        return ApiResponse.ok(service.analytics(current(request)));
    }

    private static CurrentUser current(HttpServletRequest request) {
        return (CurrentUser) request.getAttribute(AuthInterceptor.ATTR);
    }
}
