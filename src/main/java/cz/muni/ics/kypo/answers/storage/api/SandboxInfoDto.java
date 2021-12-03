package cz.muni.ics.kypo.answers.storage.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(
        value = "SandboxInfoDto"
)
public class SandboxInfoDto {

    @ApiModelProperty(value = "The identifier of a sandbox for that we store the answers", example = "12")
    private Long sandboxRefId;
    @ApiModelProperty(value = "The answers for given sandbox")
    private List<SandboxAnswersDto> sandboxAnswers = new ArrayList<>();

    public SandboxInfoDto() {
    }

    public SandboxInfoDto(Long sandboxRefId, List<SandboxAnswersDto> sandboxAnswers) {
        this.sandboxRefId = sandboxRefId;
        this.sandboxAnswers = sandboxAnswers;
    }

    public Long getSandboxRefId() {
        return sandboxRefId;
    }

    public void setSandboxRefId(Long sandboxRefId) {
        this.sandboxRefId = sandboxRefId;
    }

    public List<SandboxAnswersDto> getSandboxAnswers() {
        return sandboxAnswers;
    }

    public void setSandboxAnswers(List<SandboxAnswersDto> sandboxAnswers) {
        this.sandboxAnswers = sandboxAnswers;
    }

    @Override
    public String toString() {
        return "SandboxInfoDto{" +
                "sandboxRefId=" + sandboxRefId +
                ", sandboxAnswers=" + sandboxAnswers +
                '}';
    }
}
