package ch.uzh.ifi.feedback.repository.serialization;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.repository.model.Feedback;

public class FeedbackSerializationService extends DefaultSerializer<Feedback>{

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
			List<Part> parts = request.getParts().stream().collect(Collectors.toList());
			feedback.setScreenshots(screenshotSerializationService.ParseRequestParts(parts));
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return feedback;
	}
}
