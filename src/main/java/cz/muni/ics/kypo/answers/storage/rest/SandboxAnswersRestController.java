package cz.muni.ics.kypo.answers.storage.rest;

import cz.muni.ics.kypo.answers.storage.api.SandboxInfoCreateDto;
import cz.muni.ics.kypo.answers.storage.api.SandboxInfoDto;
import cz.muni.ics.kypo.answers.storage.api.reponses.PageResultResource;
import cz.muni.ics.kypo.answers.storage.data.entities.SandboxInfo;
import cz.muni.ics.kypo.answers.storage.exceptions.errors.ApiError;
import cz.muni.ics.kypo.answers.storage.service.SandboxAnswersService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.querydsl.core.types.Predicate;

@Api(value = "Endpoint for KYPO Sandbox Answers", tags = "sandboxes")
@RestController
@RequestMapping(path = "/sandboxes")
@Validated
public class SandboxAnswersRestController {

    private final SandboxAnswersService sandboxAnswersService;

    /**
     * Instantiates a new SandboxAnswersRestController.
     *
     * @param sandboxAnswersService the user facade
     */
    @Autowired
    public SandboxAnswersRestController(final SandboxAnswersService sandboxAnswersService) {
        this.sandboxAnswersService = sandboxAnswersService;
    }

    /**
     * Get answers for particular sandbox.
     *
     * @param sandboxRefId of a sandbox.
     * @return answers for particular sandbox.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get answers for sandbox.",
            response = SandboxInfoDto.class,
            nickname = "findTrainingDefinitionById",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The answers for particular sandbox were found.", response = SandboxInfoDto.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{sandboxRefId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SandboxInfoDto> findAnswersForParticularSandbox(@ApiParam(value = "ID of training definition to be retrieved.", required = true)
                                                                          @PathVariable(value = "sandboxRefId") Long sandboxRefId) {
        SandboxInfoDto sandboxInfoDto = sandboxAnswersService.getSandboxAnswersBySandboxRefId(sandboxRefId);
        return ResponseEntity.ok(sandboxInfoDto);
    }

    /**
     * Get answers for all sandboxes.
     *
     * @return answers for all sandboxes.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get all sandboxes and their answers.",
            response = SandboxInfoDto.class,
            nickname = "findAnswersForAllSandboxes",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The answers for all sandboxes were found.", response = SandboxInfoDto.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResultResource<SandboxInfoDto>> findAnswersForAllSandboxes(@QuerydslPredicate(root = SandboxInfo.class) Predicate predicate,
                                                                                         Pageable pageable) {
        return ResponseEntity.ok(sandboxAnswersService.getAllSandboxesAnswers(predicate, pageable));
    }

    /**
     * Store all answers for particular sandbox.
     */
    @ApiOperation(httpMethod = "POST",
            value = "Store all answers for particular sandbox.",
            nickname = "storeAnswersForParticularSandbox",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The answers for particular sandbox were created."),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> storeAnswersForParticularSandbox(@RequestBody SandboxInfoCreateDto sandboxInfoCreateDto) {
        sandboxAnswersService.storeAllAnswersForSandbox(sandboxInfoCreateDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete sandbox_ref with all answers.
     */
    @ApiOperation(httpMethod = "DELETE",
            value = "Delete sandbox_ref with all answers.",
            nickname = "deleteSandboxWithAnswers",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The sandbox with answers was successfully deleted."),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @DeleteMapping(path = "/{sandboxRefId}")
    public ResponseEntity<Void> deleteSandboxWithAnswers(@PathVariable("sandboxRefId") Long sandboxRefId) {
        sandboxAnswersService.deleteSandboxWithAnswers(sandboxRefId);
        return ResponseEntity.noContent().build();
    }

}
