package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.repositories.FeedbackRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class FeedbackIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    private String basePathEn = "/feedback_repository/en/";
    private String basePathDe = "/feedback_repository/de/";

    private Feedback feedback1;
    private Feedback feedback2;
    private Feedback feedback3;

    @Before
    public void setup() throws Exception {
        super.setup();

        feedbackRepository.deleteAllInBatch();

        feedback1 = feedbackRepository.save(new Feedback("Feedback 1 App 1", "userId1", 1, 11, "en"));
        feedback2 = feedbackRepository.save(new Feedback("Feedback 2 App 1", "userId2", 1, 11, "it"));
        feedback3 = feedbackRepository.save(new Feedback("Feedback 3 App 2", "userId3", 2, 22, "de"));
    }

    @After
    public void cleanUp() {
        super.cleanUp();
        feedbackRepository.deleteAllInBatch();
    }

    @Test(expected = ServletException.class)
    public void getFeedbacksUnauthorized() throws Exception {
        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get(basePathEn + "applications/" + 2 + "/feedbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getFeedbacks() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[0].title", is("Feedback 1 App 1")))
                .andExpect(jsonPath("$[1].id", is((int) feedback2.getId())))
                .andExpect(jsonPath("$[1].title", is("Feedback 2 App 1")));

        mockMvc.perform(get(basePathEn + "applications/" + 2 + "/feedbacks")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) feedback3.getId())))
                .andExpect(jsonPath("$[0].title", is("Feedback 3 App 2")));
    }

    @Test
    public void getFeedback() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/" + feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$.title", is("Feedback 1 App 1")));
    }

    @Test
    public void getFeedbacksByUserIdentification() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/user_identification/userId1")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[0].title", is("Feedback 1 App 1")));
    }

    @Test
    public void postFeedback() throws Exception {
        Feedback feedback = new Feedback("New Feedback", "userId1", 1, 11, "en");
        String feedbackJson = toJson(feedback);

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks")
                .contentType(contentType)
                .content(feedbackJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void postFeedbackTree() throws Exception {
        Feedback feedback = new Feedback("New Feedback", "userId1", 1, 11, "en");
        feedback.setTextFeedbacks(new ArrayList<TextFeedback>(){{
            add(new TextFeedback(feedback, "Text Feedback 1", 1));
            add(new TextFeedback(feedback, "info@example.com", 2));
        }});
        feedback.setContextInformation(new ContextInformation(feedback, "1920x1080", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36", null, new Timestamp(new Date().getTime()), "+0200", 2.0f, "CH", "ZH"));
        String feedbackJson = toJson(feedback);

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks")
                .contentType(contentType)
                .content(feedbackJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Feedback")))
                .andExpect(jsonPath("$.userIdentification", is("userId1")))
                .andExpect(jsonPath("$.applicationId", is(1)))
                .andExpect(jsonPath("$.configurationId", is(11)))
                .andExpect(jsonPath("$.language", is("en")))

                .andExpect(jsonPath("$.textFeedbacks", hasSize(2)))
                .andExpect(jsonPath("$.textFeedbacks[0].text", is("Text Feedback 1")))
                .andExpect(jsonPath("$.textFeedbacks[1].text", is("info@example.com")))

                .andExpect(jsonPath("$.contextInformation.resolution", is("1920x1080")))
                .andExpect(jsonPath("$.contextInformation.userAgent", is("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")))
                .andExpect(jsonPath("$.contextInformation.androidVersion", is(nullValue())))
                .andExpect(jsonPath("$.contextInformation.timeZone", is("+0200")))
                .andExpect(jsonPath("$.contextInformation.devicePixelRatio", is(2.0)))
                .andExpect(jsonPath("$.contextInformation.country", is("CH")))
                .andExpect(jsonPath("$.contextInformation.region", is("ZH")));
    }

    @Test
    public void postFeedbackTree2() throws Exception {
        Feedback feedback = new Feedback("New Feedback", "userId1", 1, 11, "en");
        feedback.setTextFeedbacks(new ArrayList<TextFeedback>(){{
            add(new TextFeedback(feedback, "Text Feedback 1", 1));
            add(new TextFeedback(feedback, "info@example.com", 2));
        }});
        feedback.setCategoryFeedbacks(new ArrayList<CategoryFeedback>(){{
            add(new CategoryFeedback(feedback, 1, 99, null));
            add(new CategoryFeedback(feedback, 1, 98));
            add(new CategoryFeedback(feedback, 1, "custom category"));
        }});
        feedback.setRatingFeedbacks(new ArrayList<RatingFeedback>(){{
            add(new RatingFeedback(feedback, "Please rate X", 5, 1));
            add(new RatingFeedback(feedback, "Please rate Y", 0, 2));
        }});
        feedback.setContextInformation(new ContextInformation(feedback, "1920x1080", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36", null, new Timestamp(new Date().getTime()), "+0200", 2.0f, "CH", "ZH"));
        String feedbackJson = toJson(feedback);

        this.mockMvc.perform(post(basePathEn + "applications/" + 1 + "/feedbacks")
                .contentType(contentType)
                .content(feedbackJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Feedback")))
                .andExpect(jsonPath("$.userIdentification", is("userId1")))
                .andExpect(jsonPath("$.applicationId", is(1)))
                .andExpect(jsonPath("$.configurationId", is(11)))
                .andExpect(jsonPath("$.language", is("en")))

                .andExpect(jsonPath("$.textFeedbacks", hasSize(2)))
                .andExpect(jsonPath("$.textFeedbacks[0].text", is("Text Feedback 1")))
                .andExpect(jsonPath("$.textFeedbacks[1].text", is("info@example.com")))

                .andExpect(jsonPath("$.contextInformation.resolution", is("1920x1080")))
                .andExpect(jsonPath("$.contextInformation.userAgent", is("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")))
                .andExpect(jsonPath("$.contextInformation.androidVersion", is(nullValue())))
                .andExpect(jsonPath("$.contextInformation.timeZone", is("+0200")))
                .andExpect(jsonPath("$.contextInformation.devicePixelRatio", is(2.0)))
                .andExpect(jsonPath("$.contextInformation.country", is("CH")))
                .andExpect(jsonPath("$.contextInformation.region", is("ZH")))

                .andExpect(jsonPath("$.categoryFeedbacks", hasSize(3)))
                .andExpect(jsonPath("$.categoryFeedbacks[0].mechanismId", is(1)))
                .andExpect(jsonPath("$.categoryFeedbacks[0].parameterId", is(99)))
                .andExpect(jsonPath("$.categoryFeedbacks[0].text", is(nullValue())))
                .andExpect(jsonPath("$.categoryFeedbacks[1].mechanismId", is(1)))
                .andExpect(jsonPath("$.categoryFeedbacks[1].parameterId", is(98)))
                .andExpect(jsonPath("$.categoryFeedbacks[1].text", is(nullValue())))
                .andExpect(jsonPath("$.categoryFeedbacks[2].mechanismId", is(1)))
                .andExpect(jsonPath("$.categoryFeedbacks[2].parameterId", is(0)))
                .andExpect(jsonPath("$.categoryFeedbacks[2].text", is("custom category")))

                .andExpect(jsonPath("$.ratingFeedbacks", hasSize(2)))
                .andExpect(jsonPath("$.ratingFeedbacks[0].rating", is(5)))
                .andExpect(jsonPath("$.ratingFeedbacks[0].title", is("Please rate X")))
                .andExpect(jsonPath("$.ratingFeedbacks[0].mechanismId", is(1)))
                .andExpect(jsonPath("$.ratingFeedbacks[1].rating", is(0)))
                .andExpect(jsonPath("$.ratingFeedbacks[1].title", is("Please rate Y")))
                .andExpect(jsonPath("$.ratingFeedbacks[1].mechanismId", is(2)));
    }
}