package cz.muni.ics.kypo.answers.storage.api;

public class SandboxInfoDto {

    private Long sandboxId;
    private SandboxAnswers sandboxAnswers;

    public Long getSandboxId() {
        return sandboxId;
    }

    public void setSandboxId(Long sandboxId) {
        this.sandboxId = sandboxId;
    }

    public SandboxAnswers getSandboxAnswers() {
        return sandboxAnswers;
    }

    public void setSandboxAnswers(SandboxAnswers sandboxAnswers) {
        this.sandboxAnswers = sandboxAnswers;
    }
    
}
