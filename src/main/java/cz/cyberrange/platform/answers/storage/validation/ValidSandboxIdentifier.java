package cz.cyberrange.platform.answers.storage.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * The annotated element must be class with a fields sandboxRefId, accessToken and userId.
 * Either sandboxRefId must be defined or access token along with userId.
 */
@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = SandboxIdentifierValidator.class)
@Documented
public @interface ValidSandboxIdentifier {
    String message() default "Either sandboxRefId or both accessToken and userId must be defined.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}