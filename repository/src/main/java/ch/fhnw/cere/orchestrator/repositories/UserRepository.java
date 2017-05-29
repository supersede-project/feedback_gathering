package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByApplicationId(@Param("applicationId") long applicationId);
    List<User> findByUserGroupId(@Param("userGroupId") long userGroupId);
}
