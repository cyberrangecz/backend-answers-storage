package cz.cyberrange.platform.answers.storage.api;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * API representation of one stored answer value, returned to the caller.
 */
@Schema(name = "SandboxAnswersDto", description = "One answer stored for a sandbox.")
public class SandboxAnswersDto {

    @Schema(example = "nmap 192.168.0.1")
    private String answerContent;
    @Schema(description = "Key this answer is read back by.", example = "sandbox-1-2-answer")
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
        return "SandboxAnswersDto{" +
                "answerContent='" + answerContent + '\'' +
                ", answerVariableName='" + answerVariableName + '\'' +
                '}';
    }
}
