package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.models.ApiUserPermission;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.Setting;
import ch.fhnw.cere.repository.repositories.SettingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


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

    @Before
    public void setup() throws Exception {
        settingRepository.deleteAllInBatch();
        settingRepository.save(new Setting(1, testEmailReceivers, null));
    }

    @Test
    public void sendFeedbackNotification() {
        Feedback feedback = new Feedback("Test feedback", "userId3", 2, 22, "de");
        feedbackEmailService.sendFeedbackNotification(feedback);
    }

    @Test
    public void sendMail() {
        Feedback feedback = new Feedback("Test feedback", "userId3", 2, 22, "de");
        String recipients = testEmailReceivers;
        feedbackEmailService.sendMail(feedback, recipients);
    }
}
