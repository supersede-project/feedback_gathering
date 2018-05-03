package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.AndroidUser;
import ch.fhnw.cere.repository.repositories.AndroidUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class AndroidUserServiceImpl implements AndroidUserService {

    @Autowired
    private AndroidUserRepository androidUserRepository;

    public List<AndroidUser> findAll(){
        return androidUserRepository.findAll();
    }

    public AndroidUser save(AndroidUser androidUserRole){
        return androidUserRepository.save(androidUserRole);
    }

    public AndroidUser find(long id){
        return androidUserRepository.findOne(id);
    }

    public void delete(long id){
        androidUserRepository.delete(id);
    }

    public AndroidUser findByName(String name) {
        return null;
    }

    public List<AndroidUser> findByApplicationId(long id) {
        return androidUserRepository.findByApplicationId(id);
    }

}