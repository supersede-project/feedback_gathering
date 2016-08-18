package ch.uzh.ifi.feedback.repository.serialization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Part;

import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;

public class ScreenshotSerializationService extends RepositorySerializationService<ScreenshotFeedback> {

	public List<ScreenshotFeedback> ParseRequestParts(List<Part> fileParts) {
		String rootPath = System.getProperty("catalina.home");
		String relativePath = "webapps" + File.separator + "screenshots";
		String uploadsStoragePath = rootPath + File.separator + relativePath;
		File uploadDirectory = new File(uploadsStoragePath);
		if (!uploadDirectory.exists()) {
			uploadDirectory.mkdirs();
		}

		List<ScreenshotFeedback> screenshots = new ArrayList<>(fileParts.size());
		for (Part filePart : fileParts) {
			InputStream inputStream = null;
			OutputStream outputStream = null;

			try {
				inputStream = filePart.getInputStream();
				int fileSize = (int) filePart.getSize();
				String fileName = getFileName(filePart);
				
				String fileExtension = "";
				
				if(filePart.getContentType().equals("image/png")) {
					fileExtension = ".png";
				} else if (filePart.getContentType().equals("image/jpeg")) {
					fileExtension = ".jpg";
				} else if (filePart.getContentType().equals("image/tiff")) {
					fileExtension = ".tiff";
				} else if (filePart.getContentType().equals("image/gif")) {
					fileExtension = ".gif";
				}
				
				// more or less unique filename
				String fileNameOfStoredFile = Integer.toString(fileSize) + "_" + String.valueOf(new Date().getTime()) + fileExtension;				
				
				File outputFile = new File(uploadsStoragePath, fileNameOfStoredFile);
				outputStream = new FileOutputStream(outputFile);
				
				int read = 0;
				byte[] bytes = new byte[fileSize];
				
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				
				ScreenshotFeedback s = new ScreenshotFeedback(null, null, outputFile.toPath().toString(), fileSize, fileName,
						null, "", "");
				screenshots.add(s);
				
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
		}

		return screenshots;
	}

	private static String getFileName(Part filePart) {
		String header = filePart.getHeader("content-disposition");
		if (header == null)
			return null;
		for (String headerPart : header.split(";")) {
			if (headerPart.trim().startsWith("filename")) {
				return headerPart.substring(headerPart.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}
