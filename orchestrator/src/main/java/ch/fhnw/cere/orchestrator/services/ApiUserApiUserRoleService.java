package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.ApiUserApiUserRole;

import java.util.List;


public interface ApiUserApiUserRoleService {
    public List<ApiUserApiUserRole> findAll();
    public ApiUserApiUserRole save(ApiUserApiUserRole apiUserApiUserRole);
    public ApiUserApiUserRole find(long id);
    public void delete(long id);
    public List<ApiUserApiUserRole> findByApiUserId(long id);
}



