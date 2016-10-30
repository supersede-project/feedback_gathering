package ch.uzh.ifi.feedback.orchestrator.transaction;

import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;

public class MonitoringDatabaseConfiguration extends DatabaseConfiguration {
	
	private String databaseName;
	private String testDatabaseName;
	
	public MonitoringDatabaseConfiguration() {
		
		databaseName = properties.get("monitoringDb");
		testDatabaseName = properties.get("monitoringTestDb");
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

}
