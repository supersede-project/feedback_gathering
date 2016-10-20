package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.Transaction.OrchestratorDatabaseConfiguration;

public abstract class OrchestratorServletTest extends ServletTest {

	private IDatabaseConfiguration config;
	
	public OrchestratorServletTest()
	{
		config = new OrchestratorDatabaseConfiguration();
		config.StartDebugMode();
	}
	
	@Override
	protected IDatabaseConfiguration getDatabaseConfiguration() {
		return config;
	}	
	
	@Override
	protected UserToken AuthenticateUser() throws IOException {
		InputStream stream = ServletTest.class.getResourceAsStream("api_user.json");
		String jsonString = IOUtils.toString(stream); 
		
		return PostSuccess(
				"http://localhost:8080/orchestrator/feedback/authenticate", 
				jsonString,
				UserToken.class);
	}

}
