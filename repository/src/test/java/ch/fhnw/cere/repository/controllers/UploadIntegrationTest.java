package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.RepositoryApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Aydinli on 30.01.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepositoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class UploadIntegrationTest extends BaseIntegrationTest{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Value("${supersede.profile_pic_upload_directory}")
    protected String UPLOADED_FOLDER;

    private String basePathEn = "/feedback_repository/en/";

    @Before
    public void setup() throws Exception{
        super.setup();
        try {
            Files.deleteIfExists(Paths.get("src/test/resources/upload_directory/profile_pic_api_user_1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void cleanup(){
        super.cleanUp();
        try {
            Files.deleteIfExists(Paths.get("src/test/resources/upload_directory/profile_pic_api_user_1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpload() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();
//        File file = new File("F:\\supersede_profilepics\\profile_pic_api_user_1.png");


        Path file = Paths.get(String.valueOf(this.getClass().getClassLoader().
                getResource("profile_pic_api_user_1.png").getFile())
                .replaceFirst("^/(.:/)", "$1"));

        FileInputStream fi1 = new FileInputStream(String.valueOf(file));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", String.valueOf(file.getFileName()), "multipart/form-data",fi1);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.fileUpload(basePathEn + "applications/" + 1 + "/feedbacks/"
                +"/uploadImage/user/"+1)
                .file(mockMultipartFile)
                .header("Authorization", adminJWTToken))
                .andExpect(content().string("Image successfully uploaded"));
    }

//    @Test
//    public void testUploadWithTooSmallImage() throws Exception{
//        String adminJWTToken = requestAppAdminJWTToken();
//        File file = new File("F:\\supersede_profilepics\\rsz_foto00021.png");
//        FileInputStream fi1 = new FileInputStream(file);
//        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", file.getName(), "multipart/form-data",fi1);
//
//        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        mockMvc.perform(MockMvcRequestBuilders.fileUpload(basePathEn + "applications/" + 1 + "/feedbacks/"
//                +"/uploadImage/user/"+1)
//                .file(mockMultipartFile)
//                .header("Authorization", adminJWTToken))
//                .andExpect(content().string("The uploaded image size is too small. " +
//                        "Please select an image with at least 40 x 40 Pixels"));
//    }

    @Test
    public void testGetProfilePictureForApiUser() throws Exception{
        String adminJWTToken = requestAppAdminJWTToken();
        long api_user_id = 99999999;

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/getImage/user/"
            +api_user_id)
                .header("Authorization", adminJWTToken))
                .andExpect(content().contentType(MediaType.IMAGE_PNG));

        mockMvc.perform(get(basePathEn + "applications/" + 1 + "/feedbacks/getImage/user/"
                +10)
                .header("Authorization", adminJWTToken))
                .andExpect(content().contentType(MediaType.IMAGE_PNG));

    }

}
