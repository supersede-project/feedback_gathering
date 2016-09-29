package ch.uzh.ifi.feedback.repository.serialization;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

import ch.uzh.ifi.feedback.repository.model.AttachmentFeedback;
import ch.uzh.ifi.feedback.repository.model.AudioFeedback;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.FileFeedback;
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
		
		Feedback feedback;
		if(!request.getContentType().contains("multipart/form-data"))
		{
			feedback = super.Deserialize(request);	
			return feedback;
		}	

		List<FileItem> fileItems = getFileItems(request);
		
		String data = fileItems.stream().filter(i -> i.getFieldName().equals("json")).findFirst().get().getString();
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
		feedback = gson.fromJson(data, Feedback.class);
		
		try {
			for(ScreenshotFeedback screenshot : feedback.getScreenshotFeedbacks())
			{
				screenshot = storageService.ParseFilePart(getFileItemsForFeedback(screenshot, fileItems), screenshot, "screenshots");	
			}
			
			for(AudioFeedback audio : feedback.getAudioFeedbacks())
			{
				Part filePart = request.getPart(audio.getPart());
				audio = storageService.ParseFilePart(getFileItemsForFeedback(audio, fileItems), audio, "audios");	
			}
			
			for(AttachmentFeedback attachment : feedback.getAttachmentFeedbacks())
			{
				Part filePart = request.getPart(attachment.getPart());
				attachment = storageService.ParseFilePart(getFileItemsForFeedback(attachment, fileItems), attachment, "attachments");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return feedback;
	}
	
	private FileItem getFileItemsForFeedback(FileFeedback feedback, List<FileItem> fileItems)
	{
		String partName = feedback.getPart();
		List<FileItem> parts = fileItems.stream().filter(p -> p.getFieldName().equals(partName)).collect(Collectors.toList());
		if(parts.size() > 0)
		{
			FileItem item = parts.get(0);
			fileItems.remove(item);
			return item;
		}
		return null;
	}
	
	private List<FileItem> getFileItems(HttpServletRequest request)
	{
	    try {
	        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
	        return items;
	    } catch (FileUploadException e) {
	    	e.printStackTrace();
	    }
	    
	    return new ArrayList<>();
	}
}