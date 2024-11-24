package cz.cyberrange.platform.answers.storage.validation;

import cz.cyberrange.platform.answers.storage.api.SandboxInfoCreateDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SandboxIdentifierValidator implements ConstraintValidator<ValidSandboxIdentifier, SandboxInfoCreateDto> {

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