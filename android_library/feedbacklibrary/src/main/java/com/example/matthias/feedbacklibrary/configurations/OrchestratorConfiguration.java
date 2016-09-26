package com.example.matthias.feedbacklibrary.configurations;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrator configuration.
 */
public class OrchestratorConfiguration {
    private Configuration activeConfiguration;
    private List<Configuration> configurations;
    private String createdAt;
    private GeneralConfiguration generalConfiguration;
    private long id;
    private String name;
    private long state;

    public OrchestratorConfiguration(OrchestratorConfigurationItem orchestratorConfigurationItem, boolean isPush, long selectedPullConfigurationId) {
        createdAt = orchestratorConfigurationItem.getCreatedAt();
        generalConfiguration = new GeneralConfiguration(orchestratorConfigurationItem.getGeneralConfigurationItem());
        id = orchestratorConfigurationItem.getId();
        name = orchestratorConfigurationItem.getName();
        state = orchestratorConfigurationItem.getState();
        initOrchestratorConfiguration(orchestratorConfigurationItem, isPush, selectedPullConfigurationId);
    }

    public Configuration getActiveConfiguration() {
        return activeConfiguration;
    }

    public List<Configuration> getConfigurations() {
        return configurations;
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

    public String getName() {
        return name;
    }

    public long getState() {
        return state;
    }

    private void initOrchestratorConfiguration(OrchestratorConfigurationItem orchestratorConfigurationItem, boolean isPush, long selectedPullConfigurationId) {
        configurations = new ArrayList<>();
        for (ConfigurationItem configurationItem : orchestratorConfigurationItem.getConfigurationItems()) {
            Configuration configuration = new Configuration(configurationItem);
            if((isPush && configuration.isPush()) || (!isPush && selectedPullConfigurationId == configuration.getId())) {
                activeConfiguration = configuration;
            }
            configurations.add(configuration);
        }
    }
}
