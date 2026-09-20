package cz.cyberrange.platform.answers.storage.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Which entity a request failed on, which identifier singles it out, and why it failed.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntityErrorDetail {
    @Schema(description = "Name of the entity type the request failed on.", example = "SandboxInfo")
    private String entity;
    @Schema(description = "Name of the field that singles that entity out.", example = "id")
    private String identifier;
    @Schema(description = "Value of that field.", example = "1")
    private Object identifierValue;
    /**
     * Why the request failed; null until supplied, whether at construction or afterwards.
     */
    @Schema(example = "Answers for the cloud sandbox (sandboxRefId: 3fa85f64-5717-4562-b3fc-2c963f66afa6) have been already created.")
    private String reason;

    public EntityErrorDetail() {
    }

    public EntityErrorDetail(@NotBlank String reason) {
        this.reason = reason;
    }

    public EntityErrorDetail(@NotNull Class<?> entityClass,
                             @NotBlank String reason) {
        this(reason);
        this.entity = entityClass.getSimpleName();
    }

    /**
     * @throws ClassCastException when identifierValue is not an instance of identifierClass
     */
    public EntityErrorDetail(@NotNull Class<?> entityClass,
                             @NotBlank String identifier,
                             @NotNull Class<?> identifierClass,
                             @NotNull Object identifierValue,
                             @NotBlank String reason) {
        this(entityClass, reason);
        this.identifier = identifier;
        this.identifierValue = identifierClass.cast(identifierValue);
    }

    /**
     * Creates a detail with no reason.
     *
     * @throws ClassCastException when identifierValue is not an instance of identifierClass
     */
    public EntityErrorDetail(@NotNull Class<?> entityClass,
                             @NotBlank String identifier,
                             @NotNull Class<?> identifierClass,
                             @NotNull Object identifierValue) {
        this.entity = entityClass.getSimpleName();
        this.identifier = identifier;
        this.identifierValue = identifierClass.cast(identifierValue);
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(@NotBlank String entity) {
        this.entity = entity;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(@NotBlank String identifier) {
        this.identifier = identifier;
    }

    public Object getIdentifierValue() {
        return identifierValue;
    }

    public void setIdentifierValue(@NotNull Object identifierValue) {
        this.identifierValue = identifierValue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(@NotBlank String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityErrorDetail entity = (EntityErrorDetail) o;
        return Objects.equals(getEntity(), entity.getEntity()) &&
                Objects.equals(getIdentifier(), entity.getIdentifier()) &&
                Objects.equals(getIdentifierValue(), entity.getIdentifierValue()) &&
                Objects.equals(getReason(), entity.getReason());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntity(), getIdentifier(), getIdentifierValue(), getReason());
    }

    @Override
    public String toString() {
        return "EntityErrorDetail{" +
                "entity='" + entity + '\'' +
                ", identifier='" + identifier + '\'' +
                ", identifierValue=" + identifierValue +
                ", reason='" + reason + '\'' +
                '}';
    }
}
