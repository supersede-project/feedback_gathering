package ch.uzh.ifi.feedback.repository.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.transaction.RepositoryDatabaseConfiguration;

public abstract class RepositoryServletTest extends ServletTest {

	private IDatabaseConfiguration config;
	
	public RepositoryServletTest() {
		config = new RepositoryDatabaseConfiguration();
		config.StartDebugMode();
	}
	
	@Override
	protected IDatabaseConfiguration getDatabaseConfiguration() {
		return config;
	}
	
	@Override
	protected UserToken AuthenticateUser() throws IOException
	{
		InputStream stream = ServletTest.class.getResourceAsStream("api_user.json");
		String jsonString = IOUtils.toString(stream); 
		
		return PostSuccess(
				"http://localhost:8080/feedback_repository/authenticate", 
				jsonString,
				UserToken.class);
	}
}
