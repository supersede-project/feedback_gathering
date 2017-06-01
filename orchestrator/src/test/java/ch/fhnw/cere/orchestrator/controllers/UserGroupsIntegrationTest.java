package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.models.Application;
import ch.fhnw.cere.orchestrator.models.User;
import ch.fhnw.cere.orchestrator.models.UserGroup;
import ch.fhnw.cere.orchestrator.repositories.ApplicationRepository;
import ch.fhnw.cere.orchestrator.repositories.UserGroupRepository;
import ch.fhnw.cere.orchestrator.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class UserGroupsIntegrationTest extends BaseIntegrationTest {

    private Application application1;
    private Application application2;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;

    private UserGroup app1userGroup1;
    private UserGroup app1userGroup2;
    private UserGroup app2userGroup1;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRepository userRepository;

    private String basePathEn = "/orchestrator/feedback/en/applications";

    @Before
    public void setup() throws Exception {
        super.setup();

        this.userRepository.deleteAllInBatch();
        this.userGroupRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();

        this.application1 = applicationRepository.save(new Application("Test App 1", 1, new Date(), new Date(), null));
        this.application2 = applicationRepository.save(new Application("Test App 2", 1, new Date(), new Date(), null));

        this.app1userGroup1 = userGroupRepository.save(new UserGroup("App 1 User Group 1", null, application1));
        this.app1userGroup2 = userGroupRepository.save(new UserGroup("App 1 User Group 2", null, application1));
        this.app2userGroup1 = userGroupRepository.save(new UserGroup("App 2 User Group 1", null, application2));

        this.user1 = userRepository.save(new User("User 1", "u111111", application1, app1userGroup1));
        this.user2 = userRepository.save(new User("User 2", "u222222", application1, app1userGroup1));
        this.user3 = userRepository.save(new User("User 3", "u333333", application1, app1userGroup2));
        this.user4 = userRepository.save(new User("User 4", "44-44-44-44", application2, app2userGroup1));
        this.user5 = userRepository.save(new User("User 5", "55-55-55-55", application2, app2userGroup1));
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        this.userRepository.deleteAllInBatch();
        this.userGroupRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();
    }

    @Test
    public void userGroupNotFound() throws Exception {
        this.mockMvc.perform(get(basePathEn + "/" + this.application1.getId() + "/user_groups/9999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getApplicationUserGroups() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + this.application1.getId() + "/user_groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get(basePathEn + "/" + this.application2.getId() + "/user_groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getApplicationUserGroup() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/user_groups/" + app1userGroup1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.app1userGroup1.getId())))
                .andExpect(jsonPath("$.name", is("App 1 User Group 1")))
                .andExpect(jsonPath("$.users", hasSize(2)));
    }

    @Test(expected = ServletException.class)
    public void postUserGroupUnauthorized() throws Exception {
        UserGroup userGroup = new UserGroup("App 2 New User Group", null, application2);
        String userGroupJson = toJson(userGroup);

        this.mockMvc.perform(post(basePathEn + "/" + this.application1.getId() + "/user_groups")
                .contentType(contentType)
                .content(userGroupJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postUserGroup() throws Exception {
        UserGroup userGroup = new UserGroup("App 2 New User Group", null, application2);
        String userGroupJson = toJson(userGroup);

        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(post(basePathEn + "/" + this.application1.getId() + "/user_groups")
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(userGroupJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("App 2 New User Group")));
    }

    @Test(expected = ServletException.class)
    public void deleteUserGroupUnauthorized() throws Exception {
        this.mockMvc.perform(delete(basePathEn + "/" + application1.getId() + "/user_groups/" + app1userGroup1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserGroup() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "/" + application1.getId() + "/user_groups/" + app1userGroup1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }

    @Test(expected = ServletException.class)
    public void updateUserGroupUnauthorized() throws Exception {
        app1userGroup1.setName("App 1 user group 1 new name");
        String userGroupJson = toJson(app1userGroup1);

        this.mockMvc.perform(put(basePathEn + "/" + application1.getId() + "/user_groups/" + app1userGroup1.getId())
                .contentType(contentType)
                .content(userGroupJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.app1userGroup1.getId())))
                .andExpect(jsonPath("$.name", is("App 1 user group 1 new name")));
    }

    @Test
    public void updateUserGroup() throws Exception {
        app1userGroup1.setName("App 1 user group 1 new name");
        String userGroupJson = toJson(app1userGroup1);

        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + "/" + application1.getId() + "/user_groups/" + app1userGroup1.getId())
                .contentType(contentType)
                .content(userGroupJson)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.app1userGroup1.getId())))
                .andExpect(jsonPath("$.name", is("App 1 user group 1 new name")));
    }
}