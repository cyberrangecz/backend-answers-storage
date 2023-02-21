package cz.muni.ics.kypo.answers.storage.service;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.answers.storage.api.SandboxInfoCreateDto;
import cz.muni.ics.kypo.answers.storage.api.SandboxInfoDto;
import cz.muni.ics.kypo.answers.storage.api.reponses.PageResultResource;
import cz.muni.ics.kypo.answers.storage.data.entities.QSandboxInfo;
import cz.muni.ics.kypo.answers.storage.data.entities.SandboxAnswer;
import cz.muni.ics.kypo.answers.storage.data.entities.SandboxInfo;
import cz.muni.ics.kypo.answers.storage.data.repositories.SandboxAnswerRepository;
import cz.muni.ics.kypo.answers.storage.data.repositories.SandboxInfoRepository;
import cz.muni.ics.kypo.answers.storage.exceptions.EntityConflictException;
import cz.muni.ics.kypo.answers.storage.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.answers.storage.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.answers.storage.mappers.SandboxInfoMapper;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;
import java.util.Set;

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

    @Transactional(readOnly = true)
    public SandboxInfoDto getSandboxAnswers(String sandboxRefId) {
        SandboxInfo sandboxInfo = sandboxInfoRepository.findBySandboxRefId(sandboxRefId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(SandboxInfo.class, "id", sandboxRefId.getClass(), sandboxRefId)));
        Set<SandboxAnswer> sandboxAnswerDtoSet = sandboxInfo.getSandboxAnswers();
        return new SandboxInfoDto(sandboxRefId, sandboxInfoMapper.mapToAnswers(sandboxAnswerDtoSet));
    }

    @Transactional(readOnly = true)
    public SandboxInfoDto getSandboxAnswers(String accessToken, Long userId) {
        SandboxInfo sandboxInfo = sandboxInfoRepository.findByAccessTokenAndUserIdId(accessToken, userId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(SandboxInfo.class, "id", userId.getClass(), userId)));
        Set<SandboxAnswer> sandboxAnswerDtoSet = sandboxInfo.getSandboxAnswers();
        return new SandboxInfoDto(accessToken, userId, sandboxInfoMapper.mapToAnswers(sandboxAnswerDtoSet));
    }

    @Transactional(readOnly = true)
    public String getAnswerBySandboxAndVariableName(String sandboxRefId, String answerVariableName) {
        return sandboxAnswerRepository.findAnswerBySandboxAndVariableName(sandboxRefId, answerVariableName)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(SandboxAnswer.class, "variable name", answerVariableName.getClass(), answerVariableName)))
                .getAnswerContent();
    }

    @Transactional(readOnly = true)
    public String getAnswerBySandboxAndVariableName(String accessToken, Long userId, String answerVariableName) {
        return sandboxAnswerRepository.findAnswerBySandboxAndVariableName(accessToken, userId, answerVariableName)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(SandboxAnswer.class, "variable name", answerVariableName.getClass(), answerVariableName)))
                .getAnswerContent();
    }

    @Transactional(readOnly = true)
    public PageResultResource<SandboxInfoDto> getAllSandboxesAnswers(Predicate predicate, Pageable pageable) {
        Page<SandboxInfo> sandboxInfo = sandboxInfoRepository.findAll(predicate, pageable);
        return sandboxInfoMapper.mapToPageResultResource(sandboxInfo);
    }

    public void deleteCloudSandboxReferenceWithAnswers(String sandboxRefId) {
        sandboxInfoRepository.deleteBySandboxRefId(sandboxRefId);
    }

    public void deleteCloudSandboxReferenceWithAnswers(Long allocationId) {
        sandboxInfoRepository.deleteByAllocationId(allocationId);
    }


    public void deleteLocalSandboxReferenceWithAnswers(String accessToken, Long userId) {
        sandboxInfoRepository.deleteByAccessTokenAndUserId(accessToken, userId);
    }

    public void storeAllAnswersForSandbox(SandboxInfoCreateDto sandboxInfoCreateDto) {
        checkExistenceOfSandboxInfo(sandboxInfoCreateDto);
        SandboxInfo sandboxInfo = sandboxInfoMapper.mapCreateDtoToEntity(sandboxInfoCreateDto);
        sandboxInfo.getSandboxAnswers().forEach(sandboxAnswer -> sandboxAnswer.setSandboxInfo(sandboxInfo));
        sandboxInfoRepository.save(sandboxInfo);
    }

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
