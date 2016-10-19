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

    /**
     * This method returns the date of creation as a string.
     *
     * @return the creation date as a string
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * This method returns the general configuration.
     *
     * @return the general configuration
     */
    public GeneralConfiguration getGeneralConfiguration() {
        return generalConfiguration;
    }

    /**
     * This method returns the id of the configuration.
     *
     * @return the configuration id
     */
    public long getId() {
        return id;
    }

    /**
     * This method returns all mechanisms of the configuration.
     *
     * @return all mechanisms
     */
    public List<Mechanism> getMechanisms() {
        return mechanisms;
    }

    /**
     * This method returns the type of the configuration.
     *
     * @return the type, either 'PUSH' or 'PULL'
     */
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

    /**
     * This method returns if the configuration is of type 'PUSH'.
     *
     * @return true if the configuration is of type 'PUSH', false otherwise
     */
    public boolean isPush() {
        return isPush;
    }
}
