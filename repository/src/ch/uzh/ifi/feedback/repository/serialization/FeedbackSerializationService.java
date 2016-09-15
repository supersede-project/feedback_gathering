package ch.uzh.ifi.feedback.repository.serialization;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.repository.model.Feedback;

public class FeedbackSerializationService extends RepositorySerializationService<Feedback> {

	private ScreenshotFeedbackParser screenshotParser;
	private AudioFeedbackParser audioParser;
	private AttachmentFeedbackParser attachmentParser;

	@Inject
	public FeedbackSerializationService(
			ScreenshotFeedbackParser screenshotParser, 
			AudioFeedbackParser audioParser, 
			AttachmentFeedbackParser attachmentParser) 
	{
		this.screenshotParser = screenshotParser;
		this.attachmentParser = attachmentParser;
		this.audioParser = audioParser;
	}

	@Override
	public Feedback Deserialize(HttpServletRequest request) {
		Feedback feedback = super.Deserialize(request);		

		try {		
			List<Part> screenshotParts = request.getParts().stream().filter(part -> part.getName().toLowerCase().contains("screenshot"))
					.collect(Collectors.toList());						
			feedback.setScreenshots(screenshotParser.ParseRequestParts(screenshotParts));
			
			List<Part> audioParts = request.getParts().stream().filter(part -> part.getName().toLowerCase().contains("audio"))
					.collect(Collectors.toList());						
			feedback.setAudioFeedbacks(audioParser.ParseRequestParts(audioParts));
			
			List<Part> attachmentParts = request.getParts().stream().filter(part -> part.getName().toLowerCase().contains("attachment"))
					.collect(Collectors.toList());						
			feedback.setAttachmentFeedbacks(attachmentParser.ParseRequestParts(attachmentParts));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return feedback;
	}
}