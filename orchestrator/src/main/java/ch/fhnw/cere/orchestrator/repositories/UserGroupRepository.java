package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    List<UserGroup> findByApplicationId(@Param("applicationId") long applicationId);

    @Query(value = "SELECT g.id, g.name, g.application_id " +
            "    FROM user_group g, `user` u " +
            "    WHERE g.id = u.user_group_id and g.application_id = ?1 and u.user_identification = ?2", nativeQuery = true)
    List<UserGroup> findByUserIdentification(long applicationId, String userIdentification);
}
