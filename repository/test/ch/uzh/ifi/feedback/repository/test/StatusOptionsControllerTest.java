package ch.uzh.ifi.feedback.repository.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;

import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.StatusOption;
import static java.util.Arrays.asList;

public class StatusOptionsControllerTest extends ServletTest {

	@Override
	protected UserToken AuthenticateUser() throws IOException
	{
		InputStream stream = ServletTest.class.getResourceAsStream("api_user.json");
		String jsonString = IOUtils.toString(stream); 
		
		return PostSuccess(
				"http://localhost:8080/feedback_repository/authenticate", 
				jsonString,
				UserToken.class);
	}
	
	public void testRetrievingAllOptions() throws ClientProtocolException, IOException {
		StatusOption[] options = GetSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				StatusOption[].class);

		assertEquals(options.length, 7);
	}
	
	public void testDeleteAnOption() throws ClientProtocolException, IOException {
		DeleteSuccess("http://localhost:8080/feedback_repository/en/status_options/1");

		StatusOption[] options = GetSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				StatusOption[].class);

		assertEquals(options.length, 6);
	}
	
	public void testInsertNewOption() throws IOException
	{
		InputStream stream = this.getClass().getResourceAsStream("options_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		StatusOption createdOption = PostSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				jsonString, 
				StatusOption.class);
		
		assertEquals(createdOption.getName(), "new_state");
		assertEquals(createdOption.getOrder(), 2);
		assertEquals(createdOption.isUserSpecific(), false);
		
		StatusOption[] allOptions = GetSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				StatusOption[].class);
		
		assertFalse(asList(allOptions).stream().filter(o -> o.getOrder() == 2 && o.isUserSpecific() == false).count() > 1);
	}
	
	public void testUpdateOfOption() throws IOException
	{	
		StatusOption createdOption = PutSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				"{ 'id': 3, 'order': 3}", 
				StatusOption.class);
		
		assertEquals(createdOption.getName(), "new");
		assertEquals(createdOption.getOrder(), 3);
		assertEquals(createdOption.isUserSpecific(), false);
		
		StatusOption[] allOptions = GetSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				StatusOption[].class);
		
		assertFalse(asList(allOptions).stream().filter(o -> o.getOrder() == 3).count() > 1);
	}
	
}
