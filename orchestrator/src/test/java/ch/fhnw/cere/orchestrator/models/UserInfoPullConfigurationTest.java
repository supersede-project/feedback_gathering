package ch.fhnw.cere.orchestrator.models;


import ch.fhnw.cere.orchestrator.OrchestratorApplication;
import ch.fhnw.cere.orchestrator.repositories.ApplicationRepository;
import ch.fhnw.cere.orchestrator.repositories.ConfigurationRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrchestratorApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class UserInfoPullConfigurationTest {

    private Application application;
    private UserInfoPullConfiguration userInfoPullConfiguration;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Before
    public void setup() {
        this.applicationRepository.deleteAllInBatch();
        application = this.applicationRepository.save(new Application("Info Pull Test App", 1, new Date(), new Date(), null));

        userInfoPullConfiguration = new UserInfoPullConfiguration("Your feedback regarding the print functionality was implemented today and will be shipped next Monday.");
    }

    @After
    public void cleanUp() {
        this.applicationRepository.deleteAllInBatch();
    }

    @Test
    public void testConfigurationBuilding() {
        String userIdentification = "123456";

        Configuration configuration = userInfoPullConfiguration.buildConfiguration(userIdentification, application);

        assertEquals("Info Pull Configuration for 123456", configuration.getName());
        assertEquals("Your feedback regarding the print functionality was implemented today and will be shipped next Monday.", configuration.getMechanisms().get(0).getParameters().get(0).getValue());
        assertEquals(1, configuration.getUserGroups().size());
        assertEquals(1, configuration.getUserGroups().get(0).getUsers().size());
        assertEquals("123456", configuration.getUserGroups().get(0).getUsers().get(0).getUserIdentification());
    }
}
