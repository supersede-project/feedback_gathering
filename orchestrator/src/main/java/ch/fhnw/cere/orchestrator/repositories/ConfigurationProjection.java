package ch.fhnw.cere.orchestrator.repositories;

import ch.fhnw.cere.orchestrator.models.Configuration;
import ch.fhnw.cere.orchestrator.models.TriggerType;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.List;


@Projection(name = "inlineConfiguration", types = { Configuration.class })
interface ConfigurationProjection {

    long getId();
    String getName();
    TriggerType getType();
    Date getCreatedAt();
    Date getUpdatedAt();
    List<MechanismProjection> getMechanisms();
}