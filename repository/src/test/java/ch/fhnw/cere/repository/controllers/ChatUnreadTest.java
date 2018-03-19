package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.ApiUserPermission;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackChatInformation;
import ch.fhnw.cere.repository.repositories.*;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Aydinli on 15.03.2018.
 */
public class ChatUnreadTest extends BaseIntegrationTest{
    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ChatUnreadRepository chatUnreadRepository;

    @Autowired
    private FeedbackChatInformationRepository feedbackChatInformationRepository;

    private String basePathEn = "/feedback_repository/en/";

    private EndUser endUser1;
    private EndUser endUser2;

    private Feedback feedback1;
    private Feedback feedback2;
    private FeedbackChatInformation feedbackChatInformation1;

    @Before
    public void setup() throws Exception{
        super.setup();
        endUserRepository.deleteAllInBatch();

        endUser1 = endUserRepository.save(new EndUser(1,"enduser",
                12,"foo@foo.com"));
        endUser2 = endUserRepository.save(new EndUser(1,"developer",
                12,"foo@foo.com"));

        feedback1 = feedbackRepository.save(new Feedback("Feedback 1 App 1",
                endUser1.getId(), 1, 11, "en"));
        feedback2 = feedbackRepository.save(new Feedback("Feedback 2 App 1",
                endUser1.getId(), 1, 11, "en"));

        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 1, true));
        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 20, true));
    }

    @After
    public void cleanup() {
        super.cleanUp();
        endUserRepository.deleteAllInBatch();
        feedbackRepository.deleteAllInBatch();
        chatUnreadRepository.deleteAllInBatch();
        feedbackChatInformationRepository.deleteAllInBatch();
    }

    @Test
    public void testChatUnreadController() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        String chatMessage = new JSONObject()
                .put("feedback_id",feedback1.getId())
                .put("user_id",endUser2.getId())
                .put("chat_text","chat text for enduser 2 und feedback 1")
                .toString();

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_chat")
                .header("Authorization", adminJWTToken)
                .content(chatMessage))
                .andExpect(status().isCreated());

        String result = this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/chat_unread")
                .header("Authorization", adminJWTToken)).andReturn().getResponse()
                .getContentAsString();

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/chat_unread")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/chat_unread/feedback/"+feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/chat_unread/user/"+endUser1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/chat_unread/feedback/"+feedback1.getId()+"/user/"+endUser1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));


        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/chat_unread/feedback/"+feedback1.getId()+"/user/"+endUser1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/chat_unread")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
