package cz.muni.ics.kypo.answers.storage.data.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sandbox_answer")
public class SandboxAnswers implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sandbox_answer_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "answer_content", nullable = false)
    private String answerContent;
    @Column(name = "answer_identifier", nullable = false)
    private String answerIdentifier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sandbox_info_id")
    private SandboxInfo sandboxInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public SandboxInfo getSandboxInfo() {
        return sandboxInfo;
    }

    public void setSandboxInfo(SandboxInfo sandboxInfo) {
        this.sandboxInfo = sandboxInfo;
    }

    @Override
    public String toString() {
        return "SandboxAnswers{" +
                "id=" + id +
                ", answerContent='" + answerContent + '\'' +
                ", answerIdentifier='" + answerIdentifier + '\'' +
                '}';
    }
}