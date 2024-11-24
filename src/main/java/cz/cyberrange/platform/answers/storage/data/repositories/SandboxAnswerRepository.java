package cz.cyberrange.platform.answers.storage.data.repositories;

import cz.cyberrange.platform.answers.storage.data.entities.SandboxAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SandboxAnswerRepository extends JpaRepository<SandboxAnswer, Long>, QuerydslPredicateExecutor<SandboxAnswer> {

    @Query("SELECT sa FROM SandboxInfo si INNER JOIN si.sandboxAnswers sa WHERE " +
            "si.sandboxRefId = :sandboxRefId AND sa.answerVariableName = :answerVariableName")
    Optional<SandboxAnswer> findAnswerBySandboxAndVariableName(@Param("sandboxRefId") String sandboxRefId,
                                                               @Param("answerVariableName") String answerVariableName);

    @Query("SELECT sa FROM SandboxInfo si INNER JOIN si.sandboxAnswers sa WHERE " +
            "si.accessToken = :accessToken AND si.userId = :userId AND sa.answerVariableName = :answerVariableName")
    Optional<SandboxAnswer> findAnswerBySandboxAndVariableName(@Param("accessToken") String accessToken,
                                                               @Param("userId") Long userId,
                                                               @Param("answerVariableName") String answerVariableName);
}
