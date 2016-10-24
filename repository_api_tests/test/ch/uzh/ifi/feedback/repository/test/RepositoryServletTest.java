package ch.uzh.ifi.feedback.repository.test;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import ch.uzh.ifi.feedback.library.rest.authorization.UserToken;
import ch.uzh.ifi.feedback.library.rest.test.ServletTest;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.transaction.RepositoryDatabaseConfiguration;

public abstract class RepositoryServletTest extends ServletTest {

	public RepositoryServletTest() 
	{
		super(new RepositoryDatabaseConfiguration());
	}
	
	@Override
	protected UserToken AuthenticateUser() throws IOException
	{
		InputStream stream = this.getClass().getResourceAsStream("api_user.json");
		String jsonString = IOUtils.toString(stream); 
		
		return PostSuccess(
				"http://localhost:8080/feedback_repository/authenticate", 
				jsonString,
				UserToken.class);
	}
	
	@Override
	protected InputStream getTestDatabaseDump() {
		return this.getClass().getResourceAsStream("repository_test_dump.sql");
	}
}
