package lsgwr.exam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Question {
    @Id
    private String questionId;
    @Column(columnDefinition = "LONGTEXT")
    private String questionName;
    private Integer questionScore;
    private String questionCreatorId;
    private Integer questionLevelId;
    private Integer questionTypeId;
    private Integer questionCategoryId;
    @Column(columnDefinition = "LONGTEXT")
    private String questionDescription;
    @Column(length = 2048)
    private String questionOptionIds;
    @Column(length = 2048)
    private String questionAnswerOptionIds;
    @Column(columnDefinition = "LONGTEXT")
    private String questionImage;
    private Integer visible;
    private Date createTime;
    private Date updateTime;
}
