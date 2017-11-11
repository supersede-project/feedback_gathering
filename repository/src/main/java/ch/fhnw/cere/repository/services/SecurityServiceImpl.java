package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.ApiUser;
import ch.fhnw.cere.repository.models.ApiUserPermission;
import ch.fhnw.cere.repository.models.ApiUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    ApiUserPermissionService apiUserPermissionService;

    @Override
    public Boolean hasAdminPermission(long applicationId) {
        return isSuperAdmin() || (isAdmin() && isAdminOfApplication(getApiUser().getId(), applicationId));
    }

    @Override
    public Boolean hasSuperAdminPermission() {
        return isSuperAdmin();
    }

    private boolean isSuperAdmin() {
        return (getApiUser().getAuthorities().stream().map(Object::toString).collect(Collectors.toList()).contains(new SimpleGrantedAuthority(ApiUserRole.SUPER_ADMIN.getAuthority()).toString()));
    }

    private boolean isAdmin() {
        return (getApiUser().getAuthorities().stream().map(Object::toString).collect(Collectors.toList()).contains(new SimpleGrantedAuthority(ApiUserRole.ADMIN.getAuthority()).toString()));
    }

    private boolean isAdminOfApplication(long apiUserId, long applicationId) {
        ApiUserPermission apiUserPermission = apiUserPermissionService.findByApiUserIdAndApplicationId(apiUserId, applicationId);
        return apiUserPermission != null && apiUserPermission.hasPermission();
    }

    private ApiUser getApiUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (ApiUser) authentication.getPrincipal();
    }
}