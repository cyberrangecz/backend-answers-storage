package cz.cyberrange.platform.answers.storage.api;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * One answer value submitted by the caller when creating or updating a sandbox's answers.
 */
public class SandboxAnswersCreateDto {

    @Schema(example = "nmap 192.168.0.1")
    @NotBlank(message = "{sandboxAnswers.answerContent.NotBlank.message}")
    @Size(max = 2048, message = "{sandboxAnswers.answerContent.Size.message}")
    private String answerContent;
    @Schema(description = "Key this answer is later read back by.", example = "sandbox-1-2-answer")
    @NotBlank(message = "{sandboxAnswers.answerVariableName.NotBlank.message}")
    @Size(max = 255, message = "{sandboxAnswers.answerVariableName.Size.message}")
    private String answerVariableName;

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getAnswerVariableName() {
        return answerVariableName;
    }

    public void setAnswerVariableName(String answerVariableName) {
        this.answerVariableName = answerVariableName;
    }

    @Override
    public String toString() {
        return "SandboxAnswersCreateDto{" +
                "answerContent='" + answerContent + '\'' +
                ", answerVariableName='" + answerVariableName + '\'' +
                '}';
    }
}
