package ch.uzh.ifi.feedback.repository.transaction;

import java.io.InputStream;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;

@Singleton
public class RepositoryDatabaseConfiguration extends DatabaseConfiguration {

	private String databaseName;
	private String testDatabaseName;
	
	public RepositoryDatabaseConfiguration() {
		
		databaseName = properties.get("repositoryDb");
		testDatabaseName = properties.get("repositoryTestDb");
	}
	
	@Override
	public void StartDebugMode() 
	{
		databaseName = testDatabaseName;
		super.StartDebugMode();
	}

	@Override
	public String getDatabase() {
		return databaseName;
	}

	@Override
	public String getTestDatabase() {
		return testDatabaseName;
	}

	@Override
	protected InputStream getTestDatabaseDump() {
		return this.getClass().getResourceAsStream("repository_test_dump.sql");
	}

}
