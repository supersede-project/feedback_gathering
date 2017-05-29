package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.models.ApiUser;
import ch.fhnw.cere.orchestrator.models.authentication.AuthenticationRequest;
import ch.fhnw.cere.orchestrator.repositories.ApiUserApiUserRoleRepository;
import ch.fhnw.cere.orchestrator.repositories.ApiUserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthenticationIntegrationTest extends BaseIntegrationTest {

    private ApiUser adminUser;
    private ApiUser superAdminUser;

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Autowired
    private ApiUserApiUserRoleRepository apiUserApiUserRoleRepository;

    private PasswordEncoder passwordEncoder;

    @Before
    public void setup() throws Exception {
        super.setup();
    }

    @After
    public void cleanUp() {
        super.cleanUp();
    }

    @Test
    public void testEncryption() {
        passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("password"));
        System.out.println(passwordEncoder.encode("superpassword"));
    }

    @Test
    public void testAuthenticationWithWrongCredentials() throws Exception {
        String authenticationJsonUsernameWrong = toJson(new AuthenticationRequest("username", "asdfasd"));
        mockMvc.perform(post("/authenticate")
                .contentType(contentType)
                .content(authenticationJsonUsernameWrong))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Wrong credentials")));

        String authenticationJsonPasswordWrong = toJson(new AuthenticationRequest("usernameeeee", "password"));
        mockMvc.perform(post("/authenticate")
                .contentType(contentType)
                .content(authenticationJsonPasswordWrong))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", is("Wrong credentials")));
    }

    @Test
    public void testAuthentication() throws Exception {
        String authenticationJson = toJson(new AuthenticationRequest("admin", "password"));
        mockMvc.perform(post("/authenticate")
                .contentType(contentType)
                .content(authenticationJson))
                .andExpect(status().isOk());

        String authenticationJsonSuperAdmin = toJson(new AuthenticationRequest("super_admin", "superpassword"));
        mockMvc.perform(post("/authenticate")
                .contentType(contentType)
                .content(authenticationJsonSuperAdmin))
                .andExpect(status().isOk());
    }

    @Test
    public void testAdminsJWTToken() {
        System.err.println(requestAdminJWTToken());
        System.err.println(requestSuperAdminJWTToken());
    }
}