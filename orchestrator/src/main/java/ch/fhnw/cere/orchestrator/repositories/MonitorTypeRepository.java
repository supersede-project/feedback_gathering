package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.MonitorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MonitorTypeRepository extends JpaRepository<MonitorType, Long> {
    MonitorType findByName(@Param("name") String name);
}
