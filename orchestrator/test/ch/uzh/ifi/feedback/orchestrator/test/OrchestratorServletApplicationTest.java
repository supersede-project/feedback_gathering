package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import javassist.NotFoundException;

public class OrchestratorServletApplicationTest extends ServletTest {
	
	private static int NUMBER_OF_APPLICATIONS = 9;
   
	public void testRetrievingAllApplications() throws ClientProtocolException, IOException {
		Application[] retrievedApplications = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/applications", 
				Application[].class);
		
		assertEquals(retrievedApplications.length, NUMBER_OF_APPLICATIONS);
	}
	
	public void testRetrievingSingleApplication() throws ClientProtocolException, IOException {
		Application application = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/applications/27", 
				Application.class);
		
		assertEquals(application.getId(), new Integer(27));	
	}

	public void testCreationOfApplication() throws ClientProtocolException, IOException, SQLException, NotFoundException {
		InputStream stream = this.getClass().getResourceAsStream("orchestrator_v7_example.json");
		String jsonString = IOUtils.toString(stream); 
		
		Application createdApplication = PostSuccess(
				"http://localhost:8080/feedback_orchestrator/en/applications", 
				jsonString,
				Application.class);
		
	    assertEquals(createdApplication.getName(), "Test Website 20");		
	}	
}
