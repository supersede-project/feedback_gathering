package ch.uzh.ifi.feedback.repository.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import javassist.NotFoundException;

public class FeedbackServletTest extends ServletTest {
	
	private final int NUMBER_OF_FEEDBACK_RECORDS = 10;
	
	public void testRetrievingAllFeedbacks() throws ClientProtocolException, IOException {
		Feedback[] retrievedFeedbacks = GetSuccess(
				"http://localhost:8080/feedback_repository/en/feedbacks", 
				Feedback[].class);

		assertEquals(retrievedFeedbacks.length, NUMBER_OF_FEEDBACK_RECORDS);
	}

	public void testRetrievingSingleFeedbacks() throws ClientProtocolException, IOException {
		Feedback retrievedFeedback = GetSuccess(
				"http://localhost:8080/feedback_repository/en/feedbacks/57", 
				Feedback.class);
		
		assertEquals(retrievedFeedback.getId(), new Integer(57));		
		assertEquals(retrievedFeedback.getApplicationId(), 1l);		
	}
	
	public void testCreationOfFeedback() throws ClientProtocolException, IOException, SQLException, NotFoundException {
		InputStream stream = this.getClass().getResourceAsStream("feedback_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		Feedback createdFeedback = PostSuccess(
				"http://localhost:8080/feedback_repository/en/feedbacks", 
				jsonString,
				Feedback.class);
		
	    assertEquals(createdFeedback.getApplicationId(), 1l);	
	    assertEquals(createdFeedback.getTitle(), "Feedback JUnit 648");
	    assertEquals(createdFeedback.getRatingFeedbacks().size(), 2);
	    assertEquals(createdFeedback.getCategoryFeedbacks().size(), 2);
	}	
	
	/**
	 * Note: This test will only work, when the webapps directory ot tomcat is writable...
	 */
	public void testCreationOfFeedbackWithFiles() throws ClientProtocolException, IOException, SQLException, NotFoundException {
		InputStream stream = this.getClass().getResourceAsStream("feedback_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("json", jsonString, ContentType.APPLICATION_JSON);
		builder.addBinaryBody("screenshot1", getClass().getResourceAsStream("screenshot1.jpeg"));
		builder.addBinaryBody("screenshot2", getClass().getResourceAsStream("screenshot2.jpeg"));
		builder.addBinaryBody("audio1", getClass().getResourceAsStream("audio1.mp3"));
		builder.addBinaryBody("attachment1", getClass().getResourceAsStream("attachment1.txt"));
		builder.addBinaryBody("attachment2", getClass().getResourceAsStream("attachment2.txt"));
		
		Feedback createdFeedback = PostSuccess(
				"http://localhost:8080/feedback_repository/en/feedbacks", 
				builder.build(),
				Feedback.class);
		
	    assertEquals(createdFeedback.getApplicationId(), 1l);	
	    assertEquals(createdFeedback.getTitle(), "Feedback JUnit 648");
	    assertEquals(createdFeedback.getRatingFeedbacks().size(), 2);
	    assertEquals(createdFeedback.getCategoryFeedbacks().size(), 2);
	    assertEquals(createdFeedback.getScreenshotFeedbacks().size(), 2);
	    assertEquals(createdFeedback.getAttachmentFeedbacks().size(), 2);
	    assertEquals(createdFeedback.getAudioFeedbacks().size(), 1);
	}	
}















