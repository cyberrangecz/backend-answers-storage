package cz.cyberrange.platform.answers.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Signals that a request conflicts with the current state of the target entity.
 * The request is answered with HTTP 409 and an ApiEntityError body.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The request could not be completed due to a conflict with the current state of the target resource.")
public class EntityConflictException extends ExceptionWithEntity {

    public EntityConflictException() {
        super();
    }

    public EntityConflictException(EntityErrorDetail entityErrorDetail) {
        super(entityErrorDetail);
    }

    public EntityConflictException(EntityErrorDetail entityErrorDetail, Throwable cause) {
        super(entityErrorDetail, cause);
    }

    public EntityConflictException(Throwable cause) {
        super(cause);
    }

    /**
     * Returns a conflict sentence naming the entity, and its identifier and value when both are
     * present.
     *
     * @param entityErrorDetail the detail to describe
     * @return the reason text
     */
    protected String createDefaultReason(EntityErrorDetail entityErrorDetail) {
        StringBuilder reason = new StringBuilder("Conflict with the current state of the target entity ")
                .append(entityErrorDetail.getEntity());
        if (entityErrorDetail.getIdentifier() != null && entityErrorDetail.getIdentifierValue() != null) {
            reason.append(" (")
                    .append(entityErrorDetail.getIdentifier())
                    .append(": ")
                    .append(entityErrorDetail.getIdentifierValue())
                    .append(")");
        }
        reason.append(".");
        return reason.toString();
    }
}
