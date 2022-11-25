package cz.muni.ics.kypo.answers.storage.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(
        value = "SandboxInfoDto"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SandboxInfoDto {

    @ApiModelProperty(value = "The identifier of a sandbox for that we store the answers", example = "12")
    private String sandboxRefId;
    @ApiModelProperty(value = "The identifier of the training instance in which the local sandbox is/has been used.", example = "12")
    private String accessToken;
    @ApiModelProperty(value = "The identifier of the user who possess the local sandbox.", example = "12")
    private Long userId;
    @ApiModelProperty(value = "The answers for given sandbox")
    private List<SandboxAnswersDto> sandboxAnswers = new ArrayList<>();

    public SandboxInfoDto() {
    }

    public SandboxInfoDto(String sandboxRefId, List<SandboxAnswersDto> sandboxAnswers) {
        this.sandboxRefId = sandboxRefId;
        this.sandboxAnswers = sandboxAnswers;
    }

    public SandboxInfoDto(String accessToken, Long userId, List<SandboxAnswersDto> sandboxAnswers) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.sandboxAnswers = sandboxAnswers;
    }

    public String getSandboxRefId() {
        return sandboxRefId;
    }

    public void setSandboxRefId(String sandboxRefId) {
        this.sandboxRefId = sandboxRefId;
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
                ", accessToken='" + accessToken + '\'' +
                ", userId=" + userId +
                '}';
    }
}
