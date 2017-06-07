package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.models.AttachmentFeedback;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.ScreenshotFeedback;
import ch.fhnw.cere.repository.models.Setting;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.repositories.SettingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
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
        Feedback feedback = new Feedback("Test feedback", "userId3", applicationId, 22, "en");
        ScreenshotFeedback screenshotFeedback = new ScreenshotFeedback("screenshot_1_example.png", 20000, "screenshot1", "png", feedback, 3, null);
        AttachmentFeedback attachmentFeedback = new AttachmentFeedback("test_file.pdf", 10000, "attachment1", "pdf", feedback, 4);
        feedback.setScreenshotFeedbacks(new ArrayList<ScreenshotFeedback>(){{add(screenshotFeedback);}});
        feedback.setAttachmentFeedbacks(new ArrayList<AttachmentFeedback>(){{add(attachmentFeedback);}});

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
    }

    private void removeRepositoryFilesDirectory() throws IOException {
        FileUtils.deleteDirectory(new File(repositoryFilesDirectory));
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
