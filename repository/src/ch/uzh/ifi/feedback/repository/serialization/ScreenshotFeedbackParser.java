package ch.uzh.ifi.feedback.repository.serialization;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;

public class ScreenshotFeedbackParser {

	private FileStorageService storageService;
	
	@Inject
	public ScreenshotFeedbackParser(FileStorageService storageService)
	{
		this.storageService = storageService;
	}
	
	public List<ScreenshotFeedback> ParseRequestParts(List<Part> fileParts) {

		String storagePath = storageService.CreateDirectory("screenshots");
		List<ScreenshotFeedback> screenshots = new ArrayList<>(fileParts.size());
		for (Part filePart : fileParts) {
			ScreenshotFeedback screenshot = new ScreenshotFeedback();
			screenshot = storageService.ParseFilePart(filePart, screenshot, storagePath);	
			screenshots.add(screenshot);
		}

		return screenshots;
	}
}
