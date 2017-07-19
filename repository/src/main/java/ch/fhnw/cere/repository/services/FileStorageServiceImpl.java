package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.*;
import com.sun.media.jfxmedia.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${supersede.upload_directory}")
    private String uploadDirectory;

    @Value("${supersede.upload_directory.attachments_folder_name}")
    private String attachmentsFolderName;

    @Value("${supersede.upload_directory.audios_folder_name}")
    private String audiosFolderName;

    @Value("${supersede.upload_directory.screenshots_folder_name}")
    private String screenshotsFolderName;


    @Override
    public Feedback storeFiles(Feedback feedback, MultiValueMap<String, MultipartFile> parts) throws IOException {
        createDirectories();

        feedback.setAttachmentFeedbacks((ArrayList<AttachmentFeedback>)storeFileFeedbackFiles(feedback, feedback.getAttachmentFeedbacks(), parts, attachmentsFolderName));
        feedback.setAudioFeedbacks((ArrayList<AudioFeedback>)storeFileFeedbackFiles(feedback, feedback.getAudioFeedbacks(), parts, audiosFolderName));
        feedback.setScreenshotFeedbacks((ArrayList<ScreenshotFeedback>)storeFileFeedbackFiles(feedback, feedback.getScreenshotFeedbacks(), parts, screenshotsFolderName));

        return feedback;
    }

    @Override
    public File getFeedbackAttachmentFileByPath(String path, long applicationId) {
        path = sanitizeFileName(path);
        return this.getFeedbackFileByPath(attachmentsFolderName + "/" + path, applicationId);
    }

    @Override
    public File getFeedbackAudioFileByPath(String path, long applicationId) {
        path = sanitizeFileName(path);
        return this.getFeedbackFileByPath(audiosFolderName + "/" + path, applicationId);
    }

    @Override
    public File getFeedbackScreenshotFileByPath(String path, long applicationId) {
        path = sanitizeFileName(path);
        return this.getFeedbackFileByPath(screenshotsFolderName + "/" + path, applicationId);
    }

    private File getFeedbackFileByPath(String path, long applicationId) {
        // TODO check permission
        return new File(uploadDirectory + "/" + path);
    }

    private List<?> storeFileFeedbackFiles(Feedback feedback, List<? extends FileFeedback> fileFeedbacks, MultiValueMap<String, MultipartFile> parts, String folderName) throws IOException {
        if(fileFeedbacks == null) {
            return null;
        }

        List<FileFeedback> fileFeedbacksWithPath = new ArrayList<>();
        for(FileFeedback fileFeedback : fileFeedbacks) {
            if(parts.containsKey(fileFeedback.getPart())) {
                MultipartFile file = parts.getFirst(fileFeedback.getPart());

                String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
                fileFeedback.setFileExtension(fileExtension);

                String generatedFilename = buildFilename(file.getOriginalFilename(), feedback);
                String repositoryFilesDirectory = uploadDirectory + "/" + folderName + "/" + generatedFilename;
                file.transferTo(new File(repositoryFilesDirectory));
                fileFeedback.setPath(generatedFilename);
                fileFeedback.setSize((int) file.getSize());
                fileFeedbacksWithPath.add(fileFeedback);
            } else {
                fileFeedbacksWithPath.add(fileFeedback);
            }
        }
        return fileFeedbacksWithPath;
    }

    private void createDirectories() {
        createDirectoryIfNotExisting(new File(uploadDirectory + "/" + attachmentsFolderName));
        createDirectoryIfNotExisting(new File(uploadDirectory + "/" + audiosFolderName));
        createDirectoryIfNotExisting(new File(uploadDirectory + "/" + screenshotsFolderName));
    }

    private void createDirectoryIfNotExisting(File directory) {
        if (!directory.exists()) {

            try {
                directory.mkdir();
            } catch (Exception exception) {
                Logger.logMsg(Logger.ERROR, "Feedback Repository could not create directory: " + directory.getAbsolutePath());
                Logger.logMsg(Logger.ERROR, "Feedback Repository exception: " + exception);
            }
        }
    }

    private String buildFilename(String filename, Feedback feedback) {
        return feedback.getApplicationId() + "_" + sanitizeFileName(feedback.getUserIdentification()) + "_" + String.valueOf(System.currentTimeMillis()) + "_" + sanitizeFileName(filename);
    }

    private String sanitizeFileName(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}
