package lsgwr.exam.repo;

import lsgwr.exam.entity.ExamRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRecordRepository extends JpaRepository<ExamRecord, String> {
    List<ExamRecord> findByExamJoinerIdOrderByExamJoinDateDesc(String userId);

    List<ExamRecord> findAllByOrderByExamJoinDateDesc();

    List<ExamRecord> findByExamIdOrderByExamJoinDateDesc(String examId);
}
