package cz.cyberrange.platform.answers.storage.api;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SandboxAnswersCreateDto {

    @ApiModelProperty(value = "The content of the variant answer in particular (phase/level)", example = "nmap 192.168.0.1")
    @NotBlank(message = "{sandboxAnswers.answerContent.NotBlank.message}")
    @Size(max = 2048, message = "{sandboxAnswers.answerContent.Size.message}")
    private String answerContent;
    @ApiModelProperty(value = "The variable name of the variant answer", example = "sandbox-1-2-answer")
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
