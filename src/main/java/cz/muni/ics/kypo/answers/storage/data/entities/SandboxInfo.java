package cz.muni.ics.kypo.answers.storage.data.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sandbox_info")
public class SandboxInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sandbox_info_generator")
    @SequenceGenerator(name = "sandbox_info_generator", sequenceName = "sandbox_info_id_seq")
    @Column(name = "sandbox_info_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "sandbox_ref_id")
    private String sandboxRefId;
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "user_id")
    private Long userId;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sandboxInfo",
            cascade = CascadeType.ALL)
    private Set<SandboxAnswer> sandboxAnswers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSandboxRefId() {
        return sandboxRefId;
    }

    public void setSandboxRefId(String sandboxRefId) {
        this.sandboxRefId = sandboxRefId;
    }

    public Set<SandboxAnswer> getSandboxAnswers() {
        return sandboxAnswers;
    }

    public void setSandboxAnswers(Set<SandboxAnswer> sandboxAnswers) {
        this.sandboxAnswers = sandboxAnswers;
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

    @Override
    public String toString() {
        return "SandboxInfo{" +
                "id=" + id +
                ", sandboxRefId=" + sandboxRefId +
                ", userId=" + userId +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
