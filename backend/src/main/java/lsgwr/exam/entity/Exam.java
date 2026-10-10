package lsgwr.exam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Exam {
    @Id
    private String examId;
    private String examName;
    @Column(columnDefinition = "LONGTEXT")
    private String examAvatar;
    @Column(columnDefinition = "LONGTEXT")
    private String examDescription;
    @Column(length = 4096)
    private String examQuestionIds;
    @Column(length = 2048)
    private String examQuestionIdsRadio;
    @Column(length = 2048)
    private String examQuestionIdsCheck;
    @Column(length = 2048)
    private String examQuestionIdsJudge;
    private Integer examScore;
    private Integer examScoreRadio;
    private Integer examScoreCheck;
    private Integer examScoreJudge;
    private String examCreatorId;
    private Integer examTimeLimit;
    private Date examStartDate;
    private Date examEndDate;
    private Integer visible;
    private Date createTime;
    private Date updateTime;
}
