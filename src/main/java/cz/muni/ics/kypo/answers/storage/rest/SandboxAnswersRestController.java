package cz.muni.ics.kypo.answers.storage.rest;

import cz.muni.ics.kypo.answers.storage.api.SandboxAnswersDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.querydsl.core.types.Predicate;

import java.util.List;

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
    @GetMapping(path = "/{sandboxRefId}/answers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SandboxAnswersDto>> findAnswersForParticularSandbox(@ApiParam(value = "ID sandbox for that we store answers.", required = true)
                                                                                   @PathVariable(value = "sandboxRefId") Long sandboxRefId) {
        return ResponseEntity.ok(sandboxAnswersService.getSandboxAnswersBySandboxRefId(sandboxRefId));
    }

    /**
     * Get answer for particular sandbox and by answer identifier.
     *
     * @param sandboxRefId id of a sandbox.
     * @param answerIdentifier identifier of an answer.
     * @return the content of the answer.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get answer for particular sandbox and by answer identifier.",
            response = SandboxInfoDto.class,
            nickname = "findAnswerBySandboxAndIdentifier",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The answer for particular sandbox and by identifier was found.", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{sandboxRefId}/answers/{answerIdentifier}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAnswerBySandboxAndIdentifier(@ApiParam(value = "ID sandbox for that we store answers.", required = true)
                                                                                   @PathVariable(value = "sandboxRefId") Long sandboxRefId,
                                                                                   @ApiParam(value = "Identifier of the answer.", required = true)
                                                                                   @PathVariable(value = "answerIdentifier") String answerIdentifier) {
        return ResponseEntity.ok(sandboxAnswersService.getAnswerBySandboxAndIdentifier(sandboxRefId, answerIdentifier));
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
            @ApiResponse(code = 201, message = "The answers for particular sandbox were created."),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> storeAnswersForParticularSandbox(@RequestBody SandboxInfoCreateDto sandboxInfoCreateDto) {
        sandboxAnswersService.storeAllAnswersForSandbox(sandboxInfoCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
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
            @ApiResponse(code = 204, message = "The sandbox with answers was successfully deleted."),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{sandboxRefId}")
    public ResponseEntity<Void> deleteSandboxWithAnswers(@PathVariable("sandboxRefId") Long sandboxRefId) {
        sandboxAnswersService.deleteSandboxWithAnswers(sandboxRefId);
        return ResponseEntity.noContent().build();
    }

}
