package cz.cyberrange.platform.answers.storage.data.repositories;

import cz.cyberrange.platform.answers.storage.data.entities.SandboxAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Queries for the sandbox answer stored against a sandbox and an answer variable name.
 */
@Repository
public interface SandboxAnswerRepository extends JpaRepository<SandboxAnswer, Long>, QuerydslPredicateExecutor<SandboxAnswer> {

    /**
     * Finds the answer stored for the given variable name on the sandbox with the given
     * reference id.
     *
     * @param sandboxRefId reference id of the sandbox
     * @param answerVariableName variable name of the answer
     * @return the matching answer, or empty when none matches
     */
    @Query("SELECT sa FROM SandboxInfo si INNER JOIN si.sandboxAnswers sa WHERE " +
            "si.sandboxRefId = :sandboxRefId AND sa.answerVariableName = :answerVariableName")
    Optional<SandboxAnswer> findAnswerBySandboxAndVariableName(@Param("sandboxRefId") String sandboxRefId,
                                                               @Param("answerVariableName") String answerVariableName);

    /**
     * Finds the answer stored for the given variable name on the sandbox with the given access
     * token and user id.
     *
     * @param accessToken access token of the sandbox
     * @param userId id of the user owning the sandbox
     * @param answerVariableName variable name of the answer
     * @return the matching answer, or empty when none matches
     */
    @Query("SELECT sa FROM SandboxInfo si INNER JOIN si.sandboxAnswers sa WHERE " +
            "si.accessToken = :accessToken AND si.userId = :userId AND sa.answerVariableName = :answerVariableName")
    Optional<SandboxAnswer> findAnswerBySandboxAndVariableName(@Param("accessToken") String accessToken,
                                                               @Param("userId") Long userId,
                                                               @Param("answerVariableName") String answerVariableName);
}
