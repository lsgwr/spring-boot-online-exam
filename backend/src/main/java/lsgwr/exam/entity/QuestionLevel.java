package lsgwr.exam.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class QuestionLevel {
    @Id
    private Integer questionLevelId;
    private String questionLevelName;
    private String questionLevelDescription;
}
