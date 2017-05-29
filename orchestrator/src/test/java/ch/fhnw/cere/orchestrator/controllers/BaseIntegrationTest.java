package ch.fhnw.cere.orchestrator.controllers;


import ch.fhnw.cere.orchestrator.OrchestratorApplication;
import ch.fhnw.cere.orchestrator.models.ApiUser;
import ch.fhnw.cere.orchestrator.models.ApiUserApiUserRole;
import ch.fhnw.cere.orchestrator.models.ApiUserRole;
import ch.fhnw.cere.orchestrator.repositories.ApiUserApiUserRoleRepository;
import ch.fhnw.cere.orchestrator.repositories.ApiUserRepository;
import ch.fhnw.cere.orchestrator.security.TokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrchestratorApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    public static final String PASSWORD_HASH_ADMIN = "$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy";
    public static final String PASSWORD_HASH_SUPER_ADMIN = "$2a$10$y9K.1fd6VgT26rftcoziV.Qm74r8Qe1Y0hv.Kw4L1e3IMsxEXdWJu";
    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Autowired
    private ApiUserApiUserRoleRepository apiUserApiUserRoleRepository;

    @Autowired
    private TokenUtils tokenUtils;

    protected ApiUser adminUser;
    protected ApiUser superAdminUser;

    @Before
    public void setup() throws Exception {
        SecurityContextHolder.clearContext();

        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        apiUserApiUserRoleRepository.deleteAllInBatch();
        apiUserRepository.deleteAllInBatch();

        this.adminUser = insertAdminApiUser();
        this.superAdminUser = insertSuperAdminApiUser();
    }

    @After
    public void cleanUp() {
        apiUserApiUserRoleRepository.deleteAllInBatch();
        apiUserRepository.deleteAllInBatch();

        SecurityContextHolder.clearContext();
    }

    protected String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString= null;
        try {
            jsonString = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    /**
     *
     * @return an ApiUser with username 'admin', password 'password' and the ADMIN role
     */
    protected ApiUser insertAdminApiUser() {
        ApiUser adminUser = apiUserRepository.save(new ApiUser("admin", PASSWORD_HASH_ADMIN));
        apiUserApiUserRoleRepository.save(new ApiUserApiUserRole(adminUser, ApiUserRole.ADMIN));
        return adminUser;
    }

    /**
     *
     * @return an ApiUser with username 'super_admin', password 'superpassword' and the SUPER_ADMIN role
     */
    protected ApiUser insertSuperAdminApiUser() {
        ApiUser superAdmin = apiUserRepository.save(new ApiUser("super_admin", PASSWORD_HASH_SUPER_ADMIN));
        apiUserApiUserRoleRepository.save(new ApiUserApiUserRole(superAdmin, ApiUserRole.SUPER_ADMIN));
        return superAdmin;
    }

    protected String requestAdminJWTToken() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        MvcResult result = null;
        try {
            result = mockMvc.perform(post("/orchestrator/feedback/authenticate").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"admin\",\"password\":\"password\"}"))
                    .andReturn();
            String json = result.getResponse().getContentAsString();
            JSONObject obj = new JSONObject(json);
            return obj.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    protected String requestSuperAdminJWTToken() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        MvcResult result = null;
        try {
            result = mockMvc.perform(post("/orchestrator/feedback/authenticate").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"super_admin\",\"password\":\"superpassword\"}"))
                    .andReturn();
            String json = result.getResponse().getContentAsString();
            JSONObject obj = new JSONObject(json);
            return obj.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}