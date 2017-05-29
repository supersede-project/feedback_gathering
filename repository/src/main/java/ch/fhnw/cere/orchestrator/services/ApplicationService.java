package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.Application;

import java.util.List;


public interface ApplicationService {
    public List<Application> findAll();
    public Application save(Application application);
    public Application find(long id);
    public List<Application> findByName(String name);
    public void delete(long id);
}



