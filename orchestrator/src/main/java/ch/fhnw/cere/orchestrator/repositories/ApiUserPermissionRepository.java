package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.ApiUserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ApiUserPermissionRepository extends JpaRepository<ApiUserPermission, Long> {
    ApiUserPermission findByApiUserIdAndApplicationId(@Param("apiUserId") long apiUserId, @Param("applicationId") long applicationId);
    List<ApiUserPermission> findByApiUserId(@Param("apiUserId") long apiUserId);
}
