package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.AndroidUser;

import java.util.List;

public interface AndroidUserService {
    public List<AndroidUser> findAll();
    public AndroidUser save(AndroidUser androidUser);
    public AndroidUser find(long id);
    public void delete(long id);
    public AndroidUser findByName(String name);
}
