package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.ApiUser;
import ch.fhnw.cere.orchestrator.models.ApiUserPermission;
import ch.fhnw.cere.orchestrator.models.ApiUserRole;
import ch.fhnw.cere.orchestrator.models.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private ApiUserPermissionService apiUserPermissionService;

    @Override
    public Boolean hasAdminPermission() {
        return isSuperAdmin() || isAdmin();
    }

    @Override
    public Boolean hasAdminPermission(long applicationId) {
        return isSuperAdmin() || (isAdmin() && isAdminOfApplication(getApiUser().getId(), applicationId));
    }

    @Override
    public Boolean hasAdminPermission(Application application) {
        return isSuperAdmin() || (isAdmin() && isAdminOfApplication(getApiUser().getId(), application.getId()));
    }

    @Override
    public Boolean hasSuperAdminPermission() {
        return isSuperAdmin();
    }

    private boolean isSuperAdmin() {
        return (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(Object::toString).collect(Collectors.toList()).contains(new SimpleGrantedAuthority(ApiUserRole.SUPER_ADMIN.getAuthority()).toString()));
    }

    private boolean isAdmin() {
        return (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(Object::toString).collect(Collectors.toList()).contains(new SimpleGrantedAuthority(ApiUserRole.ADMIN.getAuthority()).toString()));
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