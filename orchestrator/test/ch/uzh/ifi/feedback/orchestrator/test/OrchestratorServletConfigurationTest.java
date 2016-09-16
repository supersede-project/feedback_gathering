package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
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
	
	/*
	public void testInsertConfigurationForApplication() throws ClientProtocolException, IOException {
		
		InputStream stream = this.getClass().getResourceAsStream("mechanism_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		FeedbackMechanism createdMechanism = PostSuccess(
				"http://localhost:8080/feedback_orchestrator/en/configurations/17/mechanisms", 
				jsonString,
				FeedbackMechanism.class);
        
		assertEquals(createdMechanism.getType(), "AUDIO_TYPE");
		assertEquals(createdMechanism.isActive(), new Boolean(true));
		assertEquals(createdMechanism.getParameters().size(), 2);
		assertEquals(createdMechanism.getOrder(), new Integer(2));
	}*/
}
