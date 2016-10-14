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
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.repository.model.Status;

@Singleton
public class StatusSerializationService extends RepositorySerializationService<Status> {

	@Inject
	public StatusSerializationService() 
	{
		
	}

	@Override
	public Status Deserialize(HttpServletRequest request) {
		
		Status status;
		if(!request.getContentType().contains("multipart/form-data"))
		{
			status = super.Deserialize(request);	
			return status;
		}	

		List<FileItem> fileItems = getFileItems(request);
		
		String data = fileItems.stream().filter(i -> i.getFieldName().equals("json")).findFirst().get().getString();
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
		status = gson.fromJson(data, Status.class);

		return status;
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