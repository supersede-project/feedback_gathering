package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.ApiUserPermission;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackStatus;
import ch.fhnw.cere.repository.repositories.ApiUserPermissionRepository;
import ch.fhnw.cere.repository.repositories.EndUserRepository;
import ch.fhnw.cere.repository.repositories.FeedbackRepository;
import ch.fhnw.cere.repository.repositories.FeedbackStatusRepository;
import ch.fhnw.cere.repository.services.FeedbackService;
import ch.fhnw.cere.repository.services.FeedbackStatusService;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Aydinli on 02.02.2018.
 */
public class FeedbackStatusTest extends BaseIntegrationTest {
    private String basePathEn = "/feedback_repository/en/";

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackStatusRepository feedbackStatusRepository;

    @Autowired
    private FeedbackStatusService feedbackStatusService;

    @Autowired
    private EndUserRepository endUserRepository;

    Feedback feedback1;
    Feedback feedback2;
    Feedback feedback3;
    Feedback feedback4;

    EndUser endUser;

    FeedbackStatus feedbackStatus1;
    FeedbackStatus feedbackStatus2;
    FeedbackStatus feedbackStatus3;
    FeedbackStatus feedbackStatus4;

    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    @Before
    public void setup() throws Exception{
        super.setup();
        feedbackRepository.deleteAllInBatch();
        feedbackStatusRepository.deleteAllInBatch();

        endUser = endUserRepository.save(new EndUser(1,"kaydin1",123));

        feedback1 = feedbackRepository.save(new Feedback("Feedback 1 App 1", endUser.getId(),
                1, 11, "en"));
        feedback2 = feedbackRepository.save(new Feedback("Feedback 2 App 1", endUser.getId(),
                1, 11, "en"));
        feedback3 = feedbackRepository.save(new Feedback("Feedback 3 App 1", endUser.getId(),
                1, 22, "en"));
        feedback4 = feedbackRepository.save(new Feedback("Feedback 4 App 1", endUser.getId(),
                1, 22, "en"));

        feedbackStatus1 = feedbackStatusRepository.save(new FeedbackStatus(feedback1,"status1"));
        feedbackStatus2 = feedbackStatusRepository.save(new FeedbackStatus(feedback2,"status2"));
        feedbackStatus3 = feedbackStatusRepository.save(new FeedbackStatus(feedback3,"status3"));

        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 1, true));
        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 20, true));
    }

    @After
    public void cleanup(){
        super.cleanUp();
        endUserRepository.deleteAllInBatch();
        feedbackRepository.deleteAllInBatch();
        feedbackStatusRepository.deleteAllInBatch();
        apiUserPermissionRepository.deleteAllInBatch();
    }

    @Test
    public void testGetStates() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/status")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/status/feedback/"+feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback.id", is((int) feedback1.getId())));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/status/state/status1")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/status/"+feedbackStatus1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("status1")));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/status/"+0)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateStatus() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        String feedbackCompany = new JSONObject()
                .put("feedback_id",feedback1.getId())
                .put("status","pending")
                .toString();

        String feedbackCompany2 = new JSONObject()
                .put("feedback_id",feedback4.getId())
                .put("status","pending")
                .toString();

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/status")
                .header("Authorization", adminJWTToken)
                .content(feedbackCompany))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/status")
                .header("Authorization", adminJWTToken)
                .content(feedbackCompany2))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/status")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteStatus() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/status/"+feedbackStatus1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/status")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}