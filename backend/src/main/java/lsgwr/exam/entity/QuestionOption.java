package lsgwr.exam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class QuestionOption {
    @Id
    private String questionOptionId;
    @Column(columnDefinition = "LONGTEXT")
    private String questionOptionContent;
    @Column(columnDefinition = "LONGTEXT")
    private String questionOptionDescription;
}
