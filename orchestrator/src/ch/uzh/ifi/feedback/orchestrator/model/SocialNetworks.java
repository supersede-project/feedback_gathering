package ch.uzh.ifi.feedback.orchestrator.model;

import java.util.List;

public class SocialNetworks extends MonitorConfigurationParams {

	private String keywordExpression;
	
	public String getKeywordExpression() {
		return keywordExpression;
	}

	public void setKeywordExpression(String keywordExpression) {
		this.keywordExpression = keywordExpression;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}

	private List<String> accounts;
	
}
