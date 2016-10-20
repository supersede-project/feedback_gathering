package ch.uzh.ifi.feedback.library.transaction;

public interface IDatabaseConfiguration {
	void StartDebugMode();
	void RestoreTestDatabase();
	String getDatabase();
	String getTestDatabase();
}
