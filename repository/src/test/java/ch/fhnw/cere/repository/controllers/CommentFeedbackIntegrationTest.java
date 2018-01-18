package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.ApiUserPermission;
import ch.fhnw.cere.repository.models.CommentFeedback;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.repositories.ApiUserPermissionRepository;
import ch.fhnw.cere.repository.repositories.CommentFeedbackRepository;
import ch.fhnw.cere.repository.repositories.EndUserRepository;
import ch.fhnw.cere.repository.repositories.FeedbackRepository;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;

import kafka.utils.Json;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Aydinli on 05.01.2018.
 */
public class CommentFeedbackIntegrationTest extends BaseIntegrationTest{
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private CommentFeedbackRepository commentFeedbackRepository;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    private String basePathEn = "/feedback_repository/en/";

    private Feedback feedback1;
    private Feedback feedback2;
    private Feedback feedback3;


    private CommentFeedback commentFeedback1_1;
    private CommentFeedback commentFeedback1_2;
    private CommentFeedback commentFeedback1_1_1;
    private CommentFeedback commentFeedback1_1_2;

    private CommentFeedback commentFeedback2_1;
    private CommentFeedback commentFeedback2_2;
    private CommentFeedback commentFeedback2_3;

    private CommentFeedback commentFeedback3_1;

    private EndUser endUser1;
    private EndUser endUser2;
    private EndUser endUser3;

