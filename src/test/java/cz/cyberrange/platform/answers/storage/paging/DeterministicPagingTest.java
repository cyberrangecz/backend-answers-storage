package cz.cyberrange.platform.answers.storage.paging;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.BooleanBuilder;
import cz.cyberrange.platform.answers.storage.api.SandboxAnswersDto;
import cz.cyberrange.platform.answers.storage.api.SandboxInfoDto;
import cz.cyberrange.platform.answers.storage.config.PersistenceConfigTest;
import cz.cyberrange.platform.answers.storage.config.TestcontainersConfiguration;
import cz.cyberrange.platform.answers.storage.data.entities.SandboxAnswer;
import cz.cyberrange.platform.answers.storage.data.entities.SandboxInfo;
import cz.cyberrange.platform.answers.storage.data.repositories.SandboxAnswerRepository;
import cz.cyberrange.platform.answers.storage.data.repositories.SandboxInfoRepository;
import cz.cyberrange.platform.answers.storage.service.SandboxAnswersService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Checks that the sandbox page is a total order over a real PostgreSQL schema, including the
 * in-memory paging Hibernate applies when the answers are fetched together with the sandboxes.
 */
@Import({PersistenceConfigTest.class, TestcontainersConfiguration.class})
@SpringBootTest
class DeterministicPagingTest {

  private static final int SANDBOX_COUNT = 11;
  private static final int PAGE_SIZE = 3;
  private static final int ANSWERS_PER_SANDBOX = 3;

  @Autowired private SandboxAnswersService sandboxAnswersService;
  @Autowired private SandboxInfoRepository sandboxInfoRepository;
  @Autowired private SandboxAnswerRepository sandboxAnswerRepository;

  private List<String> sandboxReferenceIdsInIdOrder;

  @BeforeEach
  void storeSandboxesWithTiedUserIds() {
    sandboxReferenceIdsInIdOrder =
        IntStream.range(0, SANDBOX_COUNT)
            .mapToObj(index -> "sandbox-%02d".formatted(index))
            .toList();
    for (int index = 0; index < SANDBOX_COUNT; index++) {
      SandboxInfo sandboxInfo = new SandboxInfo();
      sandboxInfo.setSandboxRefId(sandboxReferenceIdsInIdOrder.get(index));
      sandboxInfo.setUserId(isOdd(index) ? 2L : 1L);
      sandboxInfoRepository.saveAndFlush(sandboxInfo);
      for (int answerIndex = 0; answerIndex < ANSWERS_PER_SANDBOX; answerIndex++) {
        storeAnswer(sandboxInfo, "variable-%d".formatted(ANSWERS_PER_SANDBOX - answerIndex));
      }
    }
  }

  @AfterEach
  void removeSandboxes() {
    sandboxInfoRepository.deleteAll();
  }

  @Test
  @DisplayName("getAllSandboxesAnswers_noSort_ordersByIdAscending")
  void getAllSandboxesAnswers_noSort_ordersByIdAscending() {
    assertThat(collectAllPages(Sort.unsorted())).isEqualTo(sandboxReferenceIdsInIdOrder);
  }

  @Test
  @DisplayName("getAllSandboxesAnswers_sortWithTies_appendsIdAscendingAsLastKey")
  void getAllSandboxesAnswers_sortWithTies_appendsIdAscendingAsLastKey() {
    List<String> expected =
        IntStream.concat(
                IntStream.range(0, SANDBOX_COUNT).filter(DeterministicPagingTest::isOdd),
                IntStream.range(0, SANDBOX_COUNT).filter(index -> !isOdd(index)))
            .mapToObj(sandboxReferenceIdsInIdOrder::get)
            .toList();

    assertThat(collectAllPages(Sort.by(Sort.Direction.DESC, "userId"))).isEqualTo(expected);
  }

  @Test
  @DisplayName("getSandboxAnswers_answers_areListedInIdOrder")
  void getSandboxAnswers_answers_areListedInIdOrder() {
    SandboxInfoDto sandboxInfoDto =
        sandboxAnswersService.getSandboxAnswers(sandboxReferenceIdsInIdOrder.getFirst());

    assertThat(sandboxInfoDto.getSandboxAnswers())
        .extracting(SandboxAnswersDto::getAnswerVariableName)
        .containsExactly("variable-3", "variable-2", "variable-1");
  }

  private List<String> collectAllPages(Sort sort) {
    List<String> collected = new ArrayList<>();
    int pageCount = (SANDBOX_COUNT + PAGE_SIZE - 1) / PAGE_SIZE;
    for (int pageNumber = 0; pageNumber < pageCount; pageNumber++) {
      Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, sort);
      sandboxAnswersService
          .getAllSandboxesAnswers(new BooleanBuilder(), pageable)
          .getContent()
          .forEach(sandboxInfoDto -> collected.add(sandboxInfoDto.getSandboxRefId()));
    }
    return collected;
  }

  private static boolean isOdd(int index) {
    return index % 2 == 1;
  }

  private void storeAnswer(SandboxInfo sandboxInfo, String variableName) {
    SandboxAnswer sandboxAnswer = new SandboxAnswer();
    sandboxAnswer.setAnswerVariableName(variableName);
    sandboxAnswer.setAnswerContent("content");
    sandboxAnswer.setSandboxInfo(sandboxInfo);
    sandboxAnswerRepository.saveAndFlush(sandboxAnswer);
  }
}
