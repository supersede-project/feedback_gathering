package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;

import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.ConfigurationType;

public class OrchestratorServletConfigurationTest extends ServletTest {
	
	public void testRetrievingAllConfigurations() throws ClientProtocolException, IOException {
		Configuration[] retrievedConfigs = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/configurations", 
				Configuration[].class);
		
		assertEquals(retrievedConfigs.length, 27);
	}
	
	public void testRetrievingSingleConfiguration() throws ClientProtocolException, IOException {
		Configuration config = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/configurations/80",
				Configuration.class);
		
		assertEquals(config.getId(), new Integer(80));
		assertEquals(config.getType(), ConfigurationType.PUSH);
		assertEquals(config.getFeedbackMechanisms().size(), 4);
		assertNotNull(config.getGeneralConfiguration());
	}
	
	public void testRetrievingAllConfigurationsForApplication() throws ClientProtocolException, IOException {
		Configuration[] retrievedConfigs = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/applications/27/configurations", 
				Configuration[].class);
		
		assertEquals(retrievedConfigs.length, 3);
	}
	
	public void testRetrievingAllConfigurationsForUserGroupAndApplication() throws ClientProtocolException, IOException {
		Configuration[] retrievedConfigs = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/applications/27/user_groups/1/configurations", 
				Configuration[].class);
		
		assertEquals(retrievedConfigs.length, 3);
	}
	
	public void testRetrievingAllConfigurationsForUserAndApplication() throws ClientProtocolException, IOException {
		Configuration[] retrievedConfigs = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/applications/27/users/u1234/configurations", 
				Configuration[].class);
		
		assertEquals(retrievedConfigs.length, 3);
	}
	
	/*
	public void testInsertConfigurationForApplication() throws ClientProtocolException, IOException {
		
		InputStream stream = this.getClass().getResourceAsStream("configuration_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		Configuration createdConfig = PostSuccess(
				"http://localhost:8080/feedback_orchestrator/en/applications/27/configurations", 
				jsonString,
				Configuration.class);
        
		assertEquals(createdConfig.getType(), "PULL");
	}*/
}
