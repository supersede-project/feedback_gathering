package ch.fhnw.cere.orchestrator.services;

import ch.fhnw.cere.orchestrator.models.User;
import ch.fhnw.cere.orchestrator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User find(long id){
        return userRepository.findOne(id);
    }

    public void delete(long id){
        userRepository.delete(id);
    }
}