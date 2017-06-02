package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.ApiUserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiUserPermissionRepository extends JpaRepository<ApiUserPermission, Long> {
    ApiUserPermission findByApiUserIdAndApplicationId(@Param("apiUserId") long apiUserId, @Param("applicationId") long applicationId);
}
