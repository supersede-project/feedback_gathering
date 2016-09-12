package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;

public class OrchestratorServletGeneralConfigurationTest extends ServletTest {
	
	public void testRetrievingAllConfigurations() throws ClientProtocolException, IOException {
		GeneralConfiguration[] retrievedConfigs = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/general_configurations", 
				GeneralConfiguration[].class);
		
		assertEquals(retrievedConfigs.length, 36);
	}
	
	public void testRetrievingSingleConfiguration() throws ClientProtocolException, IOException {
		GeneralConfiguration config = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/general_configurations/65",
				GeneralConfiguration.class);
		
		assertEquals(config.getId(), new Integer(65));
		assertEquals(config.getParameters().size(), 2);
	}
	
	public void testRetrievingConfigurationForApplication() throws ClientProtocolException, IOException {
		GeneralConfiguration retrievedConfig = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/applications/14/general_configuration", 
				GeneralConfiguration.class);
		
		assertEquals(retrievedConfig.getId(), new Integer(63));
	}
	
	public void testUpdateGeneralConfiguration() throws ClientProtocolException, IOException  {
		
		InputStream stream = this.getClass().getResourceAsStream("general_configuration_update.json");
		String jsonString = IOUtils.toString(stream); 
		
		GeneralConfiguration updatedMechanism = PutSuccess(
				"http://localhost:8080/feedback_orchestrator/en/general_configurations", 
				jsonString,
				GeneralConfiguration.class);
        
		assertEquals(updatedMechanism.getId(), new Integer(63));
		assertEquals(true, updatedMechanism.getParameters().stream().anyMatch(p -> p.getId().equals(new Integer(5062)) && p.getValue().equals(1.0)));
	}

}
