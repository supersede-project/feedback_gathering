package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.Parameter;

import java.util.List;


public interface ParameterService {
    public List<Parameter> findAll();
    public List<Parameter> findByLanguage(String language);
    public Parameter save(Parameter parameter);
    public Parameter find(long id);
    public List<Parameter> findByMechanismId(long mechanismId);
    public List<Parameter> findByGeneralConfigurationId(long generalConfigurationId);
    public List<Parameter> findByParentParameterId(long parentParameterId);
    public void delete(long id);
    public List<Parameter> findByMechanismIdAndLanguage(long mechanismId, String language);
    public List<Parameter> switchParameterOrderForMechanism(long mechanismId, long id, long secondId);
    public List<Parameter> reorderParameterForMechanism(long mechanismId, long id, int order);
    public List<Parameter> switchParameterOrderForGeneralConfiguration(long generalConfigurationId, long id, long secondId);
    public List<Parameter> reorderParameterForGeneralConfiguration(long generalConfigurationId, long id, int order);
    public List<Parameter> switchParameterOrderForParentParameter(long parentParameterId, long id, long secondId);
    public List<Parameter> reorderParameterForParentParameter(long parentParameterId, long id, int order);
}



