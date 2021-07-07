package cz.muni.ics.kypo.answers.storage.data.repositories;

import cz.muni.ics.kypo.answers.storage.data.entities.SandboxAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SandboxAnswerRepository extends JpaRepository<SandboxAnswer, Long>, QuerydslPredicateExecutor<SandboxAnswer> {

    @Query("SELECT sa FROM SandboxAnswer sa JOIN FETCH SandboxInfo si WHERE " +
            "si.sandboxRefId = :sandboxRefId AND sa.answerIdentifier = :answerIdentifier")
    Optional<SandboxAnswer> findAnswerBySandboxAndIdentifier(@Param("sandboxRefId") Long sandboxRefId, @Param("answerIdentifier") String answerIdentifier);
}
