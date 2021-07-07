package cz.muni.ics.kypo.answers.storage.service;

import cz.muni.ics.kypo.answers.storage.api.SandboxInfoCreateDto;
import cz.muni.ics.kypo.answers.storage.api.SandboxInfoDto;
import cz.muni.ics.kypo.answers.storage.api.reponses.PageResultResource;
import cz.muni.ics.kypo.answers.storage.data.entities.SandboxInfo;
import cz.muni.ics.kypo.answers.storage.data.repositories.SandboxInfoRepository;
import cz.muni.ics.kypo.answers.storage.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.answers.storage.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.answers.storage.mappers.SandboxInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Predicate;

@Service
@Transactional
public class SandboxAnswersService {

    private final SandboxInfoRepository sandboxInfoRepository;
    private final SandboxInfoMapper sandboxInfoMapper;

    @Autowired
    public SandboxAnswersService(final SandboxInfoRepository sandboxInfoRepository,
                                 final SandboxInfoMapper sandboxInfoMapper) {
        this.sandboxInfoRepository = sandboxInfoRepository;
        this.sandboxInfoMapper = sandboxInfoMapper;
    }

    @Transactional(readOnly = true)
    public SandboxInfoDto getSandboxAnswersBySandboxRefId(Long sandboxRefId) {
        SandboxInfo sandboxInfo = sandboxInfoRepository.findByRefId(sandboxRefId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(SandboxInfo.class, "id", sandboxRefId.getClass(), sandboxRefId)));
        return sandboxInfoMapper.mapToDto(sandboxInfo);
    }

    @Transactional(readOnly = true)
    public PageResultResource<SandboxInfoDto> getAllSandboxesAnswers(Predicate predicate, Pageable pageable) {
        Page<SandboxInfo> sandboxInfo = sandboxInfoRepository.findAll(predicate, pageable);
        return sandboxInfoMapper.mapToPageResultResource(sandboxInfo);
    }

    public void deleteSandboxWithAnswers(Long sandboxRefId) {
        sandboxInfoRepository.deleteSandboxInfo(sandboxRefId);
    }

    public void storeAllAnswersForSandbox(SandboxInfoCreateDto sandboxInfoCreateDto) {
        sandboxInfoRepository.save(sandboxInfoMapper.mapCreateDtoToEntity(sandboxInfoCreateDto));
    }

}
