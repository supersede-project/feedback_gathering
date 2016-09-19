package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;

import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.orchestrator.model.UserGroup;

public class OrchestratorServletUserGroupTest extends ServletTest {
	
	public void testRetrievingAllUserGroups() throws ClientProtocolException, IOException {
		UserGroup[] retrievedUsers = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/user_groups", 
				UserGroup[].class);
		
		assertEquals(retrievedUsers.length, 2);
	}
	
	public void testRetrievingSingleUserGroup() throws ClientProtocolException, IOException {
		UserGroup retrievedUserGroup = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/user_groups/1", 
				UserGroup.class);
		
		assertEquals(retrievedUserGroup.getName(), "default");
		assertEquals(retrievedUserGroup.getUsers().size(), 1);
	}
	
	public void testInsertUserGroup() throws ClientProtocolException, IOException {
		InputStream stream = this.getClass().getResourceAsStream("user_group_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		UserGroup addedUserGroup = PostSuccess(
				"http://localhost:8080/feedback_orchestrator/en/user_groups",
				jsonString,
				UserGroup.class);
		
		assertEquals(addedUserGroup.getName(), "test group");
		assertEquals(addedUserGroup.getUsers().size(), 3);
	}
}
