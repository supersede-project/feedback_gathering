package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;

import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.ConfigurationType;
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
	
	public void testUpdateOfApplication() throws ClientProtocolException, IOException, SQLException, NotFoundException {
		InputStream is = this.getClass().getResourceAsStream("application_update.json");
		String jsonString = IOUtils.toString(is); 
		
		Application updatedApplication = PutSuccess(
				"http://localhost:8080/orchestrator/feedback/en/applications", 
				jsonString,
				Application.class);
		
	    assertEquals(updatedApplication.getId(), new Integer(27));
	    assertEquals(updatedApplication.getConfigurations().stream().filter(c -> c.getType().equals(ConfigurationType.PULL)).count(), 3);
	    Stream<Configuration> stream = updatedApplication.getConfigurations().stream();
	    Configuration insertedConfiguration = stream.max((c1, c2) -> Long.compare(c1.getCreatedAt().getTime(), c2.getCreatedAt().getTime())).get();
	    assertEquals(insertedConfiguration.getFeedbackMechanisms().size(), 2);
	}
	
}
