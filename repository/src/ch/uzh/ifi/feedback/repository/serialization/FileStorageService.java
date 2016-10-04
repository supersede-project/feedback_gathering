package ch.uzh.ifi.feedback.repository.serialization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.repository.model.FileFeedback;

@Singleton
public class FileStorageService {
	
	public <T extends FileFeedback> T ParseFilePart(FileItem filePart, T feedback, String directoryName) {

		    if(filePart == null)
		    	return feedback;
		    
			InputStream inputStream = null;
			OutputStream outputStream = null;

			try {
				inputStream = filePart.getInputStream();
				int fileSize = (int) filePart.getSize();
				String storagePath = createDirectory(directoryName);

				// more or less unique filename
				String fileNameOfStoredFile = Integer.toString(fileSize) + "_" + String.valueOf(new Date().getTime()) + "." + feedback.getFileExtension();				
				
				File outputFile = new File(storagePath, fileNameOfStoredFile);
				outputStream = new FileOutputStream(outputFile);
				
				int read = 0;
				byte[] bytes = new byte[fileSize];
				
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				
				feedback.setPath(directoryName + File.separator + fileNameOfStoredFile);
				feedback.setSize(fileSize);
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

		return feedback;
	}
	
	private String createDirectory(String directoryName)
	{
		String rootPath = System.getProperty("catalina.home");
		String relativePath = "webapps" + File.separator + directoryName;
		String uploadsStoragePath = rootPath + File.separator + relativePath;
		File uploadDirectory = new File(uploadsStoragePath);
		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdirs();
		}
		
		return uploadsStoragePath;
	}

}
