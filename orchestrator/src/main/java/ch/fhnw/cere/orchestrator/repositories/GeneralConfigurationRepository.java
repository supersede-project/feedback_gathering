package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GeneralConfigurationRepository extends JpaRepository<GeneralConfiguration, Long> {
    GeneralConfiguration findByApplicationId(@Param("applicationId") long applicationId);
    GeneralConfiguration findByConfigurationId(@Param("configurationId") long configurationId);
}
