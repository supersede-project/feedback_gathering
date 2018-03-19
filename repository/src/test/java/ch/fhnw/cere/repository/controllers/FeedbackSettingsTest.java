package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.ApiUserPermission;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackSettings;
import ch.fhnw.cere.repository.repositories.ApiUserPermissionRepository;
import ch.fhnw.cere.repository.repositories.EndUserRepository;
import ch.fhnw.cere.repository.repositories.FeedbackRepository;
import ch.fhnw.cere.repository.repositories.FeedbackSettingsRepository;
import ch.fhnw.cere.repository.services.EndUserService;
import ch.fhnw.cere.repository.services.FeedbackService;
import ch.fhnw.cere.repository.services.FeedbackSettingsService;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Aydinli on 01.02.2018.
 */
public class FeedbackSettingsTest extends BaseIntegrationTest{
    private String basePathEn = "/feedback_repository/en/";

    @Autowired
    private FeedbackSettingsRepository feedbackSettingsRepository;

    @Autowired
    private FeedbackSettingsService feedbackSettingsService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private EndUserService endUserService;

    private Feedback feedback1;
    private Feedback feedback2;
    private Feedback feedback3;
    private Feedback feedback4;

    private EndUser endUser;

    private FeedbackSettings feedbackSettings1;
    private FeedbackSettings feedbackSettings2;
    private FeedbackSettings feedbackSettings3;

    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    @Before
    public void setup() throws Exception{
        super.setup();
        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();
        feedbackSettingsRepository.deleteAllInBatch();

        endUser = endUserRepository.save(new EndUser(1,"kaydin1",123));

        feedback1 = feedbackRepository.save(new Feedback("Feedback 1 App 1",
                endUser.getId(), 1, 11, "en"));
        feedback2 = feedbackRepository.save(new Feedback("Feedback 2 App 1",
                endUser.getId(), 1, 11, "en"));
        feedback3 = feedbackRepository.save(new Feedback("Feedback 3 App 1",
                endUser.getId(), 1, 22, "en"));
        feedback4 = feedbackRepository.save(new Feedback("Feedback 3 App 1",
                endUser.getId(), 1, 22, "en"));


        feedbackSettings1 = feedbackSettingsRepository.save(new FeedbackSettings(endUser,
                feedback1,true,"Email",
                false,"",false));
        feedbackSettings1 = feedbackSettingsRepository.save(new FeedbackSettings(endUser,
                feedback2,true,"Email",
                false,"",true));
        feedbackSettings1 = feedbackSettingsRepository.save(new FeedbackSettings(endUser,
                feedback3,false,"",
                true,"Email",false));

        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 1, true));
        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 20, true));
    }

    @After
    public void cleanup(){
        super.cleanUp();
        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();
        feedbackSettingsRepository.deleteAllInBatch();
    }

    @Test
    public void testGetSettings() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        String result = this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings/feedback/"+feedback1.getId())
                .header("Authorization", adminJWTToken)).andReturn().getResponse()
                .getContentAsString();

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings/feedback/"+feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusUpdates", is(true)));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings/feedback/"+0)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isNotFound());

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings/user_identification/"+endUser.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings/"+feedbackSettings1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusUpdates", is(feedbackSettings1.getStatusUpdates())))
                .andExpect(jsonPath("$.feedbackQuery", is(feedbackSettings1.getFeedbackQuery())));

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings/"+0)
                .header("Authorization", adminJWTToken))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateSettings() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        String feedbackSettings = new JSONObject()
                .put("statusUpdates",true)
                .put("statusUpdatesContactChannel","Email")
                .put("feedbackQuery",false)
                .put("feedbackQueryChannel","")
                .put("globalFeedbackSetting",false)
                .put("feedback_id",feedback4.getId())
                .toString();

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings")
                .header("Authorization", adminJWTToken)
                .content(feedbackSettings))
                .andExpect(status().isCreated());

        String feedbackSettingsUpdate = new JSONObject()
                .put("statusUpdates",false)
                .put("statusUpdatesContactChannel","")
                .put("feedbackQuery",false)
                .put("feedbackQueryChannel","")
                .put("globalFeedbackSetting",false)
                .put("feedback_id",feedback1.getId())
                .toString();

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings")
                .header("Authorization", adminJWTToken)
                .content(feedbackSettingsUpdate))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings/feedback/"+feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusUpdates", is(false)));
    }

    @Test
    public void testDeleteSettings() throws Exception{
        String adminJWTToken = requestSuperAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings/"+feedbackSettings1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/feedbacksettings")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
