package cz.cyberrange.platform.answers.storage.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cz.cyberrange.platform.answers.storage.config.PersistenceConfigTest;
import cz.cyberrange.platform.answers.storage.config.TestcontainersConfiguration;
import cz.cyberrange.platform.answers.storage.data.entities.SandboxAnswer;
import cz.cyberrange.platform.answers.storage.data.entities.SandboxInfo;
import cz.cyberrange.platform.answers.storage.data.repositories.SandboxAnswerRepository;
import cz.cyberrange.platform.answers.storage.data.repositories.SandboxInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

/** Checks the answer content column limit set by the Flyway schema on PostgreSQL. */
@Import({PersistenceConfigTest.class, TestcontainersConfiguration.class})
@SpringBootTest
@Transactional
class SandboxAnswerContentLimitTest {

  private static final int SCHEMA_ANSWER_CONTENT_LIMIT = 2048;

  @Autowired private SandboxInfoRepository sandboxInfoRepository;
  @Autowired private SandboxAnswerRepository sandboxAnswerRepository;

  @Test
  @DisplayName("saveAnswer_contentAtSchemaLimit_isStoredWhole")
  void saveAnswer_contentAtSchemaLimit_isStoredWhole() {
    SandboxAnswer saved = save("x".repeat(SCHEMA_ANSWER_CONTENT_LIMIT));

    assertThat(sandboxAnswerRepository.findById(saved.getId()))
        .get()
        .extracting(SandboxAnswer::getAnswerContent)
        .asString()
        .hasSize(SCHEMA_ANSWER_CONTENT_LIMIT);
  }

  @Test
  @DisplayName("saveAnswer_contentOverSchemaLimit_isRejectedByDatabase")
  void saveAnswer_contentOverSchemaLimit_isRejectedByDatabase() {
    assertThatThrownBy(() -> save("x".repeat(SCHEMA_ANSWER_CONTENT_LIMIT + 1)))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  private SandboxAnswer save(String answerContent) {
    SandboxInfo sandboxInfo = new SandboxInfo();
    sandboxInfo.setSandboxRefId("3fa85f64-5717-4562-b3fc-2c963f66afa6");
    sandboxInfoRepository.saveAndFlush(sandboxInfo);

    SandboxAnswer sandboxAnswer = new SandboxAnswer();
    sandboxAnswer.setAnswerVariableName("flag");
    sandboxAnswer.setAnswerContent(answerContent);
    sandboxAnswer.setSandboxInfo(sandboxInfo);
    return sandboxAnswerRepository.saveAndFlush(sandboxAnswer);
  }
}
