package cz.cyberrange.platform.answers.storage.service;

import com.querydsl.core.types.Predicate;
import cz.cyberrange.platform.answers.storage.api.SandboxInfoCreateDto;
import cz.cyberrange.platform.answers.storage.api.SandboxInfoDto;
import cz.cyberrange.platform.answers.storage.api.responses.PageResultResource;
import cz.cyberrange.platform.answers.storage.data.entities.SandboxAnswer;
import cz.cyberrange.platform.answers.storage.data.entities.SandboxInfo;
import cz.cyberrange.platform.answers.storage.data.repositories.SandboxAnswerRepository;
import cz.cyberrange.platform.answers.storage.data.repositories.SandboxInfoRepository;
import cz.cyberrange.platform.answers.storage.exceptions.EntityConflictException;
import cz.cyberrange.platform.answers.storage.exceptions.EntityErrorDetail;
import cz.cyberrange.platform.answers.storage.exceptions.EntityNotFoundException;
import cz.cyberrange.platform.answers.storage.mappers.SandboxInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Business logic for storing, deleting and querying sandboxes and their stored answers,
 * addressed either by sandbox reference id, by allocation id, or by access token and user id.
 */
@Service
@Transactional
public class SandboxAnswersService {

    private final SandboxInfoRepository sandboxInfoRepository;
    private final SandboxAnswerRepository sandboxAnswerRepository;
    private final SandboxInfoMapper sandboxInfoMapper;

    @Autowired
    public SandboxAnswersService(final SandboxInfoRepository sandboxInfoRepository,
                                 final SandboxAnswerRepository sandboxAnswerRepository,
                                 final SandboxInfoMapper sandboxInfoMapper) {
        this.sandboxInfoRepository = sandboxInfoRepository;
        this.sandboxAnswerRepository = sandboxAnswerRepository;
        this.sandboxInfoMapper = sandboxInfoMapper;
    }

