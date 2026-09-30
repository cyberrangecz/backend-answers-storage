package cz.cyberrange.platform.answers.storage.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * API representation of a stored sandbox and its answers. A field left null is omitted from the
 * serialized JSON rather than written as null.
 */
@Schema(name = "SandboxInfoDto", description = "A stored sandbox with its answers; identifiers left unset are omitted.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SandboxInfoDto {

    @Schema(format = "uuid", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private String sandboxRefId;
    @Schema(example = "12")
    private Long allocationId;
    @Schema(example = "abc-123")
    private String accessToken;
    @Schema(example = "12")
    private Long userId;
    private List<SandboxAnswersDto> sandboxAnswers = new ArrayList<>();

    public SandboxInfoDto() {
    }

    /**
     * Builds a DTO identified by sandboxRefId, leaving allocationId, accessToken and userId
     * unset.
     */
    public SandboxInfoDto(String sandboxRefId, List<SandboxAnswersDto> sandboxAnswers) {
        this.sandboxRefId = sandboxRefId;
        this.sandboxAnswers = sandboxAnswers;
    }

    /**
     * Builds a DTO identified by accessToken and userId, leaving sandboxRefId and allocationId
     * unset.
     */
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

    public Long getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
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
                ", allocationId" + allocationId +
                ", accessToken='" + accessToken + '\'' +
                ", userId=" + userId +
                '}';
    }
}
