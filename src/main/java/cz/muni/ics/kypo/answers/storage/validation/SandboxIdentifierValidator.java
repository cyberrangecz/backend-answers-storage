package cz.muni.ics.kypo.answers.storage.validation;

import cz.muni.ics.kypo.answers.storage.api.SandboxInfoCreateDto;
import cz.muni.ics.kypo.answers.storage.api.SandboxInfoDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Comparator;
import java.util.List;

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