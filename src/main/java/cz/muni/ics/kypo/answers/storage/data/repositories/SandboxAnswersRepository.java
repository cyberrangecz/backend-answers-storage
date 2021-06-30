package cz.muni.ics.kypo.answers.storage.data.repositories;

import cz.muni.ics.kypo.answers.storage.data.entities.SandboxInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SandboxAnswersRepository extends JpaRepository<SandboxInfo, Long>, QuerydslPredicateExecutor<SandboxInfo> {

    @Query("SELECT s FROM SandboxInfo s WHERE s.sandboxRefId = :id")
    Optional<SandboxInfo> findByRefId(@Param("id") Long id);
}
