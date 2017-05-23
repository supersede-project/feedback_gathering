package ch.fhnw.cere.orchestrator.services;

import ch.fhnw.cere.orchestrator.models.Parameter;
import ch.fhnw.cere.orchestrator.repositories.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    private ParameterRepository parameterRepository;

    public List<Parameter> findAll(){
        return parameterRepository.findAll();
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

    public void delete(long id){
        parameterRepository.delete(id);
    }
}