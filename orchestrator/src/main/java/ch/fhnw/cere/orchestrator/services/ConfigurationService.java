package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.Configuration;

import java.util.List;


public interface ConfigurationService {
    public List<Configuration> findAll();
    public Configuration save(Configuration configuration);
    public Configuration find(long id);
    public void delete(long id);
}



