package ch.fhnw.cere.orchestrator.models;


import java.util.ArrayList;
import java.util.List;


public class UserInfoPullConfiguration {
    private String text;

    public UserInfoPullConfiguration() {
    }

    public UserInfoPullConfiguration(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "UserInfoPullConfiguration{" +
                "text='" + text + '\'' +
                '}';
    }

    public Configuration buildConfiguration(String userIdentification, Application application) {
        String infoMechanismText = this.text;

        Parameter parameter1 = new Parameter("text", infoMechanismText);
        List<Parameter> parameters = new ArrayList<Parameter>() {{
            add(parameter1);
        }};

        Mechanism infoMechanism = new Mechanism(MechanismType.INFO_TYPE, null, parameters);
        infoMechanism.setActive(true);
        List<Mechanism> mechanisms = new ArrayList<Mechanism>() {{
            add(infoMechanism);
        }};
        parameter1.setMechanism(infoMechanism);

        UserGroup userGroup = new UserGroup("Group for " + userIdentification, null, application);
        User user = new User(userIdentification, userIdentification, application);
        userGroup.setUsers(new ArrayList<User>() {{add(user);}});
        user.setUserGroup(userGroup);

        String configurationName = "Info Pull Configuration for " + userIdentification;
        Configuration configuration = new Configuration();
        configuration.setName(configurationName);
        configuration.setType(TriggerType.PULL);
        configuration.setApplication(application);
        configuration.setMechanisms(mechanisms);

        GeneralConfiguration generalConfiguration = new GeneralConfiguration();

        Parameter generalConfigurationParameter1 = new Parameter("pullActive", "1", "en", generalConfiguration);
        Parameter generalConfigurationParameter2 = new Parameter("pullSpecificActive", "1", "en", generalConfiguration);
        Parameter generalConfigurationParameter3 = new Parameter("onlyDisplayOnce", "1", "en", generalConfiguration);
        List<Parameter> generalConfigurationParameters = new ArrayList<Parameter>() {{
            add(generalConfigurationParameter1);
            add(generalConfigurationParameter2);
            add(generalConfigurationParameter3);
        }};

        generalConfiguration.setApplication(application);
        generalConfiguration.setParameters(generalConfigurationParameters);
        generalConfiguration.setName("General Configuration for " + userIdentification);

        configuration.setGeneralConfiguration(generalConfiguration);

        ConfigurationUserGroup configurationUserGroup = new ConfigurationUserGroup(configuration, userGroup, true);
        configuration.setConfigurationUserGroups(new ArrayList<ConfigurationUserGroup>() {{add(configurationUserGroup);}});
        configurationUserGroup.setConfiguration(configuration);

        return configuration;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
