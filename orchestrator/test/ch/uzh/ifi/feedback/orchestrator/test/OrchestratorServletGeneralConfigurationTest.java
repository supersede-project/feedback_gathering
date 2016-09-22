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
				"https://localhost:8443/feedback_orchestrator/en/general_configurations", 
				GeneralConfiguration[].class);
		
		assertEquals(retrievedConfigs.length, 36);
	}
	
	public void testRetrievingSingleConfiguration() throws ClientProtocolException, IOException {
		GeneralConfiguration config = GetSuccess(
				"https://localhost:8443/feedback_orchestrator/en/general_configurations/148",
				GeneralConfiguration.class);
		
		assertEquals(config.getId(), new Integer(148));
		assertEquals(config.getParameters().size(), 2);
	}
	
	public void testRetrievingConfigurationForApplication() throws ClientProtocolException, IOException {
		GeneralConfiguration retrievedConfig = GetSuccess(
				"https://localhost:8443/feedback_orchestrator/en/applications/27/general_configuration", 
				GeneralConfiguration.class);
		
		assertEquals(retrievedConfig.getId(), new Integer(116));
	}
	
	public void testUpdateGeneralConfiguration() throws ClientProtocolException, IOException  {
		
		InputStream stream = this.getClass().getResourceAsStream("general_configuration_update.json");
		String jsonString = IOUtils.toString(stream); 
		
		GeneralConfiguration updatedMechanism = PutSuccess(
				"https://localhost:8443/feedback_orchestrator/en/general_configurations", 
				jsonString,
				GeneralConfiguration.class);
        
		assertEquals(updatedMechanism.getId(), new Integer(148));
		assertEquals(true, updatedMechanism.getParameters().stream().anyMatch(p -> p.getId().equals(new Integer(6637)) && p.getValue().equals(1.0)));
	}

}
