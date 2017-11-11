package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.ApiUserApiUserRole;
import ch.fhnw.cere.repository.repositories.ApiUserApiUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ApiUserApiUserRoleServiceImpl implements ApiUserApiUserRoleService {

    @Autowired
    private ApiUserApiUserRoleRepository apiUserApiUserRoleRepository;

    public List<ApiUserApiUserRole> findAll(){
        return apiUserApiUserRoleRepository.findAll();
    }

    public ApiUserApiUserRole save(ApiUserApiUserRole apiUserApiUserRole){
        return apiUserApiUserRoleRepository.save(apiUserApiUserRole);
    }

    public ApiUserApiUserRole find(long id){
        return apiUserApiUserRoleRepository.findOne(id);
    }

    public void delete(long id){
        apiUserApiUserRoleRepository.delete(id);
    }

    public List<ApiUserApiUserRole> findByApiUserId(long apiUserId) {
        return apiUserApiUserRoleRepository.findByApiUserId(apiUserId);
    }
}