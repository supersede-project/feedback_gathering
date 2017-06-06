package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.Setting;
import ch.fhnw.cere.repository.repositories.SettingRepository;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


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

    private long applicationId = 1;

    @Before
    public void setup() throws Exception {
        settingRepository.deleteAllInBatch();
        settingRepository.save(new Setting(applicationId, testEmailReceivers, null));

        createRepositoryFilesDirectory();
    }

    @After
    public void cleanUp() throws IOException {
        removeRepositoryFilesDirectory();
    }

    @Test
    public void sendMail() throws IOException, TemplateException {
        Feedback feedback = new Feedback("Test feedback", "userId3", applicationId, 22, "de");
        Setting setting = settingRepository.findByApplicationId(feedback.getApplicationId());
        String recipients = setting.getFeedbackEmailReceivers();
        feedbackEmailService.sendMail(feedback, recipients);
    }

    private void createRepositoryFilesDirectory() throws IOException {
        File repositoryFiles = new File(repositoryFilesDirectory);
        if (!repositoryFiles.exists()) {
            if (repositoryFiles.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
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
}
