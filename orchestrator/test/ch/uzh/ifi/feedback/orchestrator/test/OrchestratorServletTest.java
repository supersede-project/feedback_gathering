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
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import javassist.NotFoundException;
import junit.framework.TestCase;

public class OrchestratorServletTest extends TestCase {
	
	private static int NUMBER_OF_APPLICATIONS = 9;
	
	private DatabaseConfiguration config;
	
	public OrchestratorServletTest(){
		config = new DatabaseConfiguration();
	}
	
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        config.RestoreTestDatabases();
    }
    
   @Override
	protected void tearDown() throws Exception 
   {
		super.tearDown();
		config.RestoreTestDatabases();
	}
   
	public void testRetrievingAllApplications() throws ClientProtocolException, IOException {
		HttpUriRequest request = new HttpGet("http://localhost:8080/feedback_orchestrator/en/applications");
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

		String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.S").create();
	    Application[] retrievedApplications = gson.fromJson(jsonFromResponse, Application[].class);
        
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		assertEquals("application/json", mimeType);
		assertEquals(retrievedApplications.length, NUMBER_OF_APPLICATIONS);
	}
	
	public void testRetrievingSingleApplication() throws ClientProtocolException, IOException {
		HttpUriRequest request = new HttpGet("http://localhost:8080/feedback_orchestrator/en/applications/14");
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

	    String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.S").create();
	    Application application = gson.fromJson(jsonFromResponse, Application.class);
	    
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		assertEquals("application/json", mimeType);
		assertEquals(application.getId(), new Integer(14));	
	}

	public void testCreationOfApplication() throws ClientProtocolException, IOException, SQLException, NotFoundException {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost request = new HttpPost("http://localhost:8080/feedback_orchestrator/en/applications");
		
		InputStream stream = this.getClass().getResourceAsStream("orchestrator_v7_example.json");
		String jsonString = IOUtils.toString(stream); 
		StringEntity params = new StringEntity(jsonString);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);        		

		HttpResponse response = httpclient.execute(request);
		assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

	    String jsonFromResponse = EntityUtils.toString(response.getEntity());	    
	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.S").create();
	    Application createdApplication = gson.fromJson(jsonFromResponse, Application.class);
	    
	    assertEquals(createdApplication.getName(), "Test Website 11");		
	}	
}
