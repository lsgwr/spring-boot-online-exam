package lsgwr.exam.repo;

import lsgwr.exam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, String> {
    long countByQuestionCategoryIdAndVisible(Integer categoryId, Integer visible);
}
