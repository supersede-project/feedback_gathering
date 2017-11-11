package ch.fhnw.cere.repository.models;


import org.springframework.security.core.GrantedAuthority;


/**
 * ADMIN can access the API for certain applications.
 *
 * SUPER_ADMIN has all the permissions and can also create api users.
 */
public enum ApiUserRole implements GrantedAuthority {
    ADMIN,
    SUPER_ADMIN;

    private String authority;

    static {
        ADMIN.authority = "ADMIN";
        SUPER_ADMIN.authority = "SUPER_ADMIN";
    }

    public String getAuthority() {
        return authority;
    }
}