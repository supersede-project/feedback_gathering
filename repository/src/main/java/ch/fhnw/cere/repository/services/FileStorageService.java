package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.Feedback;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileStorageService {
    public Feedback storeFiles(Feedback feedback, MultiValueMap<String, MultipartFile> parts) throws IOException;
    public File getFeedbackAttachmentFileByPath(String path, long applicationId);
    public File getFeedbackAudioFileByPath(String path, long applicationId);
    public File getFeedbackScreenshotFileByPath(String path, long applicationId);
}
