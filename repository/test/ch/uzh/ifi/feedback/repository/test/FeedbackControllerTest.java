package ch.uzh.ifi.feedback.repository.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.Status;
import ch.uzh.ifi.feedback.repository.transaction.RepositoryDatabaseConfiguration;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

public class FeedbackControllerTest extends RepositoryServletTest {
	
	private final int NUMBER_OF_FEEDBACK_RECORDS = 11;

	public void testRetrievingAllFeedbacksForApplication() throws ClientProtocolException, IOException {
		Feedback[] retrievedFeedbacks = GetSuccess(
				"http://localhost:8080/feedback_repository/en/applications/1/feedbacks", 
				Feedback[].class);

		assertEquals(retrievedFeedbacks.length, NUMBER_OF_FEEDBACK_RECORDS);
	}

	public void testRetrievingSingleFeedbackForApplication() throws ClientProtocolException, IOException {
		Feedback retrievedFeedback = GetSuccess(
				"http://localhost:8080/feedback_repository/en/applications/1/feedbacks/57", 
				Feedback.class);
		
		assertEquals(retrievedFeedback.getId(), new Integer(57));		
		assertEquals(retrievedFeedback.getApplicationId(), new Integer(1));		
	}
	
	public void testDeleteSingleFeedback() throws ClientProtocolException, IOException {
		DeleteSuccess("http://localhost:8080/feedback_repository/en/applications/1/feedbacks/57");
		
		Feedback[] retrievedFeedbacks = GetSuccess(
				"http://localhost:8080/feedback_repository/en/applications/1/feedbacks", 
				Feedback[].class);
		
		assertFalse(asList(retrievedFeedbacks).stream().anyMatch(f -> f.getId().equals(new Integer(57))));
	}
	
	/**
	 * Note: This test will only work, when the webapps directory of tomcat is writable...
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
		builder.setContentType(ContentType.MULTIPART_FORM_DATA);
		
		Feedback createdFeedback = PostSuccess(
				"http://localhost:8080/feedback_repository/en/applications/1/feedbacks", 
				builder.build(),
				Feedback.class);
		
	    assertEquals(createdFeedback.getApplicationId(), new Integer(1));	
	    assertEquals(createdFeedback.getTitle(), "test_feedback");
	    assertEquals(createdFeedback.getRatingFeedbacks().size(), 2);
	    assertEquals(createdFeedback.getCategoryFeedbacks().size(), 2);
	    assertEquals(createdFeedback.getScreenshotFeedbacks().size(), 2);
	    assertEquals(createdFeedback.getAttachmentFeedbacks().size(), 2);
	    assertEquals(createdFeedback.getAudioFeedbacks().size(), 1);
	    assertTrue(createdFeedback.getScreenshotFeedbacks().stream().anyMatch(s -> s.getTextAnnotations().size() == 2));
	}	
}















