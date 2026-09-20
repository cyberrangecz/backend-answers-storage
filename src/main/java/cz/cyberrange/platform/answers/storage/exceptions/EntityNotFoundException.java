package cz.cyberrange.platform.answers.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Signals that a requested entity does not exist.
 * The request is answered with HTTP 404 and an ApiEntityError body.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The requested entity could not be found")
public class EntityNotFoundException extends ExceptionWithEntity {
    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(EntityErrorDetail entityErrorDetail) {
        super(entityErrorDetail);
    }

    public EntityNotFoundException(EntityErrorDetail entityErrorDetail, Throwable cause) {
        super(entityErrorDetail, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Returns a not-found sentence naming the entity, and its identifier and value when both are
     * present.
     *
     * @param entityErrorDetail the detail to describe
     * @return the reason text
     */
    protected String createDefaultReason(EntityErrorDetail entityErrorDetail) {
        StringBuilder reason = new StringBuilder("Entity ")
                .append(entityErrorDetail.getEntity());
        if (entityErrorDetail.getIdentifier() != null && entityErrorDetail.getIdentifierValue() != null) {
            reason.append(" (")
                    .append(entityErrorDetail.getIdentifier())
                    .append(": ")
                    .append(entityErrorDetail.getIdentifierValue())
                    .append(")");
        }
        reason.append(" not found.");
        return reason.toString();
    }
}
