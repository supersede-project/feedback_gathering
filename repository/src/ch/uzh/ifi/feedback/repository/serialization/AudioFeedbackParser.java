package ch.uzh.ifi.feedback.repository.serialization;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.repository.model.AudioFeedback;

public class AudioFeedbackParser {
	private FileStorageService storageService;
	
	@Inject
	public AudioFeedbackParser(FileStorageService storageService)
	{
		this.storageService = storageService;
	}
	
	public List<AudioFeedback> ParseRequestParts(List<Part> fileParts) {

		String storagePath = storageService.CreateDirectory("audio");
		List<AudioFeedback> feedbacks = new ArrayList<>(fileParts.size());
		for (Part filePart : fileParts) {
			AudioFeedback audio = new AudioFeedback();
			audio = storageService.ParseFilePart(filePart, audio, storagePath);	
			feedbacks.add(audio);
		}

		return feedbacks;
	}
}
