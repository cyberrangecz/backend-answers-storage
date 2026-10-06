package cz.cyberrange.platform.answers.storage.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Accepts a class carrying sandboxRefId, accessToken and userId only when it sets sandboxRefId
 * alone, or accessToken together with userId.
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
