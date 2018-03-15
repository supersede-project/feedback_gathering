package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.ApiUserPermission;
import ch.fhnw.cere.repository.models.EmailUnsubscribed;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.repositories.ApiUserPermissionRepository;
import ch.fhnw.cere.repository.repositories.EmailUnsubscribedRepository;
import ch.fhnw.cere.repository.repositories.EndUserRepository;
import ch.fhnw.cere.repository.services.EmailUnsubscribedService;
import ch.fhnw.cere.repository.services.EndUserService;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Aydinli on 01.02.2018.
 */
public class EmailUnsubscriptionTest extends BaseIntegrationTest{
    @Autowired
    private EndUserService endUserService;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private EmailUnsubscribedService emailUnsubscribedService;

    @Autowired
    private EmailUnsubscribedRepository emailUnsubscribedRepository;

    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    private String basePathEn = "/feedback_repository/en/";

    private EmailUnsubscribed emailUnsubscribed1;
    private EmailUnsubscribed emailUnsubscribed2;

    private EndUser endUser1;
    private EndUser endUser2;
    private EndUser endUser3;

    @Before
    public void setup() throws Exception{
        super.setup();
        endUserRepository.deleteAllInBatch();
        emailUnsubscribedRepository.deleteAllInBatch();

        endUser1 = endUserRepository.save(new EndUser(1,"kaydin1",
                12,"foo@foo.com"));
        endUser2 = endUserRepository.save(new EndUser(1,"kaydin2",
                12,"foo@foo.com"));
        endUser3 = endUserRepository.save(new EndUser(1,"kaydin3",
                12,"foo@foo.com"));

        emailUnsubscribed1 = emailUnsubscribedRepository.
                save(new EmailUnsubscribed(endUser1,endUser1.getEmail()));

        emailUnsubscribed2 = emailUnsubscribedRepository.
                save(new EmailUnsubscribed(endUser2,endUser2.getEmail()));

        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 1, true));
        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 20, true));
    }

    @After
    public void cleanup() {
        super.cleanUp();
        endUserRepository.deleteAllInBatch();
        emailUnsubscribedRepository.deleteAllInBatch();
    }

    @Test
    public void testGetUnsubscribed() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/unsubscribed")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/unsubscribed/user/"+endUser1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(endUser1.getEmail())));
    }

    @Test
    public void testUnsubscribeUser() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        String unsubscribe = new JSONObject()
                .put("userId",endUser3.getId())
                .put("email",endUser3.getEmail())
                .toString();

        String unsubscribeExist = new JSONObject()
                .put("userId",endUser1.getId())
                .put("email",endUser1.getEmail())
                .toString();

        HttpEntity<String> testString = null;

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/unsubscribed")
                .header("Authorization", adminJWTToken)
                .content(unsubscribe))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/unsubscribed")
                .header("Authorization", adminJWTToken)
                .content(unsubscribeExist))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/unsubscribed")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/end_user")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isCreated());
    }

    @Test
    public void testRemoveUserFromList() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/unsubscribed/"+emailUnsubscribed1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/unsubscribed")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        this.mockMvc.perform(delete(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/unsubscribed/user/"+endUser2.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/unsubscribed")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
