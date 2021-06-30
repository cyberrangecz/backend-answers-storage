package cz.muni.ics.kypo.answers.storage.service;

import cz.muni.ics.kypo.answers.storage.api.SandboxInfoDto;
import cz.muni.ics.kypo.answers.storage.data.entities.SandboxInfo;
import cz.muni.ics.kypo.answers.storage.data.repositories.SandboxAnswersRepository;
import cz.muni.ics.kypo.answers.storage.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.answers.storage.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.answers.storage.mappers.SandboxAnswersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SandboxAnswersService {

    private SandboxAnswersRepository sandboxAnswersRepository;
    private SandboxAnswersMapper sandboxAnswersMapper;

    @Autowired
    public SandboxAnswersService(SandboxAnswersRepository sandboxAnswersRepository,
                                 SandboxAnswersMapper sandboxAnswersMapper) {
        this.sandboxAnswersRepository = sandboxAnswersRepository;
        this.sandboxAnswersMapper = sandboxAnswersMapper;
    }

    @Transactional(readOnly = true)
    public SandboxInfoDto getSandboxAnswersInfo(Long sandboxRefId) {
        SandboxInfo sandboxInfo = sandboxAnswersRepository.findByRefId(sandboxRefId).orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(SandboxInfo.class, "id", sandboxRefId.getClass(), sandboxRefId)));
        return sandboxAnswersMapper.mapToDto(sandboxInfo);
    }
}
