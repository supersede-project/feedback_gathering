package ch.fhnw.cere.orchestrator.controllers.helpers;

import ch.fhnw.cere.orchestrator.models.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class ApplicationTreeBuilder {

    private Configuration pushConfiguration1;
    private Configuration pullConfiguration1;

    private Mechanism mechanism1;
    private Mechanism mechanism2;

    private ConfigurationMechanism configurationMechanism1;
    private ConfigurationMechanism configurationMechanism2;

    private GeneralConfiguration applicationGeneralConfiguration;
    private GeneralConfiguration pushConfigurationGeneralConfiguration;

    private Parameter parameter1;
    private Parameter parameter2;
    private Parameter parameter3;

    private Parameter parentParameter1;
    private Parameter childParameter1;
    private Parameter childParameter2;
    private Parameter childParameter3;
    private Parameter childParameter4;

    private Parameter applicationGeneralConfigurationParameter;
    private Parameter pushConfigurationGeneralConfigurationParameter;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;

    private UserGroup userGroup1;
    private UserGroup userGroup2;
    private UserGroup userGroup3;
    private UserGroup userGroup4;

    public ApplicationTreeBuilder() {
    }

    public Application buildApplicationTree(String applicationName) {
        Application application = new Application(applicationName, 1, new Date(), new Date(), new ArrayList<>());

        pushConfiguration1 = new Configuration("Push configuration 1 of " + applicationName, TriggerType.PUSH, new Date(), new Date(), null, application);
        pushConfiguration1.setPushDefault(true);
        pullConfiguration1 = new Configuration("Pull configuration 1 of " + applicationName, TriggerType.PULL, new Date(), new Date(), null, application);
        pullConfiguration1.setPullDefault(true);

        mechanism1 = new Mechanism(MechanismType.TEXT_TYPE, null, new ArrayList<>());
        mechanism2 = new Mechanism(MechanismType.TEXT_TYPE, null, new ArrayList<>());

        configurationMechanism1 = new ConfigurationMechanism(pushConfiguration1, mechanism1, true, 1, new Date(), new Date());
        configurationMechanism2 = new ConfigurationMechanism(pullConfiguration1, mechanism2, true, 1, new Date(), new Date());

        mechanism1.setConfigurationMechanisms(new ArrayList<ConfigurationMechanism>() {
            {
                add(configurationMechanism1);
                add(configurationMechanism2);
            }
        });

        pushConfiguration1.setConfigurationMechanisms(new ArrayList<ConfigurationMechanism>() {
            {
                add(configurationMechanism1);
            }
        });

        pullConfiguration1.setConfigurationMechanisms(new ArrayList<ConfigurationMechanism>() {
            {
                add(configurationMechanism2);
            }
        });

        parameter1 = new Parameter("title", "Title EN", new Date(), new Date(), "en", null, null, mechanism1);
        parameter2 = new Parameter("title", "Titel DE", new Date(), new Date(), "de", null, null, mechanism1);
        parameter3 = new Parameter("font-size", "10", new Date(), new Date(), "en", null, null, mechanism1);

        parentParameter1 = new Parameter("options", null, new Date(), new Date(), "en", null, null, mechanism1);
        childParameter1 = new Parameter("CAT_1", "Cat 1 EN", new Date(), new Date(), "en", parentParameter1, null, null);
        childParameter2 = new Parameter("CAT_2", "Cat 2 EN", new Date(), new Date(), "en", parentParameter1, null, null);
        childParameter3 = new Parameter("CAT_1", "Cat 1 DE", new Date(), new Date(), "de", parentParameter1, null, null);
        childParameter4 = new Parameter("CAT_3", "Cat 3 FR", new Date(), new Date(), "fr", parentParameter1, null, null);
        List<Parameter> childParameters = new ArrayList<Parameter>() {
            {
                add(childParameter1);
                add(childParameter2);
                add(childParameter3);
                add(childParameter4);
            }
        };
        parentParameter1.setParameters(childParameters);

        mechanism1.setParameters(new ArrayList<Parameter>() {
            {
                add(parameter1);
                add(parameter2);
                add(parameter3);
                add(parentParameter1);
            }
        });

        ArrayList<Configuration> configurations = new ArrayList<Configuration>() {
            {
                add(pushConfiguration1);
                add(pullConfiguration1);
            }
        };
        application.setConfigurations(configurations);

        applicationGeneralConfiguration = new GeneralConfiguration("General configuration " + applicationName, new Date(), new Date(), null, application, null);
        applicationGeneralConfigurationParameter = new Parameter("reviewActive", "true", new Date(), new Date(), "en", null, applicationGeneralConfiguration, null);
        applicationGeneralConfiguration.setParameters(new ArrayList<Parameter>(){{add(applicationGeneralConfigurationParameter);}});

        pushConfigurationGeneralConfiguration = new GeneralConfiguration("General configuration for push configuration", new Date(), new Date(), null, null, pushConfiguration1);
        pushConfigurationGeneralConfigurationParameter = new Parameter("reviewActive", "false", new Date(), new Date(), "en", null, pushConfigurationGeneralConfiguration, null);
        pushConfigurationGeneralConfiguration.setParameters(new ArrayList<Parameter>(){{add(pushConfigurationGeneralConfigurationParameter);}});

        pushConfiguration1.setGeneralConfiguration(pushConfigurationGeneralConfiguration);
        application.setGeneralConfiguration(applicationGeneralConfiguration);

        return application;
    }

    public Application buildApplicationTreeWithUserGroups(String applicationName) {
        Application application = this.buildApplicationTree(applicationName);

        this.userGroup1 = new UserGroup("App 1 User Group 1", null, application); // push 2, pull2
        this.userGroup2 = new UserGroup("App 1 User Group 2", null, application); // push 3, pull3
        this.userGroup3 = new UserGroup("App 1 User Group 3", null, application); // push 4, pull3
        this.userGroup4 = new UserGroup("App 1 User Group 4", null, application); // no configurations

        this.user1 = new User("User 1", "u111111", application, userGroup1); // push 2, pull2
        this.user2 = new User("User 2", "u222222", application, userGroup1); // push 2, pull2
        this.user3 = new User("User 3", "u333333", application, userGroup2); // push 3, pull3
        this.user4 = new User("User 4", "u444444", application, userGroup3); // push 4, pull3
        this.user5 = new User("User 5", "u555555", application, userGroup4); // no configurations

        this.userGroup1.setUsers(new ArrayList<User>(){{
            add(user1);
            add(user2);
        }});
        this.userGroup2.setUsers(new ArrayList<User>(){{
            add(user3);
        }});
        this.userGroup3.setUsers(new ArrayList<User>(){{
            add(user4);
        }});
        this.userGroup3.setUsers(new ArrayList<User>(){{
            add(user5);
        }});

        application.setUsers(new ArrayList<User>(){{
            add(user1);
            add(user2);
            add(user3);
            add(user4);
            add(user5);
        }});
        application.setUserGroups(new ArrayList<UserGroup>(){{
            add(userGroup1);
            add(userGroup2);
            add(userGroup3);
            add(userGroup4);
        }});

        Configuration pushConfiguration2 = new Configuration("Push configuration 2 of " + applicationName, TriggerType.PUSH, new Date(), new Date(), null, application);
        ConfigurationUserGroup configurationUserGroup2 = new ConfigurationUserGroup(pushConfiguration2, userGroup1, true);
        pushConfiguration2.setConfigurationUserGroups(new ArrayList<ConfigurationUserGroup>(){{add(configurationUserGroup2);}});

        Configuration pushConfiguration3 = new Configuration("Push configuration 3 of " + applicationName, TriggerType.PUSH, new Date(), new Date(), null, application);
        ConfigurationUserGroup configurationUserGroup3 = new ConfigurationUserGroup(pushConfiguration3, userGroup2, true);
        pushConfiguration3.setConfigurationUserGroups(new ArrayList<ConfigurationUserGroup>(){{add(configurationUserGroup3);}});

        Configuration pushConfiguration4 = new Configuration("Push configuration 4 of " + applicationName, TriggerType.PUSH, new Date(), new Date(), null, application);
        ConfigurationUserGroup configurationUserGroup4 = new ConfigurationUserGroup(pushConfiguration4, userGroup3, true);
        pushConfiguration4.setConfigurationUserGroups(new ArrayList<ConfigurationUserGroup>(){{add(configurationUserGroup4);}});

        Configuration pullConfiguration2 = new Configuration("Pull configuration 2 of " + applicationName, TriggerType.PULL, new Date(), new Date(), null, application);
        pullConfiguration2.setPullDefault(true);
        ConfigurationUserGroup configurationUserGroup5 = new ConfigurationUserGroup(pullConfiguration2, userGroup1, true);
        pullConfiguration2.setConfigurationUserGroups(new ArrayList<ConfigurationUserGroup>(){{add(configurationUserGroup5);}});

        Configuration pullConfiguration3 = new Configuration("Pull configuration 3 of " + applicationName, TriggerType.PULL, new Date(), new Date(), null, application);
        ConfigurationUserGroup configurationUserGroup6 = new ConfigurationUserGroup(pullConfiguration3, userGroup2, true);
        ConfigurationUserGroup configurationUserGroup7 = new ConfigurationUserGroup(pullConfiguration3, userGroup3, true);
        pullConfiguration3.setConfigurationUserGroups(new ArrayList<ConfigurationUserGroup>(){{
            add(configurationUserGroup6);
            add(configurationUserGroup7);
        }});

        List<Configuration> configurations = application.getConfigurations();
        configurations.add(pushConfiguration2);
        configurations.add(pushConfiguration3);
        configurations.add(pushConfiguration4);
        configurations.add(pullConfiguration2);
        configurations.add(pullConfiguration3);
        application.setConfigurations(configurations);

        return application;
    }
}
