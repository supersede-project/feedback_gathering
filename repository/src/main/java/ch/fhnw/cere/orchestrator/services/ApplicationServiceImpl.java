package ch.fhnw.cere.orchestrator.services;

import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> findAll(){
        return applicationRepository.findAll();
    }

    public Application save(Application application){
        return applicationRepository.save(application);
    }

    public Application find(long id){
        return applicationRepository.findOne(id);
    }

    public List<Application> findByName(String name){
        return applicationRepository.findByName(name);
    }

    public void delete(long id){
        applicationRepository.delete(id);
    }
}