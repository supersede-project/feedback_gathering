package com.example.matthias.feedbacklibrary.configurations;

import com.example.matthias.feedbacklibrary.models.Mechanism;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Feedback configuration used in the feedback activity.
 */
public class FeedbackConfiguration {
    // TODO: Set actual application name
    private String application = null;
    // TODO: Set actual configuration version
    private float configVersion = -1.0f;
    // TODO: Set actual user of the feedback application
    private String user = null;
    private boolean isPush;

    // All push mechanisms, i.e., mechanisms that are NOT in a pull configuration
    private List<Mechanism> allPushMechanisms = null;
    // All pull configurations
    private List<PullConfiguration> allPullConfigurations = null;
    // General configuration
    private GeneralConfiguration generalConfiguration = null;
    // The current mechanisms used
    private List<Mechanism> allCurrentMechanisms = null;

    public FeedbackConfiguration(OrchestratorConfiguration orchestratorConfiguration, boolean isPush, int selectedPullConfigurationIndex) {
        setApplication("Android Application");
        setConfigVersion(1.0f);
        setUser("Android User");
        initFeedbackConfiguration(orchestratorConfiguration, isPush, selectedPullConfigurationIndex);
    }

    public List<Mechanism> getAllCurrentMechanisms() {
        return allCurrentMechanisms;
    }

    public List<PullConfiguration> getAllPullConfigurations() {
        return allPullConfigurations;
    }

    public List<Mechanism> getAllPushMechanisms() {
        return allPushMechanisms;
    }

    public String getApplication() {
        return application;
    }

    public float getConfigVersion() {
        return configVersion;
    }

    public GeneralConfiguration getGeneralConfiguration() {
        return generalConfiguration;
    }

    public String getUser() {
        return user;
    }

    /**
     * This method initializes the feedback configuration.
     *
     * @param orchestratorConfiguration      the configuration retrieved from the orchestrator
     * @param isPush                         if the feedback activity is started via a pull (false) or push (true) fashion
     * @param selectedPullConfigurationIndex the index of the selected pull configuration, -1 in case of push
     */
    private void initFeedbackConfiguration(OrchestratorConfiguration orchestratorConfiguration, boolean isPush, int selectedPullConfigurationIndex) {
        setPush(isPush);

        allPushMechanisms = new ArrayList<>();
        allPullConfigurations = new ArrayList<>();

        for (MechanismConfigurationItem mechanismConfigurationItem : orchestratorConfiguration.getMechanisms()) {
            allPushMechanisms.add(mechanismConfigurationItem.createMechanism());
        }

        Collections.sort(allPushMechanisms, new Comparator<Mechanism>() {
            @Override
            public int compare(Mechanism a, Mechanism b) {
                return ((Integer) a.getOrder()).compareTo(b.getOrder());
            }
        });

        for (PullConfigurationItem item : orchestratorConfiguration.getPullConfigurationItems()) {
            allPullConfigurations.add(new PullConfiguration(item));
        }

        generalConfiguration = new GeneralConfiguration(orchestratorConfiguration.getGeneralConfigurationItems());

        if (isPush() && selectedPullConfigurationIndex == -1) {
            allCurrentMechanisms = allPushMechanisms;
        } else if (!isPush() && selectedPullConfigurationIndex != -1) {
            allCurrentMechanisms = allPullConfigurations.get(selectedPullConfigurationIndex).getAllPullMechanisms();
        }
    }

    public boolean isPush() {
        return isPush;
    }

    public void setAllCurrentMechanisms(List<Mechanism> allCurrentMechanisms) {
        this.allCurrentMechanisms = allCurrentMechanisms;
    }

    public void setAllPullConfigurations(List<PullConfiguration> allPullConfigurations) {
        this.allPullConfigurations = allPullConfigurations;
    }

    public void setAllPushMechanisms(List<Mechanism> allPushMechanisms) {
        this.allPushMechanisms = allPushMechanisms;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setConfigVersion(float configVersion) {
        this.configVersion = configVersion;
    }

    public void setGeneralConfiguration(GeneralConfiguration generalConfiguration) {
        this.generalConfiguration = generalConfiguration;
    }

    public void setPush(boolean push) {
        isPush = push;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
