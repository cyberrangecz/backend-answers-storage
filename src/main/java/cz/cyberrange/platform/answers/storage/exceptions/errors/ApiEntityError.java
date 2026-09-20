package cz.cyberrange.platform.answers.storage.exceptions.errors;

import cz.cyberrange.platform.answers.storage.exceptions.EntityErrorDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

/**
 * Body returned when a REST request fails on a particular entity, adding the entity detail to
 * the fields of a plain error body.
 */
@Schema(name = "ApiEntityError", description = "Body returned when a request fails on a particular entity.")
public class ApiEntityError extends ApiError {
    /**
     * Which entity the request failed on and why; null when the failure names no entity.
     */
    @Schema(description = "Which entity the request failed on, and why.")
    private EntityErrorDetail entityErrorDetail;

    private ApiEntityError() {
        super();
    }

    private ApiEntityError(HttpStatus httpStatus, String message, String path, EntityErrorDetail entityErrorDetail) {
        super();
        this.setStatus(httpStatus);
        this.setMessage(getMessage(entityErrorDetail, message));
        this.setPath(path);
        this.setTimestamp(System.currentTimeMillis());
        this.setEntityErrorDetail(entityErrorDetail);
    }

    /**
     * Creates an entity error body timestamped with the current time. The reason carried by the
     * entity detail is reported as the message, falling back to the given message.
     *
     * @param httpStatus the status to report
     * @param message the message to fall back on
     * @param errors the contributing errors
     * @param path the request path to report
     * @param entityErrorDetail the entity detail to report, may be null
     * @return the error body
     */
    public static ApiEntityError of(HttpStatus httpStatus, String message, List<String> errors, String path, EntityErrorDetail entityErrorDetail) {
        ApiEntityError apiEntityError = new ApiEntityError(httpStatus, message, path, entityErrorDetail);
        apiEntityError.setErrors(errors);
        return apiEntityError;
    }

    /**
     * Creates an entity error body with a single contributing error, timestamped with the current
     * time. The reason carried by the entity detail is reported as the message, falling back to
     * the given message.
     *
     * @param httpStatus the status to report
     * @param message the message to fall back on
     * @param error the single contributing error
     * @param path the request path to report
     * @param entityErrorDetail the entity detail to report, may be null
     * @return the error body
     */
    public static ApiEntityError of(HttpStatus httpStatus, String message, String error, String path, EntityErrorDetail entityErrorDetail) {
        ApiEntityError apiEntityError = new ApiEntityError(httpStatus, message, path, entityErrorDetail);
        apiEntityError.setError(error);
        return apiEntityError;
    }

    /**
     * Creates an entity error body with an empty path.
     *
     * @param httpStatus the status to report
     * @param message the message to fall back on
     * @param errors the contributing errors
     * @param entityErrorDetail the entity detail to report, may be null
     * @return the error body
     */
    public static ApiEntityError of(HttpStatus httpStatus, String message, List<String> errors, EntityErrorDetail entityErrorDetail) {
        return ApiEntityError.of(httpStatus, message, errors, "", entityErrorDetail);
    }

    /**
     * Creates an entity error body with an empty path and a single contributing error.
     *
     * @param httpStatus the status to report
     * @param message the message to fall back on
     * @param error the single contributing error
     * @param entityErrorDetail the entity detail to report, may be null
     * @return the error body
     */
    public static ApiEntityError of(HttpStatus httpStatus, String message, String error, EntityErrorDetail entityErrorDetail) {
        return ApiEntityError.of(httpStatus, message, error, "", entityErrorDetail);
    }

    private static String generateMessage(EntityErrorDetail entityErrorDetail, String defaultMessage) {
        if (entityErrorDetail != null && entityErrorDetail.getEntity() != null && entityErrorDetail.getIdentifier() != null) {
            return "Resource " + entityErrorDetail.getEntity() + " ("
                    + entityErrorDetail.getIdentifier() + ": "
                    + entityErrorDetail.getIdentifierValue() + ") not found.";
        } else if (entityErrorDetail != null && entityErrorDetail.getReason() != null && !entityErrorDetail.getReason().isBlank()) {
            return entityErrorDetail.getReason();
        } else {
            return defaultMessage;
        }
    }

    private static String getMessage(EntityErrorDetail entityErrorDetail, String defaultMessage) {
        if (entityErrorDetail == null) {
            return defaultMessage;
        }
        return entityErrorDetail.getReason() == null ? defaultMessage : entityErrorDetail.getReason();
    }


    public EntityErrorDetail getEntityErrorDetail() {
        return entityErrorDetail;
    }

    public void setEntityErrorDetail(EntityErrorDetail entityErrorDetail) {
        this.entityErrorDetail = entityErrorDetail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiEntityError)) return false;
        if (!super.equals(o)) return false;
        ApiEntityError that = (ApiEntityError) o;
        return Objects.equals(getEntityErrorDetail(), that.getEntityErrorDetail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEntityErrorDetail());
    }

    @Override
    public String toString() {
        return "ApiEntityError{" +
                "entityErrorDetail=" + entityErrorDetail +
                ", timestamp=" + getTimestamp() +
                ", status=" + getStatus() +
                ", message='" + getMessage() + '\'' +
                ", errors=" + getErrors() +
                ", path='" + getPath() + '\'' +
                '}';
    }
}
