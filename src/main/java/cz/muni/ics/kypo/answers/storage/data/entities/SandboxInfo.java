package cz.muni.ics.kypo.answers.storage.data.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sandbox_info")
public class SandboxInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sandboxInfoGenerator")
    @SequenceGenerator(name = "sandboxInfoGenerator", sequenceName = "sandbox_info_seq")
    @Column(name = "sandbox_info_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "sandbox_ref_id")
    private Long sandboxRefId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSandboxRefId() {
        return sandboxRefId;
    }

    public void setSandboxRefId(Long sandboxRefId) {
        this.sandboxRefId = sandboxRefId;
    }
}
