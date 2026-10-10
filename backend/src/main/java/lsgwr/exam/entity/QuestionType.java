package lsgwr.exam.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class QuestionType {
    @Id
    private Integer questionTypeId;
    private String questionTypeName;
    private String questionTypeDescription;
}
