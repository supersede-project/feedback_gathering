package ch.uzh.ifi.feedback.library.test;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import junit.framework.TestCase;

public abstract class ServletTest extends TestCase {
	
	protected Gson gson; 
	private UserToken token;
	private CloseableHttpClient client;
	
	public ServletTest(){
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.S").create();
		client = GetHttpClient();
	}
	
	@SuppressWarnings("deprecation")
	private CloseableHttpClient GetHttpClient()
	{
	    SSLContextBuilder builder = new SSLContextBuilder();
	    try {
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		} catch (NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}
	    SSLConnectionSocketFactory sslsf;
		try {
			sslsf = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		    CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf)
		    		.setRedirectStrategy(new DefaultRedirectStrategy(){
		    	        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)  {
			    	            boolean isRedirect=false;
			    	            try {
			    	                isRedirect = super.isRedirected(request, response, context);
			    	            } catch (ProtocolException e) {
			    	                // TODO Auto-generated catch block
			    	                e.printStackTrace();
			    	            }
			    	            if (!isRedirect) {
			    	                int responseCode = response.getStatusLine().getStatusCode();
			    	                if (responseCode == 302) {
			    	                    return true;
			    	                }
			    	            }
			    	            return isRedirect;
		    	        }
		    		})
		    		.build();
		    
		    
		    return httpclient;
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected abstract UserToken AuthenticateUser() throws IOException;
	
	protected abstract IDatabaseConfiguration getDatabaseConfiguration();
	
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        token = AuthenticateUser();
        getDatabaseConfiguration().RestoreTestDatabase();
    }
    
   @Override
	protected void tearDown() throws Exception 
    {
		super.tearDown();
        getDatabaseConfiguration().RestoreTestDatabase();
    }
   
   protected <T> T GetSuccess(String url, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpUriRequest request = new HttpGet(url);
		
		if(token != null)
			request.setHeader("Authorization", token.getToken().toString());
		
		HttpResponse httpResponse = client.execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

		String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
		T retrievedObjects = gson.fromJson(jsonFromResponse, clazz);
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		assertEquals("application/json", mimeType);
		
		return retrievedObjects;
   }
   
   protected void DeleteSuccess(String url) throws ClientProtocolException, IOException
   {
		HttpUriRequest request = new HttpDelete(url);
		
		if(token != null)
			request.setHeader("Authorization", token.getToken().toString());
		
		HttpResponse httpResponse = client.execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
   }
   
   protected <T> T PostSuccess(String url, String jsonString, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpPost request = new HttpPost(url);

		if(token != null)
			request.setHeader("Authorization", token.getToken().toString());
		
		StringEntity params = new StringEntity(jsonString);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        
		HttpResponse httpResponse = client.execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

		String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
		System.out.println(jsonFromResponse);
		T createdObjects = gson.fromJson(jsonFromResponse, clazz);
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		assertEquals("application/json", mimeType);
		
		return createdObjects;
   }
   
   protected <T> T PostSuccess(String url, HttpEntity entity, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpPost request = new HttpPost(url);
		
		if(token != null)
			request.setHeader("Authorization", token.getToken().toString());
		
        request.addHeader(entity.getContentType());
        request.setEntity(entity);
        
		HttpResponse httpResponse = client.execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

		String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
		T createdObjects = gson.fromJson(jsonFromResponse, clazz);
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
		assertEquals("application/json", mimeType);
		
		return createdObjects;
   }
   
   protected <T> T PutSuccess(String url, String jsonString, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpPut request = new HttpPut(url);
		
		if(token != null)
			request.setHeader("Authorization", token.getToken().toString());
		
		StringEntity params = new StringEntity(jsonString);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        
		HttpResponse httpResponse = client.execute(request);
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();

		String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());	    
		T createdObjects = gson.fromJson(jsonFromResponse, clazz);
		assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		assertEquals("application/json", mimeType);
		
		return createdObjects;
   }
  
}
