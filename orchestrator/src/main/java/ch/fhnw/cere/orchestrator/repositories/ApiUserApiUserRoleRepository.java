package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.ApiUserApiUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ApiUserApiUserRoleRepository extends JpaRepository<ApiUserApiUserRole, Long> {
    List<ApiUserApiUserRole> findByApiUserId(@Param("apiUserId") long apiUserId);
}
