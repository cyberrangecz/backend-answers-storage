package cz.cyberrange.platform.answers.storage.exceptions.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Body returned when a REST request fails: the HTTP status, a message, the contributing errors,
 * the request path, and when the failure happened.
 */
@Schema(name = "ApiError", description = "Body returned when a request fails.")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ApiEntityError.class, name = "ApiEntityError")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    @Schema(description = "When the error happened, in milliseconds since the epoch.", example = "1758312000000")
    private long timestamp;
    @Schema(example = "NOT_FOUND")
    private HttpStatus status;
    @Schema(description = "Short statement of what went wrong.", example = "Entity SandboxInfo (id: 3fa85f64-5717-4562-b3fc-2c963f66afa6) not found.")
    private String message;
    @Schema(description = "Detail messages behind the failure.", example = "[Entity SandboxInfo (id: 3fa85f64-5717-4562-b3fc-2c963f66afa6) not found.]")
    private List<String> errors;
    @Schema(example = "/sandboxes/3fa85f64-5717-4562-b3fc-2c963f66afa6/answers")
    private String path;

    protected ApiError() {
    }

    private ApiError(HttpStatus httpStatus, String message, String path) {
        this.status = httpStatus;
        this.message = message;
        this.path = path;
        this.timestamp = System.currentTimeMillis();

    }

    /**
     * Creates an error body timestamped with the current time.
     *
     * @param httpStatus the status to report
     * @param message the message to report
     * @param errors the contributing errors
     * @param path the request path to report
     * @return the error body
     */
    public static ApiError of(HttpStatus httpStatus, String message, List<String> errors, String path) {
        ApiError apiError = new ApiError(httpStatus, message, path);
        apiError.setErrors(errors);
        return apiError;
    }

    /**
     * Creates an error body with a single contributing error, timestamped with the current time.
     *
     * @param httpStatus the status to report
     * @param message the message to report
     * @param error the single contributing error
     * @param path the request path to report
     * @return the error body
     */
    public static ApiError of(HttpStatus httpStatus, String message, String error, String path) {
        ApiError apiError = new ApiError(httpStatus, message, path);
        apiError.setError(error);
        return apiError;
    }

    /**
     * Creates an error body with an empty path.
     *
     * @param httpStatus the status to report
     * @param message the message to report
     * @param errors the contributing errors
     * @return the error body
     */
    public static ApiError of(HttpStatus httpStatus, String message, List<String> errors) {
        return ApiError.of(httpStatus, message, errors, "");
    }

    /**
     * Creates an error body with an empty path and a single contributing error.
     *
     * @param httpStatus the status to report
     * @param message the message to report
     * @param error the single contributing error
     * @return the error body
     */
    public static ApiError of(HttpStatus httpStatus, String message, String error) {
        return ApiError.of(httpStatus, message, error, "");
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(final List<String> errors) {
        this.errors = errors;
    }

    /**
     * Replaces the contributing errors with the single given one.
     *
     * @param error the single contributing error
     */
    public void setError(final String error) {
        errors = Arrays.asList(error);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, status, message, errors, path);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ApiError))
            return false;
        ApiError other = (ApiError) obj;
        return Objects.equals(errors, other.getErrors()) &&
                Objects.equals(message, other.getMessage()) &&
                Objects.equals(path, other.getPath()) &&
                Objects.equals(status, other.getStatus()) &&
                Objects.equals(timestamp, other.getTimestamp());
    }

}
