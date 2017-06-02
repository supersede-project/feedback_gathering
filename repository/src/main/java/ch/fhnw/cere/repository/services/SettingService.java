package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.Setting;

import java.util.List;


public interface SettingService {
    public List<Setting> findAll();
    public Setting save(Setting setting);
    public Setting find(long id);
    public void delete(long id);
    Setting findByApplicationId(long applicationId);
}



