package ch.fhnw.cere.orchestrator.services;

import ch.fhnw.cere.orchestrator.models.ApiUser;
import ch.fhnw.cere.orchestrator.repositories.ApiUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ApiUserServiceImpl implements ApiUserService {

    @Autowired
    private ApiUserRepository apiUserRepository;

    public List<ApiUser> findAll(){
        return apiUserRepository.findAll();
    }

    public ApiUser save(ApiUser apiUser){
        return apiUserRepository.save(apiUser);
    }

    public ApiUser find(long id){
        return apiUserRepository.findOne(id);
    }

    public void delete(long id){
        apiUserRepository.delete(id);
    }

    public ApiUser findByName(String name) {
        return apiUserRepository.findByName(name);
    }
}