package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;

import ch.uzh.ifi.feedback.orchestrator.model.Application;
import javassist.NotFoundException;

public class OrchestratorServletApplicationTest extends OrchestratorServletTest {
	
	private static int NUMBER_OF_APPLICATIONS = 9;
	
	public void testRetrievingAllApplications() throws ClientProtocolException, IOException {
		Application[] retrievedApplications = GetSuccess(
				"http://localhost:8080/orchestrator/feedback/en/applications", 
				Application[].class);
		
		assertEquals(retrievedApplications.length, NUMBER_OF_APPLICATIONS);
	}
	
	public void testRetrievingSingleApplication() throws ClientProtocolException, IOException {
		Application application = GetSuccess(
				"http://localhost:8080/orchestrator/feedback/en/applications/27", 
				Application.class);
		
		assertEquals(application.getId(), new Integer(27));
	}

	public void testCreationOfApplication() throws ClientProtocolException, IOException, SQLException, NotFoundException {
		InputStream stream = this.getClass().getResourceAsStream("orchestrator_v7_example.json");
		String jsonString = IOUtils.toString(stream); 
		
		Application createdApplication = PostSuccess(
				"http://localhost:8080/orchestrator/feedback/en/applications", 
				jsonString,
				Application.class);
		
	    assertEquals(createdApplication.getName(), "Test Website 20");		
	}
}
