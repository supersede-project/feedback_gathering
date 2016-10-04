package ch.uzh.ifi.feedback.repository.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.authorization.UserAuthenticationService;

@Controller(void.class)
@RequestScoped
public class FileController {

	private HttpServletRequest request;
	private HttpServletResponse response;

	@Inject
	public FileController(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
	}
	
	@GET
	@Path("/attachments/{fileName}")
	@Authenticate(UserAuthenticationService.class)
	public void GetAttachmentFile(@PathParam("fileName") String filename) throws IOException
	{
		returnBinaryFile(filename, "attachments");
	}
	
	@GET
	@Path("/audios/{fileName}")
	@Authenticate(UserAuthenticationService.class)
	public void GetAudioFile(@PathParam("fileName") String filename) throws IOException
	{
		returnBinaryFile(filename, "audios");
	}
	
	@GET
	@Path("/screenshots/{fileName}")
	@Authenticate(UserAuthenticationService.class)
	public void GetScreenshot(@PathParam("fileName") String filename) throws IOException
	{
		returnBinaryFile(filename, "screenshots");
	}
	
	@GET
	@Path("/screenshots/{fileName}/base64")
	@Authenticate(UserAuthenticationService.class)
	public void GetScreenshotAsBase64(@PathParam("fileName") String filename) throws IOException
	{
		String rootPath = System.getProperty("catalina.home");
		String relativePath = "webapps" + File.separator + "screenshots" + File.separator + filename;
		String filePath = rootPath + File.separator + relativePath;
		
		File file = new File(filePath);
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		response.getOutputStream().write(encoded);
	}
	
	private void returnBinaryFile(String filename, String directory) throws IOException
	{
		String rootPath = System.getProperty("catalina.home");
		String relativePath = "webapps" + File.separator + directory + File.separator + filename;
		String filePath = rootPath + File.separator + relativePath;
		
	    response.setContentType("application/octet-stream");
	    response.setHeader("Content-Disposition", "filename="+ filename);
	    File srcFile = new File(filePath);
	    FileUtils.copyFile(srcFile, response.getOutputStream());
	}
	
	private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
		// File is too large
		}
		
		byte[] bytes = new byte[(int)length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+file.getName());
		}
		is.close();
		return bytes;
	}
}
