package ch.fhnw.cere.orchestrator.repositories;

import ch.fhnw.cere.orchestrator.models.*;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.List;


@Projection(name = "inlineMechanism", types = { Mechanism.class })
interface MechanismProjection {

    long getId();
    MechanismType getType();
    Date getCreatedAt();
    Date getUpdatedAt();
    List<Parameter> getParameters();
}