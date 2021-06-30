package cz.muni.ics.kypo.answers.storage.rest;

import cz.muni.ics.kypo.answers.storage.api.SandboxInfoDto;
import cz.muni.ics.kypo.answers.storage.exceptions.errors.ApiError;
import cz.muni.ics.kypo.answers.storage.service.SandboxAnswersService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Endpoint for KYPO Answers",
        tags = "users")
@RestController
@RequestMapping(path = "/answers")
@Validated
public class SandboxAnswersRestController {

    private SandboxAnswersService sandboxAnswersService;

    /**
     * Instantiates a new SandboxAnswersRestController.
     *
     * @param sandboxAnswersService the user facade
     */
    @Autowired
    public SandboxAnswersRestController(SandboxAnswersService sandboxAnswersService) {
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
        SandboxInfoDto sandboxInfoDto = sandboxAnswersService.getSandboxAnswersInfo(sandboxRefId);
        return ResponseEntity.ok(sandboxInfoDto);
    }


}
