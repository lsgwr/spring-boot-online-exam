package lsgwr.exam.dto;

import java.util.List;
import java.util.Map;

public final class Payloads {
    private Payloads() {
    }

    public record LoginRequest(String username, String password) {
    }

    public record RegisterRequest(String username, String password, String nickname, String email, String phone) {
    }

    public record UserWrite(String username, String password, String nickname, String email, String phone, Integer roleId, Integer enabled, String description) {
    }

    public record OptionWrite(String content, Boolean correct) {
    }

    public record QuestionWrite(String stem, String analysis, Integer score, Integer typeId, Integer levelId,
                                Integer categoryId, String image, List<OptionWrite> options) {
    }

    public record SubjectWrite(String name, String description) {
    }

    public record ExamWrite(String name, String description, String avatar, Integer timeLimit,
                            Integer radioScore, Integer checkScore, Integer judgeScore,
                            List<String> radioIds, List<String> checkIds, List<String> judgeIds) {
    }

    public record SubmitRequest(Map<String, List<String>> answers, Integer timeCost) {
    }

    public record OptionView(String id, String content, boolean correct) {
    }

    public record QuestionView(String id, String stem, String analysis, Integer score, Integer typeId, String typeName,
                               Integer levelId, String levelName, Integer categoryId, String categoryName,
                               String image, Integer visible, List<OptionView> options) {
    }

    public record ExamCard(String id, String name, String description, String avatar, Integer score, Integer timeLimit,
                           int radioCount, int checkCount, int judgeCount, String creator) {
    }

    public record PaperQuestion(String id, String stem, String image, Integer typeId, String typeName, int score, List<OptionView> options) {
    }

    public record Paper(String id, String name, String description, String avatar, Integer timeLimit, Integer totalScore,
                        List<PaperQuestion> questions) {
    }

    public record ExamEditor(String id, String name, String description, String avatar, Integer timeLimit,
                             Integer radioScore, Integer checkScore, Integer judgeScore, Integer totalScore,
                             List<String> radioIds, List<String> checkIds, List<String> judgeIds) {
    }

    public record UserView(String id, String username, String nickname, String email, String phone, Integer roleId,
                           String roleName, String avatar, String description, Integer enabled) {
    }

    public record AuthView(String token, UserView user) {
    }

    public record SubjectView(Integer id, String name, String description, long questionCount) {
    }

    public record Named(Integer id, String name, String description) {
    }

    public record Meta(List<Named> types, List<Named> levels, List<SubjectView> subjects) {
    }

    public record RecordRow(String id, String examId, String examName, String studentId, String studentName,
                            Integer score, Integer total, Integer timeCost, String joinDate) {
    }

    public record AnswerLine(String questionId, String stem, String image, boolean correct, int awarded,
                             List<OptionView> options, List<String> chosen) {
    }

    public record RecordDetail(RecordRow summary, List<AnswerLine> lines) {
    }

    public record Bucket(String label, long count) {
    }

    public record ExamStat(String examId, String name, long attempts, double average, int max, int min, double passRate) {
    }

    public record CategoryStat(String name, long count) {
    }

    public record Overview(long users, long exams, long questions, long attempts, double average) {
    }

    public record Analytics(String role, Overview overview, List<Bucket> buckets, List<ExamStat> examStats,
                            List<CategoryStat> categories, List<RecordRow> recent) {
    }

    public record ImportResult(int created, int failed, List<String> errors) {
    }
}
