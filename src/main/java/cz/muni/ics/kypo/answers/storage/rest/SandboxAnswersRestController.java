package cz.muni.ics.kypo.answers.storage.rest;

import com.querydsl.core.types.Predicate;
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

import javax.validation.Valid;

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
     * Get answers for particular cloud sandbox.
     *
     * @param sandboxRefId of a cloud sandbox.
     * @return answers for particular cloud sandbox.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get answers for cloud sandbox.",
            response = SandboxInfoDto.class,
            nickname = "findAnswersForParticularCloudSandbox",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The answers for particular cloud sandbox were found.", response = SandboxInfoDto.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{sandboxRefId}/answers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SandboxInfoDto> findAnswersForParticularCloudSandbox(
            @ApiParam(value = "ID sandbox for that we store answers.", required = true) @PathVariable(value = "sandboxRefId") Long sandboxRefId) {
        return ResponseEntity.ok(sandboxAnswersService.getSandboxAnswers(sandboxRefId));
    }

    /**
     * Get answers for particular local sandbox.
     *
     * @param accessToken access token identifies sandbox instance in which the local sandbox is/has been used.
     * @param userId ID of the user who possess the local sandbox.
     * @return answers for particular local sandbox.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get answers for local sandbox.",
            response = SandboxInfoDto.class,
            nickname = "findAnswersForParticularLocalSandbox",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The answers for particular local sandbox were found.", response = SandboxInfoDto.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/access-tokens/{accessToken}/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SandboxInfoDto> findAnswersForParticularLocalSandbox(
            @ApiParam(value = "Token of the training instance in which the local sandbox is used.", required = true) @PathVariable("accessToken") String accessToken,
            @ApiParam(value = "ID of the user whose local sandbox to delete.", required = true) @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(sandboxAnswersService.getSandboxAnswers(accessToken, userId));
    }

    /**
     * Get answer for particular cloud sandbox and by answer variable name.
     *
     * @param sandboxRefId       id of a sandbox.
     * @param answerVariableName variable name of an answer.
     * @return the content of the answer.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get answer for particular cloud sandbox and by answer variable name.",
            response = String.class,
            nickname = "findAnswerByCloudSandboxAndVariableName",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The answer for particular cloud sandbox and by identifier was found.", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{sandboxRefId}/answers/{answerVariableName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAnswerByCloudSandboxAndVariableName(
            @ApiParam(value = "ID of sandbox for that we store answers.", required = true) @PathVariable(value = "sandboxRefId") Long sandboxRefId,
            @ApiParam(value = "Variable name of the answer.", required = true) @PathVariable(value = "answerVariableName") String answerVariableName) {
        return ResponseEntity.ok(sandboxAnswersService.getAnswerBySandboxAndVariableName(sandboxRefId, answerVariableName));
    }

    /**
     * Get answer for particular local sandbox and by answer variable name.
     *
     * @param accessToken access token identifies sandbox instance in which the local sandbox is/has been used.
     * @param userId ID of the user who possess the local sandbox.
     * @param answerVariableName variable name of an answer.
     * @return the content of the answer.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get answer for particular local sandbox and by answer variable name.",
            response = String.class,
            nickname = "findAnswerByLocalSandboxAndVariableName",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The answer for particular local sandbox and by identifier was found.", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/access-tokens/{accessToken}/users/{userId}/answers/{answerVariableName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAnswerByLocalSandboxAndVariableName(
            @ApiParam(value = "Token of the training instance in which the local sandbox is used.", required = true) @PathVariable("accessToken") String accessToken,
            @ApiParam(value = "ID of the user whose local sandbox to delete.", required = true) @PathVariable("userId") Long userId,
            @ApiParam(value = "Variable name of the answer.", required = true) @PathVariable(value = "answerVariableName") String answerVariableName) {
        return ResponseEntity.ok(sandboxAnswersService.getAnswerBySandboxAndVariableName(accessToken, userId, answerVariableName));
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
     * Store all answers for particular cloud/local sandbox.
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
    public ResponseEntity<Void> storeAnswersForParticularSandbox(@RequestBody @Valid SandboxInfoCreateDto sandboxInfoCreateDto) {
        sandboxAnswersService.storeAllAnswersForSandbox(sandboxInfoCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Delete cloud sandbox reference with all answers.
     */
    @ApiOperation(httpMethod = "DELETE",
            value = "Delete cloud sandbox reference with all answers.",
            nickname = "deleteCloudSandboxReferenceWithAnswers",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The cloud sandbox reference with answers was successfully deleted."),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{sandboxRefId}")
    public ResponseEntity<Void> deleteCloudSandboxReferenceWithAnswers(
            @ApiParam(value = "ID of the user whose cloud sandbox to delete.", required = true) @PathVariable("sandboxRefId") Long sandboxRefId) {
        sandboxAnswersService.deleteCloudSandboxReferenceWithAnswers(sandboxRefId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete local sandbox reference with all answers.
     */
    @ApiOperation(httpMethod = "DELETE",
            value = "Delete local sandbox reference with all answers.",
            nickname = "deleteLocalSandboxReferenceWithAnswers",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The local sandbox reference with answers was successfully deleted."),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/access-tokens/{accessToken}/users/{userId}")
    public ResponseEntity<Void> deleteLocalSandboxReferenceWithAnswers(
            @ApiParam(value = "Token of the training instance in which the local sandbox is used.", required = true) @PathVariable("accessToken") String accessToken,
            @ApiParam(value = "ID of the user whose local sandbox to delete.", required = true) @PathVariable("userId") Long userId) {
        sandboxAnswersService.deleteLocalSandboxReferenceWithAnswers(accessToken, userId);
        return ResponseEntity.noContent().build();
    }

}
