package cz.cyberrange.platform.answers.storage.rest;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cz.cyberrange.platform.answers.storage.exceptions.EntityNotFoundException;
import cz.cyberrange.platform.answers.storage.service.SandboxAnswersService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Pins the shape of the error bodies written for failed requests. */
@WebMvcTest(SandboxAnswersRestController.class)
class ErrorBodyTest {

  private static final String SANDBOX_REF_ID = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
  private static final String ISO_TIMESTAMP = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z";

  @Autowired private MockMvc mockMvc;
  @MockitoBean private SandboxAnswersService sandboxAnswersService;

  @Test
  @DisplayName("getSandboxAnswers_failureWithoutMessage_omitsErrorsField")
  void getSandboxAnswers_failureWithoutMessage_omitsErrorsField() throws Exception {
    when(sandboxAnswersService.getSandboxAnswers(SANDBOX_REF_ID))
        .thenThrow(new EntityNotFoundException());

    mockMvc
        .perform(get("/sandboxes/{sandboxRefId}/answers", SANDBOX_REF_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors").doesNotExist())
        .andExpect(jsonPath("$.timestamp").value(matchesPattern(ISO_TIMESTAMP)));
  }

  @Test
  @DisplayName("getSandboxAnswers_failureWithMessage_listsThatMessageOnly")
  void getSandboxAnswers_failureWithMessage_listsThatMessageOnly() throws Exception {
    when(sandboxAnswersService.getSandboxAnswers(SANDBOX_REF_ID))
        .thenThrow(new EntityNotFoundException(new RuntimeException("missing sandbox")));

    mockMvc
        .perform(get("/sandboxes/{sandboxRefId}/answers", SANDBOX_REF_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errors.length()").value(1))
        .andExpect(jsonPath("$.errors[0]").isNotEmpty());
  }
}
