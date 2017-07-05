package ch.fhnw.cere.orchestrator.controllers;

import ch.fhnw.cere.orchestrator.models.*;
import ch.fhnw.cere.orchestrator.repositories.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class GeneralConfigurationIntegrationTest extends BaseIntegrationTest {

    private Application application;

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
    private Parameter pushConfigurationGeneralConfigurationParameter2;

    @Autowired
    private ParameterRepository parameterRepository;
    @Autowired
    private MechanismRepository mechanismRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ConfigurationRepository configurationRepository;
    @Autowired
    private ConfigurationMechanismRepository configurationMechanismRepository;
    private String basePathEn = "/orchestrator/feedback/en/applications";
    private String basePathDe = "/orchestrator/feedback/de/applications";


    @Before
    public void setup() throws Exception {
        super.setup();

        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();
        this.configurationRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();

        this.application = applicationRepository.save(buildApplicationTree("Test application for general configuration"));
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();
        this.configurationRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();
    }

    @Test
    public void getApplicationGeneralConfiguration() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application.getId() + "/general_configuration"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.applicationGeneralConfiguration.getId())))
                .andExpect(jsonPath("$.name", is("General configuration Test application for general configuration")))
                .andExpect(jsonPath("$.parameters", hasSize(1)))
                .andExpect(jsonPath("$.parameters[0].key", is("reviewActive")))
                .andExpect(jsonPath("$.parameters[0].value", is(true)));

        mockMvc.perform(get(basePathEn + "/" + application.getId() + "/general_configuration/" + applicationGeneralConfiguration.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.applicationGeneralConfiguration.getId())))
                .andExpect(jsonPath("$.name", is("General configuration Test application for general configuration")))
                .andExpect(jsonPath("$.parameters", hasSize(1)))
                .andExpect(jsonPath("$.parameters[0].key", is("reviewActive")))
                .andExpect(jsonPath("$.parameters[0].value", is(true)));
    }

    @Test
    public void getConfigurationGeneralConfiguration() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application.getId() + "/configurations/" + pushConfiguration1.getId() + "/general_configuration"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.pushConfigurationGeneralConfiguration.getId())))
                .andExpect(jsonPath("$.name", is("General configuration for push configuration")))
                .andExpect(jsonPath("$.parameters", hasSize(2)))
                .andExpect(jsonPath("$.parameters[0].key", is("font-family")))
                .andExpect(jsonPath("$.parameters[0].value", is("Arial")))
                .andExpect(jsonPath("$.parameters[1].key", is("reviewActive")))
                .andExpect(jsonPath("$.parameters[1].value", is(false)));

        mockMvc.perform(get(basePathEn + "/" + application.getId() + "/general_configuration/" + pushConfigurationGeneralConfiguration.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.pushConfigurationGeneralConfiguration.getId())))
                .andExpect(jsonPath("$.name", is("General configuration for push configuration")))
                .andExpect(jsonPath("$.parameters", hasSize(2)))
                .andExpect(jsonPath("$.parameters[0].key", is("font-family")))
                .andExpect(jsonPath("$.parameters[0].value", is("Arial")))
                .andExpect(jsonPath("$.parameters[1].key", is("reviewActive")))
                .andExpect(jsonPath("$.parameters[1].value", is(false)));
    }

    private Application buildApplicationTree(String applicationName) {
        Application application = new Application(applicationName, 1, new Date(), new Date(), new ArrayList<>());

        pushConfiguration1 = new Configuration("Push configuration 1 of " + applicationName, TriggerType.PUSH, new Date(), new Date(), null, application);
        pullConfiguration1 = new Configuration("Pull configuration 1 of " + applicationName, TriggerType.PULL, new Date(), new Date(), null, application);

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
        pushConfigurationGeneralConfigurationParameter2 = new Parameter("font-family", "Arial", new Date(), new Date(), "en", null, pushConfigurationGeneralConfiguration, null);
        pushConfigurationGeneralConfiguration.setParameters(new ArrayList<Parameter>(){{
            add(pushConfigurationGeneralConfigurationParameter);
            add(pushConfigurationGeneralConfigurationParameter2);
        }});

        pushConfiguration1.setGeneralConfiguration(pushConfigurationGeneralConfiguration);
        application.setGeneralConfiguration(applicationGeneralConfiguration);

        return application;
    }
}