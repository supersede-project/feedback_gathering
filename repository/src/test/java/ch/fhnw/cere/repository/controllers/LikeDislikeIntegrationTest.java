package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.repositories.*;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.client.ExpectedCount;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Aydinli on 18.01.2018.
 */
public class LikeDislikeIntegrationTest extends BaseIntegrationTest{
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private UserFeedbackLikeRepository userFeedbackLikeRepository;

    @Autowired
    private UserFeedbackDislikeRepository userFeedbackDislikeRepository;

    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    private String basePathEn = "/feedback_repository/en/";

    private Feedback feedback1;
    private Feedback feedback2;
    private Feedback feedback3;
    private Feedback feedback4;

    private EndUser endUser1;
    private EndUser endUser2;
    private EndUser endUser3;

    private UserFBLike userFBLike1;
    private UserFBLike userFBLike2;
    private UserFBLike userFBLike3;
    private UserFBLike userFBLike4;

    private UserFBDislike userFBDislike1;
    private UserFBDislike userFBDislike2;
    private UserFBDislike userFBDislike3;
    private UserFBDislike userFBDislike4;


    @Before
    public void setup() throws Exception{
        super.setup();

        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();

        feedback1 = feedbackRepository.save(new Feedback("Feedback 1 App 1", 11111, 1, 11, "en"));
        feedback2 = feedbackRepository.save(new Feedback("Feedback 2 App 1", 22222, 1, 11, "en"));
        feedback3 = feedbackRepository.save(new Feedback("Feedback 3 App 1", 33333, 1, 22, "en"));
        feedback4 = feedbackRepository.save(new Feedback("Feedback 4 App 1", 44444, 1, 22, "en"));

        endUser1 = endUserRepository.save(new EndUser(1,"kaydin1",123));
        endUser2 = endUserRepository.save(new EndUser(1,"kaydin2",123));
        endUser3 = endUserRepository.save(new EndUser(1,"kaydin3",123));

        userFBLike1 = userFeedbackLikeRepository.save(new UserFBLike(endUser1,feedback1));
        userFBLike2 = userFeedbackLikeRepository.save(new UserFBLike(endUser1,feedback2));
        userFBLike3 = userFeedbackLikeRepository.save(new UserFBLike(endUser2,feedback3));
        userFBLike4 = userFeedbackLikeRepository.save(new UserFBLike(endUser2,feedback4));

        userFBDislike1 = userFeedbackDislikeRepository.save(new UserFBDislike(endUser1,feedback3));
        userFBDislike2 = userFeedbackDislikeRepository.save(new UserFBDislike(endUser1,feedback4));
        userFBDislike3 = userFeedbackDislikeRepository.save(new UserFBDislike(endUser2,feedback1));
        userFBDislike4 = userFeedbackDislikeRepository.save(new UserFBDislike(endUser2,feedback2));

        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 1, true));
    }

    @After
    public void cleanUp(){
        super.cleanUp();

        feedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();
        userFeedbackLikeRepository.deleteAllInBatch();
        userFeedbackDislikeRepository.deleteAllInBatch();
        apiUserPermissionRepository.deleteAllInBatch();
    }

    @Test
    public void getLikesAndDislikes() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/likes")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/likes/feedback/"+feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/likes/user/"+endUser1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/likes/"+userFBLike1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) userFBLike1.getId())));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/dislikes")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/dislikes/feedback/"+feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/dislikes/user/"+endUser1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/dislikes/"+userFBDislike1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) userFBDislike1.getId())));
    }

    @Test
    public void testEndUser1DislikeFeedback1() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        String postDislike = new JSONObject()
                .put("user_id",endUser1.getId())
                .put("feedback_id",feedback1.getId())
                .toString();

        String postLike = new JSONObject()
                .put("user_id",endUser3.getId())
                .put("feedback_id",feedback1.getId())
                .toString();

        String postLike2 = new JSONObject()
                .put("user_id",endUser1.getId())
                .put("feedback_id",feedback3.getId())
                .toString();

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/dislikes")
                .contentType(contentType)
                .content(postLike2))
                .andExpect(status().isCreated());

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/dislikes")
                .contentType(contentType)
                .content(postDislike))
                .andExpect(status().isCreated());

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/likes/both/" +
                +endUser1.getId()+"/"+feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(content().string(""));

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/likes")
                .contentType(contentType)
                .content(postLike))
                .andExpect(status().isCreated());

//        String jsonReturn2 = mockMvc.perform(get(basePathEn + "applications/" + 1 +
//                "/feedbacks/" + feedback1.getId())
//                .header("Authorization", adminJWTToken))
//                .andReturn().getResponse().getContentAsString();
//
//        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/"
//                +feedback1.getId())
//                .header("Authorization", adminJWTToken))
//                .andExpect(jsonPath("$.likeCount", is(0)))
//                .andExpect(jsonPath("$.dislikeCount", is(2)));
    }


    @Test
    public void testEndUser1MultipleLike() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();
        String postLike = new JSONObject()
                .put("user_id",endUser1.getId())
                .put("feedback_id",feedback1.getId())
                .toString();

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks/likes")
                .contentType(contentType)
                .content(postLike))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",is((int) userFBLike1.getId())));
    }

    @Test
    public void testDeleteLikeAndDislike() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();

        this.mockMvc.perform(delete(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/likes/" + userFBLike1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());

        this.mockMvc.perform(delete(basePathEn + "applications/" + 1 + "/feedbacks" +
                "/dislikes/" + userFBDislike1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk());
    }
}
