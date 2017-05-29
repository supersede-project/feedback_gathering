package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.Application;

public interface SecurityService {

    public Boolean hasAdminPermission();
    public Boolean hasAdminPermission(long applicationId);
    public Boolean hasAdminPermission(Application application);
    public Boolean hasSuperAdminPermission();
}