package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.helpers.ApplicationTreeBuilder;
import ch.fhnw.cere.orchestrator.models.*;
import ch.fhnw.cere.orchestrator.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConfigurationMechanismsIntegrationTest extends BaseIntegrationTest {

    private ConfigurationMechanism configurationMechanism1;
    private Application application1;
    private Configuration configuration1;
    private Configuration configuration2;
    private Mechanism mechanism1;
    private Mechanism mechanism2;
    private Mechanism mechanism3;
    private ApiUserPermission apiUserPermission;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ParameterRepository parameterRepository;
    @Autowired
    private MechanismRepository mechanismRepository;
    @Autowired
    private ConfigurationMechanismRepository configurationMechanismRepository;
    @Autowired
    private ApplicationTreeBuilder applicationTreeBuilder;
    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    private String basePathEn = "/orchestrator/feedback/en/applications";


    @Before
    public void setup() throws Exception {
        super.setup();

        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();

        this.application1 = applicationRepository.save(applicationTreeBuilder.buildApplicationTree("Test application for mechanisms"));

        this.configuration1 = application1.getConfigurations().stream().filter(configuration -> configuration.getType().equals(TriggerType.PUSH)).findFirst().get();
        this.configuration2 = application1.getConfigurations().stream().filter(configuration -> configuration.getType().equals(TriggerType.PULL)).findFirst().get();

        this.configurationMechanism1 = configuration1.getConfigurationMechanisms().get(0);

        this.mechanism1 = configuration1.getMechanisms().get(0);
        this.mechanism2 = configuration2.getMechanisms().get(0);
        this.mechanism3 = mechanismRepository.save(new Mechanism(MechanismType.RATING_TYPE, null, new ArrayList<>()));

        this.apiUserPermission = apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, application1, true));
    }

    @After
    public void cleanUp() {
        super.cleanUp();

        this.parameterRepository.deleteAllInBatch();
        this.configurationMechanismRepository.deleteAllInBatch();
        this.mechanismRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();
    }

    @Test
    public void getConfigurationMechanisms() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/configuration_mechanisms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[0].mechanism.id", is((int) mechanism1.getId())));
    }

    @Test
    public void getConfigurationMechanism() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/configuration_mechanisms/" + configurationMechanism1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active", is(true)))
                .andExpect(jsonPath("$.order", is(1)))
                .andExpect(jsonPath("$.mechanism.id", is((int) mechanism1.getId())));
    }

    @Test
    public void createConfigurationMechanism() throws Exception {
        ConfigurationMechanism configurationMechanism = new ConfigurationMechanism(this.configuration1, false, 5);
        String configurationMechanismJson = toJson(configurationMechanism);

        String adminJWTToken = requestAppAdminJWTToken();

        mockMvc.perform(post(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/configuration_mechanisms/mechanisms/" + mechanism3.getId())
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(configurationMechanismJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.active", is(false)))
                .andExpect(jsonPath("$.order", is(5)))
                .andExpect(jsonPath("$.mechanism.id", is((int) mechanism3.getId())));


        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/configuration_mechanisms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[0].mechanism.id", is((int) mechanism1.getId())))
                .andExpect(jsonPath("$[1].active", is(false)))
                .andExpect(jsonPath("$[1].order", is(5)))
                .andExpect(jsonPath("$[1].mechanism.id", is((int) mechanism3.getId())));
    }

    @Test
    public void updateConfigurationMechanism() throws Exception {
        this.configurationMechanism1.setActive(false);
        this.configurationMechanism1.setOrder(2);

        String configurationMechanismJson = toJson(configurationMechanism1);

        String adminJWTToken = requestAppAdminJWTToken();

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/configuration_mechanisms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[0].mechanism.id", is((int) mechanism1.getId())));

        mockMvc.perform(put(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/configuration_mechanisms")
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(configurationMechanismJson))
                .andExpect(status().isOk());

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/configuration_mechanisms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].active", is(false)))
                .andExpect(jsonPath("$[0].order", is(2)))
                .andExpect(jsonPath("$[0].mechanism.id", is((int) mechanism1.getId())));
    }

    @Test
    public void deleteConfigurationMechanism() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/configuration_mechanisms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].active", is(true)))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[0].mechanism.id", is((int) mechanism1.getId())));

        mockMvc.perform(delete(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/configuration_mechanisms/" + this.configurationMechanism1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/configurations/" + configuration1.getId() + "/configuration_mechanisms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
