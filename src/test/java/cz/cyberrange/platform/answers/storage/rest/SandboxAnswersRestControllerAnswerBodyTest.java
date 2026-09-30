package cz.cyberrange.platform.answers.storage.rest;

import cz.cyberrange.platform.answers.storage.service.SandboxAnswersService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pins the wire format of the single-answer endpoints, whose bodies are plain strings
 * served as text/plain. The answer text is written verbatim,
 * without JSON quoting or escaping.
 */
@WebMvcTest(SandboxAnswersRestController.class)
class SandboxAnswersRestControllerAnswerBodyTest {

    private static final String SANDBOX_REF_ID = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
    private static final String ACCESS_TOKEN = "token-1234";
    private static final long USER_ID = 42L;
    private static final String VARIABLE_NAME = "flag";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SandboxAnswersService sandboxAnswersService;

    @ParameterizedTest
    @ValueSource(strings = {"secret42", "he said \"hi\" \\ done"})
    void cloudSandboxAnswerIsWrittenVerbatim(String answer) throws Exception {
        when(sandboxAnswersService.getAnswerBySandboxAndVariableName(SANDBOX_REF_ID, VARIABLE_NAME)).thenReturn(answer);

        mockMvc.perform(get("/sandboxes/{sandboxRefId}/answers/{answerVariableName}", SANDBOX_REF_ID, VARIABLE_NAME)
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/plain;charset=UTF-8"))
                .andExpect(content().string(answer));
    }

    @ParameterizedTest
    @ValueSource(strings = {"secret42", "he said \"hi\" \\ done"})
    void localSandboxAnswerIsWrittenVerbatim(String answer) throws Exception {
        when(sandboxAnswersService.getAnswerBySandboxAndVariableName(ACCESS_TOKEN, USER_ID, VARIABLE_NAME)).thenReturn(answer);

        mockMvc.perform(get("/sandboxes/access-tokens/{accessToken}/users/{userId}/answers/{answerVariableName}",
                        ACCESS_TOKEN, USER_ID, VARIABLE_NAME)
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/plain;charset=UTF-8"))
                .andExpect(content().string(answer));
    }
}
