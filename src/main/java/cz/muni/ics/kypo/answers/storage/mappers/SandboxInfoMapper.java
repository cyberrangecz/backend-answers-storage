package cz.muni.ics.kypo.answers.storage.mappers;

import cz.muni.ics.kypo.answers.storage.api.SandboxInfoCreateDto;
import cz.muni.ics.kypo.answers.storage.api.SandboxInfoDto;
import cz.muni.ics.kypo.answers.storage.api.SandboxAnswersDto;
import cz.muni.ics.kypo.answers.storage.api.reponses.PageResultResource;
import cz.muni.ics.kypo.answers.storage.data.entities.SandboxAnswer;
import cz.muni.ics.kypo.answers.storage.data.entities.SandboxInfo;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface SandboxInfoMapper extends ParentMapper {

    SandboxInfoDto mapToDto(SandboxInfo sandboxInfo);

    List<SandboxAnswersDto> mapToAnswers(Set<SandboxAnswer> sandboxAnswerSet);

    SandboxInfo mapCreateDtoToEntity(SandboxInfoCreateDto sandboxInfoCreateDto);

    default PageResultResource<SandboxInfoDto> mapToPageResultResource(Page<SandboxInfo> objects) {
        List<SandboxInfoDto> mapped = new ArrayList<>();
        objects.forEach(object -> mapped.add(mapToDto(object)));
        return new PageResultResource<>(mapped, createPagination(objects));
    }

}