    /**
     * Returns the answers stored for the given cloud sandbox. The result carries only the
     * reference id and the answers; the other identifiers stay unset.
     *
     * @param sandboxRefId reference id of the sandbox
     * @return the sandbox reference id with its answers
     * @throws EntityNotFoundException when no sandbox has that reference id
     */
    @Transactional(readOnly = true)
    public SandboxInfoDto getSandboxAnswers(String sandboxRefId) {
        SandboxInfo sandboxInfo = sandboxInfoRepository.findBySandboxRefId(sandboxRefId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(SandboxInfo.class, "id", sandboxRefId.getClass(), sandboxRefId)));
        Set<SandboxAnswer> sandboxAnswerDtoSet = sandboxInfo.getSandboxAnswers();
        return new SandboxInfoDto(sandboxRefId, sandboxInfoMapper.mapToAnswers(sandboxAnswerDtoSet));
    }

    /**
     * Returns the answers stored for the local sandbox with the given access token and user id.
     * The result carries only the access token, the user id and the answers; the reference id
     * and allocation id stay unset.
     *
     * @param accessToken access token of the sandbox
     * @param userId id of the user owning the sandbox
     * @return the access token and user id with the sandbox's answers
     * @throws EntityNotFoundException when no sandbox matches both values
     */
    @Transactional(readOnly = true)
    public SandboxInfoDto getSandboxAnswers(String accessToken, Long userId) {
        SandboxInfo sandboxInfo = sandboxInfoRepository.findByAccessTokenAndUserIdId(accessToken, userId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(SandboxInfo.class, "id", userId.getClass(), userId)));
        Set<SandboxAnswer> sandboxAnswerDtoSet = sandboxInfo.getSandboxAnswers();
        return new SandboxInfoDto(accessToken, userId, sandboxInfoMapper.mapToAnswers(sandboxAnswerDtoSet));
    }

    /**
     * Returns the content of one answer of the cloud sandbox with the given reference id.
     *
     * @param sandboxRefId reference id of the sandbox
     * @param answerVariableName variable name of the answer
     * @return the stored answer content
     * @throws EntityNotFoundException when that sandbox has no answer for that variable name
     */
    @Transactional(readOnly = true)
    public String getAnswerBySandboxAndVariableName(String sandboxRefId, String answerVariableName) {
        return sandboxAnswerRepository.findAnswerBySandboxAndVariableName(sandboxRefId, answerVariableName)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(SandboxAnswer.class, "variable name", answerVariableName.getClass(), answerVariableName)))
                .getAnswerContent();
    }

    /**
     * Returns the content of one answer of the local sandbox with the given access token and
     * user id.
     *
     * @param accessToken access token of the sandbox
     * @param userId id of the user owning the sandbox
     * @param answerVariableName variable name of the answer
     * @return the stored answer content
     * @throws EntityNotFoundException when that sandbox has no answer for that variable name
     */
    @Transactional(readOnly = true)
    public String getAnswerBySandboxAndVariableName(String accessToken, Long userId, String answerVariableName) {
        return sandboxAnswerRepository.findAnswerBySandboxAndVariableName(accessToken, userId, answerVariableName)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(SandboxAnswer.class, "variable name", answerVariableName.getClass(), answerVariableName)))
                .getAnswerContent();
    }

    /**
     * Returns one page of sandboxes with their answers, keeping only those satisfying the given
     * filter. The page is empty when nothing matches.
     *
     * @param predicate condition sandboxes must satisfy
     * @param pageable page and sort to apply
     * @return the matching page of sandboxes with a pagination summary
     */
    @Transactional(readOnly = true)
    public PageResultResource<SandboxInfoDto> getAllSandboxesAnswers(Predicate predicate, Pageable pageable) {
        Page<SandboxInfo> sandboxInfo = sandboxInfoRepository.findAll(predicate, pageable);
        return sandboxInfoMapper.mapToPageResultResource(sandboxInfo);
    }

    /**
     * Deletes the cloud sandbox with the given reference id together with its answers. Succeeds
     * even when no sandbox has that reference id.
     *
     * @param sandboxRefId reference id of the sandbox to delete
     */
    public void deleteCloudSandboxReferenceWithAnswers(String sandboxRefId) {
        sandboxInfoRepository.deleteBySandboxRefId(sandboxRefId);
    }

    /**
     * Deletes every cloud sandbox with the given allocation id together with their answers.
     * Succeeds even when none matches.
     *
     * @param allocationId allocation id of the sandboxes to delete
     */
    public void deleteCloudSandboxReferenceWithAnswers(Long allocationId) {
        sandboxInfoRepository.deleteByAllocationId(allocationId);
    }


    /**
     * Deletes the local sandbox with the given access token and user id together with its
     * answers. Succeeds even when no sandbox matches both values.
     *
     * @param accessToken access token of the sandbox to delete
     * @param userId id of the user owning the sandbox to delete
     */
    public void deleteLocalSandboxReferenceWithAnswers(String accessToken, Long userId) {
        sandboxInfoRepository.deleteByAccessTokenAndUserId(accessToken, userId);
    }

    /**
     * Stores a new sandbox together with its answers. The allocation unit id given in the input
     * is not stored.
     *
     * @param sandboxInfoCreateDto the sandbox and answers to store
     * @throws EntityConflictException when answers for that sandbox are already stored
     */
    public void storeAllAnswersForSandbox(SandboxInfoCreateDto sandboxInfoCreateDto) {
        checkExistenceOfSandboxInfo(sandboxInfoCreateDto);
        SandboxInfo sandboxInfo = sandboxInfoMapper.mapCreateDtoToEntity(sandboxInfoCreateDto);
        sandboxInfo.getSandboxAnswers().forEach(sandboxAnswer -> sandboxAnswer.setSandboxInfo(sandboxInfo));
        sandboxInfoRepository.save(sandboxInfo);
    }

    /**
     * Rejects the given input when a sandbox is already stored under its identifiers, matched by
     * reference id when one is given and by user id with access token otherwise.
     *
     * @param sandboxInfo the input to check
     * @throws EntityConflictException when such a sandbox already exists
     */
    private void checkExistenceOfSandboxInfo(SandboxInfoCreateDto sandboxInfo) {
        if (sandboxInfo.getSandboxRefId() != null && sandboxInfoRepository.existsBySandboxRefId(sandboxInfo.getSandboxRefId())) {
            throw new EntityConflictException(new EntityErrorDetail(SandboxInfo.class,
                    "Answers for the cloud sandbox (sandboxRefId: " + sandboxInfo.getSandboxRefId() + ") have been already created."));
        }
        if (sandboxInfo.getSandboxRefId() == null && sandboxInfoRepository.existsByUserIdAndAccessToken(sandboxInfo.getUserId(), sandboxInfo.getAccessToken())) {
            throw new EntityConflictException(new EntityErrorDetail(SandboxInfo.class,
                    "Answers for the local sandbox (userId: " + sandboxInfo.getUserId() + ", accessToken: " + sandboxInfo.getAccessToken() + ") have been already created."));
        }
    }

}
