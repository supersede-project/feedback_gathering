package ch.fhnw.cere.orchestrator.services;

import ch.fhnw.cere.orchestrator.models.Parameter;
import ch.fhnw.cere.orchestrator.repositories.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
@Transactional
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    private ParameterRepository parameterRepository;

    public List<Parameter> findAll(){
        return parameterRepository.findAll();
    }

    public List<Parameter> findByLanguage(String language) {
        return parameterRepository.findByLanguage(language);
    }

    public Parameter save(Parameter application){
        return parameterRepository.save(application);
    }

    public Parameter find(long id){
        return parameterRepository.findOne(id);
    }

    public List<Parameter> findByMechanismId(long mechanismId){
        return parameterRepository.findByMechanismId(mechanismId);
    }

    @Override
    public List<Parameter> findByGeneralConfigurationId(long generalConfigurationId) {
        return parameterRepository.findByGeneralConfigurationId(generalConfigurationId);
    }

    @Override
    public List<Parameter> findByParentParameterId(long parentParameterId) {
        return parameterRepository.findByParentParameterId(parentParameterId);
    }

    public void delete(long id){
        parameterRepository.delete(id);
    }

    public List<Parameter> findByMechanismIdAndLanguage(long mechanismId, String language) {
        List<Parameter> parameters = parameterRepository.findByMechanismIdAndLanguage(mechanismId, language);
        return parameters;
    }

    @Override
    public List<Parameter> switchParameterOrderForMechanism(long mechanismId, long id, long secondId) {
        switchParameterOrder(id, secondId);
        return parameterRepository.findByMechanismIdOrderByOrder(mechanismId);
    }

    @Override
    public List<Parameter> reorderParameterForMechanism(long mechanismId, long id, int order) {
        List<Parameter> parametersToOrder = parameterRepository.findByMechanismIdOrderByOrder(mechanismId);
        return parameterRepository.save(reorderParameter(parametersToOrder, id, order));
    }

    @Override
    public List<Parameter> switchParameterOrderForGeneralConfiguration(long generalConfigurationId, long id, long secondId) {
        switchParameterOrder(id, secondId);
        return parameterRepository.findByGeneralConfigurationIdOrderByOrder(generalConfigurationId);
    }

    @Override
    public List<Parameter> reorderParameterForGeneralConfiguration(long generalConfigurationId, long id, int order) {
        List<Parameter> parametersToOrder = parameterRepository.findByGeneralConfigurationIdOrderByOrder(generalConfigurationId);
        return parameterRepository.save(reorderParameter(parametersToOrder, id, order));
    }

    @Override
    public List<Parameter> switchParameterOrderForParentParameter(long parentParameterId, long id, long secondId) {
        switchParameterOrder(id, secondId);
        return parameterRepository.findByParentParameterIdOrderByOrder(parentParameterId);
    }

    @Override
    public List<Parameter> reorderParameterForParentParameter(long parentParameterId, long id, int order) {
        List<Parameter> parametersToOrder = parameterRepository.findByParentParameterIdOrderByOrder(parentParameterId);
        return parameterRepository.save(reorderParameter(parametersToOrder, id, order));
    }

    private void switchParameterOrder(long id, long secondId) {
        Parameter firstParameter = parameterRepository.findOne(id);
        Parameter secondParameter = parameterRepository.findOne(secondId);

        int firstParameterOrder = firstParameter.getOrder();
        firstParameter.setOrder(secondParameter.getOrder());
        secondParameter.setOrder(firstParameterOrder);

        parameterRepository.saveAndFlush(firstParameter);
        parameterRepository.saveAndFlush(secondParameter);
    }

    private List<Parameter> reorderParameter(List<Parameter> parameters, long id, int order) {
        if(parameters.stream().filter(parameter -> parameter.getId() == id).findFirst() == null || order > parameters.size()) {
            return parameters;
        }
        Parameter parameterToMove = parameters.stream().filter(parameter -> parameter.getId() == id).findFirst().get();
        parameters.remove(parameterToMove);

        parameters.add(order - 1, parameterToMove);

        for(int i = 0; i < parameters.size(); i++) {
            parameters.get(i).setOrder(i + 1);
        }

        return parameters;
    }
}