package ch.fhnw.cere.repository.services;


public interface SecurityService {

    public Boolean hasAdminPermission();
    public Boolean hasAdminPermission(long applicationId);
    public Boolean hasSuperAdminPermission();
}