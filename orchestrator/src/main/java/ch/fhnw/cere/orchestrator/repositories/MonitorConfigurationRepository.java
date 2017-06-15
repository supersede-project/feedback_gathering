package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.MonitorConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MonitorConfigurationRepository extends JpaRepository<MonitorConfiguration, Long> {
}
