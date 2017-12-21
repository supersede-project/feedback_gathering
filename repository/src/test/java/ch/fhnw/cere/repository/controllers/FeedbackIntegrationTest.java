package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.ServletException;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class FeedbackIntegrationTest extends BaseIntegrationTest {

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
    private String basePathDe = "/feedback_repository/de/";

    private Feedback feedback1;
    private Feedback feedback2;
    private Feedback feedback3;
    private Feedback feedback4;
    private Feedback feedback5;

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

        feedback1 = feedbackRepository.save(new Feedback("Feedback 1 App 1", 11111, 1, 11, "en"));
        feedback2 = feedbackRepository.save(new Feedback("Feedback 2 App 1", 22222, 1, 11, "it"));
        feedback3 = feedbackRepository.save(new Feedback("Feedback 3 App 2", 33333, 2, 22, "de"));

        feedback4 = feedbackRepository.save(new Feedback("Feedback 4 App 20", 11111, 20, 39, "en"));
        feedback5 = feedbackRepository.save(new Feedback("Feedback 5 App 20", 22222, 20, 39, "it"));

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

    @Test(expected = ServletException.class)
    public void getFeedbacksUnauthorized() throws Exception {
        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(get(basePathEn + "applications/" + 2 + "/feedbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test(expected = ServletException.class)
    public void getFeedbacksWithoutPermission() throws Exception {
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

    @Test(expected = ServletException.class)
    public void getFeedbacksOfAnotherApplication() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 2 + "/feedbacks")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) feedback3.getId())))
                .andExpect(jsonPath("$[0].title", is("Feedback 3 App 2")));
    }

    @Test
    public void getFeedbacks() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[0].title", is("Feedback 1 App 1")))
                .andExpect(jsonPath("$[1].id", is((int) feedback2.getId())))
                .andExpect(jsonPath("$[1].title", is("Feedback 2 App 1")));
    }

    @Test
    public void getDetailedFeedbacks() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        // TODO: add mock orchestrator. Currently the supersede platform's orchestrator application 20 is called
        MvcResult result = mockMvc.perform(get(basePathEn + "applications/" + 20 + "/feedbacks/full")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is((int) feedback4.getId())))
                .andExpect(jsonPath("$[0].title", is("Feedback 4 App 20")))
                .andExpect(jsonPath("$[1].id", is((int) feedback5.getId())))
                .andExpect(jsonPath("$[1].title", is("Feedback 5 App 20")))
                .andExpect(jsonPath("$[0].application.id", is(20)))
                .andExpect(jsonPath("$[1].application.name", is("Senercon Test Application")))
                .andExpect(jsonPath("$[0].application.id", is(20)))
                .andExpect(jsonPath("$[1].application.name", is("Senercon Test Application"))).andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test(expected = ServletException.class)
    public void getFeedbackWithoutPermission() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/" + feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$.title", is("Feedback 1 App 1")));
    }

    @Test
    public void getFeedback() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/" + feedback1.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$.title", is("Feedback 1 App 1")));
    }

    @Test(expected = ServletException.class)
    public void getFeedbacksByUserIdentificationWithoutPermission() throws Exception {
        String adminJWTToken = requestAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/user_identification/userId1")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[0].title", is("Feedback 1 App 1")));
    }

    @Test
    public void getFeedbacksByUserIdentification() throws Exception {
        String adminJWTToken = requestAppAdminJWTToken();

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/user_identification/userId1")
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is((int) feedback1.getId())))
                .andExpect(jsonPath("$[0].title", is("Feedback 1 App 1")));
    }

    @Test
    public void postFeedback() throws Exception {
        Feedback feedback = new Feedback("New Feedback", 11111, 1, 11, "en");
        String feedbackJson = toJson(feedback);

        MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", feedbackJson.getBytes());

        this.mockMvc.perform(fileUpload(basePathEn + "applications/" + 1 + "/feedbacks")
                .file(jsonFile))
                .andExpect(status().isCreated());
    }

    @Test
    public void postFeedbackTree() throws Exception {
        Feedback feedback = new Feedback("New Feedback", 11111, 1, 11, "en");
        feedback.setTextFeedbacks(new ArrayList<TextFeedback>(){{
            add(new TextFeedback(feedback, "Text Feedback 1", 1));
            add(new TextFeedback(feedback, "info@example.com", 2));
        }});
        feedback.setContextInformation(new ContextInformation(feedback, "1920x1080", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36",
                null, new Timestamp(new Date().getTime()), "+0200", 2.0f, "CH", "ZH", "http://example.com/subpage1", "{\"diagram\": \"diagramX2\"}"));
        String feedbackJson = toJson(feedback);

        MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", feedbackJson.getBytes());

        this.mockMvc.perform(fileUpload(basePathEn + "applications/" + 1 + "/feedbacks")
                .file(jsonFile))
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
                .andExpect(jsonPath("$.contextInformation.url", is("http://example.com/subpage1")))
                .andExpect(jsonPath("$.contextInformation.metaData", is("{\"diagram\": \"diagramX2\"}")));
    }

    @Test
    public void postFeedbackTree2() throws Exception {
        Feedback feedback = new Feedback("New Feedback", 11111, 1, 11, "en");
        feedback.setTextFeedbacks(new ArrayList<TextFeedback>(){{
            add(new TextFeedback(feedback, "Text Feedback 1", 1));
            add(new TextFeedback(feedback, "info@example.com", 2));
        }});
        feedback.setCategoryFeedbacks(new ArrayList<CategoryFeedback>(){{
            add(new CategoryFeedback(feedback, 1, 99L, null));
            add(new CategoryFeedback(feedback, 1, 98L));
            add(new CategoryFeedback(feedback, 1, "custom category"));
        }});
        feedback.setRatingFeedbacks(new ArrayList<RatingFeedback>(){{
            add(new RatingFeedback(feedback, "Please rate X", 5, 1));
            add(new RatingFeedback(feedback, "Please rate Y", 0, 2));
        }});
        feedback.setContextInformation(new ContextInformation(feedback, "1920x1080", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36",
                null, new Timestamp(new Date().getTime()), "+0200", 2.0f, "CH", "ZH", "http://example.com/subpage1", "{\"diagram\": \"diagramX2\"}"));
        String feedbackJson = toJson(feedback);

        MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", feedbackJson.getBytes());

        MvcResult result = this.mockMvc.perform(fileUpload(basePathEn + "applications/" + 1 + "/feedbacks")
                .file(jsonFile))
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
                .andExpect(jsonPath("$.categoryFeedbacks[2].parameterId", is(nullValue())))
                .andExpect(jsonPath("$.categoryFeedbacks[2].text", is("custom category")))

                .andExpect(jsonPath("$.ratingFeedbacks", hasSize(2)))
                .andExpect(jsonPath("$.ratingFeedbacks[0].rating", is(5)))
                .andExpect(jsonPath("$.ratingFeedbacks[0].title", is("Please rate X")))
                .andExpect(jsonPath("$.ratingFeedbacks[0].mechanismId", is(1)))
                .andExpect(jsonPath("$.ratingFeedbacks[1].rating", is(0)))
                .andExpect(jsonPath("$.ratingFeedbacks[1].title", is("Please rate Y")))
                .andExpect(jsonPath("$.ratingFeedbacks[1].mechanismId", is(2))).andReturn();

        String createdFeedbackString = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Feedback createdFeedback = mapper.readValue(createdFeedbackString, Feedback.class);

        String adminJWTToken = requestAppAdminJWTToken();
        mockMvc.perform(get(basePathEn + "applications/" + createdFeedback.getApplicationId() + "/feedbacks/" + createdFeedback.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) createdFeedback.getId())))
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
                .andExpect(jsonPath("$.categoryFeedbacks[2].parameterId", is(nullValue())))
                .andExpect(jsonPath("$.categoryFeedbacks[2].text", is("custom category")))

                .andExpect(jsonPath("$.ratingFeedbacks", hasSize(2)))
                .andExpect(jsonPath("$.ratingFeedbacks[0].rating", is(5)))
                .andExpect(jsonPath("$.ratingFeedbacks[0].title", is("Please rate X")))
                .andExpect(jsonPath("$.ratingFeedbacks[0].mechanismId", is(1)))
                .andExpect(jsonPath("$.ratingFeedbacks[1].rating", is(0)))
                .andExpect(jsonPath("$.ratingFeedbacks[1].title", is("Please rate Y")))
                .andExpect(jsonPath("$.ratingFeedbacks[1].mechanismId", is(2)));
    }


    @Test
    public void postFeedbackTreeWithFile() throws Exception {
        Feedback feedback = new Feedback("New Feedback", 11111, 1, 11, "en");
        feedback.setTextFeedbacks(new ArrayList<TextFeedback>(){{
            add(new TextFeedback(feedback, "Text Feedback 1", 1));
            add(new TextFeedback(feedback, "info@example.com", 2));
        }});
        feedback.setContextInformation(new ContextInformation(feedback, "1920x1080", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36", null,
                new Timestamp(new Date().getTime()), "+0200", 2.0f, "CH", "ZH", "http://example.com/subpage1", "{\"diagram\": \"diagramX2\"}"));
        feedback.setAttachmentFeedbacks(new ArrayList<AttachmentFeedback>(){{
            add(new AttachmentFeedback("attachment1", feedback, 3));
            add(new AttachmentFeedback("attachment2", feedback, 3));
        }});
        feedback.setScreenshotFeedbacks(new ArrayList<ScreenshotFeedback>(){{
            add(new ScreenshotFeedback("screenshot1", feedback, 4, null));
        }});
        String feedbackJson = toJson(feedback);
        MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", feedbackJson.getBytes());

        File resourcesDirectory = new File("src/test/resources");

        FileInputStream fileInputStream1 = new FileInputStream(new File(resourcesDirectory.getAbsolutePath() + "/test_file.pdf"));
        FileInputStream fileInputStream2 = new FileInputStream(new File(resourcesDirectory.getAbsolutePath() + "/test_file"));
        FileInputStream fineInputStream3 = new FileInputStream(new File(resourcesDirectory.getAbsolutePath() + "/screenshot_1_example.png"));
        MockMultipartFile pdfFile = new MockMultipartFile("attachment1", "test_Kopie.pdf", "application/pdf", fileInputStream1);
        MockMultipartFile pdfFile2 = new MockMultipartFile("attachment2", "test_Kopie", "application/pdf", fileInputStream2);
        MockMultipartFile screenshotFile = new MockMultipartFile("screenshot1", "screenshot_1_example.png", "image/png", fineInputStream3);

        MvcResult result = this.mockMvc.perform(fileUpload(basePathEn + "applications/" + 1 + "/feedbacks")
                .file(jsonFile)
                .file(pdfFile)
                .file(pdfFile2)
                .file(screenshotFile))
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

                .andExpect(jsonPath("$.attachmentFeedbacks", hasSize(2)))
                .andExpect(jsonPath("$.attachmentFeedbacks[0].part", is("attachment1")))
                .andExpect(jsonPath("$.attachmentFeedbacks[0].mechanismId", is(3)))
                //.andExpect(jsonPath("$.attachmentFeedbacks[0].path", is("1_userId1_test_Kopie.pdf")))

                .andExpect(jsonPath("$.screenshotFeedbacks", hasSize(1)))
                .andExpect(jsonPath("$.screenshotFeedbacks[0].part", is("screenshot1")))
                .andExpect(jsonPath("$.screenshotFeedbacks[0].mechanismId", is(4)))
                //.andExpect(jsonPath("$.screenshotFeedbacks[0].path", is("1_userId1_screenshot_1_example.png")))
                .andReturn();

        String createdFeedbackString = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Feedback createdFeedback = mapper.readValue(createdFeedbackString, Feedback.class);

        String adminJWTToken = requestAppAdminJWTToken();
        mockMvc.perform(get(basePathEn + "applications/" + createdFeedback.getApplicationId() + "/feedbacks/" + createdFeedback.getId())
                .header("Authorization", adminJWTToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) createdFeedback.getId())))

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

                .andExpect(jsonPath("$.screenshotFeedbacks", hasSize(1)))
                .andExpect(jsonPath("$.screenshotFeedbacks[0].mechanismId", is(4)))
                //.andExpect(jsonPath("$.screenshotFeedbacks[0].path", is("1_userId1_screenshot_1_example.png")))

                .andExpect(jsonPath("$.attachmentFeedbacks", hasSize(2)))
                .andExpect(jsonPath("$.attachmentFeedbacks[0].mechanismId", is(3)))
                //.andExpect(jsonPath("$.attachmentFeedbacks[0].path", is("1_userId1_test_Kopie.pdf")))
                .andExpect(jsonPath("$.attachmentFeedbacks[1].mechanismId", is(3)));
                //.andExpect(jsonPath("$.attachmentFeedbacks[1].path", is("1_userId1_test_Kopie")));
    }
}



















