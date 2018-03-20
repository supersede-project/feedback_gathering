package ch.fhnw.cere.repository.models;

import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.models.orchestrator.Mechanism;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepositoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ApplicationTest {
    private Application application;
    private List<Feedback> feedbacks;

    @Before
    public void setup() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String applicationJson = readFile("src/test/resources/orchestrator_application_25.json",  StandardCharsets.UTF_8);
        application = mapper.readValue(applicationJson, Application.class);

        String feedbacksJson = readFile("src/test/resources/feedback_app_25.json",  StandardCharsets.UTF_8);
        feedbacks = mapper.readValue(feedbacksJson, new TypeReference<List<Feedback>>(){});
    }

    @Test
    public void testCategoryMechanismByConfigurationIdAndCategoryMechanismParameterId() throws Exception {
        assertEquals(25, application.getId().intValue());

        Mechanism categoryMechanism = application.categoryMechanismByConfigurationIdAndCategoryMechanismParameterId(48, 606);
        assertEquals(104, categoryMechanism.getId().intValue());
    }

    @Test
    public void testApplicationMerge() throws Exception {
        Feedback feedback = feedbacks.get(0);
        Feedback.appendMechanismsToFeedback(application, feedback);

        assertEquals(737, feedback.getId());
        assertEquals("81829", feedback.getUserIdentification());
        assertEquals("Please write about your problem", feedback.getTextFeedbacks().get(0).getMechanism().getParameters().stream().filter(parameter -> parameter.getKey().equals("label")).findAny().orElse(null).getValue());
        assertEquals("Please choose one of the following categories", feedback.getCategoryFeedbacks().get(0).getMechanism().getParameters().stream().filter(parameter -> parameter.getKey().equals("title")).findAny().orElse(null).getValue());

        System.err.println(feedback);
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
