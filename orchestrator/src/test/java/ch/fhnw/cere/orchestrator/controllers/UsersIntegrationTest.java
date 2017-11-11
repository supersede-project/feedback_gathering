package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.models.*;
import ch.fhnw.cere.orchestrator.repositories.ApiUserPermissionRepository;
import ch.fhnw.cere.orchestrator.repositories.ApplicationRepository;
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


public class UsersIntegrationTest extends BaseIntegrationTest {

    private Application application1;

    private User user1;
    private User user2;

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    private String basePathEn = "/orchestrator/feedback/en/applications";

    @Before
    public void setup() throws Exception {
        super.setup();

        this.userRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();

        this.application1 = applicationRepository.save(new Application("Test App 1", 1, new Date(), new Date(), null));
        this.user1 = userRepository.save(new User("User 1", "u111111", application1, null));
        this.user2 = userRepository.save(new User("User 2", "u222222", application1, null));

        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, application1, true));
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        this.userRepository.deleteAllInBatch();
        this.applicationRepository.deleteAllInBatch();
        this.apiUserPermissionRepository.deleteAllInBatch();
    }

    @Test
    public void userNotFound() throws Exception {
        this.mockMvc.perform(get(basePathEn + "/" + this.application1.getId() + "/users/9999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getApplicationUsers() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + this.application1.getId() + "/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getApplicationUser() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + application1.getId() + "/users/" + user1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.user1.getId())))
                .andExpect(jsonPath("$.name", is("User 1")))
                .andExpect(jsonPath("$.userIdentification", is("u111111")));
    }

    @Test(expected = ServletException.class)
    public void postUserUnauthorized() throws Exception {
        User user = new User("User 3", "u333333", application1, null);
        String userJson = toJson(user);

        this.mockMvc.perform(post(basePathEn + "/" + this.application1.getId() + "/users")
                .contentType(contentType)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postUser() throws Exception {
        User user = new User("User 3", "u333333", application1, null);
        String userJson = toJson(user);

        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(post(basePathEn + "/" + this.application1.getId() + "/users")
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("User 3")))
                .andExpect(jsonPath("$.userIdentification", is("u333333")));
    }

    @Test(expected = Exception.class)
    public void deleteUserUnauthorized() throws Exception {
        this.mockMvc.perform(delete(basePathEn + "/" + application1.getId() + "/users/" + user1.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "/" + application1.getId() + "/users/" + user1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }

    @Test(expected = ServletException.class)
    public void updateUserUnauthorized() throws Exception {
        user1.setName("User 1 new name");
        user1.setUserIdentification("u11111x");
        String userJson = toJson(user1);

        this.mockMvc.perform(put(basePathEn + "/" + application1.getId() + "/users/" + user1.getId())
                .contentType(contentType)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.user1.getId())))
                .andExpect(jsonPath("$.name", is("User 1 new name")))
                .andExpect(jsonPath("$.userIdentification", is("u11111x")));
    }

    @Test
    public void updateUser() throws Exception {
        user1.setName("User 1 new name");
        user1.setUserIdentification("u11111x");
        String userJson = toJson(user1);

        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + "/" + application1.getId() + "/users/" + user1.getId())
                .contentType(contentType)
                .content(userJson)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.user1.getId())))
                .andExpect(jsonPath("$.name", is("User 1 new name")))
                .andExpect(jsonPath("$.userIdentification", is("u11111x")));
    }
}