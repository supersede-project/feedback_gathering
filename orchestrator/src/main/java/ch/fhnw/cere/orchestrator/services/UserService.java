package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.User;

import java.util.List;


public interface UserService {
    public List<User> findAll();
    public User save(User user);
    public User find(long id);
    public void delete(long id);
}



