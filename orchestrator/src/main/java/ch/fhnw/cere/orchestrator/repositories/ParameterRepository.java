package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {
    List<Parameter> findByMechanismId(@Param("mechanism_id") long mechanismId);
    List<Parameter> findByGeneralConfigurationId(@Param("general_configuration_id") long generalConfigurationId);
    List<Parameter> findByParentParameterId(@Param("parent_parameter_id") long parentParameterId);
    List<Parameter> findByMechanismIdOrderByOrder(@Param("mechanism_id") long mechanismId);
    List<Parameter> findByGeneralConfigurationIdOrderByOrder(@Param("general_configuration_id") long generalConfigurationId);
    List<Parameter> findByParentParameterIdOrderByOrder(@Param("parent_parameter_id") long parentParameterId);
    List<Parameter> findByLanguage(@Param("language") String language);

    @Query(value = "SELECT " +
            "COALESCE(p2.id, p.id) as id, p.key, " +
            "COALESCE(p2.value, p.value) as value, " +
            "COALESCE(p2.language, p.language) as language, " +
            "p.created_at, p.updated_at, p.general_configuration_id, p.mechanism_id, p.parent_parameter_id, p.order " +
            "    FROM parameter p " +
            "        LEFT OUTER JOIN parameter p2 ON p.key=p2.key and p2.language = ?2 and p2.mechanism_id = ?1 " +
            "    WHERE p.language='en' and p.mechanism_id = ?1 ORDER BY p.order ASC", nativeQuery = true)
    List<Parameter> findByMechanismIdAndLanguage(long mechanismId, String language);
}
