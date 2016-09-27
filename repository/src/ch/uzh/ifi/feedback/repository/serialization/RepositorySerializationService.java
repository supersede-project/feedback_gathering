package ch.uzh.ifi.feedback.repository.serialization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;

public abstract class RepositorySerializationService<T> extends DefaultSerializer<T> {
	
	@Override
	public T Deserialize(HttpServletRequest request) {
		
		String data = GetRequestContent(request);
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
		T requestObject = gson.fromJson(data, serializationType);
		return requestObject;
	}
	
	private String GetRequestContent(HttpServletRequest request)
	{
	    StringBuffer buffer = new StringBuffer();
		String line = null;
		
		try{
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null)
			{
				buffer.append(line);
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
	private String getRequestContent(InputStream in)
	{
	    StringBuffer buffer = new StringBuffer();
		String line = null;
		
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			while((line = reader.readLine()) != null)
			{
				buffer.append(line);
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
	private String getMultiPartContent(HttpServletRequest request)
	{
	    try {
	        List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
	        for (FileItem item : items) {
	            if (item.isFormField()) {
	                String fieldName = item.getFieldName();
	                if(fieldName.equals("json"))
	                {
	                	return item.getString();
	                }
	            } 
	        }
	    } catch (FileUploadException e) {
	    	e.printStackTrace();
	    }
	    
	    return null;
	}
}
