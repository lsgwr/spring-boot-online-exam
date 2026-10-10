package lsgwr.exam;

import com.jayway.jsonpath.JsonPath;
import lsgwr.exam.dto.Payloads.QuestionWrite;
import lsgwr.exam.dto.Payloads.OptionWrite;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExamFlowTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper json;

    @Test
    void loginTakeExamManageAndImport() throws Exception {
        String admin = token("admin", "admin123");
        mvc.perform(get("/api/analytics").header("Access-Token", admin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.role").value("admin"));

        String subjectName = "学科" + System.nanoTime();
        String subject = mvc.perform(post("/api/subjects").header("Access-Token", admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of("name", subjectName, "description", "新栈测试"))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        int categoryId = JsonPath.read(subject, "$.data.id");

        QuestionWrite question = new QuestionWrite("<p>地球是<strong>圆</strong>的吗？</p>", "<p>解析</p>", 5, 3, 3, categoryId,
                "data:image/png;base64,aGVsbG8=",
                List.of(new OptionWrite("<p>正确</p>", true), new OptionWrite("<p>错误</p>", false)));
        MvcResult created = mvc.perform(post("/api/questions").header("Access-Token", admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(question)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.stem").exists())
                .andReturn();
        String questionId = JsonPath.read(created.getResponse().getContentAsString(StandardCharsets.UTF_8), "$.data.id");
        String correctId = JsonPath.read(created.getResponse().getContentAsString(StandardCharsets.UTF_8), "$.data.options[0].id");

        String examBody = json.writeValueAsString(Map.of(
                "name", "重构验收考试",
                "description", "端到端",
                "avatar", "data:image/png;base64,aGVsbG8=",
                "timeLimit", 20,
                "radioScore", 0,
                "checkScore", 0,
                "judgeScore", 5,
                "radioIds", List.of(),
                "checkIds", List.of(),
                "judgeIds", List.of(questionId)
        ));
        MvcResult exam = mvc.perform(post("/api/exams").header("Access-Token", admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(examBody))
                .andExpect(status().isOk())
                .andReturn();
        String examId = JsonPath.read(exam.getResponse().getContentAsString(StandardCharsets.UTF_8), "$.data.id");

        mvc.perform(delete("/api/questions/" + questionId).header("Access-Token", admin))
                .andExpect(status().isConflict());

        String student = token("student", "admin123");
        mvc.perform(get("/api/exams/" + examId + "/paper").header("Access-Token", student))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.questions[0].options[0].correct").value(false));

        mvc.perform(post("/api/exams/" + examId + "/submit").header("Access-Token", student)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of("answers", Map.of(questionId, List.of(correctId)), "timeCost", 12))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.summary.score").value(5));

        String csv = "type,level,category,score,stem,optionA,optionB,optionC,optionD,answer\n" +
                "single,易," + subjectName + ",5,1+1=?,1,2,3,4,B\n";
        mvc.perform(multipart("/api/questions/import").file("file", csv.getBytes(StandardCharsets.UTF_8))
                        .header("Access-Token", admin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.created").value(1));

        mvc.perform(post("/api/users").header("Access-Token", admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of(
                                "username", "clerk" + System.nanoTime(),
                                "password", "secret123",
                                "nickname", "教务",
                                "roleId", 2))))
                .andExpect(status().isOk());

        mvc.perform(get("/api/grades").header("Access-Token", admin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        mvc.perform(delete("/api/exams/" + examId).header("Access-Token", admin)).andExpect(status().isOk());
        mvc.perform(delete("/api/questions/" + questionId).header("Access-Token", admin)).andExpect(status().isOk());
        mvc.perform(delete("/api/subjects/" + categoryId).header("Access-Token", admin)).andExpect(status().isConflict());
    }

    private String token(String username, String password) throws Exception {
        MvcResult result = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of("username", username, "password", password))))
                .andExpect(status().isOk())
                .andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(StandardCharsets.UTF_8), "$.data.token");
    }
}
