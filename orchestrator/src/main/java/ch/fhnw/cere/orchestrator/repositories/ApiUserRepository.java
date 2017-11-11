package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser, Long> {
    ApiUser findByName(@Param("name") String name);
}
