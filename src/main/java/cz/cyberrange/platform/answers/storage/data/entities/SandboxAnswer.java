package cz.cyberrange.platform.answers.storage.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "sandbox_answer")
public class SandboxAnswer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sandbox_answer_generator")
    @SequenceGenerator(name = "sandbox_answer_generator", sequenceName = "sandbox_answer_id_seq")
    @Column(name = "sandbox_answer_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "answer_content", nullable = false)
    private String answerContent;
    @Column(name = "answer_variable_name", nullable = false)
    private String answerVariableName;
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

    public String getAnswerVariableName() {
        return answerVariableName;
    }

    public void setAnswerVariableName(String answerVariableName) {
        this.answerVariableName = answerVariableName;
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
                ", answerVariableName='" + answerVariableName + '\'' +
                '}';
    }
}