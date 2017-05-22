package ch.fhnw.cere.orchestrator.repositories;


import java.util.List;

import ch.fhnw.cere.orchestrator.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByName(@Param("name") String name);
}
