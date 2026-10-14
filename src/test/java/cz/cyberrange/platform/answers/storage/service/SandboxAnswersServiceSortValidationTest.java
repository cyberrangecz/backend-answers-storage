package cz.cyberrange.platform.answers.storage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.querydsl.core.BooleanBuilder;
import cz.cyberrange.platform.answers.storage.data.repositories.SandboxAnswerRepository;
import cz.cyberrange.platform.answers.storage.data.repositories.SandboxInfoRepository;
import cz.cyberrange.platform.answers.storage.mappers.SandboxInfoMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Pins which sorts the sandbox page accepts and how a refused sort is answered. */
class SandboxAnswersServiceSortValidationTest {

  private final SandboxInfoRepository sandboxInfoRepository = mock(SandboxInfoRepository.class);
  private final SandboxAnswersService sandboxAnswersService =
      new SandboxAnswersService(
          sandboxInfoRepository,
          mock(SandboxAnswerRepository.class),
          mock(SandboxInfoMapper.class));

  static List<Sort> sortableSorts() {
    return List.of(
        Sort.unsorted(),
        Sort.by("id"),
        Sort.by(Sort.Direction.DESC, "userId"),
        Sort.by("sandboxRefId", "accessToken", "allocationId"),
        Sort.by(Sort.Order.asc("userId").ignoreCase()));
  }

  static List<Sort> unsortableSorts() {
    return List.of(
        Sort.by("sandboxAnswers"),
        Sort.by(Sort.Direction.DESC, "unknown"),
        Sort.by("userId", "unknown"),
        Sort.by("userId", "sandboxAnswers"),
        Sort.by(" unknown"),
        Sort.by("userId "),
        Sort.by(" desc"),
        Sort.by("?"));
  }

  @ParameterizedTest
  @MethodSource("sortableSorts")
  @DisplayName("getAllSandboxesAnswers_sortableSort_queriesWithIdAppended")
  void getAllSandboxesAnswers_sortableSort_queriesWithIdAppended(Sort sort) {
    assertThatCode(
            () -> sandboxAnswersService.getAllSandboxesAnswers(new BooleanBuilder(), page(sort)))
        .doesNotThrowAnyException();

    ArgumentCaptor<Pageable> queried = ArgumentCaptor.forClass(Pageable.class);
    verify(sandboxInfoRepository).findAll(any(BooleanBuilder.class), queried.capture());
    assertThat(
            queried.getValue().getSort().stream().map(Sort.Order::getProperty).toList().getLast())
        .isEqualTo("id");
  }

  @ParameterizedTest
  @MethodSource("unsortableSorts")
  @DisplayName("getAllSandboxesAnswers_unsortableSort_answersBadRequestProblemDetail")
  void getAllSandboxesAnswers_unsortableSort_answersBadRequestProblemDetail(Sort sort) {
    assertThatThrownBy(
            () -> sandboxAnswersService.getAllSandboxesAnswers(new BooleanBuilder(), page(sort)))
        .isInstanceOfSatisfying(
            ResponseStatusException.class,
            exception -> {
              assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
              assertThat(exception.getBody().getDetail()).contains("Sortable properties");
            });
    verifyNoInteractions(sandboxInfoRepository);
  }

  private static Pageable page(Sort sort) {
    return PageRequest.of(0, 10, sort);
  }
}
