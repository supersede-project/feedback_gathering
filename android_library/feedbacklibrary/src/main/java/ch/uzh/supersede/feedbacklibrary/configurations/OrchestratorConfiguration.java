package ch.uzh.supersede.feedbacklibrary.configurations;

import java.util.ArrayList;
import java.util.List;

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

    private void initOrchestratorConfiguration(OrchestratorConfigurationItem orchestratorConfigurationItem, boolean isPush, long selectedPullConfigurationId) {
        configurations = new ArrayList<>();
        for (ConfigurationItem configurationItem : orchestratorConfigurationItem.getConfigurationItems()) {
            Configuration configuration = new Configuration(configurationItem);
            if ((isPush && configuration.isPush()) || (!isPush && selectedPullConfigurationId == configuration.getId())) {
                activeConfiguration = configuration;
            }
            configurations.add(configuration);
        }
    }

    public Configuration getActiveConfiguration() {
        return activeConfiguration;
    }

    public void setActiveConfiguration(Configuration activeConfiguration) {
        this.activeConfiguration = activeConfiguration;
    }

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public GeneralConfiguration getGeneralConfiguration() {
        return generalConfiguration;
    }

    public void setGeneralConfiguration(GeneralConfiguration generalConfiguration) {
        this.generalConfiguration = generalConfiguration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }
}
