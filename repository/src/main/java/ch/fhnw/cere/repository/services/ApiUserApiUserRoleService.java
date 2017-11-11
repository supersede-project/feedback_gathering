package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.ApiUserApiUserRole;

import java.util.List;


public interface ApiUserApiUserRoleService {
    public List<ApiUserApiUserRole> findAll();
    public ApiUserApiUserRole save(ApiUserApiUserRole apiUserApiUserRole);
    public ApiUserApiUserRole find(long id);
    public void delete(long id);
    public List<ApiUserApiUserRole> findByApiUserId(long id);
}



