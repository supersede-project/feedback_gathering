package ch.uzh.ifi.feedback.library.transaction;

public interface IDatabaseConfiguration {
	void StartDebugMode();
	void RestoreTestDatabases();
	String getRepositoryDb();
	String getOrchestratorDb();
	String getRepositoryDbTest();
	String getOrchestratorDbTest();
}
