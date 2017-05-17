package ch.fhnw.cere.orchestrator.repositories;


import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.Configuration;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;


@Projection(name = "applicationProjection", types = { Application.class })
public interface ApplicationProjection {

    long getId();
    String getName();
    List<ConfigurationProjection> getConfigurations();

}