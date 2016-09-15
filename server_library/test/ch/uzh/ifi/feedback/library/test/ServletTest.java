package ch.uzh.ifi.feedback.library.test;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import junit.framework.TestCase;

public class ServletTest extends TestCase {
	
	protected DatabaseConfiguration config;
	protected Gson gson; 
	
	public ServletTest(){
		config = new DatabaseConfiguration();
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.S").create();
		
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
   
   protected <T> T GetSuccess(String url, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpUriRequest request = new HttpGet(url);
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

		String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
		T retrievedObjects = gson.fromJson(jsonFromResponse, clazz);
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		assertEquals("application/json", mimeType);
		
		return retrievedObjects;
   }
   
   protected <T> T PostSuccess(String url, String jsonString, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpPost request = new HttpPost(url);
		StringEntity params = new StringEntity(jsonString);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

		String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
		T createdObjects = gson.fromJson(jsonFromResponse, clazz);
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		assertEquals("application/json", mimeType);
		
		return createdObjects;
   }
   
   protected <T> T PostSuccess(String url, HttpEntity entity, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpPost request = new HttpPost(url);
        request.addHeader(entity.getContentType());
        request.setEntity(entity);
        
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

		String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
		T createdObjects = gson.fromJson(jsonFromResponse, clazz);
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		assertEquals("application/json", mimeType);
		
		return createdObjects;
   }
   
   protected <T> T PutSuccess(String url, String jsonString, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpPut request = new HttpPut(url);
		StringEntity params = new StringEntity(jsonString);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

		String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
		T createdObjects = gson.fromJson(jsonFromResponse, clazz);
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		assertEquals("application/json", mimeType);
		
		return createdObjects;
   }
  
}
