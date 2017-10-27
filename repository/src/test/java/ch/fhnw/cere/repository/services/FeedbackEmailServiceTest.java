package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.repositories.SettingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepositoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class FeedbackEmailServiceTest {

    @Autowired
    private FeedbackEmailService feedbackEmailService;

    @Autowired
    private SettingRepository settingRepository;

    @Value("${supersede.test_email_receivers}")
    protected String testEmailReceivers;

    @Value("${supersede.upload_directory}")
    protected String repositoryFilesDirectory;

    @Value("${supersede.upload_directory.screenshots_folder_name}")
    protected String screenshotsDirectory;

    @Value("${supersede.upload_directory.attachments_folder_name}")
    protected String attachmentsDirectory;

    @Value("${supersede.upload_directory.audios_folder_name}")
    protected String audiosDirectory;

    private long applicationId = 20;

    @Before
    public void setup() throws Exception {
        settingRepository.deleteAllInBatch();
        settingRepository.save(new Setting(applicationId, testEmailReceivers, null));

        setOrchestratorMockService();
        createRepositoryFilesDirectory();
    }

    @After
    public void cleanUp() throws IOException {
        removeRepositoryFilesDirectory();
    }

    @Test
    public void sendMail() throws IOException, TemplateException {
        Feedback feedback = new Feedback("Test feedback", "userId3", applicationId, 39, "en");
        ScreenshotFeedback screenshotFeedback = new ScreenshotFeedback("screenshot_1_example.png", 20000, "screenshot1", "png", feedback, 61, null);
        AttachmentFeedback attachmentFeedback = new AttachmentFeedback("test_file.pdf", 10000, "attachment1", "pdf", feedback, 65);
        TextFeedback textFeedback = new TextFeedback(feedback, "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.", 59);
        RatingFeedback ratingFeedback = new RatingFeedback(feedback, "Rate your user experience on this page", 4, 62);
        AudioFeedback audioFeedback = new AudioFeedback("audio_example.wav", 44, "audio1", "wav", feedback, 60,0);
        CategoryFeedback categoryFeedback1 = new CategoryFeedback(feedback, 64L, 511L);
        CategoryFeedback categoryFeedback2 = new CategoryFeedback(feedback, 64L, "Praise");
        ContextInformation contextInformation = new ContextInformation(feedback, "2500x1200", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36", null, null, "+0200", 2.0f, null, null, "http://example.com/subpage/path");

        feedback.setScreenshotFeedbacks(new ArrayList<ScreenshotFeedback>(){{add(screenshotFeedback);}});
        feedback.setAttachmentFeedbacks(new ArrayList<AttachmentFeedback>(){{add(attachmentFeedback);}});
        feedback.setTextFeedbacks(new ArrayList<TextFeedback>(){{add(textFeedback);}});
        feedback.setRatingFeedbacks(new ArrayList<RatingFeedback>(){{add(ratingFeedback);}});
        feedback.setAudioFeedbacks(new ArrayList<AudioFeedback>(){{add(audioFeedback);}});
        feedback.setCategoryFeedbacks(new ArrayList<CategoryFeedback>(){{
            add(categoryFeedback1);
            add(categoryFeedback2);
        }});
        feedback.setContextInformation(contextInformation);

        Setting setting = settingRepository.findByApplicationId(feedback.getApplicationId());
        String recipients = setting.getFeedbackEmailReceivers();
        feedbackEmailService.sendMail(feedback, recipients);
    }

    private void setOrchestratorMockService() throws IOException {
        OrchestratorApplicationService orchestratorApplicationServiceMock = Mockito.mock(OrchestratorApplicationService.class);

        String exampleConfiguration = readFile("src/test/resources/orchestrator_application.json",  StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        Application application = mapper.readValue(exampleConfiguration, Application.class);
        Mockito.when(orchestratorApplicationServiceMock.loadApplication("en", applicationId)).thenReturn(application);
        feedbackEmailService.setOrchestratorService(orchestratorApplicationServiceMock);
    }

    private void createRepositoryFilesDirectory() throws IOException {
        File repositoryFiles = new File(repositoryFilesDirectory);
        if (!repositoryFiles.exists()) {
            repositoryFiles.mkdir();
        }

        File srcAttachment = new File("src/test/resources" + File.separator + "test_file.pdf");
        File destAttachment = new File(repositoryFilesDirectory + File.separator + attachmentsDirectory + File.separator + "test_file.pdf");
        FileUtils.copyFile(srcAttachment, destAttachment);


        File srcScreenshot = new File("src/test/resources" + File.separator + "screenshot_1_example.png");
        File destScreenshot = new File(repositoryFilesDirectory + File.separator + screenshotsDirectory + File.separator + "screenshot_1_example.png");
        FileUtils.copyFile(srcScreenshot, destScreenshot);

        File srcAudio = new File("src/test/resources" + File.separator + "audio_example.wav");
        File destAudio = new File(repositoryFilesDirectory + File.separator + audiosDirectory + File.separator + "audio_example.wav");
        FileUtils.copyFile(srcAudio, destAudio);
    }

    private void removeRepositoryFilesDirectory() throws IOException {
        FileUtils.deleteDirectory(new File(repositoryFilesDirectory));
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
