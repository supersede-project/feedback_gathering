package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;

import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;

public class OrchestratorServletMechanismTest extends ServletTest {
	
	public void testRetrievingAllMechanisms() throws ClientProtocolException, IOException {
		FeedbackMechanism[] retrievedMechanisms = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/mechanisms", 
				FeedbackMechanism[].class);
		assertEquals(retrievedMechanisms.length, 81);
	}
	
	public void testRetrievingSingleMechanism() throws ClientProtocolException, IOException {
		FeedbackMechanism mechanism = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/mechanisms/830",
				FeedbackMechanism.class);
		
		assertEquals(mechanism.getId(), new Integer(830));
		assertEquals(mechanism.getType(), "RATING_TYPE");
	}
	
	public void testRetrievingAllMechanismsForConfiguration() throws ClientProtocolException, IOException {
		FeedbackMechanism[] retrievedMechanisms = GetSuccess(
				"http://localhost:8080/feedback_orchestrator/en/configurations/80/mechanisms", 
				FeedbackMechanism[].class);
		
		assertEquals(retrievedMechanisms.length, 4);
	}
	
	public void testInsertMechanismForConfiguration() throws ClientProtocolException, IOException {
		
		InputStream stream = this.getClass().getResourceAsStream("mechanism_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		FeedbackMechanism createdMechanism = PostSuccess(
				"http://localhost:8080/feedback_orchestrator/en/configurations/80/mechanisms", 
				jsonString,
				FeedbackMechanism.class);
        
		assertEquals(createdMechanism.getType(), "AUDIO_TYPE");
		assertEquals(createdMechanism.isActive(), new Boolean(true));
		assertEquals(createdMechanism.getParameters().size(), 2);
		assertEquals(createdMechanism.getOrder(), new Integer(2));
	}
	
	public void testUpdateMechanismForConfiguration() throws ClientProtocolException, IOException  {
		
		InputStream stream = this.getClass().getResourceAsStream("mechanism_update.json");
		String jsonString = IOUtils.toString(stream); 
		
		FeedbackMechanism updatedMechanism = PutSuccess(
				"http://localhost:8080/feedback_orchestrator/en/configurations/80/mechanisms", 
				jsonString,
				FeedbackMechanism.class);
        
		assertEquals(updatedMechanism.getId(), new Integer(827));
		assertEquals(updatedMechanism.isActive(), new Boolean(false));
		boolean parameterCheck = updatedMechanism.getParameters().stream().anyMatch(p -> p.getId().equals(6640) && p.getValue().equals(100.0));
		assertTrue(parameterCheck);
	}

}
