package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.ApiUserPermission;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackChatInformation;
import ch.fhnw.cere.repository.repositories.ApiUserPermissionRepository;
import ch.fhnw.cere.repository.repositories.EndUserRepository;
import ch.fhnw.cere.repository.repositories.FeedbackChatInformationRepository;
import ch.fhnw.cere.repository.repositories.FeedbackRepository;
import ch.fhnw.cere.repository.services.EndUserService;
import ch.fhnw.cere.repository.services.FeedbackChatInformationService;
import ch.fhnw.cere.repository.services.FeedbackService;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Aydinli on 01.02.2018.
 */
public class FeedbackChatTest extends BaseIntegrationTest{
    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private EndUserService endUserService;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    private String basePathEn = "/feedback_repository/en/";

    @Autowired
    private FeedbackChatInformationService feedbackChatInformationService;

    @Autowired
    private FeedbackChatInformationRepository feedbackChatInformationRepository;

    private Feedback feedback1;
    private Feedback feedback2;
    private Feedback feedback3;

    private EndUser endUser1;

    private FeedbackChatInformation feedbackChatInformation1;
    private FeedbackChatInformation feedbackChatInformation2;
    private FeedbackChatInformation feedbackChatInformation3;

    @Before
    public void setup() throws Exception{
        super.setup();
        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();
        feedbackChatInformationRepository.deleteAllInBatch();

        endUser1 = endUserRepository.save(new EndUser(1,"kaydin1"));

        feedback1 = feedbackRepository.save(new Feedback("Feedback 1 App 1",
                endUser1.getId(), 1, 11, "en"));
        feedback2 = feedbackRepository.save(new Feedback("Feedback 2 App 1",
                endUser1.getId(), 1, 11, "en"));
        feedback3 = feedbackRepository.save(new Feedback("Feedback 3 App 1",
                endUser1.getId(), 1, 22, "en"));

        feedbackChatInformation1 = new FeedbackChatInformation(endUser1,feedback1,
                "chat message for User 1 and Feedback 1");
        feedbackChatInformationService.save(feedbackChatInformation1);
        feedbackChatInformation2 = new FeedbackChatInformation(endUser1,feedback2,
                "chat message for User 1 and Feedback 2");
        feedbackChatInformationService.save(feedbackChatInformation2);
        feedbackChatInformation3 = new FeedbackChatInformation(endUser1,feedback3,
                "chat message for User 1 and Feedback 3");
        feedbackChatInformationService.save(feedbackChatInformation3);

        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 1, true));
        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 20, true));
    }

    @After
    public void cleanup(){
        super.cleanUp();
        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();
        feedbackChatInformationRepository.deleteAllInBatch();
        apiUserPermissionRepository.deleteAllInBatch();
    }

    @Test
    public void testGetMessagesForUserAndFeedback() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_chat")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_chat/feedback/"+feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_chat/user/"+endUser1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testCreateChatMessage() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        String chatMessage = new JSONObject()
                .put("feedback_id",feedback1.getId())
                .put("user_id",endUser1.getId())
                .put("chat_text","chat text for enduser 1 und feedback 1")
                .toString();

        String chatMessageUser = new JSONObject()
                .put("feedback_id",feedback1.getId())
                .put("user_id",endUser1.getId())
                .put("chat_text","chat text for enduser 1 und feedback 1")
                .put("initiated_by_user",true)
                .toString();

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_chat")
                .header("Authorization", adminJWTToken)
                .content(chatMessage))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_chat")
                .header("Authorization", adminJWTToken)
                .content(chatMessageUser))
                .andExpect(status().isCreated());

        String result = this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_chat")
                .header("Authorization", adminJWTToken)).andReturn().getResponse()
                .getContentAsString();

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_chat")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void testDeleteChateMessage() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_chat/"+feedbackChatInformation1.getFeedbackChatId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_chat")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedback_chat/"+feedbackChatInformation2.getFeedbackChatId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackChatId",
                        is((int) feedbackChatInformation2.getFeedbackChatId())));
    }
}
