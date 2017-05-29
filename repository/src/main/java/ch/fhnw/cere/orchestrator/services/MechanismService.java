package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.Mechanism;

import java.util.List;


public interface MechanismService {
    public List<Mechanism> findAll();
    public Mechanism save(Mechanism mechanism);
    public Mechanism find(long id);
    public void delete(long id);
}



