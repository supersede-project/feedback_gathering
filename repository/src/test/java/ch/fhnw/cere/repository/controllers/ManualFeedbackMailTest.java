package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.repositories.SettingRepository;
import ch.fhnw.cere.repository.services.FeedbackEmailService;
import ch.fhnw.cere.repository.services.OrchestratorApplicationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepositoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ManualFeedbackMailTest {

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
    }

    @After
    public void cleanUp() throws IOException {
        //removeRepositoryFilesDirectory();
    }

    @Ignore
    @Test
    public void sendMail() throws IOException, TemplateException {
        ObjectMapper mapper = new ObjectMapper();
        String feedbacksJson = readFile("src/test/resources/feedback_app_25.json",  StandardCharsets.UTF_8);
        List<Feedback> feedbacks = mapper.readValue(feedbacksJson, new TypeReference<List<Feedback>>(){});

        for(Feedback feedback : feedbacks) {
            feedbackEmailService.sendMail(feedback, "ronnieschaniel@gmail.com,energiesparkonto@co2online.de");
        }
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
