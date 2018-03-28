package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FileFeedback;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileStorageService {
    public Feedback storeFiles(Feedback feedback, MultiValueMap<String, MultipartFile> parts) throws IOException;
    public File getFeedbackAttachmentFileByPath(String path, long applicationId);
    public File getFeedbackAudioFileByPath(String path, long applicationId);
    public File getFeedbackScreenshotFileByPath(String path, long applicationId);
    public List<File> getFeedbackFiles(Feedback feedback, List<? extends FileFeedback> fileFeedbacks, MultiValueMap<String, MultipartFile> parts) throws IOException;
    public List<File> getAllStoredFilesOfFeedback(Feedback feedback);
}
