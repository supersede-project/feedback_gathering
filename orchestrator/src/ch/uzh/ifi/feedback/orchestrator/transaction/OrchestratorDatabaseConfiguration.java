package ch.uzh.ifi.feedback.orchestrator.transaction;

import java.io.InputStream;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;

@Singleton
public class OrchestratorDatabaseConfiguration extends DatabaseConfiguration {

	private String databaseName;
	private String testDatabaseName;
	
	public OrchestratorDatabaseConfiguration() {
		
		databaseName = properties.get("orchestratorDb");
		testDatabaseName = properties.get("orchestratorTestDb");
	}
	
	@Override
	public void StartDebugMode() 
	{
		databaseName = testDatabaseName;
	}

	@Override
	public String getDatabase() {
		return databaseName;
	}

	@Override
	public String getTestDatabase() {
		return testDatabaseName;
	}
}
