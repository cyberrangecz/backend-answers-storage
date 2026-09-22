package cz.cyberrange.platform.answers.storage.exceptionhandler;


import cz.cyberrange.platform.answers.storage.exceptions.BadRequestException;
import cz.cyberrange.platform.answers.storage.exceptions.EntityConflictException;
import cz.cyberrange.platform.answers.storage.exceptions.EntityNotFoundException;
import cz.cyberrange.platform.answers.storage.exceptions.InternalServerErrorException;
import cz.cyberrange.platform.answers.storage.exceptions.errors.ApiEntityError;
import cz.cyberrange.platform.answers.storage.exceptions.errors.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Turns framework and application exceptions into REST error responses. Each handler answers with
 * an ApiError or ApiEntityError body carrying the status, message and path of the failure.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();
    private static final Logger LOG = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    /**
     * Handles a TypeMismatchException with HTTP 400. The reported path is the context path rather
     * than the request URI.
     *
     * @param ex the exception being handled
     * @param headers unused
     * @param status unused
     * @param request the current request
     * @return the error response
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status,
                                                        final WebRequest request) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles a MissingServletRequestPartException with HTTP 400. The reported path is the
     * context path rather than the request URI.
     *
     * @param ex the exception being handled
     * @param headers unused
     * @param status unused
     * @param request the current request
     * @return the error response
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers,
                                                                     final HttpStatus status, final WebRequest request) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles a MissingServletRequestParameterException with HTTP 400. The reported path is the
     * context path rather than the request URI.
     *
     * @param ex the exception being handled
     * @param headers unused
     * @param status unused
     * @param request the current request
     * @return the error response
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers,
                                                                          final HttpStatus status, final WebRequest request) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles a NoHandlerFoundException with HTTP 404. The reported path is the context path
     * rather than the request URI.
     *
     * @param ex the exception being handled
     * @param headers unused
     * @param status unused
     * @param request the current request
     * @return the error response
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status,
                                                                   final WebRequest request) {
        final ApiError apiError = ApiError.of(
                HttpStatus.NOT_FOUND,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles an HttpRequestMethodNotSupportedException with HTTP 404. The rejected method and
     * the supported ones are reported as the contributing error, and the reported path is the
     * context path rather than the request URI.
     *
     * @param ex the exception being handled
     * @param headers unused
     * @param status unused
     * @param request the current request
     * @return the error response
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers,
                                                                         final HttpStatus status, final WebRequest request) {
        final StringBuilder supportedHttpMethods = new StringBuilder();
        supportedHttpMethods.append(ex.getMethod());
        supportedHttpMethods.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> supportedHttpMethods.append(t + " "));

        final ApiError apiError = ApiError.of(
                HttpStatus.NOT_FOUND,
                getInitialException(ex).getLocalizedMessage(),
                supportedHttpMethods.toString(),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles an HttpMediaTypeNotSupportedException with HTTP 415. The rejected content type and
     * the supported media types are reported as the contributing error, and the reported path is
     * the context path rather than the request URI.
     *
     * @param ex the exception being handled
     * @param headers unused
     * @param status unused
     * @param request the current request
     * @return the error response
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers,
                                                                     final HttpStatus status, final WebRequest request) {
        final StringBuilder supportedMediaTypes = new StringBuilder();
        supportedMediaTypes.append(ex.getContentType());
        supportedMediaTypes.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> supportedMediaTypes.append(t + " "));

        final ApiError apiError = ApiError.of(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                getInitialException(ex).getLocalizedMessage(),
                supportedMediaTypes.toString(),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles a MethodArgumentNotValidException with HTTP 400. The message lists the failed
     * validations, and the reported path is the context path rather than the request URI.
     *
     * @param ex the exception being handled
     * @param headers unused
     * @param status unused
     * @param request the current request
     * @return the error response
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers,
                                                                  final HttpStatus status, final WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> objectErrors = ex.getBindingResult().getGlobalErrors();
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                (fieldErrors.isEmpty() ? objectErrors : fieldErrors)
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(java.util.stream.Collectors.joining(", ")),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles an HttpMessageNotReadableException with HTTP 400. The reported path is the context
     * path rather than the request URI.
     *
     * @param ex the exception being handled
     * @param headers unused
     * @param status unused
     * @param request the current request
     * @return the error response
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers,
                                                                  final HttpStatus status, final WebRequest request) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                ex.getMostSpecificCause().getMessage(),
                getErrorMessage(ex),
                request.getContextPath());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }


    // Handling of own exceptions

    /**
     * Handles a ConstraintViolationException with HTTP 400.
     *
     * @param ex the exception being handled
     * @param req the current request
     * @return the error response
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex,
                                                            HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles a BadRequestException with HTTP 400.
     *
     * @param ex the exception being handled
     * @param request unused
     * @param req the current request
     * @return the error response
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(final BadRequestException ex, final WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                BadRequestException.class.getAnnotation(ResponseStatus.class).value(),
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles an InternalServerErrorException with HTTP 500.
     *
     * @param ex the exception being handled
     * @param request unused
     * @param req the current request
     * @return the error response
     */
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleInternalServerErrorException(final InternalServerErrorException ex, final WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                InternalServerErrorException.class.getAnnotation(ResponseStatus.class).value(),
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles an EntityNotFoundException with HTTP 404 and an ApiEntityError body.
     *
     * @param ex the exception being handled
     * @param request unused
     * @param req the current request
     * @return the error response
     */
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex, final WebRequest request, HttpServletRequest req) {
        final ApiEntityError apiError = ApiEntityError.of(
                EntityNotFoundException.class.getAnnotation(ResponseStatus.class).value(),
                EntityNotFoundException.class.getAnnotation(ResponseStatus.class).reason(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req),
                ex.getEntityErrorDetail());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles an EntityConflictException with HTTP 409 and an ApiEntityError body.
     *
     * @param ex the exception being handled
     * @param request unused
     * @param req the current request
     * @return the error response
     */
    @ExceptionHandler({EntityConflictException.class})
    public ResponseEntity<Object> handleEntityConflictException(final EntityConflictException ex, final WebRequest request, HttpServletRequest req) {
        final ApiEntityError apiError = ApiEntityError.of(
                EntityConflictException.class.getAnnotation(ResponseStatus.class).value(),
                EntityConflictException.class.getAnnotation(ResponseStatus.class).reason(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req),
                ex.getEntityErrorDetail());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles an IllegalArgumentException with HTTP 406.
     *
     * @param ex the exception being handled
     * @param request unused
     * @param req the current request
     * @return the error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex, final WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                HttpStatus.NOT_ACCEPTABLE,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles a NullPointerException with HTTP 400.
     *
     * @param ex the exception being handled
     * @param request unused
     * @param req the current request
     * @return the error response
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(final NullPointerException ex, final WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                HttpStatus.BAD_REQUEST,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles any exception no more specific handler covers, with HTTP 500.
     *
     * @param ex the exception being handled
     * @param request unused
     * @param req the current request
     * @return the error response
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request, HttpServletRequest req) {
        final ApiError apiError = ApiError.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                getInitialException(ex).getLocalizedMessage(),
                getErrorMessage(ex),
                URL_PATH_HELPER.getRequestUri(req));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Returns the deepest cause of the given exception, or the exception itself when it has none.
     *
     * @param exception the exception to walk
     * @return the deepest cause
     */
    private Exception getInitialException(Exception exception) {
        while (exception.getCause() != null) {
            exception = (Exception) exception.getCause();
        }
        return exception;
    }

    /**
     * Logs the exception's stack trace at error level and returns its message.
     *
     * @param exception the exception to log
     * @return the exception's message, or a failure notice when the stack trace could not be
     * written
     */
    private String getErrorMessage(Exception exception) {
        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw)) {
            exception.printStackTrace(pw);
            LOG.error(sw.toString());
            return exception.getMessage();
        } catch (IOException ex) {
            LOG.error("It was not possible to get the stack trace for that exception: ", ex);
        }
        return "It was not possible to get the stack trace for that exception.";
    }
}