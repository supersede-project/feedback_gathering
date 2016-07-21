package ch.uzh.ifi.feedback.repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.mysql.jdbc.Statement;
import com.sun.jndi.toolkit.url.Uri;

import ch.uzh.ifi.feedback.library.rest.Controller;
import ch.uzh.ifi.feedback.library.rest.DefaultSerializer;
import ch.uzh.ifi.feedback.library.rest.ISerializationService;
import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import ch.uzh.ifi.feedback.repository.interfaces.FeedbackReceiver;

@Controller
(Route = "/{Application}/feedback")
public class FeedbackController extends RestController<Feedback>{
	
	private ISerializationService<Feedback> serializationService;
	private FeedbackService feedbackService;
	
	public FeedbackController() {
		super();
		
		this.feedbackService = new FeedbackService(new TransactionManager(), new FeedbackParser());
		this.serializationService = new DefaultSerializer<>(getSerializationType());
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, Feedback feedback) throws Exception 
	{
	    List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList()); // Retrieves <input type="file" name="file" multiple="true">
	    List<Screenshot> screenshots = SaveScreenshots(fileParts);
	    feedback.setScreenshots(screenshots);
	    feedbackService.SaveFeedback(feedback);
		
		response.setStatus(201);
		response.getWriter().append(serializationService.Serialize(feedback));
	}

	
	private List<Screenshot> SaveScreenshots(List<Part> fileParts) throws Exception
	{
		File dir = new File("screenshots");
		boolean isDir = dir.mkdirs();
	    List<Screenshot> screenshots = new ArrayList<>(fileParts.size());
	    for (Part filePart : fileParts) {
	        InputStream fileContent = filePart.getInputStream();
	        int fileSize = (int) filePart.getSize();
	        byte[] content = new byte[fileSize];
	        fileContent.read(content);
	        String fileName = getFileName(filePart);
	        File tmp = new File(dir, String.valueOf(new Date().getTime()));
	        tmp.createNewFile();
	        Files.write(tmp.toPath(), content);
	        Screenshot s = new Screenshot(fileName, tmp.getAbsolutePath(), fileSize);
	        screenshots.add(s);
	    }
	    
	    return screenshots;
	}
	
	public static String getFileName(Part filePart)
	{
	    String header = filePart.getHeader("content-disposition");
	    if(header == null)
	        return null;
	    for(String headerPart : header.split(";"))
	    {
	        if(headerPart.trim().startsWith("filename"))
	        {
	            return headerPart.substring(headerPart.indexOf('=') + 1).trim()
	                             .replace("\"", "");
	        }
	    }
	    return null;
	}

	@Override
	public ISerializationService<Feedback> getSerializationService() {
		return serializationService;
	}
}
