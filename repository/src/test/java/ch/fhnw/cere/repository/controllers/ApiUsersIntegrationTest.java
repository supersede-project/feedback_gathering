package ch.fhnw.cere.repository.controllers;


import ch.fhnw.cere.repository.models.ApiUser;
import ch.fhnw.cere.repository.repositories.ApiUserPermissionRepository;
import ch.fhnw.cere.repository.repositories.ApiUserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ApiUsersIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ApiUserRepository apiUserRepository;
    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    private String basePathEn = "/feedback_repository/en/api_users";

    @Before
    public void setup() throws Exception {
        super.setup();
        // 3 api users are inserted in BaseIntegrationTest
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        this.apiUserRepository.deleteAllInBatch();
        this.apiUserPermissionRepository.deleteAllInBatch();
    }

    @Test(expected = Exception.class)
    public void userNotFoundUnauthorized() throws Exception {
        this.mockMvc.perform(get(basePathEn + "/99999999"))
                .andExpect(status().isNotFound());
    }

    @Test(expected = Exception.class)
    public void getApiUsersUnauthorized() throws Exception {
        mockMvc.perform(get(basePathEn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test(expected = Exception.class)
    public void getApiUserUnauthorized() throws Exception {
        mockMvc.perform(get(basePathEn + "/" + superAdminUser.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.superAdminUser.getId())))
                .andExpect(jsonPath("$.name", is("super_admin")));
    }

    @Test
    public void userNotFound() throws Exception {
        String superAdminJWTToken = requestSuperAdminJWTToken();

        this.mockMvc.perform(get(basePathEn + "/99999999")
                .header("Authorization", superAdminJWTToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getApiUsers() throws Exception {
        String superAdminJWTToken = requestSuperAdminJWTToken();

        mockMvc.perform(get(basePathEn)
                .header("Authorization", superAdminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void getApiUser() throws Exception {
        String superAdminJWTToken = requestSuperAdminJWTToken();

        mockMvc.perform(get(basePathEn + "/" + superAdminUser.getId())
                .header("Authorization", superAdminJWTToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int) this.superAdminUser.getId())))
                .andExpect(jsonPath("$.name", is("super_admin")));
    }

    @Test(expected = Exception.class)
    public void postApiUserAsNormalUser() throws Exception {
        ApiUser apiUser = new ApiUser("new user", "secret");
        String userJson = toJson(apiUser);

        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(post(basePathEn)
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("new user")));
    }

    @Test
    public void postApiUser() throws Exception {
        ApiUser apiUser = new ApiUser("new user", "secret");
        String userJson = toJson(apiUser);

        String adminJWTToken = requestSuperAdminJWTToken();

        this.mockMvc.perform(post(basePathEn)
                .contentType(contentType)
                .header("Authorization", adminJWTToken)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("new user")));
    }

    @Test(expected = Exception.class)
    public void deleteUserUnauthorized() throws Exception {
        this.mockMvc.perform(delete(basePathEn + "/" + this.adminUser.getId()))
                .andExpect(status().isOk());
    }

    @Test(expected = Exception.class)
    public void deleteUserAsAdmin() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "/" + this.adminUser.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser() throws Exception {
        String adminJWTToken = requestSuperAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "/" + this.adminUser.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUser() throws Exception {
        appAdminUser.setName("app_admin_new_name");
        String apiUserJson = toJson(appAdminUser);

        String adminJWTToken = requestSuperAdminJWTToken();

        this.mockMvc.perform(put(basePathEn + "/")
                .contentType(contentType)
                .content(apiUserJson)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) this.appAdminUser.getId())))
                .andExpect(jsonPath("$.name", is("app_admin_new_name")));
    }
}