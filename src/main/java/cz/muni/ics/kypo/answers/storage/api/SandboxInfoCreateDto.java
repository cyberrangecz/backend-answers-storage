package cz.muni.ics.kypo.answers.storage.api;

import cz.muni.ics.kypo.answers.storage.validation.ValidSandboxIdentifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@ApiModel(
        value = "SandboxInfoCreateDto"
)
@ValidSandboxIdentifier
public class SandboxInfoCreateDto {

    @ApiModelProperty(value = "The identifier of a sandbox for that we store the answers", example = "string")
    private String sandboxRefId;
    @ApiModelProperty(value = "The identifier of a sandbox allocation", example = "12")
    private Long allocationUnitId;
    @ApiModelProperty(value = "The identifier of the training instance in which the local sandbox is/has been used.", example = "abc-123")
    private String accessToken;
    @ApiModelProperty(value = "The identifier of the user who possess the local sandbox.", example = "12")
    private Long userId;
    @Valid
    @ApiModelProperty(value = "The answers for given sandbox")
    private List<SandboxAnswersCreateDto> sandboxAnswers = new ArrayList<>();

    public String getSandboxRefId() {
        return sandboxRefId;
    }

    public void setSandboxRefId(String sandboxRefId) {
        this.sandboxRefId = sandboxRefId;
    }

    public Long getAllocationUnitId() {
        return allocationUnitId;
    }

    public void setAllocationUnitId(Long allocationUnitId) {
        this.allocationUnitId = allocationUnitId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<SandboxAnswersCreateDto> getSandboxAnswers() {
        return sandboxAnswers;
    }

    public void setSandboxAnswers(List<SandboxAnswersCreateDto> sandboxAnswers) {
        this.sandboxAnswers = sandboxAnswers;
    }

    @Override
    public String toString() {
        return "CloudSandboxInfoCreateDto{" +
                "sandboxRefId=" + sandboxRefId +
                ", allocationUnitId=" + allocationUnitId +
                ", accessToken='" + accessToken + '\'' +
                ", userId=" + userId +
                '}';
    }
}
