package ch.fhnw.cere.repository.controllers;


import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.models.ApiUser;
import ch.fhnw.cere.repository.models.ApiUserApiUserRole;
import ch.fhnw.cere.repository.models.ApiUserRole;
import ch.fhnw.cere.repository.repositories.ApiUserApiUserRoleRepository;
import ch.fhnw.cere.repository.repositories.ApiUserRepository;
import ch.fhnw.cere.repository.security.TokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RepositoryApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    public static final String PASSWORD_HASH_ADMIN = "$2a$10$HM01Z2Vfhw5s4LG2Svze1.IpC7Vn0vkU1or.4h9WaSbTRwwOCOEAy";
    public static final String PASSWORD_HASH_SUPER_ADMIN = "$2a$10$y9K.1fd6VgT26rftcoziV.Qm74r8Qe1Y0hv.Kw4L1e3IMsxEXdWJu";
    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Autowired
    private ApiUserApiUserRoleRepository apiUserApiUserRoleRepository;

    @Autowired
    private TokenUtils tokenUtils;

    protected ApiUser adminUser;
    protected ApiUser appAdminUser;
    protected ApiUser superAdminUser;

    @Value("${supersede.upload_directory}")
    protected String uploadDirectory;

    @Value("${supersede.upload_directory.attachments_folder_name}")
    protected String attachmentsFolderName;

    @Value("${supersede.upload_directory.audios_folder_name}")
    protected String audiosFolderName;

    @Value("${supersede.upload_directory.screenshots_folder_name}")
    protected String screenshotsFolderName;

    @Before
    public void setup() throws Exception {
        SecurityContextHolder.clearContext();

        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        apiUserApiUserRoleRepository.deleteAllInBatch();
        apiUserRepository.deleteAllInBatch();

        this.adminUser = insertAdminApiUser();
        this.appAdminUser = insertAppAdminApiUser();
        this.superAdminUser = insertSuperAdminApiUser();
    }

    @After
    public void cleanUp() {
        apiUserApiUserRoleRepository.deleteAllInBatch();
        apiUserRepository.deleteAllInBatch();

        SecurityContextHolder.clearContext();

        deleteUploadDirectorySubFolders();
    }

    protected String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString= null;
        try {
            jsonString = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    /**
     *
     * @return an ApiUser with username 'admin', password 'password' and the ADMIN role
     */
    protected ApiUser insertAdminApiUser() {
        ApiUser adminUser = apiUserRepository.save(new ApiUser("admin", PASSWORD_HASH_ADMIN));
        apiUserApiUserRoleRepository.save(new ApiUserApiUserRole(adminUser, ApiUserRole.ADMIN));
        return adminUser;
    }

    /**
     *
     * @return an ApiUser with username 'app_admin', password 'password' and the ADMIN role
     */
    protected ApiUser insertAppAdminApiUser() {
        ApiUser adminUser = apiUserRepository.save(new ApiUser("app_admin", PASSWORD_HASH_ADMIN));
        apiUserApiUserRoleRepository.save(new ApiUserApiUserRole(adminUser, ApiUserRole.ADMIN));
        return adminUser;
    }

    /**
     *
     * @return an ApiUser with username 'super_admin', password 'superpassword' and the SUPER_ADMIN role
     */
    protected ApiUser insertSuperAdminApiUser() {
        ApiUser superAdmin = apiUserRepository.save(new ApiUser("super_admin", PASSWORD_HASH_SUPER_ADMIN));
        apiUserApiUserRoleRepository.save(new ApiUserApiUserRole(superAdmin, ApiUserRole.SUPER_ADMIN));
        return superAdmin;
    }

    protected String requestAdminJWTToken() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        MvcResult result = null;
        try {
            result = mockMvc.perform(post("/feedback_repository/authenticate").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"admin\",\"password\":\"password\"}"))
                    .andReturn();
            String json = result.getResponse().getContentAsString();
            JSONObject obj = new JSONObject(json);
            return obj.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    protected String requestAppAdminJWTToken() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        MvcResult result = null;
        try {
            result = mockMvc.perform(post("/feedback_repository/authenticate").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"app_admin\",\"password\":\"password\"}"))
                    .andReturn();
            String json = result.getResponse().getContentAsString();
            JSONObject obj = new JSONObject(json);
            return obj.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    protected String requestSuperAdminJWTToken() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        MvcResult result = null;
        try {
            result = mockMvc.perform(post("/feedback_repository/authenticate").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"super_admin\",\"password\":\"superpassword\"}"))
                    .andReturn();
            String json = result.getResponse().getContentAsString();
            JSONObject obj = new JSONObject(json);
            return obj.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    protected void deleteUploadDirectorySubFolders() {
        deleteFolder(new File(uploadDirectory + "/" + attachmentsFolderName));
        deleteFolder(new File(uploadDirectory + "/" + screenshotsFolderName));
        deleteFolder(new File(uploadDirectory + "/" + audiosFolderName));
    }

    protected void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    protected void createRepositoryFilesDirectory() throws IOException {
        File repositoryFiles = new File(uploadDirectory);
        if (!repositoryFiles.exists()) {
            repositoryFiles.mkdir();
        }

        File srcAttachment = new File("src/test/resources" + File.separator + "test_file.pdf");
        File destAttachment = new File(uploadDirectory + File.separator + attachmentsFolderName + File.separator + "test_file.pdf");
        FileUtils.copyFile(srcAttachment, destAttachment);

        File srcScreenshot = new File("src/test/resources" + File.separator + "screenshot_1_example.png");
        File destScreenshot = new File(uploadDirectory + File.separator + screenshotsFolderName + File.separator + "screenshot_1_example.png");
        FileUtils.copyFile(srcScreenshot, destScreenshot);
    }
}