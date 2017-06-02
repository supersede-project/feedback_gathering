package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.ApiUserPermission;

import java.util.List;


public interface ApiUserPermissionService {
    public List<ApiUserPermission> findAll();
    public ApiUserPermission save(ApiUserPermission apiUserPermission);
    public ApiUserPermission find(long id);
    public void delete(long id);
    public ApiUserPermission findByApiUserIdAndApplicationId(long apiUserId, long applicationId);
}



