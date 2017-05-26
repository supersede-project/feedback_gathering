package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.ConfigurationMechanism;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ConfigurationMechanismRepository extends JpaRepository<ConfigurationMechanism, Long> {
}
