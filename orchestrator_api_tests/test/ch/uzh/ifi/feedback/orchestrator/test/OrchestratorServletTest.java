package ch.uzh.ifi.feedback.orchestrator.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import ch.uzh.ifi.feedback.library.rest.test.ServletTest;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.orchestrator.transaction.OrchestratorDatabaseConfiguration;

public abstract class OrchestratorServletTest extends ServletTest {

	private IDatabaseConfiguration config;
	
	public OrchestratorServletTest()
	{
		super(new OrchestratorDatabaseConfiguration());
	}
	
	@Override
	protected UserToken AuthenticateUser() throws IOException {
		InputStream stream = this.getClass().getResourceAsStream("api_user.json");
		String jsonString = IOUtils.toString(stream); 
		
		return PostSuccess(
				"http://localhost:8080/orchestrator/feedback/authenticate", 
				jsonString,
				UserToken.class);
	}
	
	@Override
	protected InputStream getTestDatabaseDump() {
		return this.getClass().getResourceAsStream("orchestrator_test_dump.sql");
	}

}
