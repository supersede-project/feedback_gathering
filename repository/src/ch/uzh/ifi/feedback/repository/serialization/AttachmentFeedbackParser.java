package ch.uzh.ifi.feedback.repository.serialization;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.repository.model.AttachmentFeedback;

public class AttachmentFeedbackParser {
	private FileStorageService storageService;
	
	@Inject
	public AttachmentFeedbackParser(FileStorageService storageService)
	{
		this.storageService = storageService;
	}
	
	public List<AttachmentFeedback> ParseRequestParts(List<Part> fileParts) {

		String storagePath = storageService.CreateDirectory("attachments");
		List<AttachmentFeedback> attachments = new ArrayList<>(fileParts.size());
		for (Part filePart : fileParts) {
			AttachmentFeedback attachment = new AttachmentFeedback();
			attachment = storageService.ParseFilePart(filePart, attachment, storagePath);	
			attachments.add(attachment);
		}

		return attachments;
	}
}
