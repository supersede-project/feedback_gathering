package ch.uzh.ifi.feedback.repository.test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.commons.io.FileUtils;
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
import ch.uzh.ifi.feedback.repository.model.Feedback;
import javassist.NotFoundException;
import junit.framework.TestCase;


// TODO run the tests against a test database that is cleanup after every test method
public class FeedbackServletTest extends TestCase {
	private final int NUMBER_OF_FEEDBACK_RECORDS = 10;
	
	private DatabaseConfiguration config;
	
	public FeedbackServletTest() {
        config = new DatabaseConfiguration();
	}
	
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        config.UseTestConfiguration();
    }
    
   @Override
	protected void tearDown() throws Exception {
		super.tearDown();
        
		config.RestoreConfiguration();
	}
    
	public void testRetrievingAllFeedbacks() throws ClientProtocolException, IOException {
		HttpUriRequest request = new HttpGet("http://localhost:8080/feedback_repository/en/feedbacks");
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

		String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.S").create();
	    Feedback[] retrievedFeedbacks = gson.fromJson(jsonFromResponse, Feedback[].class);
        
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		assertEquals("application/json", mimeType);
		assertEquals(retrievedFeedbacks.length, NUMBER_OF_FEEDBACK_RECORDS);
	}

	public void testRetrievingSingleFeedbacks() throws ClientProtocolException, IOException {
		HttpUriRequest request = new HttpGet("http://localhost:8080/feedback_repository/en/feedbacks/57");
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

	    String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.S").create();
	    Feedback retrievedFeedback = gson.fromJson(jsonFromResponse, Feedback.class);
	    
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		assertEquals("application/json", mimeType);
		assertEquals(retrievedFeedback.getId(), new Integer(57));		
		assertEquals(retrievedFeedback.getApplicationId(), 1l);		
	}
	

	public void testCreationOfFeedback() throws ClientProtocolException, IOException, SQLException, NotFoundException {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost request = new HttpPost("http://localhost:8080/feedback_repository/en/feedbacks");
		
		StringEntity params = new StringEntity("{\"title\":\"Feedback JUnit\",\"userIdentification\":\"u8102390\",\"language\":\"EN\",\"applicationId\":1,\"configurationId\":1,\"textFeedbacks\":[{\"text\":\"This is the feedback text\",\"mechanismId\":1}]}");
        request.addHeader("content-type", "application/json");
        request.setEntity(params);        		

		HttpResponse response = httpclient.execute(request);
		assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

	    String jsonFromResponse = EntityUtils.toString(response.getEntity());	    
	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.S").create();
	    Feedback createdFeedback = gson.fromJson(jsonFromResponse, Feedback.class);
	    
	    assertEquals(createdFeedback.getApplicationId(), 1l);	
	    assertEquals(createdFeedback.getTitle(), "Feedback JUnit");			
	}	
}















