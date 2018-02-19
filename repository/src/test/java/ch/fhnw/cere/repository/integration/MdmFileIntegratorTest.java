package ch.fhnw.cere.repository.integration;

import ch.fhnw.cere.repository.RepositoryApplication;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.services.OrchestratorApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class MdmFileIntegratorTest {

    @Value("${supersede.upload_directory}")
    protected String uploadDirectory;

    @Value("${supersede.upload_directory.attachments_folder_name}")
    protected String attachmentsFolderName;

    @Value("${supersede.upload_directory.audios_folder_name}")
    protected String audiosFolderName;

    @Value("${supersede.upload_directory.screenshots_folder_name}")
    protected String screenshotsFolderName;

    @Autowired
    private MdmFileIntegrator mdmFileIntegrator;


    @Before
    public void setup() throws Exception {
        createRepositoryFilesDirectory();
    }

    @After
    public void tearDown() throws Exception {
        deleteUploadDirectorySubFolders();
    }

    @Test
    public void testFileForwardingToMdm() throws Exception {
        File resourcesDirectory = new File("src/test/resources");
        File attachment = new File(resourcesDirectory.getAbsolutePath() + "/test_file.pdf");

        mdmFileIntegrator.sendFile(attachment);
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

        File srcAttachment2 = new File("src/test/resources" + File.separator + "test_file");
        File destAttachment2 = new File(uploadDirectory + File.separator + attachmentsFolderName + File.separator + "test_file");
        FileUtils.copyFile(srcAttachment2, destAttachment2);

        File srcScreenshot = new File("src/test/resources" + File.separator + "screenshot_1_example.png");
        File destScreenshot = new File(uploadDirectory + File.separator + screenshotsFolderName + File.separator + "screenshot_1_example.png");
        FileUtils.copyFile(srcScreenshot, destScreenshot);
    }
}
