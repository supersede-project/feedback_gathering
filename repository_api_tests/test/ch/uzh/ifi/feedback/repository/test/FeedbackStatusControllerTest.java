package ch.uzh.ifi.feedback.repository.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.Status;

public class FeedbackStatusControllerTest extends RepositoryServletTest {

	private Feedback createdFeedback;
	private Status createdStatus;
	private Status createdUserStatus;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		InputStream stream = this.getClass().getResourceAsStream("feedback_insert_small.json");
		String jsonString = IOUtils.toString(stream); 
		
		createdFeedback = PostSuccess("http://localhost:8080/feedback_repository/en/applications/1/feedbacks", jsonString,Feedback.class);
		
		createdStatus = GetSuccess(
				"http://localhost:8080/feedback_repository/en/applications/1/feedbacks/" + createdFeedback.getId() + "/status", 
				Status.class);
		assertTrue(createdStatus.getStatus().equals("new"));
		
		createdUserStatus = GetSuccess(
				"http://localhost:8080/feedback_repository/en/applications/1/feedbacks/" + createdFeedback.getId() + "/api_users/1/status", 
				Status.class);
		assertTrue(createdUserStatus.getStatus().equals("unread"));
	}

	public void testUpdateInitialStateForUser() throws ClientProtocolException, IOException
	{
		Status updatedStatus = PutSuccess(
				"http://localhost:8080/feedback_repository/en/applications/1/states",
				"{ 'id': "+ createdUserStatus.getId() + ", 'status': 'read'}",
				Status.class);
		
		assertTrue(updatedStatus.getStatus().equals("read"));
	}
	
	public void testUpdateInitialStateForFeedback() throws ClientProtocolException, IOException
	{
		Status updatedStatus = PutSuccess(
				"http://localhost:8080/feedback_repository/en/applications/1/states",
				"{ 'id': "+ createdStatus.getId() + ", 'status': 'approved'}",
				Status.class);
		
		assertTrue(updatedStatus.getStatus().equals("approved"));
		
		updatedStatus = PutSuccess(
				"http://localhost:8080/feedback_repository/en/applications/1/states",
				"{ 'id': "+ createdStatus.getId() + ", 'status': 'implemented'}",
				Status.class);
		
		assertTrue(updatedStatus.getStatus().equals("implemented"));
	}
}
