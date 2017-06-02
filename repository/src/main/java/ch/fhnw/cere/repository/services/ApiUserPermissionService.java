package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.ApiUserPermission;

import java.util.List;


public interface ApiUserPermissionService {
    public List<ApiUserPermission> findAll();
    public ApiUserPermission save(ApiUserPermission apiUserPermission);
    public ApiUserPermission find(long id);
    public void delete(long id);
    public ApiUserPermission findByApiUserIdAndApplicationId(long apiUserId, long applicationId);
}



