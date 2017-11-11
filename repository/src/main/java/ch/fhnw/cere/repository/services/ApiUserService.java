package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.ApiUser;

import java.util.List;


public interface ApiUserService {
    public List<ApiUser> findAll();
    public ApiUser save(ApiUser apiUser);
    public ApiUser find(long id);
    public void delete(long id);
    public ApiUser findByName(String name);
}



