package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.ApiUserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    public Boolean hasAdminPermission() {
        // TODO check whether always admin user is in security context
        return isSuperAdmin() || isAdmin();
    }

    @Override
    public Boolean hasAdminPermission(long applicationId) {
        // TODO check application permission
        return isSuperAdmin() || isAdmin();
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
}