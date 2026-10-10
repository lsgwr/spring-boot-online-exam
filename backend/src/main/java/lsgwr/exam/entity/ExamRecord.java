package lsgwr.exam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class ExamRecord {
    @Id
    private String examRecordId;
    private String examId;
    @Column(length = 4096)
    private String answerOptionIds;
    private String examJoinerId;
    private Date examJoinDate;
    private Integer examTimeCost;
    private Integer examJoinScore;
    private Integer examResultLevel;
}
