package cz.muni.ics.kypo.answers.storage.mappers;

import cz.muni.ics.kypo.answers.storage.api.SandboxInfoDto;
import cz.muni.ics.kypo.answers.storage.data.entities.SandboxInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SandboxAnswersMapper {

    SandboxInfoDto mapToDto(SandboxInfo sandboxInfo);
}
