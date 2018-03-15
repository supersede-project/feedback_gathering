package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.mail.EmailService;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.TextFeedback;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.repositories.EndUserRepository;
import ch.fhnw.cere.repository.repositories.FeedbackRepository;
import ch.fhnw.cere.repository.services.FeedbackService;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import scala.xml.pull.ExceptionEvent;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Created by Aydinli on 30.01.2018.
 *
 * This test suite is responsible for testing API endpoints regarding the BLOCKED, VISIBLE and
 * PUBLISHED attributes of feedbacks. There attributes come in handy when dealing with feedbacks
 * supposed to be visible and shared in the Forum
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepositoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class FeedbackAttributeIntegrationTest extends BaseIntegrationTest{

    private String basePathEn = "/feedback_repository/en/";

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private EndUserRepository endUserRepository;

    private static Logger log = LoggerFactory.getLogger(Application.class);

    private Feedback feedback1;
    private Feedback feedback2;
    private Feedback feedback3;
    private Feedback feedback4;
    private Feedback feedback5;
    private Feedback feedback6;
    private Feedback feedback7;
    private Feedback feedback8;
    private Feedback feedback9;

    private EndUser endUser1;
    private EndUser endUser2;
    private EndUser endUser3;

    @Before
    public void setup() throws Exception{
        super.setup();
        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();

        endUser1 = endUserRepository.save(new EndUser(1,"Ronnie Schaniel",123,"f2f_central@hotmail.com"));
        endUser2 = endUserRepository.save(new EndUser(1,"Melanie Stade",123,"f2f_central@hotmail.com"));
        endUser3 = endUserRepository.save(new EndUser(1,"Marina Melwin",123,"f2f_central@hotmail.com"));

        feedback1 = feedbackRepository.save(new Feedback("Feedback 1 Ronnie", endUser1.getId(), 1, 11, "en"));
        feedback1.setBlocked(true);feedback1.setVisibility(true);feedback1.setPublished(false);
        feedbackRepository.save(feedback1);

        feedback2 = feedbackRepository.save(new Feedback("Feedback 2 Ronnie", endUser1.getId(), 1, 11, "en"));
        feedback2.setPublished(true);feedback2.setVisibility(true);feedback2.setBlocked(false);
        feedbackRepository.save(feedback2);

        feedback3 = feedbackRepository.save(new Feedback("Feedback 3 Ronnie", endUser1.getId(), 1, 22, "en"));
        feedback3.setVisibility(true);feedback3.setBlocked(false);feedback3.setPublished(false);
        feedbackRepository.save(feedback3);

        feedback4 = feedbackRepository.save(new Feedback("Feedback 4 Melanie", endUser2.getId(), 1, 22, "en"));
        feedback4.setBlocked(true);feedback4.setVisibility(false);feedback4.setPublished(false);
        feedbackRepository.save(feedback4);

        feedback5 = feedbackRepository.save(new Feedback("Feedback 5 Melanie", endUser2.getId(), 1, 11, "en"));
        feedback5.setBlocked(true);feedback5.setVisibility(false);feedback5.setPublished(false);
        feedbackRepository.save(feedback5);

        feedback6 = feedbackRepository.save(new Feedback("Feedback 6 Melanie", endUser2.getId(), 1, 11, "en"));
        feedback6.setPublished(true);feedback6.setVisibility(true);feedback6.setBlocked(false);
        feedbackRepository.save(feedback6);

        feedback7 = feedbackRepository.save(new Feedback("Feedback 7 Marina", endUser3.getId(), 1, 22, "en"));
        feedback7.setVisibility(true);feedback7.setBlocked(false);feedback7.setPublished(false);
        feedbackRepository.save(feedback7);

        feedback8 = feedbackRepository.save(new Feedback("Feedback 8 Marina", endUser3.getId(), 1, 22, "en"));
        feedback8.setBlocked(true);feedback8.setVisibility(true);feedback8.setPublished(true);
        feedbackRepository.save(feedback8);
    }

    @After
    public void cleanup(){
        super.cleanUp();
        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();
    }

    @Test
    public void testGetBlockedFeedbacks() throws Exception{
        String adminJWTToken = requestSuperAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/get_published/unread/"+endUser1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isNotFound());

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/get_blocked/false")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/get_blocked/true")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void testGetVisibleFeedbacks() throws Exception{
        String adminJWTToken = requestSuperAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/get_visible/true")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/get_visible/false")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetPublishedFeedbacks() throws Exception{
        String adminJWTToken = requestSuperAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/get_published/true")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/get_published/false")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void testGetPendingFeedbacks() throws Exception{
        String adminJWTToken = requestSuperAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/pending_publication")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testBlockFeedbacks() throws Exception{
        String adminJWTToken = requestSuperAdminJWTToken();

        String blockJson1 = new JSONObject()
                .put("blocked",true)
                .toString();

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/blocked" +
                "/"+feedback2.getId())
                .contentType(contentType)
                .content(blockJson1))
                .andExpect(content().string("Feedback blocked status changed!"));

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/blocked" +
                "/"+0)
                .contentType(contentType)
                .content(blockJson1))
                .andExpect(content().string("Requested Feedback does not exist"));

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/blocked" +
                "/"+0)
                .contentType(contentType))
                .andExpect(content().string("JSON Body is NULL"));

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/blocked" +
                "/"+feedback3.getId())
                .contentType(contentType)
                .content(blockJson1))
                .andExpect(content().string("Feedback blocked status changed!"));

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/blocked" +
                "/"+feedback6.getId())
                .contentType(contentType)
                .content(blockJson1))
                .andExpect(content().string("Feedback blocked status changed!"));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/get_blocked/true")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }

    @Test
    public void testSetFeedbacksVisible() throws Exception{
        String adminJWTToken = requestSuperAdminJWTToken();

        String blockJson1 = new JSONObject()
                .put("visible",true)
                .toString();

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/visibility" +
                "/"+feedback4.getId())
                .contentType(contentType)
                .content(blockJson1))
                .andExpect(content().string("Feedback visibility changed!"));

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/visibility" +
                "/"+feedback5.getId())
                .contentType(contentType)
                .content(blockJson1))
                .andExpect(content().string("Feedback visibility changed!"));

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/visibility" +
                "/"+0)
                .contentType(contentType)
                .content(blockJson1))
                .andExpect(content().string("Requested Feedback does not exist"));

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/visibility" +
                "/"+0)
                .contentType(contentType))
                .andExpect(content().string("JSON Body is NULL"));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/get_visible/true")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)));
    }

    @Test
    public void testPublishFeedbacks() throws Exception{
        String adminJWTToken = requestSuperAdminJWTToken();

        String blockJson1 = new JSONObject()
                .put("published",true)
                .toString();

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/published" +
                "/"+feedback1.getId())
                .contentType(contentType)
                .content(blockJson1))
                .andExpect(content().string("Feedback published state changed!"));

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/published" +
                "/"+0)
                .contentType(contentType)
                .content(blockJson1))
                .andExpect(content().string("Requested Feedback does not exist"));

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/published" +
                "/"+0)
                .contentType(contentType))
                .andExpect(content().string("JSON Body is NULL"));

        this.mockMvc.perform(put(basePathEn + "applications/" + 1 + "/feedbacks/published" +
                "/"+feedback3.getId())
                .contentType(contentType)
                .content(blockJson1))
                .andExpect(content().string("Feedback published state changed!"));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/get_published/true")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }
}
