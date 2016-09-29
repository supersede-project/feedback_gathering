package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;


import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;

public class OrchestratorServletParameterTest extends ServletTest {
	
	private static int NUMBER_OF_PARAMETERS = 648;
	
	public void testRetrievingAllParameters() throws ClientProtocolException, IOException {
		FeedbackParameter[] retrievedParameters = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/parameters", 
				FeedbackParameter[].class);
		assertEquals(retrievedParameters.length, NUMBER_OF_PARAMETERS);
	}
	
	public void testRetrievingSingleParameter() throws ClientProtocolException, IOException {
		FeedbackParameter parameter = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/parameters/6640", 
				FeedbackParameter.class);
		
		assertEquals(parameter.getId(), new Integer(6640));
		assertEquals(parameter.getKey(), "maxLength");
		assertEquals(parameter.getValue(), 200.0);
	}
	
	public void testRetrievingAllParametersForGeneralConfiguration() throws ClientProtocolException, IOException  {
		FeedbackParameter[] retrievedParameters = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/general_configurations/148/parameters", 
				FeedbackParameter[].class);
		assertEquals(retrievedParameters.length, 2);
	}
	
	public void testRetrievingAllParametersForMechanism() throws ClientProtocolException, IOException  {    
		FeedbackParameter[] retrievedParameters = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/mechanisms/829/parameters", 
				FeedbackParameter[].class);
        
		assertEquals(retrievedParameters.length, 2);
	}
	
	public void testInsertParameterForGeneralConfiguration() throws ClientProtocolException, IOException  {
		
		InputStream stream = this.getClass().getResourceAsStream("parameter_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		FeedbackParameter createdParameter = PostSuccess(
				"http://localhost:8080/feedback_orchestrator/en/general_configurations/148/parameters", 
				jsonString,
				FeedbackParameter.class);
        
		assertEquals(createdParameter.getKey(), "test");
		assertEquals(createdParameter.getValue(), "test");
	}
	
	public void testInsertParameterForMechanism() throws ClientProtocolException, IOException  {
		
		InputStream stream = this.getClass().getResourceAsStream("parameter_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		FeedbackParameter createdParameter = PostSuccess(
				"http://localhost:8080/feedback_orchestrator/en/mechanisms/829/parameters", 
				jsonString,
				FeedbackParameter.class);
        
		assertEquals(createdParameter.getKey(), "test");
		assertEquals(createdParameter.getValue(), "test");
	}
	
	public void testUpdateParameter() throws ClientProtocolException, IOException  {
		
		InputStream stream = this.getClass().getResourceAsStream("parameter_update.json");
		String jsonString = IOUtils.toString(stream); 
		
		FeedbackParameter updatedParameter = PutSuccess(
				"http://localhost:8080/feedback_orchestrator/en/parameters", 
				jsonString,
				FeedbackParameter.class);
        
		assertEquals(updatedParameter.getId(), new Integer(6640));
		assertEquals(updatedParameter.getKey(), "maxLength");
		assertEquals(updatedParameter.getValue(), 100.0);
	}
}
