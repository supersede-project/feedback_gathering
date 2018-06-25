package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.ApiUserPermission;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.RatingFeedback;
import ch.fhnw.cere.repository.models.Setting;
import ch.fhnw.cere.repository.repositories.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ReportsIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private AttachmentFeedbackRepository attachmentFeedbackRepository;
    @Autowired
    private AudioFeedbackRepository audioFeedbackRepository;
    @Autowired
    private CategoryFeedbackRepository categoryFeedbackRepository;
    @Autowired
    private ContextInformationRepository contextInformationRepository;
    @Autowired
    private RatingFeedbackRepository ratingFeedbackRepository;
    @Autowired
    private TextAnnotationRepository textAnnotationRepository;
    @Autowired
    private ScreenshotFeedbackRepository screenshotFeedbackRepository;
    @Autowired
    private TextFeedbackRepository textFeedbackRepository;

    @Autowired
    private ApiUserPermissionRepository apiUserPermissionRepository;

    @Autowired
    private SettingRepository settingRepository;

    private String basePathEn = "/feedback_repository/en/";

    private Feedback feedback1;
    private Feedback feedback2;
    private Feedback feedback3;
    private Feedback feedback4;
    private Feedback feedback5;
    private Feedback feedback6;
    private Feedback feedback7;
    private Feedback feedback8;

    @Value("${supersede.test_email_receivers}")
    protected String testEmailReceivers;

    @Before
    public void setup() throws Exception {
        super.setup();

        createRepositoryFilesDirectory();

        attachmentFeedbackRepository.deleteAllInBatch();
        audioFeedbackRepository.deleteAllInBatch();
        categoryFeedbackRepository.deleteAllInBatch();
        contextInformationRepository.deleteAllInBatch();
        ratingFeedbackRepository.deleteAllInBatch();
        textAnnotationRepository.deleteAllInBatch();
        screenshotFeedbackRepository.deleteAllInBatch();
        textFeedbackRepository.deleteAllInBatch();
        feedbackRepository.deleteAllInBatch();
        settingRepository.deleteAllInBatch();
        apiUserPermissionRepository.deleteAllInBatch();

        String ratingTitle = "Please rate this page";
        long mechanismId = 99;
        RatingFeedback ratingFeedback1 = new RatingFeedback(ratingTitle, 2, mechanismId);
        RatingFeedback ratingFeedback2 = new RatingFeedback(ratingTitle, 3, mechanismId);
        RatingFeedback ratingFeedback3 = new RatingFeedback(ratingTitle, 4, mechanismId);
        RatingFeedback ratingFeedback4 = new RatingFeedback(ratingTitle, 5, mechanismId);
        RatingFeedback ratingFeedback5 = new RatingFeedback(ratingTitle, 1, mechanismId);
        RatingFeedback ratingFeedback6 = new RatingFeedback("Another rating question", 28, 100);

        feedback1 = new Feedback("Feedback 1 App 1", "userId1", 1, 11, "en");
        feedback2 = new Feedback("Feedback 2 App 1", "userId2", 1, 11, "it");
        feedback3 = new Feedback("Feedback 3 App 2", "userId3", 2, 22, "de");
        feedback4 = new Feedback("Feedback 4 App 20", "userId1", 20, 39, "en");
        feedback5 = new Feedback("Feedback 5 App 20", "userId2", 20, 39, "it");
        feedback6 = new Feedback("Feedback 6 App 1", "userId1", 1, 11, "en");
        feedback7 = new Feedback("Feedback 7 App 1", "userId2", 1, 11, "it");
        feedback8 = new Feedback("Feedback 8 App 1", "userId1", 1, 11, "en");

        feedback1.addRatingFeedback(ratingFeedback1);
        feedback2.addRatingFeedback(ratingFeedback2);
        feedback6.addRatingFeedback(ratingFeedback3);
        feedback7.addRatingFeedback(ratingFeedback4);
        feedback7.addRatingFeedback(ratingFeedback5);
        feedback8.addRatingFeedback(ratingFeedback6);

        feedback1 = feedbackRepository.save(feedback1);
        feedback2 = feedbackRepository.save(feedback2);
        feedback3 = feedbackRepository.save(feedback3);
        feedback4 = feedbackRepository.save(feedback4);
        feedback5 = feedbackRepository.save(feedback5);
        feedback6 = feedbackRepository.save(feedback6);
        feedback7 = feedbackRepository.save(feedback7);
        feedback8 = feedbackRepository.save(feedback8);

        ratingFeedback1.setFeedback(feedback1);
        ratingFeedbackRepository.save(ratingFeedback1);
        ratingFeedback2.setFeedback(feedback2);
        ratingFeedbackRepository.save(ratingFeedback2);
        ratingFeedback3.setFeedback(feedback6);
        ratingFeedbackRepository.save(ratingFeedback3);
        ratingFeedback4.setFeedback(feedback7);
        ratingFeedbackRepository.save(ratingFeedback4);
        ratingFeedback5.setFeedback(feedback7);
        ratingFeedbackRepository.save(ratingFeedback5);
        ratingFeedback6.setFeedback(feedback8);
        ratingFeedbackRepository.save(ratingFeedback6);

        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 1, true));
        apiUserPermissionRepository.save(new ApiUserPermission(appAdminUser, 20, true));

        settingRepository.save(new Setting(1, testEmailReceivers, null));
    }

    @After
    public void cleanUp() {
        super.cleanUp();

        attachmentFeedbackRepository.deleteAllInBatch();
        audioFeedbackRepository.deleteAllInBatch();
        categoryFeedbackRepository.deleteAllInBatch();
        contextInformationRepository.deleteAllInBatch();
        ratingFeedbackRepository.deleteAllInBatch();
        textAnnotationRepository.deleteAllInBatch();
        screenshotFeedbackRepository.deleteAllInBatch();
        textFeedbackRepository.deleteAllInBatch();
        feedbackRepository.deleteAllInBatch();
        settingRepository.deleteAllInBatch();
        apiUserPermissionRepository.deleteAllInBatch();
    }

    @Test
    public void getReportForApplication() throws Exception {

        MvcResult result = mockMvc.perform(get(basePathEn + "applications/" + 1 + "/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingReports", hasSize(2)))
                .andExpect(jsonPath("$.ratingReports[0].ratingTitle", is("Please rate this page")))
                .andExpect(jsonPath("$.ratingReports[0].ratingMechanismId", is(99)))
                /*
                .andExpect(jsonPath("$.ratingReports[0].averageRating", is(3.0f)))
                .andExpect(jsonPath("$.ratingReports[0].numberOfRatings", is(5)))
                .andExpect(jsonPath("$.ratingReports[0].minRating", is((float) 1.0)))
                .andExpect(jsonPath("$.ratingReports[0].maxRating", is((float) 5.0)))
                */

                .andExpect(jsonPath("$.ratingReports[1].ratingTitle", is("Another rating question")))
                .andExpect(jsonPath("$.ratingReports[1].ratingMechanismId", is( 100)))
                /*
                .andExpect(jsonPath("$.ratingReports[1].averageRating", is((float) 28.0)))
                .andExpect(jsonPath("$.ratingReports[1].numberOfRatings", is(1)))
                .andExpect(jsonPath("$.ratingReports[1].minRating", is((float) 28.0)))
                .andExpect(jsonPath("$.ratingReports[1].maxRating", is((float) 28.0)))
                */
                .andReturn();

        System.err.println("result:");
        System.err.println(result.getResponse().getContentAsString());
    }
}

