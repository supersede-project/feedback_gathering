package ch.uzh.ifi.feedback.repository.serialization;

import java.io.File;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Part;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;

public class ScreenshotSerializationService extends RepositorySerializationService<ScreenshotFeedback> {
	
	public List<ScreenshotFeedback> ParseRequestParts(List<Part> fileParts)
	{
		File dir = new File("screenshots");
		boolean isDir = dir.mkdirs();
	    List<ScreenshotFeedback> screenshots = new ArrayList<>(fileParts.size());
	    for (Part filePart : fileParts) {
	    	try
	    	{
		        InputStream fileContent = filePart.getInputStream();
		        int fileSize = (int) filePart.getSize();
		        byte[] content = new byte[fileSize];
		        fileContent.read(content);
		        String fileName = getFileName(filePart);
		        File tmp = new File(dir, String.valueOf(new Date().getTime()));
		        tmp.createNewFile();
		        ScreenshotFeedback s = new ScreenshotFeedback(null, null, tmp.toPath().toString(), fileSize, fileName, null, "", "");
		        screenshots.add(s);
	    	}catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    }
	    
	    return screenshots;
	}
	
	private static String getFileName(Part filePart)
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
}
