package ch.fhnw.cere.orchestrator.services;

import ch.fhnw.cere.orchestrator.models.Configuration;
import ch.fhnw.cere.orchestrator.repositories.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    public List<Configuration> findAll(){
        return configurationRepository.findAll();
    }

    public Configuration save(Configuration configuration){
        return configurationRepository.save(configuration);
    }

    public Configuration find(long id){
        return configurationRepository.findOne(id);
    }

    public void delete(long id){
        configurationRepository.delete(id);
    }

    @Override
    public List<Configuration> findByApplicationId(long applicationId) {
        return configurationRepository.findByApplicationId(applicationId);
    }
}