package ch.uzh.ifi.feedback.repository.serialization;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

import ch.uzh.ifi.feedback.repository.model.AudioFeedback;

public class AudioFeedbackParser {
	private FileStorageService storageService;
	
	public AudioFeedbackParser(FileStorageService storageService)
	{
		this.storageService = storageService;
	}
	
	public List<AudioFeedback> ParseRequestParts(List<Part> fileParts) {

		String storagePath = storageService.CreateDirectory("audio");
		List<AudioFeedback> screenshots = new ArrayList<>(fileParts.size());
		for (Part filePart : fileParts) {
			AudioFeedback audio = new AudioFeedback();
			audio = storageService.ParseFilePart(filePart, audio, storagePath);	
		}

		return screenshots;
	}
}
