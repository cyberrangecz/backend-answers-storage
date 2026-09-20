package cz.cyberrange.platform.answers.storage.rest;

import com.querydsl.core.types.Predicate;
import cz.cyberrange.platform.answers.storage.api.SandboxInfoCreateDto;
import cz.cyberrange.platform.answers.storage.api.SandboxInfoDto;
import cz.cyberrange.platform.answers.storage.api.reponses.PageResultResource;
import cz.cyberrange.platform.answers.storage.data.entities.SandboxInfo;
import cz.cyberrange.platform.answers.storage.exceptions.errors.ApiError;
import cz.cyberrange.platform.answers.storage.service.SandboxAnswersService;
import cz.cyberrange.platform.answers.storage.exceptions.errors.ApiEntityError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST endpoint for storing, deleting and reading sandboxes and the answers stored for them,
 * addressed by sandbox reference id, by allocation id, or by the combination of access token
 * and user id.
 */
@Tag(name = "sandboxes", description = "Sandboxes and the answers stored for them.")
@ApiResponses({
        @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
})
@RestController
@RequestMapping(path = "/sandboxes")
@Validated
public class SandboxAnswersRestController {

    private final SandboxAnswersService sandboxAnswersService;

    @Autowired
    public SandboxAnswersRestController(final SandboxAnswersService sandboxAnswersService) {
        this.sandboxAnswersService = sandboxAnswersService;
    }

    /**
     * Returns the answers stored for the cloud sandbox with the given reference id.
     *
     * @param sandboxRefId reference id of the sandbox
     * @return the sandbox with its answers
     */
    @Operation(
            operationId = "findAnswersForParticularCloudSandbox",
            summary = "Get the answers of a cloud sandbox",
            description = "A cloud sandbox with no stored answers is reported as not found.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The sandbox with its answers.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SandboxInfoDto.class))),
            @ApiResponse(responseCode = "404", description = "No sandbox has the given reference id.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiEntityError.class)))
    })
    @GetMapping(path = "/{sandboxRefId}/answers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SandboxInfoDto> findAnswersForParticularCloudSandbox(
            @Parameter(schema = @Schema(format = "uuid")) @PathVariable(value = "sandboxRefId") String sandboxRefId) {
        return ResponseEntity.ok(sandboxAnswersService.getSandboxAnswers(sandboxRefId));
    }

