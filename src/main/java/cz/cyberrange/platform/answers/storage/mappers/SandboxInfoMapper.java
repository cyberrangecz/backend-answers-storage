package cz.cyberrange.platform.answers.storage.mappers;

import cz.cyberrange.platform.answers.storage.api.SandboxAnswersDto;
import cz.cyberrange.platform.answers.storage.api.SandboxInfoCreateDto;
import cz.cyberrange.platform.answers.storage.api.SandboxInfoDto;
import cz.cyberrange.platform.answers.storage.api.responses.PageResultResource;
import cz.cyberrange.platform.answers.storage.data.entities.SandboxAnswer;
import cz.cyberrange.platform.answers.storage.data.entities.SandboxInfo;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Converts stored sandbox and answer entities to their API representation, and sandbox creation
 * input into a persisted sandbox entity.
 */
@Mapper(componentModel = "spring")
public interface SandboxInfoMapper extends ParentMapper {

    /**
     * Converts a stored sandbox to its API representation, identifiers and answers included.
     *
     * @param sandboxInfo the sandbox to convert
     * @return the converted DTO
     */
    SandboxInfoDto mapToDto(SandboxInfo sandboxInfo);

    /**
     * Converts stored answers to their API representation.
     *
     * @param sandboxAnswerSet the answers to convert
     * @return the converted answers, in the set's iteration order
     */
    List<SandboxAnswersDto> mapToAnswers(Set<SandboxAnswer> sandboxAnswerSet);

    /**
     * Converts creation input into a new, unpersisted sandbox with its answers. The allocation
     * id of the input is not carried over, and the sandbox and its answers keep no id and no
     * link back to their owner until persisted.
     *
     * @param sandboxInfoCreateDto the creation input to convert
     * @return the new sandbox
     */
    SandboxInfo mapCreateDtoToEntity(SandboxInfoCreateDto sandboxInfoCreateDto);

    /**
     * Converts a page of sandboxes into an API page result, in page order.
     *
     * @param objects the page of sandboxes to convert
     * @return the page of DTOs with its pagination summary
     */
    default PageResultResource<SandboxInfoDto> mapToPageResultResource(Page<SandboxInfo> objects) {
        List<SandboxInfoDto> mapped = new ArrayList<>();
        objects.forEach(object -> mapped.add(mapToDto(object)));
        return new PageResultResource<>(mapped, createPagination(objects));
    }

}