    @Before
    public void setup() throws Exception {
        super.setup();

        feedbackRepository.deleteAllInBatch();
        commentFeedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();

        feedback1 = feedbackRepository.save(new Feedback("Feedback 1 App 1", 11111, 1, 11, "en"));
        feedback2 = feedbackRepository.save(new Feedback("Feedback 2 App 1", 22222, 1, 11, "en"));
        feedback3 = feedbackRepository.save(new Feedback("Feedback 3 App 1", 33333, 1, 22, "en"));

        endUser1 = endUserRepository.save(new EndUser(1,"kaydin1",123));
        endUser2 = endUserRepository.save(new EndUser(1,"kaydin2",123));
        endUser3 = endUserRepository.save(new EndUser(1,"kaydin3",123));


        commentFeedback1_1 = commentFeedbackRepository.save(new CommentFeedback(feedback1,
                endUser1,false,"First Comment of Feedback 1",
                false,null));
        commentFeedback1_1_1 = commentFeedbackRepository.save(new CommentFeedback(feedback1,
                endUser1,false,"First Subcomment of Comment 1",
                false,commentFeedback1_1));
        commentFeedback1_1_2 = commentFeedbackRepository.save(new CommentFeedback(feedback1,
                endUser1,false,"Second Subcomment of Comment 1",
                false,commentFeedback1_1));
        commentFeedback1_2 = commentFeedbackRepository.save(new CommentFeedback(feedback1,
                endUser1,false,"Second Comment of Feedback 1",
                false,null));

        commentFeedback2_1 = commentFeedbackRepository.save(new CommentFeedback(feedback2,
                endUser2,false,"First Comment of Feedback 2",
                false,null));
        commentFeedback2_2 = commentFeedbackRepository.save(new CommentFeedback(feedback2,
                endUser2,false,"Second Comment of Feedback 2",
                false,null));
        commentFeedback2_3 = commentFeedbackRepository.save(new CommentFeedback(feedback2,
                endUser2,false,"Third Comment of Feedback 2",
                false,null));

        commentFeedback3_1 = commentFeedbackRepository.save(new CommentFeedback(feedback3,
                endUser3,false,"First Comment of Feedback 3",
                false,null));

        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 1, true));
        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 20, true));
    }

    @After
    public void cleanUp(){
        super.cleanUp();

        feedbackRepository.deleteAllInBatch();
        commentFeedbackRepository.deleteAllInBatch();
        endUserRepository.deleteAllInBatch();
        apiUserPermissionRepository.deleteAllInBatch();
    }

    @Test(expected = ServletException.class)
    public void getFeedbacksUnauthorized() throws Exception {
        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getCommentsOfApplication() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        String jsonReturn =mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/comments")
                .header("Authorization", adminJWTToken)).andReturn().getResponse().getContentAsString();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/comments")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$[0].id", is((int) commentFeedback1_1.getId())))
                .andExpect(jsonPath("$[0].commentText", is("First Comment of Feedback 1")))
                .andExpect(jsonPath("$[0].feedback.id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[0].user.id", is((int) endUser1.getId())))

                .andExpect(jsonPath("$[1].id", is((int) commentFeedback1_1_1.getId())))
                .andExpect(jsonPath("$[1].commentText", is("First Subcomment of Comment 1")))
                .andExpect(jsonPath("$[1].feedback.id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[1].user.id", is((int) endUser1.getId())))

                .andExpect(jsonPath("$[2].id", is((int) commentFeedback1_1_2.getId())))
                .andExpect(jsonPath("$[2].commentText", is("Second Subcomment of Comment 1")))
                .andExpect(jsonPath("$[2].feedback.id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[2].user.id", is((int) endUser1.getId())))

                .andExpect(jsonPath("$[3].id", is((int) commentFeedback1_2.getId())))
                .andExpect(jsonPath("$[3].commentText", is("Second Comment of Feedback 1")))
                .andExpect(jsonPath("$[3].feedback.id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[3].user.id", is((int) endUser1.getId())))

                .andExpect(jsonPath("$[4].id", is((int) commentFeedback2_1.getId())))
                .andExpect(jsonPath("$[4].commentText", is("First Comment of Feedback 2")))
                .andExpect(jsonPath("$[4].feedback.id", is((int) feedback2.getId())))
                .andExpect(jsonPath("$[4].user.id", is((int) endUser2.getId())))

                .andExpect(jsonPath("$[5].id", is((int) commentFeedback2_2.getId())))
                .andExpect(jsonPath("$[5].commentText", is("Second Comment of Feedback 2")))
                .andExpect(jsonPath("$[5].feedback.id", is((int) feedback2.getId())))
                .andExpect(jsonPath("$[5].user.id", is((int) endUser2.getId())))

                .andExpect(jsonPath("$[6].id", is((int) commentFeedback2_3.getId())))
                .andExpect(jsonPath("$[6].commentText", is("Third Comment of Feedback 2")))
                .andExpect(jsonPath("$[6].feedback.id", is((int) feedback2.getId())))
                .andExpect(jsonPath("$[6].user.id", is((int) endUser2.getId())))

                .andExpect(jsonPath("$[7].id", is((int) commentFeedback3_1.getId())))
                .andExpect(jsonPath("$[7].commentText", is("First Comment of Feedback 3")))
                .andExpect(jsonPath("$[7].feedback.id", is((int) feedback3.getId())))
                .andExpect(jsonPath("$[7].user.id", is((int) endUser3.getId())));
    }

    @Test
    public void getCommentsForFeedback() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        String jsonReturn = mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/comments/" +
                "feedback/"+feedback1.getId())
                .header("Authorization", adminJWTToken)).andReturn().getResponse().getContentAsString();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/comments/" +
                "feedback/"+feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is((int) commentFeedback1_1.getId())))
                .andExpect(jsonPath("$[0].commentText", is("First Comment of Feedback 1")))
                .andExpect(jsonPath("$[0].feedback.id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[0].user.id", is((int) endUser1.getId())))

                .andExpect(jsonPath("$[1].id", is((int) commentFeedback1_1_1.getId())))
                .andExpect(jsonPath("$[1].commentText", is("First Subcomment of Comment 1")))
                .andExpect(jsonPath("$[1].feedback.id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[1].user.id", is((int) endUser1.getId())))

                .andExpect(jsonPath("$[2].id", is((int) commentFeedback1_1_2.getId())))
                .andExpect(jsonPath("$[2].commentText", is("Second Subcomment of Comment 1")))
                .andExpect(jsonPath("$[2].feedback.id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[2].user.id", is((int) endUser1.getId())))

                .andExpect(jsonPath("$[3].id", is((int) commentFeedback1_2.getId())))
                .andExpect(jsonPath("$[3].commentText", is("Second Comment of Feedback 1")))
                .andExpect(jsonPath("$[3].feedback.id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[3].user.id", is((int) endUser1.getId())));
    }

    @Test
    public void getCommentsForUser() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        String jsonReturn = mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/comments/" +
                "user/"+endUser1.getId())
                .header("Authorization", adminJWTToken)).andReturn().getResponse().getContentAsString();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/comments/" +
                "user/"+endUser1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].user.id", is((int) endUser1.getId())))
                .andExpect(jsonPath("$[1].user.id", is((int) endUser1.getId())))
                .andExpect(jsonPath("$[2].user.id", is((int) endUser1.getId())))
                .andExpect(jsonPath("$[3].user.id", is((int) endUser1.getId())));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/comments/" +
                "user/"+endUser2.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].user.id", is((int) endUser2.getId())))
                .andExpect(jsonPath("$[1].user.id", is((int) endUser2.getId())))
                .andExpect(jsonPath("$[2].user.id", is((int) endUser2.getId())));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/comments/" +
                "user/"+endUser3.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user.id", is((int) endUser3.getId())));
    }

    @Test
    public void getComment() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        String jsonReturn = mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/comments/" +
                commentFeedback1_1.getId())
                .header("Authorization", adminJWTToken)).andReturn().getResponse().getContentAsString();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/comments/" +
                commentFeedback1_1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is((int) commentFeedback1_1.getId())))
                .andExpect(jsonPath("$.commentText", is("First Comment of Feedback 1")));
    }

    @Test
    public void postComment() throws Exception {
        CommentFeedback commentFeedback = new CommentFeedback(feedback1,endUser1,false,
                "test comment for posting",false,null);
        String commentJson = new JSONObject()
                .put("feedback_id",commentFeedback.getFeedback().getId())
                .put("user_id",commentFeedback.getUser().getId())
                .put("commentText",commentFeedback.getCommentText())
                .put("bool_is_developer",commentFeedback.check_is_developer())
                .put("activeStatus",commentFeedback.getActiveStatus())
                .toString();

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks/comments")
            .contentType(contentType)
            .content(commentJson))
        .andExpect(status().isCreated());
    }
}
