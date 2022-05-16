package cz.muni.ics.kypo.answers.storage.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@ApiModel(
        value = "SandboxInfoCreateDto"
)
public class SandboxInfoCreateDto {

    @ApiModelProperty(value = "The identifier of a sandbox for that we store the answers", example = "12")
    private Long sandboxRefId;
    @Valid
    @ApiModelProperty(value = "The answers for given sandbox")
    private List<SandboxAnswersCreateDto> sandboxAnswers = new ArrayList<>();

    public Long getSandboxRefId() {
        return sandboxRefId;
    }

    public void setSandboxRefId(Long sandboxRefId) {
        this.sandboxRefId = sandboxRefId;
    }

    public List<SandboxAnswersCreateDto> getSandboxAnswers() {
        return sandboxAnswers;
    }

    public void setSandboxAnswers(List<SandboxAnswersCreateDto> sandboxAnswers) {
        this.sandboxAnswers = sandboxAnswers;
    }

    @Override
    public String toString() {
        return "SandboxInfoCreateDto{" +
                "sandboxRefId=" + sandboxRefId +
                ", sandboxAnswers=" + sandboxAnswers +
                '}';
    }
}
