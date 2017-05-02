package ch.uzh.ifi.feedback.library.rest.validation.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
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


/**
 * This class is the base class for all servlet tests.
 * It provides methods for the validation of requests. Further it provides setUp and tearDown methods to authenticate the test user
 * and to restore the test database before and after each request.
 *
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
public abstract class ServletTest extends TestCase {

	protected Gson gson;
	private UserToken token;
	private CloseableHttpClient client;
	private IDatabaseConfiguration config;
	private String testDatabaseDumpFile;

	public ServletTest(IDatabaseConfiguration config)
	{
		this.config = config;
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.S").create();
		client = GetHttpClient();
		CreateDumps();
	}

	/**
	 * Configures a CloseableHttpClient that  accepts unsigned certificates for HTTPS requests.
	 *
	 * @return the CloseableHttpClient for issuing requests
	 */
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

	/**
	 * Authenticates the test user. This method must be overriden for each servlet since the URL for authentication can change.
	 *
	 * @return the UserToken identifiying the test user
	 * @throws IOException
	 */
	protected abstract UserToken AuthenticateUser() throws IOException;

	/**
	 * This method is executed before a request. It authenticates the test user and restores the test database.
	 */
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        token = AuthenticateUser();
        RestoreTestDatabase();
    }

	/**
	 * This method is executed after a request. It restores the test database.
	 */
   @Override
	protected void tearDown() throws Exception
    {
		super.tearDown();
        RestoreTestDatabase();
    }

   /**
    * Restores the test database from a dump file on the local filesystem.
    */
	private void RestoreTestDatabase()
	{
		if(testDatabaseDumpFile == null)
			return;

       //Restore Databases from dump file
       String restoreTestDbCmd = String.format("mysql -u %s -p%s %s < %s", config.getUserName(), config.getPassword(), config.getTestDatabase(), testDatabaseDumpFile);
       try {
       	if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
       	{
				Runtime.getRuntime().exec(new String[]{"bash", "-c", restoreTestDbCmd}).waitFor();
       	}else if(SystemUtils.IS_OS_WINDOWS)
       	{
				Runtime.getRuntime().exec(new String[]{"cmd","/c", restoreTestDbCmd}).waitFor();
       	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method returns an InputStream to the test database dump file. It must be overriden for different ServletTests.
	 * @return the InputStream to the test database dump file
	 */
	protected abstract InputStream getTestDatabaseDump();

	/**
	 * Creates the dump file for the test database. Note that the getTestDatabaseDump() method must return a valid InputStream
	 * to an SQL file.
	 */
	private void CreateDumps()
	{
		InputStream inputStream = getTestDatabaseDump();

		if (inputStream != null)
			testDatabaseDumpFile = generateTempFile(inputStream, "dump_" + config.getTestDatabase());
	}

	/**
	 * Creates the dump file for the test database. Note that the getTestDatabaseDump() method must return a valid InputStream
	 * to an SQL file.
	 *
	 * @param input the InputStream to the SQL dump file
	 * @param filename the filename for the dump file
	 * @return
	 */
	private String generateTempFile(InputStream input, String filename)
	{
       try {
           File file = File.createTempFile(filename, ".tmp");
           OutputStream out = new FileOutputStream(file);
           int read;
           byte[] bytes = new byte[1024];

           while ((read = input.read(bytes)) != -1) {
               out.write(bytes, 0, read);
           }
           file.deleteOnExit();

           return file.getAbsolutePath();

       } catch (IOException ex) {
           ex.printStackTrace();
       }

       return null;
	}

	/**
	 * Issues a delete request and asserts the HttpStatus 200
	 *
	 * @param url the request url
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
   protected void DeleteSuccess(String url) throws ClientProtocolException, IOException
   {
		HttpUriRequest request = new HttpDelete(url);
		AssertRequestStatus(request, HttpStatus.SC_OK);
   }

   /**
    * Issues a DELETE request and asserts the HttpStatus provided
    *
    * @param url the request url
    * @param status the status to assert
    * @throws ClientProtocolException
    * @throws IOException
    */
   protected void DeleteFail(String url, int status) throws ClientProtocolException, IOException
   {
		HttpUriRequest request = new HttpDelete(url);
		AssertRequestStatus(request, status);
   }

   /**
    * Issues a GET request, asserts the HttpStatus 200 and returns the deserialized response object.
    *
    * @param url the request url
    * @param clazz the class of the response
    * @return the deserialized response from the servlet
    * @throws ClientProtocolException
    * @throws IOException
    */
   protected <T> T GetSuccess(String url, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpUriRequest request = new HttpGet(url);
		HttpResponse response = AssertRequestStatus(request, HttpStatus.SC_OK);
		return ExtractObjects(clazz, response);
   }

   /**
    * Issues a GET request and asserts the HttpStatus provided.
    *
    * @param url the request url
    * @param status the status to assert
    * @return the deserialized response from the servlet
    * @throws ClientProtocolException
    * @throws IOException
    */
   protected void GetFail(String url, int status) throws ClientProtocolException, IOException
   {
		HttpUriRequest request = new HttpGet(url);
		AssertRequestStatus(request, status);
   }

   /**
    * Issues a POST request, asserts the HttpStatus 201 and returns the deserialized response object.
    *
    * @param url the request url
    * @param jsonString the JSON string to send to the servlet
    * @param clazz the class of the response
    * @return the deserialized response from the servlet
    * @throws ClientProtocolException
    * @throws IOException
    */
   protected <T> T PostSuccess(String url, String jsonString, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpPost request = new HttpPost(url);
		StringEntity params = new StringEntity(jsonString);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        HttpResponse response = AssertRequestStatus(request, HttpStatus.SC_CREATED);
		return ExtractObjects(clazz, response);
   }

   /**
    * Issues a POST request and asserts the HttpStatus provided.
    *
    * @param url the request url
    * @param jsonString the JSON string to send to the servlet
    * @param status the status to assert
    * @return the deserialized response from the servlet
    * @throws ClientProtocolException
    * @throws IOException
    */
   protected void PostFail(String url, String jsonString, int status) throws ClientProtocolException, IOException
   {
		HttpPost request = new HttpPost(url);
		StringEntity params = new StringEntity(jsonString);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        AssertRequestStatus(request, status);
   }

   /**
    * Issues a POST request, asserts the HttpStatus 201 and returns the deserialized response object.
    *
    * @param url the request url
    * @param entity the HttpEntity to send to the servlet
    * @param clazz the class of the response
    * @return the deserialized response from the servlet
    * @throws ClientProtocolException
    * @throws IOException
    */
   protected <T> T PostSuccess(String url, HttpEntity entity, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpPost request = new HttpPost(url);

        request.addHeader(entity.getContentType());
        request.setEntity(entity);

        HttpResponse response = AssertRequestStatus(request, HttpStatus.SC_CREATED);
		return ExtractObjects(clazz, response);
   }

   /**
    * Issues a PUT request, asserts the HttpStatus 200 and returns the deserialized response object.
    *
    * @param url the request url
    * @param jsonString the JSON string to send to the servlet
    * @param clazz the class of the response
    * @return the deserialized response from the servlet
    * @throws ClientProtocolException
    * @throws IOException
    */
   protected <T> T PutSuccess(String url, String jsonString, Class<T> clazz) throws ClientProtocolException, IOException
   {
		HttpPut request = new HttpPut(url);

		StringEntity params = new StringEntity(jsonString);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        HttpResponse response = AssertRequestStatus(request, HttpStatus.SC_OK);
		return ExtractObjects(clazz, response);
   }

   /**
    * Issues a PUT request and asserts the HttpStatus provided.
    *
    * @param url the request url
    * @param jsonString the JSON string to send to the servlet
    * @param status the status to assert
    * @return the deserialized response from the servlet
    * @throws ClientProtocolException
    * @throws IOException
    */
   protected void PutFail(String url, String jsonString, int status) throws ClientProtocolException, IOException
   {
		HttpPut request = new HttpPut(url);

		StringEntity params = new StringEntity(jsonString);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);

        AssertRequestStatus(request, status);
   }

   /**
    * Issues a request to the servlet and asserts the status provided
    *
    * @param request the HttpUriRequest to issue
    * @param status the status to assert
    * @return the HttpResponse
    * @throws ClientProtocolException
    * @throws IOException
    */
   private HttpResponse AssertRequestStatus(HttpUriRequest request, int status) throws ClientProtocolException, IOException
   {
		if(token != null)
			request.setHeader("Authorization", token.getToken().toString());

		HttpResponse httpResponse = client.execute(request);
		assertEquals(httpResponse.getStatusLine().getStatusCode(), status);

		return httpResponse;
   }

   /**
    * Extracts (deserializes) object(s) of the provided type from an HttpResponse.
    *
    * @param clazz the class of the objects to extract
    * @param httpResponse the response from the server
    * @return The deserialized object(s) from the response
    * @throws IOException
    * @throws ClientProtocolException
    */
	private <T> T ExtractObjects(Class<T> clazz, HttpResponse httpResponse) throws IOException, ClientProtocolException
	{
		String mimeType = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType();
		assertEquals("application/json", mimeType);

		String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
		T createdObjects = gson.fromJson(jsonFromResponse, clazz);

		return createdObjects;
	}
}
