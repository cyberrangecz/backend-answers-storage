package cz.cyberrange.platform.answers.storage.data.repositories;

import com.querydsl.core.types.Predicate;
import cz.cyberrange.platform.answers.storage.data.entities.SandboxInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Queries for sandboxes and their stored answers, addressed either by reference id or by
 * access token and user id.
 */
@Repository
public interface SandboxInfoRepository extends JpaRepository<SandboxInfo, Long>, QuerydslPredicateExecutor<SandboxInfo> {

    /**
     * Finds the sandbox with the given reference id. Its answers come back loaded.
     *
     * @param sandboxRefId reference id of the sandbox
     * @return the matching sandbox, or empty when none matches
     */
    @Query("SELECT si FROM SandboxInfo si JOIN FETCH si.sandboxAnswers WHERE si.sandboxRefId = :sandboxRefId")
    Optional<SandboxInfo> findBySandboxRefId(@Param("sandboxRefId") String sandboxRefId);

    /**
     * Finds the sandbox with the given access token and user id. Its answers come back loaded.
     *
     * @param accessToken access token of the sandbox
     * @param userId id of the owning user
     * @return the matching sandbox, or empty when none matches
     */
    @Query("SELECT si FROM SandboxInfo si JOIN FETCH si.sandboxAnswers WHERE si.accessToken = :accessToken AND si.userId = :userId")
    Optional<SandboxInfo> findByAccessTokenAndUserIdId(@Param("accessToken") String accessToken,
                                                       @Param("userId") Long userId);

    /**
     * Finds one page of the sandboxes matching the given predicate. Their answers come back
     * loaded through the declared entity graph.
     *
     * @param predicate condition the sandboxes must satisfy
     * @param pageable page and sort to apply
     * @return the matching page, empty when nothing matches
     */
    @EntityGraph(attributePaths = {"sandboxAnswers"})
    Page<SandboxInfo> findAll(Predicate predicate, Pageable pageable);

    /**
     * Deletes the sandbox with the given reference id and its answers. Does nothing when no
     * sandbox has that reference id.
     *
     * @param sandboxRefId reference id of the sandbox
     */
    void deleteBySandboxRefId(String sandboxRefId);

    /**
     * Deletes every sandbox with the given allocation id and their answers. Does nothing when no
     * sandbox has that allocation id.
     *
     * @param allocationId allocation id of the sandboxes
     */
    void deleteByAllocationId(Long allocationId);

    /**
     * Deletes the sandbox with the given access token and user id and its answers. Does nothing
     * when no sandbox matches both values.
     *
     * @param accessToken access token of the sandbox
     * @param userId id of the owning user
     */
    void deleteByAccessTokenAndUserId(String accessToken, Long userId);

    /**
     * Reports whether a sandbox with the given reference id exists.
     *
     * @param sandboxRefId reference id to check
     * @return true when such a sandbox exists
     */
    boolean existsBySandboxRefId(String sandboxRefId);

    /**
     * Reports whether a sandbox with the given user id and access token exists.
     *
     * @param userId id of the owning user
     * @param accessToken access token to check
     * @return true when such a sandbox exists
     */
    boolean existsByUserIdAndAccessToken(Long userId, String accessToken);

}
