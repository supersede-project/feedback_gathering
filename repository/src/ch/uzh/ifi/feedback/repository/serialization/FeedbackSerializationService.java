package ch.uzh.ifi.feedback.repository.serialization;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.repository.model.Feedback;

public class FeedbackSerializationService extends RepositorySerializationService<Feedback>{

	private ScreenshotSerializationService screenshotSerializationService;
	
	@Inject
	public FeedbackSerializationService(ScreenshotSerializationService screenshotSerializationService) 
	{
		this.screenshotSerializationService = screenshotSerializationService;
	}

	@Override
	public Feedback Deserialize(HttpServletRequest request) {
		Feedback feedback = super.Deserialize(request);
		
		try{
			List<Part> screenshotParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList());
			feedback.setScreenshots(screenshotSerializationService.ParseRequestParts(screenshotParts));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return feedback;
	}
}
