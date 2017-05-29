package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.ApiUser;

import java.util.List;


public interface ApiUserService {
    public List<ApiUser> findAll();
    public ApiUser save(ApiUser apiUser);
    public ApiUser find(long id);
    public void delete(long id);
    public ApiUser findByName(String name);
}



