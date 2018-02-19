package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.controllers.helpers.ApplicationTreeBuilder;
import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.User;
import ch.fhnw.cere.orchestrator.repositories.ApplicationRepository;
import ch.fhnw.cere.orchestrator.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ApplicationsByUserIntegrationTest extends BaseIntegrationTest {

    private Application application1;
    long userGroup1Id;
    long userGroup2Id;
    long userGroup3Id;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserRepository userRepository;
    private String basePathEn = "/orchestrator/feedback/en/applications";

    @Autowired
    private ApplicationTreeBuilder applicationTreeBuilder;

    @Before
    public void setup() throws Exception {
        super.setup();
        this.applicationRepository.deleteAllInBatch();

        this.application1 = applicationRepository.save(applicationTreeBuilder.buildApplicationTreeWithUserGroups("Test application 1"));
        userGroup1Id = this.application1.getUserGroups().stream().filter(userGroup -> userGroup.getName().equals("App 1 User Group 1")).findFirst().get().getId();
        userGroup2Id = this.application1.getUserGroups().stream().filter(userGroup -> userGroup.getName().equals("App 1 User Group 2")).findFirst().get().getId();
        userGroup3Id = this.application1.getUserGroups().stream().filter(userGroup -> userGroup.getName().equals("App 1 User Group 3")).findFirst().get().getId();
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        this.applicationRepository.deleteAllInBatch();
    }

    @Test
    /* should deliver the default configurations for users that are not member of a user group */
    public void getApplicationForAnonymousUser() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.name", is("Test application 1")))
                .andExpect(jsonPath("$.state", is(1)))
                .andExpect(jsonPath("$.users", hasSize(5)))
                .andExpect(jsonPath("$.userGroups", hasSize(4)))
                .andExpect(jsonPath("$.generalConfiguration.name", is("General configuration Test application 1")))
                .andExpect(jsonPath("$.generalConfiguration.parameters", hasSize(1)))
                .andExpect(jsonPath("$.generalConfiguration.parameters[0].key", is("reviewActive")))
                .andExpect(jsonPath("$.generalConfiguration.parameters[0].value", is(true)))
                .andExpect(jsonPath("$.configurations", hasSize(3)))
                .andExpect(jsonPath("$.configurations[0].name", is("Push configuration 1 of Test application 1")))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[0].pushDefault", is(true)))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].type", is("TEXT_TYPE")))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].active", is(true)))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].order", is(1)))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters", hasSize(3)))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters[0].language", is("en")))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters[1].language", is("en")))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters[2].language", is("en")))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.name", is("General configuration for push configuration")))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.parameters", hasSize(1)))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.parameters[0].key", is("reviewActive")))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.parameters[0].value", is(false)))

                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 1 of Test application 1")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[1].pullDefault", is(true)))
                .andExpect(jsonPath("$.configurations[1].mechanisms[0].type", is("TEXT_TYPE")))
                .andExpect(jsonPath("$.configurations[1].mechanisms[0].active", is(true)))
                .andExpect(jsonPath("$.configurations[1].mechanisms[0].order", is(1)))
                .andExpect(jsonPath("$.configurations[1].mechanisms[0].parameters", hasSize(0)))

                .andExpect(jsonPath("$.configurations[2].name", is("Pull configuration 2 of Test application 1")))
                .andExpect(jsonPath("$.configurations[2].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[2].pullDefault", is(true)))
                .andExpect(jsonPath("$.configurations[2].userGroups", hasSize(1)))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].name", is("App 1 User Group 1")))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users", hasSize(2)))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users[0].name", is("User 1")))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users[0].userIdentification", is("u111111")))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users[1].name", is("User 2")))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users[1].userIdentification", is("u222222")));
    }

    @Test
    public void getFullApplication() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/full"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.name", is("Test application 1")))
                .andExpect(jsonPath("$.state", is(1)))
                .andExpect(jsonPath("$.users", hasSize(5)))
                .andExpect(jsonPath("$.userGroups", hasSize(4)))
                .andExpect(jsonPath("$.generalConfiguration.name", is("General configuration Test application 1")))
                .andExpect(jsonPath("$.generalConfiguration.parameters", hasSize(1)))
                .andExpect(jsonPath("$.generalConfiguration.parameters[0].key", is("reviewActive")))
                .andExpect(jsonPath("$.generalConfiguration.parameters[0].value", is(true)))
                .andExpect(jsonPath("$.configurations", hasSize(7)))
                .andExpect(jsonPath("$.configurations[0].name", is("Push configuration 1 of Test application 1")))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].type", is("TEXT_TYPE")))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].active", is(true)))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].order", is(1)))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters", hasSize(3)))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters[0].language", is("en")))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters[1].language", is("en")))
                .andExpect(jsonPath("$.configurations[0].mechanisms[0].parameters[2].language", is("en")))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.name", is("General configuration for push configuration")))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.parameters", hasSize(1)))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.parameters[0].key", is("reviewActive")))
                .andExpect(jsonPath("$.configurations[0].generalConfiguration.parameters[0].value", is(false)))
                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 1 of Test application 1")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[1].mechanisms[0].type", is("TEXT_TYPE")))
                .andExpect(jsonPath("$.configurations[1].mechanisms[0].active", is(true)))
                .andExpect(jsonPath("$.configurations[1].mechanisms[0].order", is(1)))
                .andExpect(jsonPath("$.configurations[1].mechanisms[0].parameters", hasSize(0)))

                .andExpect(jsonPath("$.configurations[2].name", is("Push configuration 2 of Test application 1")))
                .andExpect(jsonPath("$.configurations[2].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[2].userGroups", hasSize(1)))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].name", is("App 1 User Group 1")))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users", hasSize(2)))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users[0].name", is("User 1")))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users[0].userIdentification", is("u111111")))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users[1].name", is("User 2")))
                .andExpect(jsonPath("$.configurations[2].userGroups[0].users[1].userIdentification", is("u222222")))

                .andExpect(jsonPath("$.configurations[3].name", is("Push configuration 3 of Test application 1")))
                .andExpect(jsonPath("$.configurations[3].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[3].userGroups", hasSize(1)))
                .andExpect(jsonPath("$.configurations[3].userGroups[0].name", is("App 1 User Group 2")))
                .andExpect(jsonPath("$.configurations[3].userGroups[0].users", hasSize(1)))
                .andExpect(jsonPath("$.configurations[3].userGroups[0].users[0].name", is("User 3")))
                .andExpect(jsonPath("$.configurations[3].userGroups[0].users[0].userIdentification", is("u333333")))

                .andExpect(jsonPath("$.configurations[4].name", is("Push configuration 4 of Test application 1")))
                .andExpect(jsonPath("$.configurations[4].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[4].userGroups", hasSize(1)))
                .andExpect(jsonPath("$.configurations[4].userGroups[0].name", is("App 1 User Group 3")))
                .andExpect(jsonPath("$.configurations[4].userGroups[0].users", hasSize(1)))
                .andExpect(jsonPath("$.configurations[4].userGroups[0].users[0].name", is("User 4")))
                .andExpect(jsonPath("$.configurations[4].userGroups[0].users[0].userIdentification", is("u444444")))

                .andExpect(jsonPath("$.configurations[5].name", is("Pull configuration 2 of Test application 1")))
                .andExpect(jsonPath("$.configurations[5].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[5].userGroups", hasSize(1)))
                .andExpect(jsonPath("$.configurations[5].userGroups[0].name", is("App 1 User Group 1")))
                .andExpect(jsonPath("$.configurations[5].userGroups[0].users", hasSize(2)))
                .andExpect(jsonPath("$.configurations[5].userGroups[0].users[0].name", is("User 1")))
                .andExpect(jsonPath("$.configurations[5].userGroups[0].users[0].userIdentification", is("u111111")))
                .andExpect(jsonPath("$.configurations[5].userGroups[0].users[1].name", is("User 2")))
                .andExpect(jsonPath("$.configurations[5].userGroups[0].users[1].userIdentification", is("u222222")))

                .andExpect(jsonPath("$.configurations[6].name", is("Pull configuration 3 of Test application 1")))
                .andExpect(jsonPath("$.configurations[6].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[6].userGroups", hasSize(2)))
                .andExpect(jsonPath("$.configurations[6].userGroups[0].name", is("App 1 User Group 2")))
                .andExpect(jsonPath("$.configurations[6].userGroups[0].users", hasSize(1)))
                .andExpect(jsonPath("$.configurations[6].userGroups[0].users[0].name", is("User 3")))
                .andExpect(jsonPath("$.configurations[6].userGroups[0].users[0].userIdentification", is("u333333")))
                .andExpect(jsonPath("$.configurations[6].userGroups[1].name", is("App 1 User Group 3")))
                .andExpect(jsonPath("$.configurations[6].userGroups[1].users", hasSize(1)))
                .andExpect(jsonPath("$.configurations[6].userGroups[1].users[0].name", is("User 4")))
                .andExpect(jsonPath("$.configurations[6].userGroups[1].users[0].userIdentification", is("u444444")));
    }

    @Test
    public void getApplicationByIdAndGroupId() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_group/" + userGroup1Id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.configurations", hasSize(2)))
                .andExpect(jsonPath("$.configurations[0].name", is("Push configuration 2 of Test application 1")))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 2 of Test application 1")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_group/" + userGroup2Id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.configurations", hasSize(2)))
                .andExpect(jsonPath("$.configurations[0].name", is("Push configuration 3 of Test application 1")))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 3 of Test application 1")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_group/" + userGroup3Id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.configurations", hasSize(2)))
                .andExpect(jsonPath("$.configurations[0].name", is("Push configuration 4 of Test application 1")))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 3 of Test application 1")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")));
    }

    @Test
    public void getApplicationByIdAndUserIdentification() throws Exception {
        userRepository.save(new User("Not assigned to a group yet", "notAssignedToAGroupYet", application1, null));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_identification/u111111"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.configurations", hasSize(2)))
                .andExpect(jsonPath("$.configurations[0].name", is("Push configuration 2 of Test application 1")))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 2 of Test application 1")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_identification/u222222"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.configurations", hasSize(2)))
                .andExpect(jsonPath("$.configurations[0].name", is("Push configuration 2 of Test application 1")))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 2 of Test application 1")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_identification/u333333"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.configurations", hasSize(2)))
                .andExpect(jsonPath("$.configurations[0].name", is("Push configuration 3 of Test application 1")))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 3 of Test application 1")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_identification/u444444"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.configurations", hasSize(2)))
                .andExpect(jsonPath("$.configurations[0].name", is("Push configuration 4 of Test application 1")))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 3 of Test application 1")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_identification/u555555"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.configurations", hasSize(2)));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_identification/notAssignedToAGroupYet"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.configurations", hasSize(3)))
                .andExpect(jsonPath("$.configurations[0].name", is("Push configuration 1 of Test application 1")))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[0].pushDefault", is(true)))

                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 1 of Test application 1")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[1].pullDefault", is(true)))

                .andExpect(jsonPath("$.configurations[2].name", is("Pull configuration 2 of Test application 1")))
                .andExpect(jsonPath("$.configurations[2].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[2].pullDefault", is(true)));

        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_identification/notInTheSystemYet"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.application1.getId())))
                .andExpect(jsonPath("$.configurations", hasSize(3)))
                .andExpect(jsonPath("$.configurations[0].name", is("Push configuration 1 of Test application 1")))
                .andExpect(jsonPath("$.configurations[0].type", is("PUSH")))
                .andExpect(jsonPath("$.configurations[0].pushDefault", is(true)))

                .andExpect(jsonPath("$.configurations[1].name", is("Pull configuration 1 of Test application 1")))
                .andExpect(jsonPath("$.configurations[1].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[1].pullDefault", is(true)))

                .andExpect(jsonPath("$.configurations[2].name", is("Pull configuration 2 of Test application 1")))
                .andExpect(jsonPath("$.configurations[2].type", is("PULL")))
                .andExpect(jsonPath("$.configurations[2].pullDefault", is(true)));
    }
}





