package cz.cyberrange.platform.answers.storage.api;

import cz.cyberrange.platform.answers.storage.validation.ValidSandboxIdentifier;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Sandbox and answer creation input submitted by the caller. It must carry either sandboxRefId
 * alone, or accessToken together with userId. The allocation unit id is not stored with the
 * sandbox.
 */
@Schema(name = "SandboxInfoCreateDto", description = "A sandbox and the answers to store for it.")
@ValidSandboxIdentifier
public class SandboxInfoCreateDto {

    @Schema(description = "Identifies a cloud sandbox; leave it out when sending accessToken and userId.", format = "uuid", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String sandboxRefId;
    @Schema(description = "Not kept with the stored sandbox.", example = "12")
    private Long allocationUnitId;
    @Schema(description = "Identifies a local sandbox together with userId; leave it out when sending sandboxRefId.", example = "abc-123")
    private String accessToken;
    @Schema(description = "Identifies a local sandbox together with accessToken; leave it out when sending sandboxRefId.", example = "12")
    private Long userId;
    @Valid
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
