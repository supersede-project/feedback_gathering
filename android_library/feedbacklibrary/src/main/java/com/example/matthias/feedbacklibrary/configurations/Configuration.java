package com.example.matthias.feedbacklibrary.configurations;

import com.example.matthias.feedbacklibrary.models.Mechanism;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Configuration either of type 'PUSH' or 'PULL'.
 */
public class Configuration {
    private String createdAt;
    private GeneralConfiguration generalConfiguration;
    private long id;
    private boolean isPush;
    private List<Mechanism> mechanisms;
    private String type;

    public Configuration(ConfigurationItem configurationItem) {
        createdAt = configurationItem.getCreatedAt();
        generalConfiguration = new GeneralConfiguration(configurationItem.getGeneralConfigurationItem());
        id = configurationItem.getId();
        isPush = configurationItem.getType().equals("PUSH");
        type = configurationItem.getType();
        initMechanisms(configurationItem);
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public GeneralConfiguration getGeneralConfiguration() {
        return generalConfiguration;
    }

    public long getId() {
        return id;
    }

    public List<Mechanism> getMechanisms() {
        return mechanisms;
    }

    public String getType() {
        return type;
    }

    private void initMechanisms(ConfigurationItem configurationItem) {
        mechanisms = new ArrayList<>();
        for (MechanismConfigurationItem mechanismConfigurationItem : configurationItem.getMechanismConfigurationItems()) {
            mechanisms.add(mechanismConfigurationItem.createMechanism());
        }

        Collections.sort(mechanisms, new Comparator<Mechanism>() {
            @Override
            public int compare(Mechanism a, Mechanism b) {
                return ((Integer) a.getOrder()).compareTo(b.getOrder());
            }
        });
    }

    public boolean isPush() {
        return isPush;
    }
}
