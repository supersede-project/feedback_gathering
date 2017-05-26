package ch.fhnw.cere.orchestrator.services;

import ch.fhnw.cere.orchestrator.models.GeneralConfiguration;
import ch.fhnw.cere.orchestrator.repositories.GeneralConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class GeneralConfigurationServiceImpl implements GeneralConfigurationService {

    @Autowired
    private GeneralConfigurationRepository generalConfigurationRepository;

    public List<GeneralConfiguration> findAll(){
        return generalConfigurationRepository.findAll();
    }

    public GeneralConfiguration save(GeneralConfiguration generalConfiguration){
        return generalConfigurationRepository.save(generalConfiguration);
    }

    public GeneralConfiguration find(long id){
        return generalConfigurationRepository.findOne(id);
    }

    public void delete(long id){
        generalConfigurationRepository.delete(id);
    }

    public GeneralConfiguration findByApplicationId(long applicationId){
        return generalConfigurationRepository.findByApplicationId(applicationId);
    }

    public GeneralConfiguration findByConfigurationId(long configurationId){
        return generalConfigurationRepository.findByConfigurationId(configurationId);
    }
}