    /**
     * Returns the answers stored for the local sandbox with the given access token and user id.
     *
     * @param accessToken access token of the training instance the sandbox is used in
     * @param userId id of the user who owns the sandbox
     * @return the sandbox with its answers
     */
    @Operation(
            operationId = "findAnswersForParticularLocalSandbox",
            summary = "Get the answers of a local sandbox",
            description = "A local sandbox is addressed by access token together with user id. " +
                    "One with no stored answers is reported as not found.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The sandbox with its answers.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SandboxInfoDto.class))),
            @ApiResponse(responseCode = "404", description = "No sandbox matches the access token and user id.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiEntityError.class))),
            @ApiResponse(responseCode = "400", description = "The user id is not a valid number.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(path = "/access-tokens/{accessToken}/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SandboxInfoDto> findAnswersForParticularLocalSandbox(
            @PathVariable("accessToken") String accessToken,
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(sandboxAnswersService.getSandboxAnswers(accessToken, userId));
    }

    /**
     * Returns the content of one answer of the cloud sandbox with the given reference id.
     *
     * @param sandboxRefId       reference id of the sandbox
     * @param answerVariableName variable name of the answer
     * @return the stored answer content
     */
    @Operation(
            operationId = "findAnswerByCloudSandboxAndVariableName",
            summary = "Get one answer of a cloud sandbox")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The stored answer content.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "No answer matches the sandbox and variable name.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiEntityError.class)))
    })
    @GetMapping(path = "/{sandboxRefId}/answers/{answerVariableName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAnswerByCloudSandboxAndVariableName(
            @Parameter(schema = @Schema(format = "uuid")) @PathVariable(value = "sandboxRefId") String sandboxRefId,
            @PathVariable(value = "answerVariableName") String answerVariableName) {
        return ResponseEntity.ok(sandboxAnswersService.getAnswerBySandboxAndVariableName(sandboxRefId, answerVariableName));
    }

    /**
     * Returns the content of one answer of the local sandbox with the given access token and
     * user id.
     *
     * @param accessToken access token of the training instance the sandbox is used in
     * @param userId id of the user who owns the sandbox
     * @param answerVariableName variable name of the answer
     * @return the stored answer content
     */
    @Operation(
            operationId = "findAnswerByLocalSandboxAndVariableName",
            summary = "Get one answer of a local sandbox",
            description = "A local sandbox is addressed by access token together with user id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The stored answer content.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "No answer matches the sandbox and variable name.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiEntityError.class))),
            @ApiResponse(responseCode = "400", description = "The user id is not a valid number.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(path = "/access-tokens/{accessToken}/users/{userId}/answers/{answerVariableName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAnswerByLocalSandboxAndVariableName(
            @PathVariable("accessToken") String accessToken,
            @PathVariable("userId") Long userId,
            @PathVariable(value = "answerVariableName") String answerVariableName) {
        return ResponseEntity.ok(sandboxAnswersService.getAnswerBySandboxAndVariableName(accessToken, userId, answerVariableName));
    }

    /**
     * Returns one page of sandboxes with their answers. The id, sandboxRefId, allocationId,
     * accessToken and userId query parameters each filter on the whole value, case-sensitively;
     * the page is empty when nothing matches.
     *
     * @param predicate condition sandboxes must satisfy
     * @param pageable page and sort to apply
     * @return the matching page of sandboxes with their answers
     */
    @Operation(
            operationId = "findAnswersForAllSandboxes",
            summary = "List sandboxes with their answers",
            description = "Filter with the id, sandboxRefId, allocationId, accessToken and userId query parameters. " +
                    "Each one matches the whole value and is case-sensitive.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "A page of sandboxes with their answers.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResultResource.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResultResource<SandboxInfoDto>> findAnswersForAllSandboxes(@QuerydslPredicate(root = SandboxInfo.class) Predicate predicate,
                                                                                         @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(sandboxAnswersService.getAllSandboxesAnswers(predicate, pageable));
    }

    /**
     * Stores a new sandbox together with all of its answers, answering with HTTP 201 and no
     * body. The allocation unit id given in the body is not stored.
     *
     * @param sandboxInfoCreateDto the sandbox and its answers to store
     * @return an empty response
     */
    @Operation(
            operationId = "storeAnswersForParticularSandbox",
            summary = "Store a sandbox with all its answers",
            description = "Identify the sandbox by sandboxRefId alone, or by accessToken with userId. " +
                    "The allocation unit id sent here is not stored. " +
                    "Storing a second time for the same sandbox is rejected.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sandbox and answers stored."),
            @ApiResponse(responseCode = "400", description = "The request body failed validation.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Answers for that sandbox already exist.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiEntityError.class))),
            @ApiResponse(responseCode = "415", description = "The content type is not JSON.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> storeAnswersForParticularSandbox(@RequestBody @Valid SandboxInfoCreateDto sandboxInfoCreateDto) {
        sandboxAnswersService.storeAllAnswersForSandbox(sandboxInfoCreateDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Deletes the cloud sandbox with the given reference id together with its answers, answering
     * with HTTP 204 and no body even when no sandbox has that reference id.
     *
     * @param sandboxRefId reference id of the sandbox to delete
     * @return an empty response
     */
    @Operation(
            operationId = "deleteCloudSandboxReferenceWithAnswers",
            summary = "Delete a cloud sandbox and its answers",
            description = "Succeeds even when no sandbox has that reference id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sandbox and its answers deleted.")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{sandboxRefId}")
    public ResponseEntity<Void> deleteCloudSandboxReferenceWithAnswers(
            @Parameter(schema = @Schema(format = "uuid")) @PathVariable("sandboxRefId") String sandboxRefId) {
        sandboxAnswersService.deleteCloudSandboxReferenceWithAnswers(sandboxRefId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes every cloud sandbox with the given allocation id together with their answers,
     * answering with HTTP 204 and no body even when none matches. The path segment is declared
     * as {@code allocationId} while the parameter is bound under the name {@code sandboxRefId}.
     *
     * @param allocationId allocation id of the sandboxes to delete
     * @return an empty response
     */
    @Operation(
            operationId = "deleteCloudSandboxReferenceWithAnswersByAllocId",
            summary = "Delete cloud sandboxes by allocation id",
            description = "Deletes every sandbox sharing the allocation id. Succeeds even when none matches.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Matching sandboxes and their answers deleted."),
            @ApiResponse(responseCode = "400", description = "The allocation id is not a valid number.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{allocationId}")
    public ResponseEntity<Void> deleteCloudSandboxReferenceWithAnswers(
            @PathVariable("sandboxRefId") Long allocationId) {
        sandboxAnswersService.deleteCloudSandboxReferenceWithAnswers(allocationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes the local sandbox with the given access token and user id together with its
     * answers, answering with HTTP 204 and no body even when no sandbox matches both values.
     *
     * @param accessToken access token of the training instance the sandbox is used in
     * @param userId id of the user who owns the sandbox
     * @return an empty response
     */
    @Operation(
            operationId = "deleteLocalSandboxReferenceWithAnswers",
            summary = "Delete a local sandbox and its answers",
            description = "Succeeds even when no sandbox matches the access token and user id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sandbox and its answers deleted."),
            @ApiResponse(responseCode = "400", description = "The user id is not a valid number.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/access-tokens/{accessToken}/users/{userId}")
    public ResponseEntity<Void> deleteLocalSandboxReferenceWithAnswers(
            @PathVariable("accessToken") String accessToken,
            @PathVariable("userId") Long userId) {
        sandboxAnswersService.deleteLocalSandboxReferenceWithAnswers(accessToken, userId);
        return ResponseEntity.noContent().build();
    }

}
