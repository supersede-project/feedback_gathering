package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.MonitorTool;
import ch.fhnw.cere.orchestrator.models.MonitorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MonitorToolRepository extends JpaRepository<MonitorTool, Long> {
    MonitorTool findByMonitorTypeAndName(@Param("monitorType") MonitorType monitorType, @Param("name") String name);
}
