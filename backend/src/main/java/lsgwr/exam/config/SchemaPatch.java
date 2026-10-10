package lsgwr.exam.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SchemaPatch implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(SchemaPatch.class);
    private final JdbcTemplate jdbc;

    public SchemaPatch(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) {
        ignore("ALTER TABLE question ADD COLUMN visible TINYINT NOT NULL DEFAULT 1");
        ignore("ALTER TABLE exam ADD COLUMN visible TINYINT NOT NULL DEFAULT 1");
        ignore("ALTER TABLE user ADD COLUMN enabled TINYINT NOT NULL DEFAULT 1");
        ignore("ALTER TABLE question ADD COLUMN question_image LONGTEXT NULL");
        ignore("ALTER TABLE question_option MODIFY question_option_content LONGTEXT NOT NULL");
        ignore("ALTER TABLE question_option MODIFY question_option_description LONGTEXT NULL");
        ignore("ALTER TABLE user MODIFY user_avatar LONGTEXT NULL");
        ignore("ALTER TABLE user MODIFY user_password VARCHAR(128) NOT NULL");
        ignore("ALTER TABLE exam MODIFY exam_description LONGTEXT NULL");
        ignore("ALTER TABLE exam MODIFY exam_question_ids VARCHAR(4096) NULL");
        ignore("ALTER TABLE exam MODIFY exam_question_ids_radio VARCHAR(2048) NULL");
        ignore("ALTER TABLE exam MODIFY exam_question_ids_check VARCHAR(2048) NULL");
        ignore("ALTER TABLE exam MODIFY exam_question_ids_judge VARCHAR(2048) NULL");
        ignore("ALTER TABLE question MODIFY question_option_ids VARCHAR(2048) NULL");
        ignore("ALTER TABLE question MODIFY question_answer_option_ids VARCHAR(2048) NULL");
    }

    private void ignore(String sql) {
        try {
            jdbc.execute(sql);
        } catch (RuntimeException ex) {
            log.debug("schema patch skipped: {} ({})", sql, ex.getMessage());
        }
    }
}
