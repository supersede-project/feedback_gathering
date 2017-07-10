package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.ConfigurationMechanism;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ConfigurationMechanismRepository extends JpaRepository<ConfigurationMechanism, Long> {
    List<ConfigurationMechanism> findByConfigurationId(@Param("configurationId") long configurationId);
    List<ConfigurationMechanism> findByMechanismId(@Param("mechanismId") long mechanismId);
    List<ConfigurationMechanism> findByConfigurationIdAndMechanismId(@Param("configurationId") long configurationId, @Param("mechanismId") long mechanismId);
}
