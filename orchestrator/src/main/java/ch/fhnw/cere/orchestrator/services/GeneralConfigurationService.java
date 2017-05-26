package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;

import java.util.List;


public interface GeneralConfigurationService {
    public List<GeneralConfiguration> findAll();
    public GeneralConfiguration save(GeneralConfiguration generalConfiguration);
    public GeneralConfiguration find(long id);
    public void delete(long id);
    public GeneralConfiguration findByApplicationId(long id);
    public GeneralConfiguration findByConfigurationId(long id);
}



