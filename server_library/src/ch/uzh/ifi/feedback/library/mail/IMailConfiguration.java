package ch.uzh.ifi.feedback.library.mail;

public interface IMailConfiguration {
	
	public String getHost();

	public String getPort();
	
	public String getUser();
	
	public String getPassword();
	
	public String getFeedbackMailKeyName();
	
	public String getOrchestratorUrl();
	
	public String getRepositoryUrl();
	
	public boolean isMailFeedbackEnabled();
}
