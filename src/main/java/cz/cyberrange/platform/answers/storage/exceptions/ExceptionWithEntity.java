package cz.cyberrange.platform.answers.storage.exceptions;

/**
 * Base for exceptions that carry a detail about the entity involved in the failure.
 * Each subclass maps to its own HTTP status and is answered with an ApiEntityError body.
 */
public abstract class ExceptionWithEntity extends RuntimeException {
    private EntityErrorDetail entityErrorDetail;

    protected ExceptionWithEntity() {
        super();
    }

    /**
     * Attaches the given entity detail, giving it a default reason when it carries none.
     *
     * @param entityErrorDetail the detail to attach
     */
    protected ExceptionWithEntity(EntityErrorDetail entityErrorDetail) {
        this.entityErrorDetail = entityErrorDetail;
        if (entityErrorDetail.getReason() == null) {
            this.entityErrorDetail.setReason(createDefaultReason(this.entityErrorDetail));
        }
    }

    /**
     * Attaches the given entity detail and cause, giving the detail a default reason when it
     * carries none.
     *
     * @param entityErrorDetail the detail to attach
     * @param cause the exception that caused this one
     */
    protected ExceptionWithEntity(EntityErrorDetail entityErrorDetail, Throwable cause) {
        super(cause);
        this.entityErrorDetail = entityErrorDetail;
        if (entityErrorDetail.getReason() == null) {
            this.entityErrorDetail.setReason(createDefaultReason(this.entityErrorDetail));
        }
    }

    protected ExceptionWithEntity(Throwable cause) {
        super(cause);
    }

    /**
     * @return the attached entity detail, or null when the exception carries none
     */
    public EntityErrorDetail getEntityErrorDetail() {
        return entityErrorDetail;
    }

    /**
     * Returns the reason text to use when the entity detail carries none.
     *
     * @param entityErrorDetail the detail to describe
     * @return the reason text
     */
    protected abstract String createDefaultReason(EntityErrorDetail entityErrorDetail);

}
