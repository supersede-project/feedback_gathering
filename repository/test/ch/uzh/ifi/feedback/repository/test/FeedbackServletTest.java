package ch.uzh.ifi.feedback.repository.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
import org.junit.After;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import javassist.NotFoundException;
import junit.framework.TestCase;


// TODO run the tests against a test database that is cleanup after every test method
public class FeedbackServletTest extends TestCase {
	
	private Connection dbConnection;
	private final int NUMBER_OF_FEEDBACK_RECORDS = 36;
		
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        Connection dbConnection = TransactionManager.createDatabaseConnection();
        dbConnection.setAutoCommit(false);
    }
    
    @After
    protected void cleanUp() {
    	
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
		HttpUriRequest request = new HttpGet("http://localhost:8080/feedback_repository/en/feedbacks/1");
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

	    String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
	    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.S").create();
	    Feedback retrievedFeedback = gson.fromJson(jsonFromResponse, Feedback.class);
	    
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		assertEquals("application/json", mimeType);
		assertEquals(retrievedFeedback.getId(), new Integer(1));		
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















