package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;

import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.orchestrator.model.User;
import ch.uzh.ifi.feedback.orchestrator.model.UserGroup;

public class OrchestratorServletUserTest extends ServletTest {
	
	public void testRetrievingAllUsers() throws ClientProtocolException, IOException {
		User[] retrievedUsers = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/users", 
				User[].class);
		
		assertEquals(retrievedUsers.length, 1);
	}
	
	public void testInsertUserForGroup() throws ClientProtocolException, IOException {
		InputStream stream = this.getClass().getResourceAsStream("user_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		User addedUser = PostSuccess(
				"http://localhost:8080/feedback_orchestrator/en/user_groups/1/users",
				jsonString,
				User.class);
		
		assertEquals(addedUser.getName(), "test_user");
		assertEquals(addedUser.getGroupId(), 1);
	}
	
	public void testChangeGroupOfUser() throws ClientProtocolException, IOException {
		InputStream stream = this.getClass().getResourceAsStream("user_update.json");
		String jsonString = IOUtils.toString(stream); 
		
		User updatedUser = PutSuccess(
				"http://localhost:8080/feedback_orchestrator/en/users",
				jsonString,
				User.class);
		
		assertEquals(updatedUser.getName(), "u1234");
		assertEquals(updatedUser.getGroupId(), 2);
		
		UserGroup group1 = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/user_groups/1",
				UserGroup.class);
		
		assertEquals(group1.getUsers().size(), 0);
		
		UserGroup group2 = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/user_groups/2",
				UserGroup.class);
		
		assertEquals(group2.getUsers().size(), 1);
		assertEquals(group2.getUsers().get(0).getName(), "u1234");
	}
}
