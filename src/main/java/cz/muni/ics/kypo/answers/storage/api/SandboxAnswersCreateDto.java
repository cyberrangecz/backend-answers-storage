package cz.muni.ics.kypo.answers.storage.api;

import io.swagger.annotations.ApiModelProperty;

public class SandboxAnswersCreateDto {
    @ApiModelProperty(value = "The content of the KYPO answer in particular (phase/level)", example = "nmap 192.168.0.1")
    private String answerContent;
    @ApiModelProperty(value = "The identifier of the KYPO answer", example = "sandbox-1-2-answer")
    private String answerIdentifier;

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getAnswerIdentifier() {
        return answerIdentifier;
    }

    public void setAnswerIdentifier(String answerIdentifier) {
        this.answerIdentifier = answerIdentifier;
    }

    @Override
    public String toString() {
        return "SandboxAnswersCreateDto{" +
                "answerContent='" + answerContent + '\'' +
                ", answerIdentifier='" + answerIdentifier + '\'' +
                '}';
    }
}
