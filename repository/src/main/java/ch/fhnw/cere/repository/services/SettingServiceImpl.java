package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.Setting;
import ch.fhnw.cere.repository.repositories.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Override
    public List<Setting> findAll() {
        return settingRepository.findAll();
    }

    @Override
    public Setting save(Setting setting) {
        return settingRepository.save(setting);
    }

    @Override
    public Setting find(long id) {
        return settingRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        settingRepository.delete(id);
    }

    @Override
    public Setting findByApplicationId(long applicationId) {
        return settingRepository.findByApplicationId(applicationId);
    }
}
