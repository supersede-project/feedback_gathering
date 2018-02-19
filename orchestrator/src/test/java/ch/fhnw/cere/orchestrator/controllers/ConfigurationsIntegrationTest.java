package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.helpers.ApplicationTreeBuilder;
import ch.fhnw.cere.orchestrator.helpers.JsonHelper;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.UserInfoPullConfiguration;
import ch.fhnw.cere.orchestrator.repositories.ApiUserPermissionRepository;
import ch.fhnw.cere.orchestrator.repositories.ApplicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ConfigurationsIntegrationTest extends BaseIntegrationTest {

    private Application application1;
    private String userIdentification1 = "user1";

    @Autowired
    private ApplicationRepository applicationRepository;
    private String basePathEn = "/orchestrator/feedback/en/applications";
    @Autowired
    private ApplicationTreeBuilder applicationTreeBuilder;
    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;
    @Autowired
    private JsonHelper jsonHelper;

    @Before
    public void setup() throws Exception {
        super.setup();
        this.applicationRepository.deleteAllInBatch();
        this.application1 = applicationRepository.save(applicationTreeBuilder.buildApplicationTree("Test application Pull"));
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        //this.applicationRepository.deleteAllInBatch();
        //this.apiUserPermissionRepository.deleteAllInBatch();
    }

    @Test
    public void getApplication() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.name", is("Test application Pull")))
                .andExpect(jsonPath("$.state", is(1)))
                .andExpect(jsonPath("$.generalConfiguration.name", is("General configuration Test application Pull")))
                .andExpect(jsonPath("$.generalConfiguration.parameters", hasSize(1)))
                .andExpect(jsonPath("$.generalConfiguration.parameters[0].key", is("reviewActive")))
                .andExpect(jsonPath("$.generalConfiguration.parameters[0].value", is(true)))
                .andExpect(jsonPath("$.configurations", hasSize(2)))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].active", is(true)))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].order", is(1)))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters", hasSize(3)))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters[0].language", is("en")))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters[1].language", is("en")))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters[2].language", is("en")))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.name", is("General configuration for push configuration")))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.parameters", hasSize(1)))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.parameters[0].key", is("reviewActive")))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.parameters[0].value", is(false)));
    }

    @Test
    public void createPullConfigurationForUserInfo() throws Exception {
        UserInfoPullConfiguration userInfoPullConfiguration = new UserInfoPullConfiguration("This text is shown to the user");
        String userInfoPullConfigurationJson = toJson(userInfoPullConfiguration);

        String superAdminJWTToken = requestSuperAdminJWTToken();

        this.mockMvc.perform(post(basePathEn + "/" + application1.getId() + "/configurations/" + userIdentification1 + "/info")
                .contentType(contentType)
                .header("Authorization", superAdminJWTToken)
                .content(userInfoPullConfigurationJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get(basePathEn + "/" + application1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.name", is("Test application Pull")))
                .andExpect(jsonPath("$.state", is(1)))
                .andExpect(jsonPath("$.configurations", hasSize(2)));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/full"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.name", is("Test application Pull")))
                .andExpect(jsonPath("$.state", is(1)))
                .andExpect(jsonPath("$.configurations", hasSize(3)))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 1 of Test application Pull")))
                .andExpect(jsonPath("$.configurations[2].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[2].name", is("Info Pull Configuration for user1")))
                .andExpect(jsonPath("$.configurations[2].userGroups", hasSize(1)))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].name", is("Group for user1")))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users", hasSize(1)))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users[0].name", is("user1")))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users[0].userIdentification", is("user1")));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_identification/" + userIdentification1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.name", is("Test application Pull")))
                .andExpect(jsonPath("$.state", is(1)))
                .andExpect(jsonPath("$.configurations", hasSize(2)))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[1].name", is("Info Pull Configuration for user1")));
    }
}