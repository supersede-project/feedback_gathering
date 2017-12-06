package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EndUserRepository extends JpaRepository<EndUser, Long> {
    EndUser findByUsername(@Param("username") String username);
    List<EndUser> findByApplicationId(@Param("applicationId") long applicationId);
}
