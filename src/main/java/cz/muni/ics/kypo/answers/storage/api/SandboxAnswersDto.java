package cz.muni.ics.kypo.answers.storage.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "SandboxAnswersDto"
)
public class SandboxAnswersDto {

    @ApiModelProperty(value = "The content of the KYPO answer in particular (phase/level)", example = "nmap 192.168.0.1")
    private String answerContent;
    @ApiModelProperty(value = "The variable name of the KYPO answer", example = "sandbox-1-2-answer")
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
