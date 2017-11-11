package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.models.CategoryFeedback;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepositoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class OrchestratorApplicationServiceTest {

    @Autowired
    private OrchestratorApplicationService orchestratorApplicationService;

    private Application exampleApplication;

    @Before
    public void setup() throws Exception {
        initExampleApplication();
    }

    @Test
    public void applicationObject() {
        assertTrue(20L == exampleApplication.getId());
        assertEquals(10, exampleApplication.getConfigurations().get(0).getMechanisms().size());
    }

    private void initExampleApplication() throws IOException {
        String exampleConfiguration = readFile("src/test/resources/orchestrator_application.json",  StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        exampleApplication = mapper.readValue(exampleConfiguration, Application.class);
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
