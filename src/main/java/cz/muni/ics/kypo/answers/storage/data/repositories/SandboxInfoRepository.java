package cz.muni.ics.kypo.answers.storage.data.repositories;

import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.answers.storage.data.entities.SandboxInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SandboxInfoRepository extends JpaRepository<SandboxInfo, Long>, QuerydslPredicateExecutor<SandboxInfo> {

    @Query("SELECT si FROM SandboxInfo si JOIN FETCH si.sandboxAnswers WHERE si.sandboxRefId = :sandboxRefId")
    Optional<SandboxInfo> findByRefId(@Param("sandboxRefId") Long sandboxRefId);

    @EntityGraph(attributePaths = {"sandboxAnswers"})
    Page<SandboxInfo> findAll(Predicate predicate, Pageable pageable);

    void deleteBySandboxRefId(Long sandboxRefId);

}
