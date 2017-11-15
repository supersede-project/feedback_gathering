package ch.fhnw.cere.orchestrator.services;

import ch.fhnw.cere.orchestrator.models.ApiUserPermission;
import ch.fhnw.cere.orchestrator.repositories.ApiUserPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ApiUserPermissionServiceImpl implements ApiUserPermissionService {

    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    public List<ApiUserPermission> findAll(){
        return apiUserPermissionRepository.findAll();
    }

    @Override
    public ApiUserPermission save(ApiUserPermission apiUserPermission) {
        return null;
    }

    public ApiUserPermission find(long id){
        return apiUserPermissionRepository.findOne(id);
    }

    public void delete(long id){
        apiUserPermissionRepository.delete(id);
    }

    @Override
    public ApiUserPermission findByApiUserIdAndApplicationId(long apiUserId, long applicationId) {
        return apiUserPermissionRepository.findByApiUserIdAndApplicationId(apiUserId, applicationId);
    }

    @Override
    public List<ApiUserPermission> findByApiUserId(long apiUserId) {
        return apiUserPermissionRepository.findByApiUserId(apiUserId);
    }
}