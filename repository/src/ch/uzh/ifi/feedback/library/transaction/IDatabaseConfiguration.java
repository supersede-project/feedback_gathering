package ch.uzh.ifi.feedback.library.transaction;

public interface IDatabaseConfiguration 
{
	void StartDebugMode();
	String getDatabase();
	String getTestDatabase();
	String getUserName();
	String getPassword();
}
