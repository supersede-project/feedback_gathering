package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    List<UserGroup> findByApplicationId(@Param("applicationId") long applicationId);
}
