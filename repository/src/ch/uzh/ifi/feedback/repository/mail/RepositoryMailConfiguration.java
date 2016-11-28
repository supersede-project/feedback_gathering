package ch.uzh.ifi.feedback.repository.mail;

import ch.uzh.ifi.feedback.library.mail.MailConfiguration;

public class RepositoryMailConfiguration extends MailConfiguration {

	@Override
	public String getHost() {
		return properties.get("repositoryMailHost");
	}

	@Override
	public String getPort() {
		return properties.get("repositoryMailPort");
	}

	@Override
	public String getUser() {
		return properties.get("repositoryMailUser");
	}

	@Override
	public String getPassword() {
		return properties.get("repositoryMailPassword");
	}
	
	@Override
	public String getOrchestratorUrl() {
		return properties.get("orchestratorBaseUrl");
	}

	@Override
	public String getFeedbackMailKeyName() {
		return properties.get("feedbackMailKeyName");
	}

	@Override
	public String getRepositoryUrl() {
		return properties.get("repositoryBaseUrl");
	}

	@Override
	public boolean isMailFeedbackEnabled() {
		return properties.get("isMailFeedbackEnabled").equalsIgnoreCase("true");
	}

}
