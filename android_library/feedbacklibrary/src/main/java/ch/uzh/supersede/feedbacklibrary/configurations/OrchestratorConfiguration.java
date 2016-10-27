package ch.uzh.supersede.feedbacklibrary.configurations;

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

    /**
     * This method returns the active configuration.
     *
     * @return the active configuration
     */
    public Configuration getActiveConfiguration() {
        return activeConfiguration;
    }

    /**
     * This method returns all configurations.
     *
     * @return all configurations
     */
    public List<Configuration> getConfigurations() {
        return configurations;
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
     * This method returns the general configuration of the orchestrator configuration.
     *
     * @return the general configuration
     */
    public GeneralConfiguration getGeneralConfiguration() {
        return generalConfiguration;
    }

    /**
     * This method returns the id of the orchestrator configuration, i.e., the application id.
     *
     * @return the orchestrator configuration id, i.e., the application id
     */
    public long getId() {
        return id;
    }

    /**
     * This method returns the name of the orchestrator configuration, i.e., the application name.
     *
     * @return the orchestrator configuration name, i.e., the application name
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the state of the orchestrator configuration, i.e., the application state.
     *
     * @return the orchestrator configuration state, i.e., the application state
     */
    public long getState() {
        return state;
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
}
