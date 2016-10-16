package ch.uzh.ifi.feedback.repository.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;

import ch.uzh.ifi.feedback.library.rest.authorization.ApiUser;
import ch.uzh.ifi.feedback.library.rest.authorization.UserRole;
import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import ch.uzh.ifi.feedback.library.test.ServletTest;

public class ApiUserControllerTest extends ServletTest {

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
	
	public void testRetrievingAllApiUsers() throws ClientProtocolException, IOException {
		ApiUser[] retrievedUsers = GetSuccess(
				"http://localhost:8080/feedback_repository/en/api_users", 
				ApiUser[].class);

		assertEquals(retrievedUsers.length, 1);
	}

	public void testRetrievingSingleUser() throws ClientProtocolException, IOException {
		ApiUser retrievedUser = GetSuccess(
				"http://localhost:8080/feedback_repository/en/api_users/1", 
				ApiUser.class);
		
		assertEquals(retrievedUser.getId(), new Integer(1));		
		assertEquals(retrievedUser.getName(), "api_user");
		assertEquals(retrievedUser.getRole(), UserRole.ADMIN);	
	}
	
	public void testCreateApiUser() throws ClientProtocolException, IOException {
		InputStream stream = this.getClass().getResourceAsStream("test_user.json");
		String jsonString = IOUtils.toString(stream); 
		
		ApiUser createdUser = PostSuccess(
				"http://localhost:8080/feedback_repository/en/api_users", 
				jsonString,
				ApiUser.class);
				
		assertEquals(createdUser.getName(), "test_user");
		assertEquals(createdUser.getRole(), UserRole.USER);	
	}
	
}
