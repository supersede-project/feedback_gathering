package ch.fhnw.cere.orchestrator.services;

import ch.fhnw.cere.orchestrator.models.Mechanism;
import ch.fhnw.cere.orchestrator.repositories.MechanismRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class MechanismServiceImpl implements MechanismService {

    @Autowired
    private MechanismRepository mechanismRepository;

    public List<Mechanism> findAll(){
        return mechanismRepository.findAll();
    }

    public Mechanism save(Mechanism application){
        return mechanismRepository.save(application);
    }

    public Mechanism find(long id){
        return mechanismRepository.findOne(id);
    }

    public void delete(long id){
        mechanismRepository.delete(id);
    }
}