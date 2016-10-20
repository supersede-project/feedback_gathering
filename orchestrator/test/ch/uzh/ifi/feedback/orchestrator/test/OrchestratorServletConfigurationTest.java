package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;

import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.Transaction.OrchestratorDatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.ConfigurationType;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;

public class OrchestratorServletConfigurationTest extends OrchestratorServletTest {
	
	public void testRetrievingSingleConfiguration() throws ClientProtocolException, IOException {
		Configuration config = GetSuccess(
				"http://localhost:8080/orchestrator/feedback/en/configurations/80",
				Configuration.class);
		
		assertEquals(config.getId(), new Integer(80));
		assertEquals(config.getType(), ConfigurationType.PUSH);
		assertEquals(config.getFeedbackMechanisms().size(), 4);
		assertNotNull(config.getGeneralConfiguration());
	}
	
	public void testRetrievingAllConfigurationsForApplication() throws ClientProtocolException, IOException {
		Configuration[] retrievedConfigs = GetSuccess(
				"http://localhost:8080/orchestrator/feedback/en/applications/27/configurations", 
				Configuration[].class);
		
		assertEquals(retrievedConfigs.length, 3);
	}
	
	public void testRetrievingAllConfigurationsForUserGroupAndApplication() throws ClientProtocolException, IOException {
		Configuration[] retrievedConfigs = GetSuccess(
				"http://localhost:8080/orchestrator/feedback/en/applications/27/user_groups/1/configurations", 
				Configuration[].class);
		
		assertEquals(retrievedConfigs.length, 3);
	}
	
	public void testRetrievingAllConfigurationsForUserAndApplication() throws ClientProtocolException, IOException {
		Configuration[] retrievedConfigs = GetSuccess(
				"http://localhost:8080/orchestrator/feedback/en/applications/27/users/1/configurations", 
				Configuration[].class);
		
		assertEquals(retrievedConfigs.length, 3);
	}
	

	public void testInsertConfigurationForApplication() throws ClientProtocolException, IOException {
		
		InputStream stream = this.getClass().getResourceAsStream("configuration_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		Configuration createdConfig = PostSuccess(
				"http://localhost:8080/orchestrator/feedback/en/applications/27/configurations", 
				jsonString,
				Configuration.class);
        
		assertEquals(createdConfig.getType(), ConfigurationType.PULL);
		assertNotNull(createdConfig.getGeneralConfiguration());
		assertEquals(createdConfig.getGeneralConfiguration().getParameters().size(), 3);
		assertEquals(createdConfig.getFeedbackMechanisms().size(), 2);
	}
	
	public void testInsertConfigurationForApplicationAndUserGroup() throws ClientProtocolException, IOException {
		
		InputStream stream = this.getClass().getResourceAsStream("configuration_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		Configuration createdConfig = PostSuccess(
				"http://localhost:8080/orchestrator/feedback/en/applications/27/user_groups/1/configurations", 
				jsonString,
				Configuration.class);
        
		assertEquals(createdConfig.getType(), ConfigurationType.PULL);
		assertNotNull(createdConfig.getGeneralConfiguration());
		assertEquals(createdConfig.getGeneralConfiguration().getParameters().size(), 3);
		assertEquals(createdConfig.getFeedbackMechanisms().size(), 2);
	}
	
	public void testUpdateConfigurationForApplication() throws ClientProtocolException, IOException {
		
		InputStream stream = this.getClass().getResourceAsStream("configuration_update.json");
		String jsonString = IOUtils.toString(stream); 
		
		Configuration updatedConfig = PutSuccess(
				"http://localhost:8080/orchestrator/feedback/en/applications/27/configurations", 
				jsonString,
				Configuration.class);
        
		assertEquals(updatedConfig.getType(), ConfigurationType.PULL);
		assertNotNull(updatedConfig.getGeneralConfiguration());
		assertEquals(updatedConfig.getGeneralConfiguration().getParameters().size(), 3);
		assertEquals(updatedConfig.getFeedbackMechanisms().size(), 2);
		FeedbackMechanism updatedMechanism = updatedConfig.getFeedbackMechanisms().stream().filter(m -> m.getType().equals("RATING_TYPE")).findFirst().get();
		assertEquals(updatedMechanism.isActive(), new Boolean(false));
		assertEquals(updatedMechanism.isCanBeActivated(), new Boolean(true));
		FeedbackParameter updatedParam = updatedMechanism.getParameters().stream().filter(p -> p.getId().equals(6101)).findFirst().get();
		assertEquals(updatedParam.getValue(), "Enter your rating here");
	}
}
