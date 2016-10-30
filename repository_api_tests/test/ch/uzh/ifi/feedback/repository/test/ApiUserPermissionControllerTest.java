package ch.uzh.ifi.feedback.repository.test;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import ch.uzh.ifi.feedback.library.rest.authorization.ApiUserPermission;
import static java.util.Arrays.asList;

public class ApiUserPermissionControllerTest extends RepositoryServletTest
{
	public void testRetrievingAllPermissionsForUser() throws ClientProtocolException, IOException 
	{
		ApiUserPermission[] retrievedPermissions = GetSuccess(
				"http://localhost:8080/feedback_repository/en/api_users/1/permissions", 
				ApiUserPermission[].class);
		
		assertEquals(asList(retrievedPermissions).size(), 1);		
		assertEquals(retrievedPermissions[0].getApplicationId(), new Integer(1));
	}
	
	public void testCreatePermissionFroUser() throws ClientProtocolException, IOException {
		InputStream stream = this.getClass().getResourceAsStream("api_user_permission.json");
		String jsonString = IOUtils.toString(stream); 
		
		ApiUserPermission createdPermission = PostSuccess(
				"http://localhost:8080/feedback_repository/en/api_users/1/permissions", 
				jsonString,
				ApiUserPermission.class);
				
		assertEquals(createdPermission.getApplicationId(), new Integer(2));
		assertEquals(createdPermission.isHasPermission(), true);
	}
	
	public void testDeletePermissionById() throws ClientProtocolException, IOException 
	{
		DeleteSuccess("http://localhost:8080/feedback_repository/en/api_users/permissions/1");
		ApiUserPermission[] retrievedPermissions = GetSuccess(
				"http://localhost:8080/feedback_repository/en/api_users/1/permissions", 
				ApiUserPermission[].class);
		
		assertEquals(asList(retrievedPermissions).size(), 0);		
	}
}
