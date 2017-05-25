package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.models.*;
import ch.fhnw.cere.orchestrator.repositories.ApplicationRepository;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.nullValue;
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


public class ApplicationsIntegrationTest extends BaseIntegrationTest {

    private Application application1;
    private Application application2;

    private Application application3;

    private Configuration pushConfiguration1;
    private Configuration pullConfiguration1;

    private Mechanism mechanism1;

    private ConfigurationMechanism configurationMechanism1;
    private ConfigurationMechanism configurationMechanism2;

    private Parameter parameter1;
    private Parameter parameter2;
    private Parameter parameter3;

    private Parameter parentParameter1;
    private Parameter childParameter1;
    private Parameter childParameter2;
    private Parameter childParameter3;
    private Parameter childParameter4;

    @Autowired
    private ApplicationRepository applicationRepository;
    private String basePathEn = "/en/applications";


    @Before
    public void setup() throws Exception {
        super.setup();

        this.applicationRepository.deleteAllInBatch();

        this.application1 = applicationRepository.save(new Application("Test App 1", 1, new Date(), new Date(), null));
        this.application2 = applicationRepository.save(new Application("Test App 2", 1, new Date(), new Date(), null));

        buildApplicationTree();
        this.application3 = applicationRepository.save(this.application3);
    }

    @Test
    public void applicationNotFound() throws Exception {
        this.mockMvc.perform(get(basePathEn + "/9999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getApplications() throws Exception {
        mockMvc.perform(get(basePathEn + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void getApplication() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.name", is("Test App 1")))
                .andExpect(jsonPath("$.state", is(1)))
                .andExpect(jsonPath("$.configurations", is(Matchers.empty())));


        mockMvc.perform(get(basePathEn + "/" + application3.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application3.getId())))
                .andExpect(jsonPath("$.name", is("Test application 3")))
                .andExpect(jsonPath("$.state", is(1)))
                .andExpect(jsonPath("$.configurations", hasSize(2)));
    }

    @Test
    public void postApplication() throws Exception {
        Application application = new Application("Test App 4", 1, new Date(), new Date(), null);
        String applicationJson = toJson(application);

        this.mockMvc.perform(post(basePathEn)
                .contentType(contentType)
                .content(applicationJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteApplication() throws Exception {
        this.mockMvc.perform(delete(basePathEn + "/" + application1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateApplication() throws Exception {
        this.application2.setName("Updated name for App 2");
        this.application2.setState(0);
        String applicationJson = toJson(this.application2);

        this.mockMvc.perform(put(basePathEn + "/")
                .contentType(contentType)
                .content(applicationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.application2.getId())))
                .andExpect(jsonPath("$.name", is("Updated name for App 2")))
                .andExpect(jsonPath("$.state", is(0)))
                .andExpect(jsonPath("$.configurations", is(nullValue())));
    }

    private void buildApplicationTree() {
        application3 = new Application("Test application 3", 1, new Date(), new Date(), new ArrayList<>());

        pushConfiguration1 = new Configuration("Push configuration 1", TriggerType.PUSH, new Date(), new Date(), null, application3);
        pullConfiguration1 = new Configuration("Pull configuration 1", TriggerType.PULL, new Date(), new Date(), null, application3);

        mechanism1 = new Mechanism(MechanismType.TEXT_TYPE, null, new ArrayList<>());

        configurationMechanism1 = new ConfigurationMechanism(pushConfiguration1, mechanism1, true, 1, new Date(), new Date());
        configurationMechanism2 = new ConfigurationMechanism(pullConfiguration1, mechanism1, true, 1, new Date(), new Date());

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
        application3.setConfigurations(configurations);
    }
}