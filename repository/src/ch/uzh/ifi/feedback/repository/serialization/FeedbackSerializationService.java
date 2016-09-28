package ch.uzh.ifi.feedback.repository.serialization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.repository.model.AttachmentFeedback;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;

public class FeedbackSerializationService extends RepositorySerializationService<Feedback> {

	private FileStorageService storageService;

	@Inject
	public FeedbackSerializationService(
			FileStorageService storageService) 
	{
		this.storageService = storageService;
	}

	@Override
	public Feedback Deserialize(HttpServletRequest request) {
		Feedback feedback = super.Deserialize(request);		

		try {
			String storagePath = storageService.CreateDirectory("screenshots");
			for(ScreenshotFeedback screenshot : feedback.getScreenshotFeedbacks())
			{
				request.getParts();
				Part filePart = request.getPart(screenshot.getPart());
				screenshot = storageService.ParseFilePart(filePart, screenshot, storagePath);	
			}
			
			storagePath = storageService.CreateDirectory("audios");
			for(AudioFeedback audio : feedback.getAudioFeedbacks())
			{
				Part filePart = request.getPart(audio.getPart());
				audio = storageService.ParseFilePart(filePart, audio, storagePath);	
			}
			
			storagePath = storageService.CreateDirectory("attachments");
			for(AttachmentFeedback attachment : feedback.getAttachmentFeedbacks())
			{
				Part filePart = request.getPart(attachment.getPart());
				attachment = storageService.ParseFilePart(filePart, attachment, storagePath);	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return feedback;
	}
}