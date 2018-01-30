package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.RepositoryApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Aydinli on 30.01.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepositoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class UploadIntegrationTest {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Value("${supersede.profile_pic_upload_directory}")
    protected String UPLOADED_FOLDER;

    @Test
    public void testUpload() throws Exception{

    }

}
