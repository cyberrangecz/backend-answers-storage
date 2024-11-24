package cz.cyberrange.platform.answers.storage.data.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sandbox_info")
@NamedQueries({
        @NamedQuery(
                name = "SandboxInfo.deleteByAllocationId",
                query = "DELETE FROM SandboxInfo si WHERE si.allocationId = :allocationId"
        )
})
public class SandboxInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sandbox_info_generator")
    @SequenceGenerator(name = "sandbox_info_generator", sequenceName = "sandbox_info_id_seq")
    @Column(name = "sandbox_info_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "sandbox_ref_id")
    private String sandboxRefId;
    @Column(name = "allocation_id")
    private Long allocationId;
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

    public Long getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
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
                ", allocationId=" + allocationId +
                ", userId=" + userId +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
