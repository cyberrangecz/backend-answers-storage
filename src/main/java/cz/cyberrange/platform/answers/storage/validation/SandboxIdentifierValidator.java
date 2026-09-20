package cz.cyberrange.platform.answers.storage.validation;

import cz.cyberrange.platform.answers.storage.api.SandboxInfoCreateDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks that a {@link SandboxInfoCreateDto} identifies its sandbox in exactly one of the two
 * supported ways.
 */
public class SandboxIdentifierValidator implements ConstraintValidator<ValidSandboxIdentifier, SandboxInfoCreateDto> {

    /**
     * Accepts sandboxRefId alone, or accessToken together with userId, and rejects every other
     * combination. A null DTO passes.
     *
     * @param entity the DTO to check, or null
     * @param context validation context, ignored
     * @return true when the DTO identifies its sandbox in exactly one of the two ways
     */
    @Override
    public boolean isValid(SandboxInfoCreateDto entity, ConstraintValidatorContext context) {
        if (entity == null) {
            return true;
        }
        if(entity.getSandboxRefId() != null && entity.getAccessToken() == null && entity.getUserId() == null) {
            return true;
        }
        return entity.getSandboxRefId() == null && entity.getAccessToken() != null && entity.getUserId() != null;
    }

}